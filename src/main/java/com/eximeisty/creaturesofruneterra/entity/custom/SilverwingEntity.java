package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.UUID;

public class SilverwingEntity extends TamableAnimal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final int TICKS_PER_FLAP = Mth.ceil(24.166098F);
    Vec3 moveTargetPoint = Vec3.ZERO;
    BlockPos anchorPoint = BlockPos.ZERO;
    SilverwingEntity.AttackPhase attackPhase = SilverwingEntity.AttackPhase.CIRCLE;

    static enum AttackPhase {
        CIRCLE,
        SWOOP;
    }
    public SilverwingEntity(EntityType<? extends TamableAnimal> p_21803_, Level p_21804_) {
        super(p_21803_, p_21804_);
        this.moveControl = new SilverwingEntity.PhantomMoveControl(this);
        this.lookControl = new SilverwingEntity.PhantomLookControl(this);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new SilverwingEntity.PhantomAttackStrategyGoal());
        this.goalSelector.addGoal(2, new SilverwingEntity.PhantomSweepAttackGoal());
        this.goalSelector.addGoal(3, new SilverwingEntity.PhantomCircleAroundAnchorGoal());
        this.targetSelector.addGoal(1, new SilverwingEntity.PhantomAttackPlayerTargetGoal());
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
    }

    public static AttributeSupplier setAttributes(){
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.MOVEMENT_SPEED, 0.8)
                .add(Attributes.ATTACK_DAMAGE, 7)
                .add(Attributes.FOLLOW_RANGE, 70)
                .add(Attributes.ATTACK_KNOCKBACK, 0)
                .add(Attributes.ATTACK_SPEED, 0.8).build();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        SilverwingEntity silverwing = ModEntities.SILVERWING.get().create(p_146743_);
        if (silverwing != null) {
            UUID uuid = this.getOwnerUUID();
            if (uuid != null) {
                silverwing.setOwnerUUID(uuid);
                silverwing.setTame(true);
            }
        }

        return silverwing;
    }

    public boolean isFlapping() {
        return (this.getUniqueFlapTickOffset() + this.tickCount) % TICKS_PER_FLAP == 0;
    }

    public int getUniqueFlapTickOffset() {
        return this.getId() * 3;
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            float f = Mth.cos((float)(this.getUniqueFlapTickOffset() + this.tickCount) * 7.448451F * ((float)Math.PI / 180F) + (float)Math.PI);
            float f1 = Mth.cos((float)(this.getUniqueFlapTickOffset() + this.tickCount + 1) * 7.448451F * ((float)Math.PI / 180F) + (float)Math.PI);
            if (f > 0.0F && f1 <= 0.0F && !this.onGround()) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.PHANTOM_FLAP, this.getSoundSource(), 4.5F + this.random.nextFloat() * 0.05F, 0.2F + this.random.nextFloat() * 0.05F, false);
            }
        }
    }
    
    //FLYING------------------------------------------------------------------------------------------------------------
    protected void checkFallDamage(double p_20809_, boolean p_20810_, BlockState p_20811_, BlockPos p_20812_) {
    }

    public boolean onClimbable() {
        return false;
    }

    class PhantomAttackPlayerTargetGoal extends Goal {
        private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0D);
        private int nextScanTick = reducedTickDelay(20);

        public boolean canUse() {
            if (this.nextScanTick > 0) {
                --this.nextScanTick;
                return false;
            } else {
                this.nextScanTick = reducedTickDelay(60);
                Animal target = SilverwingEntity.this.level().getNearestEntity(Animal.class, attackTargeting, SilverwingEntity.this, SilverwingEntity.this.getX(), SilverwingEntity.this.getY(), SilverwingEntity.this.getZ(), SilverwingEntity.this.getBoundingBox().inflate(16.0D, 64.0D, 16.0D));
                if(target!=null){
                    if (SilverwingEntity.this.canAttack(target, TargetingConditions.DEFAULT)) {
                        SilverwingEntity.this.setTarget(target);
                        return true;
                    }
                }
                return false;
            }
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = SilverwingEntity.this.getTarget();
            return livingentity != null ? SilverwingEntity.this.canAttack(livingentity, TargetingConditions.DEFAULT) : false;
        }
    }

    class PhantomAttackStrategyGoal extends Goal {
        private int nextSweepTick;

        public boolean canUse() {
            LivingEntity livingentity = SilverwingEntity.this.getTarget();
            return livingentity != null ? SilverwingEntity.this.canAttack(livingentity, TargetingConditions.DEFAULT) : false;
        }

        public void start() {
            this.nextSweepTick = this.adjustedTickDelay(10);
            SilverwingEntity.this.attackPhase = SilverwingEntity.AttackPhase.CIRCLE;
            this.setAnchorAboveTarget();
        }

        public void stop() {
            SilverwingEntity.this.anchorPoint = SilverwingEntity.this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, SilverwingEntity.this.anchorPoint).above(10 + SilverwingEntity.this.random.nextInt(20));
        }

        public void tick() {
            if (SilverwingEntity.this.attackPhase == SilverwingEntity.AttackPhase.CIRCLE) {
                --this.nextSweepTick;
                if (this.nextSweepTick <= 0) {
                    SilverwingEntity.this.attackPhase = SilverwingEntity.AttackPhase.SWOOP;
                    this.setAnchorAboveTarget();
                    this.nextSweepTick = this.adjustedTickDelay((8 + SilverwingEntity.this.random.nextInt(4)) * 20);
                }
            }

        }

        private void setAnchorAboveTarget() {
            SilverwingEntity.this.anchorPoint = SilverwingEntity.this.getTarget().blockPosition().above(20 + SilverwingEntity.this.random.nextInt(20));
            if (SilverwingEntity.this.anchorPoint.getY() < SilverwingEntity.this.level().getSeaLevel()) {
                SilverwingEntity.this.anchorPoint = new BlockPos(SilverwingEntity.this.anchorPoint.getX(), SilverwingEntity.this.level().getSeaLevel() + 1, SilverwingEntity.this.anchorPoint.getZ());
            }

        }
    }

    class PhantomCircleAroundAnchorGoal extends SilverwingEntity.PhantomMoveTargetGoal {
        private float angle;
        private float distance;
        private float height;
        private float clockwise;

        public boolean canUse() {
            return SilverwingEntity.this.getTarget() == null || SilverwingEntity.this.attackPhase == SilverwingEntity.AttackPhase.CIRCLE;
        }

        public void start() {
            this.distance = 5.0F + SilverwingEntity.this.random.nextFloat() * 10.0F;
            this.height = -4.0F + SilverwingEntity.this.random.nextFloat() * 9.0F;
            this.clockwise = SilverwingEntity.this.random.nextBoolean() ? 1.0F : -1.0F;
            this.selectNext();
        }

        public void tick() {
            if (SilverwingEntity.this.random.nextInt(this.adjustedTickDelay(350)) == 0) {
                this.height = -4.0F + SilverwingEntity.this.random.nextFloat() * 9.0F;
            }

            if (SilverwingEntity.this.random.nextInt(this.adjustedTickDelay(250)) == 0) {
                ++this.distance;
                if (this.distance > 15.0F) {
                    this.distance = 5.0F;
                    this.clockwise = -this.clockwise;
                }
            }

            if (SilverwingEntity.this.random.nextInt(this.adjustedTickDelay(450)) == 0) {
                this.angle = SilverwingEntity.this.random.nextFloat() * 2.0F * (float)Math.PI;
                this.selectNext();
            }

            if (this.touchingTarget()) {
                this.selectNext();
            }

            if (SilverwingEntity.this.moveTargetPoint.y < SilverwingEntity.this.getY() && !SilverwingEntity.this.level().isEmptyBlock(SilverwingEntity.this.blockPosition().below(1))) {
                this.height = Math.max(1.0F, this.height);
                this.selectNext();
            }

            if (SilverwingEntity.this.moveTargetPoint.y > SilverwingEntity.this.getY() && !SilverwingEntity.this.level().isEmptyBlock(SilverwingEntity.this.blockPosition().above(1))) {
                this.height = Math.min(-1.0F, this.height);
                this.selectNext();
            }

        }

        private void selectNext() {
            if (BlockPos.ZERO.equals(SilverwingEntity.this.anchorPoint)) {
                SilverwingEntity.this.anchorPoint = SilverwingEntity.this.blockPosition();
            }

            this.angle += this.clockwise * 15.0F * ((float)Math.PI / 180F);
            SilverwingEntity.this.moveTargetPoint = Vec3.atLowerCornerOf(SilverwingEntity.this.anchorPoint).add((double)(this.distance * Mth.cos(this.angle)), (double)(-4.0F + this.height), (double)(this.distance * Mth.sin(this.angle)));
        }
    }

    class PhantomLookControl extends LookControl {
        public PhantomLookControl(Mob p_33235_) {
            super(p_33235_);
        }

        public void tick() {
        }
    }

    class PhantomMoveControl extends MoveControl {
        private float speed = 0.1F;

        public PhantomMoveControl(Mob p_33241_) {
            super(p_33241_);
        }

        public void tick() {
            if (SilverwingEntity.this.horizontalCollision) {
                SilverwingEntity.this.setYRot(SilverwingEntity.this.getYRot() + 180.0F);
                this.speed = 0.1F;
            }

            double d0 = SilverwingEntity.this.moveTargetPoint.x - SilverwingEntity.this.getX();
            double d1 = SilverwingEntity.this.moveTargetPoint.y - SilverwingEntity.this.getY();
            double d2 = SilverwingEntity.this.moveTargetPoint.z - SilverwingEntity.this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            if (Math.abs(d3) > (double)1.0E-5F) {
                double d4 = 1.0D - Math.abs(d1 * (double)0.7F) / d3;
                d0 *= d4;
                d2 *= d4;
                d3 = Math.sqrt(d0 * d0 + d2 * d2);
                double d5 = Math.sqrt(d0 * d0 + d2 * d2 + d1 * d1);
                float f = SilverwingEntity.this.getYRot();
                float f1 = (float)Mth.atan2(d2, d0);
                float f2 = Mth.wrapDegrees(SilverwingEntity.this.getYRot() + 90.0F);
                float f3 = Mth.wrapDegrees(f1 * (180F / (float)Math.PI));
                SilverwingEntity.this.setYRot(Mth.approachDegrees(f2, f3, 4.0F) - 90.0F);
                SilverwingEntity.this.yBodyRot = SilverwingEntity.this.getYRot();
                if (Mth.degreesDifferenceAbs(f, SilverwingEntity.this.getYRot()) < 3.0F) {
                    this.speed = Mth.approach(this.speed, 1.8F, 0.005F * (1.8F / this.speed));
                } else {
                    this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
                }

                float f4 = (float)(-(Mth.atan2(-d1, d3) * (double)(180F / (float)Math.PI)));
                SilverwingEntity.this.setXRot(f4);
                float f5 = SilverwingEntity.this.getYRot() + 90.0F;
                double d6 = (double)(this.speed * Mth.cos(f5 * ((float)Math.PI / 180F))) * Math.abs(d0 / d5);
                double d7 = (double)(this.speed * Mth.sin(f5 * ((float)Math.PI / 180F))) * Math.abs(d2 / d5);
                double d8 = (double)(this.speed * Mth.sin(f4 * ((float)Math.PI / 180F))) * Math.abs(d1 / d5);
                Vec3 vec3 = SilverwingEntity.this.getDeltaMovement();
                SilverwingEntity.this.setDeltaMovement(vec3.add((new Vec3(d6, d8, d7)).subtract(vec3).scale(0.2D)));
            }
        }
    }

    abstract class PhantomMoveTargetGoal extends Goal {
        public PhantomMoveTargetGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        protected boolean touchingTarget() {
            return SilverwingEntity.this.moveTargetPoint.distanceToSqr(SilverwingEntity.this.getX(), SilverwingEntity.this.getY(), SilverwingEntity.this.getZ()) < 4.0D;
        }
    }

    class PhantomSweepAttackGoal extends SilverwingEntity.PhantomMoveTargetGoal {

        public boolean canUse() {
            return SilverwingEntity.this.getTarget() != null && SilverwingEntity.this.attackPhase == SilverwingEntity.AttackPhase.SWOOP;
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = SilverwingEntity.this.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                if (livingentity instanceof Player) {
                    Player player = (Player)livingentity;
                    if (livingentity.isSpectator() || player.isCreative()) {
                        return false;
                    }
                }
                if (!this.canUse()) {
                    return false;
                } else {
                    return true;
                }
            }
        }

        public void start() {
        }

        public void stop() {
            SilverwingEntity.this.setTarget((LivingEntity)null);
            SilverwingEntity.this.attackPhase = SilverwingEntity.AttackPhase.CIRCLE;
        }

        public void tick() {
            LivingEntity livingentity = SilverwingEntity.this.getTarget();
            if (livingentity != null) {
                SilverwingEntity.this.moveTargetPoint = new Vec3(livingentity.getX(), livingentity.getY(0.5D), livingentity.getZ());
                if (SilverwingEntity.this.getBoundingBox().inflate((double)0.2F).intersects(livingentity.getBoundingBox())) {
                    SilverwingEntity.this.doHurtTarget(livingentity);
                    SilverwingEntity.this.attackPhase = SilverwingEntity.AttackPhase.CIRCLE;
                    if (!SilverwingEntity.this.isSilent()) {
                        SilverwingEntity.this.level().levelEvent(1039, SilverwingEntity.this.blockPosition(), 0);
                    }
                } else if (SilverwingEntity.this.horizontalCollision || SilverwingEntity.this.hurtTime > 0) {
                    SilverwingEntity.this.attackPhase = SilverwingEntity.AttackPhase.CIRCLE;
                }
            }
        }
    }
    //END---FLYING------------------------------------------------------------------------------------------------------
    
    public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState)  {
        if (tAnimationState.isMoving()) {
            //tAnimationState.getController().setAnimation(WALK_ANIM);
            return PlayState.CONTINUE;
        }
        //tAnimationState.getController().setAnimation(IDLE_ANIM);
        return PlayState.CONTINUE;
    }

    public <T extends GeoAnimatable> PlayState predicate2(AnimationState<T> tAnimationState)  {
        tAnimationState.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers){
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
        controllers.add(new AnimationController<>(this, "attacks", 0, this::predicate2));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}