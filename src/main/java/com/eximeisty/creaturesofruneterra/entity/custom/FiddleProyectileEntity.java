package com.eximeisty.creaturesofruneterra.entity.custom;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.eximeisty.creaturesofruneterra.entity.ModEntities;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FiddleProyectileEntity extends Projectile {
   private static final double SPEED = 0.15D;
   @Nullable
   private Entity finalTarget;
   @Nullable
   private Direction currentMoveDirection;
   private int flightSteps;
   private double targetDeltaX;
   private double targetDeltaY;
   private double targetDeltaZ;
   @Nullable
   private UUID targetId;
   private LivingEntity parent;

   public FiddleProyectileEntity(EntityType<? extends FiddleProyectileEntity> p_37319_, Level p_37320_) {
      super(p_37319_, p_37320_);
      this.noPhysics = true;
   }

   public FiddleProyectileEntity(Level p_37330_, LivingEntity p_37331_, Entity p_37332_, Direction.Axis p_37333_, @Nullable LivingEntity parent) {
      this(ModEntities.FIDDLE_PROYECTILE.get(), p_37330_);
      this.parent = parent;
      this.setOwner(p_37331_);
      BlockPos blockpos = p_37331_.blockPosition();
      double d0 = (double)blockpos.getX() + 0.5D;
      double d1 = (double)blockpos.getY() + 0.5D;
      double d2 = (double)blockpos.getZ() + 0.5D;
      this.moveTo(d0, d1, d2, this.getYRot(), this.getXRot());
      this.finalTarget = p_37332_;
      this.currentMoveDirection = Direction.UP;
      this.selectNextMoveDirection(p_37333_);
   }

   public SoundSource getSoundSource() {
      return SoundSource.HOSTILE;
   }

   protected void addAdditionalSaveData(CompoundTag pCompound) {
      super.addAdditionalSaveData(pCompound);
      if (this.finalTarget != null) {
         pCompound.putUUID("Target", this.finalTarget.getUUID());
      }

      if (this.currentMoveDirection != null) {
         pCompound.putInt("Dir", this.currentMoveDirection.get3DDataValue());
      }

      pCompound.putInt("Steps", this.flightSteps);
      pCompound.putDouble("TXD", this.targetDeltaX);
      pCompound.putDouble("TYD", this.targetDeltaY);
      pCompound.putDouble("TZD", this.targetDeltaZ);
   }

   /**
    * (abstract) Protected helper method to read subclass entity data from NBT.
    */
   protected void readAdditionalSaveData(CompoundTag pCompound) {
      super.readAdditionalSaveData(pCompound);
      this.flightSteps = pCompound.getInt("Steps");
      this.targetDeltaX = pCompound.getDouble("TXD");
      this.targetDeltaY = pCompound.getDouble("TYD");
      this.targetDeltaZ = pCompound.getDouble("TZD");
      if (pCompound.contains("Dir", 99)) {
         this.currentMoveDirection = Direction.from3DDataValue(pCompound.getInt("Dir"));
      }

      if (pCompound.hasUUID("Target")) {
         this.targetId = pCompound.getUUID("Target");
      }

   }

   protected void defineSynchedData() {
   }

   @Nullable
   private Direction getMoveDirection() {
      return this.currentMoveDirection;
   }

   private void setMoveDirection(@Nullable Direction pDirection) {
      this.currentMoveDirection = pDirection;
   }

   private void selectNextMoveDirection(@Nullable Direction.Axis p_37349_) {
      double d0 = 0.5D;
      BlockPos blockpos;
      if (this.finalTarget == null) {
         blockpos = this.blockPosition().below();
      } else {
         d0 = (double)this.finalTarget.getBbHeight() * 0.5D;
         blockpos = new BlockPos(this.finalTarget.getBlockX(), (int)(this.finalTarget.getBlockY() + d0), this.finalTarget.getBlockZ());
      }

      double d1 = (double)blockpos.getX() + 0.5D;
      double d2 = (double)blockpos.getY() + d0;
      double d3 = (double)blockpos.getZ() + 0.5D;
      Direction direction = null;
      if (!blockpos.closerToCenterThan(this.position(), 2.0D)) {
         BlockPos blockpos1 = this.blockPosition();
         List<Direction> list = Lists.newArrayList();
         if (p_37349_ != Direction.Axis.X) {
            if (blockpos1.getX() < blockpos.getX() && this.level().isEmptyBlock(blockpos1.east())) {
               list.add(Direction.EAST);
            } else if (blockpos1.getX() > blockpos.getX() && this.level().isEmptyBlock(blockpos1.west())) {
               list.add(Direction.WEST);
            }
         }

         if (p_37349_ != Direction.Axis.Y) {
            if (blockpos1.getY() < blockpos.getY() && this.level().isEmptyBlock(blockpos1.above())) {
               list.add(Direction.UP);
            } else if (blockpos1.getY() > blockpos.getY() && this.level().isEmptyBlock(blockpos1.below())) {
               list.add(Direction.DOWN);
            }
         }

         if (p_37349_ != Direction.Axis.Z) {
            if (blockpos1.getZ() < blockpos.getZ() && this.level().isEmptyBlock(blockpos1.south())) {
               list.add(Direction.SOUTH);
            } else if (blockpos1.getZ() > blockpos.getZ() && this.level().isEmptyBlock(blockpos1.north())) {
               list.add(Direction.NORTH);
            }
         }

         direction = Direction.getRandom(this.random);
         if (list.isEmpty()) {
            for(int i = 5; !this.level().isEmptyBlock(blockpos1.relative(direction)) && i > 0; --i) {
               direction = Direction.getRandom(this.random);
            }
         } else {
            direction = list.get(this.random.nextInt(list.size()));
         }

         d1 = this.getX() + (double)direction.getStepX();
         d2 = this.getY() + (double)direction.getStepY();
         d3 = this.getZ() + (double)direction.getStepZ();
      }

      this.setMoveDirection(direction);
      double d6 = d1 - this.getX();
      double d7 = d2 - this.getY();
      double d4 = d3 - this.getZ();
      double d5 = Math.sqrt(d6 * d6 + d7 * d7 + d4 * d4);
      if (d5 == 0.0D) {
         this.targetDeltaX = 0.0D;
         this.targetDeltaY = 0.0D;
         this.targetDeltaZ = 0.0D;
      } else {
         this.targetDeltaX = d6 / d5 * 0.15D;
         this.targetDeltaY = d7 / d5 * 0.15D;
         this.targetDeltaZ = d4 / d5 * 0.15D;
      }

      this.hasImpulse = true;
      this.flightSteps = 10 + this.random.nextInt(5) * 10;
   }

   /**
    * Makes the entity despawn if requirements are reached
    */
   public void checkDespawn() {
      if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
         this.discard();
      }
   }

   public void tick() {
      super.tick();
      if (!this.level().isClientSide) {
         if (this.finalTarget == null && this.targetId != null) {
            this.finalTarget = ((ServerLevel)this.level()).getEntity(this.targetId);
            if (this.finalTarget == null) {
               this.targetId = null;
            }
         }

         if (this.finalTarget == null || !this.finalTarget.isAlive() || this.finalTarget instanceof Player && this.finalTarget.isSpectator()) {
            if (!this.isNoGravity()) {
               this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
            }
         } else {
            this.targetDeltaX = Mth.clamp(this.targetDeltaX * 1.025D, -1.0D, 1.0D);
            this.targetDeltaY = Mth.clamp(this.targetDeltaY * 1.025D, -1.0D, 1.0D);
            this.targetDeltaZ = Mth.clamp(this.targetDeltaZ * 1.025D, -1.0D, 1.0D);
            Vec3 vec3 = this.getDeltaMovement();
            this.setDeltaMovement(vec3.add((this.targetDeltaX - vec3.x) * 0.2D, (this.targetDeltaY - vec3.y) * 0.2D, (this.targetDeltaZ - vec3.z) * 0.2D));
         }
         HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
         if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            this.onHit(hitresult);
         }
      }

      this.checkInsideBlocks();
      Vec3 vec31 = this.getDeltaMovement();
      this.setPos(this.getX() + vec31.x, this.getY() + vec31.y, this.getZ() + vec31.z);
      ProjectileUtil.rotateTowardsMovement(this, 0.5F);
      if (this.level().isClientSide) {
         this.level().addParticle(ParticleTypes.EFFECT, this.getX() - vec31.x, this.getY() - vec31.y + 0.15D, this.getZ() - vec31.z, 0.0D, 0.0D, 0.0D);
      } else if (this.finalTarget != null && !this.finalTarget.isRemoved()) {
         if (this.flightSteps > 0) {
            --this.flightSteps;
            if (this.flightSteps == 0) {
               this.selectNextMoveDirection(this.currentMoveDirection == null ? null : this.currentMoveDirection.getAxis());
            }
         }

         if (this.currentMoveDirection != null) {
            BlockPos blockpos = this.blockPosition();
            Direction.Axis direction$axis = this.currentMoveDirection.getAxis();
            if (this.level().loadedAndEntityCanStandOn(blockpos.relative(this.currentMoveDirection), this)) {
               this.selectNextMoveDirection(direction$axis);
            } else {
               BlockPos blockpos1 = this.finalTarget.blockPosition();
               if (direction$axis == Direction.Axis.X && blockpos.getX() == blockpos1.getX() || direction$axis == Direction.Axis.Z && blockpos.getZ() == blockpos1.getZ() || direction$axis == Direction.Axis.Y && blockpos.getY() == blockpos1.getY()) {
                  this.selectNextMoveDirection(direction$axis);
               }
            }
         }
      }
   }

   protected boolean canHitEntity(Entity p_37341_) {
      return super.canHitEntity(p_37341_) && !p_37341_.noPhysics;
   }
   public boolean isOnFire() {
      return false;
   }
   public boolean shouldRenderAtSqrDistance(double pDistance) {
      return pDistance < 16384.0D;
   }
   public float getBrightness() {
      return 1.0F;
   }

   protected void onHitEntity(EntityHitResult pResult) {
      super.onHitEntity(pResult);
      Entity entity = pResult.getEntity();
      Entity entity1 = this.getOwner();
      LivingEntity livingentity = entity1 instanceof LivingEntity ? (LivingEntity)entity1 : null;
      boolean flag = entity.hurt(damageSources().mobProjectile(this, livingentity), 4.0F);
      if (flag) {
         this.doEnchantDamageEffects(livingentity, entity);
         if (entity instanceof LivingEntity) {
            if(entity1 instanceof FiddlesticksEntity){
               ((FiddlesticksEntity)entity1).heal(6F);
               ((LivingEntity)entity).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20*10));
            }else{
               if(parent!=null) parent.heal(3F);
               ((LivingEntity)entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20*15));
            }
            if(entity1 instanceof Player) ((Player)entity1).heal(2);
         }
      }
   }

   protected void onHitBlock(BlockHitResult p_37343_) {
      super.onHitBlock(p_37343_);
      ((ServerLevel)this.level()).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
      this.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
   }

   protected void onHit(HitResult pResult) {
      super.onHit(pResult);
      this.discard();
   }

   public boolean isPickable() {
      return true;
   }

   public boolean hurt(DamageSource pSource, float pAmount) {
      if (!this.level().isClientSide) {
         this.playSound(SoundEvents.SHULKER_BULLET_HURT, 1.0F, 1.0F);
         ((ServerLevel)this.level()).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 15, 0.2D, 0.2D, 0.2D, 0.0D);
         this.discard();
      }

      return true;
   }

   public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
      super.recreateFromPacket(pPacket);
      double d0 = pPacket.getXa();
      double d1 = pPacket.getYa();
      double d2 = pPacket.getZa();
      this.setDeltaMovement(d0, d1, d2);
   }
   
}