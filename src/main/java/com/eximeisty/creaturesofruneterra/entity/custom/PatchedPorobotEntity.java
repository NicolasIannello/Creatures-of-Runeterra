package com.eximeisty.creaturesofruneterra.entity.custom;

import java.util.EnumSet;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.RunAroundLikeCrazyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.passive.horse.AbstractChestedHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class PatchedPorobotEntity extends AbstractChestedHorseEntity implements IAnimatable{//, IInventoryChangedListener{
   private AnimationFactory factory = new AnimationFactory(this);
   private static final DataParameter<Boolean> DATA_ID_CHEST = EntityDataManager.createKey(PatchedPorobotEntity.class, DataSerializers.BOOLEAN);

   public PatchedPorobotEntity(EntityType<? extends AbstractChestedHorseEntity> type, World worldIn) {
      super(type, worldIn);
   }

   public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
      return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 50)
      .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(1, new PanicGoal(this, 1.2D));
      this.goalSelector.addGoal(1, new RunAroundLikeCrazyGoal(this, 1.2D));
      this.goalSelector.addGoal(0, new SwimGoal(this));
      this.goalSelector.addGoal(5, new PatchedPorobotEntity.FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
   }

   public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.poro.idle", true));
      return PlayState.CONTINUE;
   }

   @Override
   public void registerControllers(AnimationData data) {
      data.addAnimationController(new AnimationController<IAnimatable>(this, "controller", 0, this::predicate));
   }

   @Override
   public AnimationFactory getFactory() {
      return factory;
   }

   @Override
   public AgeableEntity createChild(ServerWorld world, AgeableEntity mate) {
      return null;
   }

   protected void registerData() {
      super.registerData();
      this.dataManager.register(DATA_ID_CHEST, true);
   }

   // public void onDeath(DamageSource cause) {
   //     if (!this.world.isRemote && this.world.getGameRules().getBoolean(GameRules.SHOW_DEATH_MESSAGES) && this.getOwner() instanceof ServerPlayerEntity) {
   //        this.getOwner().sendMessage(this.getCombatTracker().getDeathMessage(), Util.DUMMY_UUID);
   //     }
   //     super.onDeath(cause);
   // }
    
   /*INVENTORY*/
   public int getInventoryColumns() { return 5; }

   public boolean hasChest() { return this.dataManager.get(DATA_ID_CHEST); }

   public void setChested(boolean chested) { this.dataManager.set(DATA_ID_CHEST, true); }

   protected int getInventorySize() { return this.hasChest() ? 17 : super.getInventorySize(); }

   public ActionResultType getEntityInteractionResult(PlayerEntity playerIn, Hand hand) {
      this.openGUI(playerIn);
      return ActionResultType.func_233537_a_(this.world.isRemote); 
   }
   /*TAMED */
   
   @Nullable
   public LivingEntity getOwner() {
      try {
         UUID uuid = this.getOwnerUniqueId();
         return uuid == null ? null : this.world.getPlayerByUuid(uuid);
      } catch (IllegalArgumentException illegalargumentexception) {
         return null;
      }
   }

   public class FollowOwnerGoal extends Goal {
      private final PatchedPorobotEntity tameable;
      private LivingEntity owner;
      private final IWorldReader world;
      private final double followSpeed;
      private final PathNavigator navigator;
      private int timeToRecalcPath;
      private final float maxDist;
      private final float minDist;
      private float oldWaterCost;
      private final boolean teleportToLeaves;
   
      public FollowOwnerGoal(PatchedPorobotEntity tameable, double speed, float minDist, float maxDist, boolean teleportToLeaves) {
         this.tameable = tameable;
         this.world = tameable.world;
         this.followSpeed = speed;
         this.navigator = tameable.getNavigator();
         this.minDist = minDist;
         this.maxDist = maxDist;
         this.teleportToLeaves = teleportToLeaves;
         this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
         if (!(tameable.getNavigator() instanceof GroundPathNavigator) && !(tameable.getNavigator() instanceof FlyingPathNavigator)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
         }
      }
   
      public boolean shouldExecute() {
         LivingEntity livingentity = this.tameable.getOwner();
         if (livingentity == null) {
            return false;
         } else if (livingentity.isSpectator()) {
            return false;
         } else if (this.tameable.getDistanceSq(livingentity) < (double)(this.minDist * this.minDist)) {
            return false;
         } else {
            this.owner = livingentity;
            return true;
         }
      }
   
      public boolean shouldContinueExecuting() {
         if (this.navigator.noPath()) {
            return false;
         }else {
            return !(this.tameable.getDistanceSq(this.owner) <= (double)(this.maxDist * this.maxDist));
         }
      }
   
      public void startExecuting() {
         this.timeToRecalcPath = 0;
         this.oldWaterCost = this.tameable.getPathPriority(PathNodeType.WATER);
         this.tameable.setPathPriority(PathNodeType.WATER, 0.0F);
      }

      public void resetTask() {
         this.owner = null;
         this.navigator.clearPath();
         this.tameable.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
      }

      public void tick() {
         this.tameable.getLookController().setLookPositionWithEntity(this.owner, 10.0F, (float)this.tameable.getVerticalFaceSpeed());
         if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.tameable.getLeashed() && !this.tameable.isPassenger()) {
               if (this.tameable.getDistanceSq(this.owner) >= 144.0D) {
                  this.tryToTeleportNearEntity();
               } else {
                  this.navigator.tryMoveToEntityLiving(this.owner, this.followSpeed);
               }
            }
         }
      }
   
      private void tryToTeleportNearEntity() {
         BlockPos blockpos = this.owner.getPosition();
         for(int i = 0; i < 10; ++i) {
            int j = this.getRandomNumber(-3, 3);
            int k = this.getRandomNumber(-1, 1);
            int l = this.getRandomNumber(-3, 3);
            boolean flag = this.tryToTeleportToLocation(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
            if (flag) {
               return;
            }
         }
      }
   
      private boolean tryToTeleportToLocation(int x, int y, int z) {
         if (Math.abs((double)x - this.owner.getPosX()) < 2.0D && Math.abs((double)z - this.owner.getPosZ()) < 2.0D) {
            return false;
         } else if (!this.isTeleportFriendlyBlock(new BlockPos(x, y, z))) {
            return false;
         } else {
            this.tameable.setLocationAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, this.tameable.rotationYaw, this.tameable.rotationPitch);
            this.navigator.clearPath();
            return true;
         }
      }
   
      private boolean isTeleportFriendlyBlock(BlockPos pos) {
         PathNodeType pathnodetype = WalkNodeProcessor.getFloorNodeType(this.world, pos.toMutable());
         if (pathnodetype != PathNodeType.WALKABLE) {
            return false;
         } else {
            BlockState blockstate = this.world.getBlockState(pos.down());
            if (!this.teleportToLeaves && blockstate.getBlock() instanceof LeavesBlock) {
               return false;
            } else {
               BlockPos blockpos = pos.subtract(this.tameable.getPosition());
               return this.world.hasNoCollisions(this.tameable, this.tameable.getBoundingBox().offset(blockpos));
            }
         }
      }
   
      private int getRandomNumber(int min, int max) {
         return this.tameable.getRNG().nextInt(max - min + 1) + min;
      }
   }
    
   protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_BAT_DEATH; }
   protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SoundEvents.ENTITY_FOX_HURT; }
   public SoundEvent getAmbientSound() { return SoundEvents.ENTITY_FOX_AMBIENT; }
}