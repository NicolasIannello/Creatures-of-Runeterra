package com.eximeisty.creaturesofruneterra.entity.custom;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import com.google.common.collect.Lists;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class FiddleProyectileEntity extends Entity {
   private UUID owner;
   private int field_234610_c_;
   private boolean leftOwner;
   private Entity target;
   @Nullable
   private Direction direction;
   private int steps;
   private double targetDeltaX;
   private double targetDeltaY;
   private double targetDeltaZ;
   @Nullable
   private UUID targetUniqueId;

   public FiddleProyectileEntity(EntityType<? extends FiddleProyectileEntity> type, World world) {
      super(type, world);
      this.noClip = true;
   }

   @OnlyIn(Dist.CLIENT)
   public FiddleProyectileEntity(World worldIn, double x, double y, double z, double motionXIn, double motionYIn, double motionZIn) {
      this(ModEntityTypes.FIDDLE_PROYECTILE.get(), worldIn);
      this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
      this.setMotion(motionXIn, motionYIn, motionZIn);
   }

   public FiddleProyectileEntity(World worldIn, LivingEntity ownerIn, Entity targetIn, Direction.Axis p_i46772_4_) {
      this(ModEntityTypes.FIDDLE_PROYECTILE.get(), worldIn);
      this.setShooter(ownerIn);
      BlockPos blockpos = ownerIn.getPosition();
      double d0 = (double)blockpos.getX() + 0.5D;
      double d1 = (double)blockpos.getY() + 0.5D;
      double d2 = (double)blockpos.getZ() + 0.5D;
      this.setLocationAndAngles(d0, d1, d2, this.rotationYaw, this.rotationPitch);
      this.target = targetIn;
      this.direction = Direction.UP;
      this.selectNextMoveDirection(p_i46772_4_);
   }

   private void setDirection(@Nullable Direction directionIn) { this.direction = directionIn; }

   private void selectNextMoveDirection(@Nullable Direction.Axis p_184569_1_) {
      double d0 = 0.5D;
      BlockPos blockpos;
      if (this.target == null) {
         blockpos = this.getPosition().down();
      } else {
         d0 = (double)this.target.getHeight() * 0.5D;
         blockpos = new BlockPos(this.target.getPosX(), this.target.getPosY() + d0, this.target.getPosZ());
      }
      double d1 = (double)blockpos.getX() + 0.5D;
      double d2 = (double)blockpos.getY() + d0;
      double d3 = (double)blockpos.getZ() + 0.5D;
      Direction direction = null;
      if (!blockpos.withinDistance(this.getPositionVec(), 2.0D)) {
         BlockPos blockpos1 = this.getPosition();
         List<Direction> list = Lists.newArrayList();
         if (p_184569_1_ != Direction.Axis.X) {
            if (blockpos1.getX() < blockpos.getX() && this.world.isAirBlock(blockpos1.east())) {
               list.add(Direction.EAST);
            } else if (blockpos1.getX() > blockpos.getX() && this.world.isAirBlock(blockpos1.west())) {
               list.add(Direction.WEST);
            }
         }
         if (p_184569_1_ != Direction.Axis.Y) {
            if (blockpos1.getY() < blockpos.getY() && this.world.isAirBlock(blockpos1.up())) {
               list.add(Direction.UP);
            } else if (blockpos1.getY() > blockpos.getY() && this.world.isAirBlock(blockpos1.down())) {
               list.add(Direction.DOWN);
            }
         }
         if (p_184569_1_ != Direction.Axis.Z) {
            if (blockpos1.getZ() < blockpos.getZ() && this.world.isAirBlock(blockpos1.south())) {
               list.add(Direction.SOUTH);
            } else if (blockpos1.getZ() > blockpos.getZ() && this.world.isAirBlock(blockpos1.north())) {
               list.add(Direction.NORTH);
            }
         }
         direction = Direction.getRandomDirection(this.rand);
         if (list.isEmpty()) {
            for(int i = 5; !this.world.isAirBlock(blockpos1.offset(direction)) && i > 0; --i) {
               direction = Direction.getRandomDirection(this.rand);
            }
         } else {
            direction = list.get(this.rand.nextInt(list.size()));
         }
         d1 = this.getPosX() + (double)direction.getXOffset();
         d2 = this.getPosY() + (double)direction.getYOffset();
         d3 = this.getPosZ() + (double)direction.getZOffset();
      }
      this.setDirection(direction);
      double d6 = d1 - this.getPosX();
      double d7 = d2 - this.getPosY();
      double d4 = d3 - this.getPosZ();
      double d5 = (double)MathHelper.sqrt(d6 * d6 + d7 * d7 + d4 * d4);
      if (d5 == 0.0D) {
         this.targetDeltaX = 0.0D;
         this.targetDeltaY = 0.0D;
         this.targetDeltaZ = 0.0D;
      } else {
         this.targetDeltaX = d6 / d5 * 0.15D;
         this.targetDeltaY = d7 / d5 * 0.15D;
         this.targetDeltaZ = d4 / d5 * 0.15D;
      }
      this.isAirBorne = true;
      this.steps = 10 + this.rand.nextInt(5) * 10;
   }

   public void setShooter(@Nullable Entity entityIn) {
      if (entityIn != null) {
         this.owner = entityIn.getUniqueID();
         this.field_234610_c_ = entityIn.getEntityId();
      }
   }

   @Nullable
   public Entity getShooter() {
      if (this.owner != null && this.world instanceof ServerWorld) {
         return ((ServerWorld)this.world).getEntityByUuid(this.owner);
      } else {
         return this.field_234610_c_ != 0 ? this.world.getEntityByID(this.field_234610_c_) : null;
      }
   }

   protected void writeAdditional(CompoundNBT compound) {
      if (this.owner != null) compound.putUniqueId("Owner", this.owner);
      if (this.leftOwner) compound.putBoolean("LeftOwner", true);
      if (this.target != null) compound.putUniqueId("Target", this.target.getUniqueID());
      if (this.direction != null) compound.putInt("Dir", this.direction.getIndex());
      compound.putInt("Steps", this.steps);
      compound.putDouble("TXD", this.targetDeltaX);
      compound.putDouble("TYD", this.targetDeltaY);
      compound.putDouble("TZD", this.targetDeltaZ);
   }

   protected void readAdditional(CompoundNBT compound) {
      if (compound.hasUniqueId("Owner")) this.owner = compound.getUniqueId("Owner");
      this.leftOwner = compound.getBoolean("LeftOwner");
      this.steps = compound.getInt("Steps");
      this.targetDeltaX = compound.getDouble("TXD");
      this.targetDeltaY = compound.getDouble("TYD");
      this.targetDeltaZ = compound.getDouble("TZD");
      if (compound.contains("Dir", 99)) this.direction = Direction.byIndex(compound.getInt("Dir"));
      if (compound.hasUniqueId("Target")) this.targetUniqueId = compound.getUniqueId("Target");
   }

   @SuppressWarnings("deprecation")
   public void tick() {
      if (!this.leftOwner) {
         this.leftOwner = this.func_234615_h_();
      }
      super.tick();
      if (!this.world.isRemote) {
         if (this.target == null && this.targetUniqueId != null) {
            this.target = ((ServerWorld)this.world).getEntityByUuid(this.targetUniqueId);
            if (this.target == null) {
               this.targetUniqueId = null;
            }
         }
         if (this.target == null || !this.target.isAlive() || this.target instanceof PlayerEntity && ((PlayerEntity)this.target).isSpectator()) {
            if (!this.hasNoGravity()) {
               this.setMotion(this.getMotion().add(0.0D, -0.04D, 0.0D));
            }
         } else {
            this.targetDeltaX = MathHelper.clamp(this.targetDeltaX * 1.025D, -1.0D, 1.0D);
            this.targetDeltaY = MathHelper.clamp(this.targetDeltaY * 1.025D, -1.0D, 1.0D);
            this.targetDeltaZ = MathHelper.clamp(this.targetDeltaZ * 1.025D, -1.0D, 1.0D);
            Vector3d vector3d = this.getMotion();
            this.setMotion(vector3d.add((this.targetDeltaX - vector3d.x) * 0.2D, (this.targetDeltaY - vector3d.y) * 0.2D, (this.targetDeltaZ - vector3d.z) * 0.2D));
         }

         RayTraceResult raytraceresult = ProjectileHelper.func_234618_a_(this, this::func_230298_a_);
            if (raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
            this.onImpact(raytraceresult);
         }
      }
      this.doBlockCollisions();
      Vector3d vector3d1 = this.getMotion();
      this.setPosition(this.getPosX() + vector3d1.x, this.getPosY() + vector3d1.y, this.getPosZ() + vector3d1.z);
      ProjectileHelper.rotateTowardsMovement(this, 0.5F);
      if (this.world.isRemote) {
         this.world.addParticle(ParticleTypes.EFFECT, this.getPosX() - vector3d1.x, this.getPosY() - vector3d1.y + 0.15D, this.getPosZ() - vector3d1.z, 0.0D, 0.0D, 0.0D);
      } else if (this.target != null && !this.target.removed) {
         if (this.steps > 0) {
            --this.steps;
            if (this.steps == 0) {
               this.selectNextMoveDirection(this.direction == null ? null : this.direction.getAxis());
            }
         }
         if (this.direction != null) {
            BlockPos blockpos = this.getPosition();
            Direction.Axis direction$axis = this.direction.getAxis();
            if (this.world.isTopSolid(blockpos.offset(this.direction), this)) {
               this.selectNextMoveDirection(direction$axis);
            } else {
               BlockPos blockpos1 = this.target.getPosition();
               if (direction$axis == Direction.Axis.X && blockpos.getX() == blockpos1.getX() || direction$axis == Direction.Axis.Z && blockpos.getZ() == blockpos1.getZ() || direction$axis == Direction.Axis.Y && blockpos.getY() == blockpos1.getY()) {
                  this.selectNextMoveDirection(direction$axis);
               }
            }
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void handleStatusUpdate(byte id) {
      if (id == 13) {
         this.world.addParticle(ParticleTypes.CRIMSON_SPORE, this.getPosXRandom(0.30), this.getPosYRandom(), this.getPosZRandom(0.30), 0.0D, 0.0D, 0.0D);
      } else {
         super.handleStatusUpdate(id);
      }
   }

   private boolean func_234615_h_() {
      Entity entity = this.getShooter();
      if (entity != null) {
         for(Entity entity1 : this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox().expand(this.getMotion()).grow(1.0D), (p_234613_0_) -> {
            return !p_234613_0_.isSpectator() && p_234613_0_.canBeCollidedWith();
         })) {
            if (entity1.getLowestRidingEntity() == entity.getLowestRidingEntity()) {
               return false;
            }
         }
      }
      return true;
   }

   public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
      Vector3d vector3d = (new Vector3d(x, y, z)).normalize().add(this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy).scale((double)velocity);
      this.setMotion(vector3d);
      float f = MathHelper.sqrt(horizontalMag(vector3d));
      this.rotationYaw = (float)(MathHelper.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI));
      this.rotationPitch = (float)(MathHelper.atan2(vector3d.y, (double)f) * (double)(180F / (float)Math.PI));
      this.prevRotationYaw = this.rotationYaw;
      this.prevRotationPitch = this.rotationPitch;
   }

   public void setDirectionAndMovement(Entity projectile, float x, float y, float z, float velocity, float inaccuracy) {
      float f = -MathHelper.sin(y * ((float)Math.PI / 180F)) * MathHelper.cos(x * ((float)Math.PI / 180F));
      float f1 = -MathHelper.sin((x + z) * ((float)Math.PI / 180F));
      float f2 = MathHelper.cos(y * ((float)Math.PI / 180F)) * MathHelper.cos(x * ((float)Math.PI / 180F));
      this.shoot((double)f, (double)f1, (double)f2, velocity, inaccuracy);
      Vector3d vector3d = projectile.getMotion();
      this.setMotion(this.getMotion().add(vector3d.x, projectile.isOnGround() ? 0.0D : vector3d.y, vector3d.z));
   }

   protected void onImpact(RayTraceResult result) {
      RayTraceResult.Type raytraceresult$type = result.getType();
      if (raytraceresult$type == RayTraceResult.Type.ENTITY) {
         this.onEntityHit((EntityRayTraceResult)result);
      } else if (raytraceresult$type == RayTraceResult.Type.BLOCK) {
         this.func_230299_a_((BlockRayTraceResult)result);
      }
      this.remove();
   }

   protected void onEntityHit(EntityRayTraceResult result) {
      Entity entity = result.getEntity();
      CreatureEntity entity1 = (CreatureEntity)this.getShooter();
      LivingEntity livingentity = entity1 instanceof LivingEntity ? (LivingEntity)entity1 : null;
      boolean flag = entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, livingentity).setProjectile(), 4.0F);
      if (flag) {
         this.applyEnchantments(livingentity, entity);
         if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).addPotionEffect(new EffectInstance(Effects.WEAKNESS, 200));
            entity1.heal(5F);
         }
      }
   }

   protected void func_230299_a_(BlockRayTraceResult result) {
      // BlockState blockstate = this.world.getBlockState(result.getPos());
      // blockstate.onProjectileCollision(this.world, blockstate, result, this);
      ((ServerWorld)this.world).spawnParticle(ParticleTypes.EXPLOSION, this.getPosX(), this.getPosY(), this.getPosZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
      this.playSound(SoundEvents.ENTITY_SHULKER_BULLET_HIT, 1.0F, 1.0F);
   }

   @OnlyIn(Dist.CLIENT)
   public void setVelocity(double x, double y, double z) {
      this.setMotion(x, y, z);
      if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
         float f = MathHelper.sqrt(x * x + z * z);
         this.rotationPitch = (float)(MathHelper.atan2(y, (double)f) * (double)(180F / (float)Math.PI));
         this.rotationYaw = (float)(MathHelper.atan2(x, z) * (double)(180F / (float)Math.PI));
         this.prevRotationPitch = this.rotationPitch;
         this.prevRotationYaw = this.rotationYaw;
         this.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, this.rotationPitch);
      }
   }

   protected boolean func_230298_a_(Entity entityIn) {
      if (!entityIn.isSpectator() && entityIn.isAlive() && entityIn.canBeCollidedWith()) {
         Entity entity = this.getShooter();
         return (entity == null || this.leftOwner || !entity.isRidingSameEntity(entityIn)) && !entityIn.noClip;
      } else {
         return false;
      }
   }

   protected void updatePitchAndYaw() {
      Vector3d vector3d = this.getMotion();
      float f = MathHelper.sqrt(horizontalMag(vector3d));
      this.rotationPitch = func_234614_e_(this.prevRotationPitch, (float)(MathHelper.atan2(vector3d.y, (double)f) * (double)(180F / (float)Math.PI)));
      this.rotationYaw = func_234614_e_(this.prevRotationYaw, (float)(MathHelper.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI)));
   }

   protected static float func_234614_e_(float p_234614_0_, float p_234614_1_) {
      while(p_234614_1_ - p_234614_0_ < -180.0F) { p_234614_0_ -= 360.0F; }
      while(p_234614_1_ - p_234614_0_ >= 180.0F) { p_234614_0_ += 360.0F; }
      return MathHelper.lerp(0.2F, p_234614_0_, p_234614_1_);
   }

   public SoundCategory getSoundCategory() { return SoundCategory.HOSTILE; }
   public void checkDespawn() { if (this.world.getDifficulty() == Difficulty.PEACEFUL) this.remove(); }
   @Override
   protected void registerData() {   }
   public boolean isBurning() { return false; }
   @OnlyIn(Dist.CLIENT)
   public boolean isInRangeToRenderDist(double distance) { return distance < 16384.0D; }
   public float getBrightness() { return 1.0F; }
   public boolean canBeCollidedWith() { return true; }
   public IPacket<?> createSpawnPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
}