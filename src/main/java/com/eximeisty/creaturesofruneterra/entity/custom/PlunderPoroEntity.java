package com.eximeisty.creaturesofruneterra.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.EnumSet;

public class PlunderPoroEntity extends TamableAnimal implements IAnimatable, RangedAttackMob {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final EntityDataAccessor<Boolean> STATE = SynchedEntityData.defineId(PlunderPoroEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ATTACK = SynchedEntityData.defineId(PlunderPoroEntity.class, EntityDataSerializers.BOOLEAN);
    private int ticks=0;
    public ItemStack forgeItem=ItemStack.EMPTY;
    private static final AnimationBuilder IDLE_ANIM = new AnimationBuilder().addAnimation("animation.plunderporo.idle", ILoopType.EDefaultLoopTypes.LOOP);
    private static final AnimationBuilder WALK_ANIM = new AnimationBuilder().addAnimation("animation.plunderporo.walk", ILoopType.EDefaultLoopTypes.LOOP);
    private static final AnimationBuilder SIT_ANIM = new AnimationBuilder().addAnimation("animation.plunderporo.sit", ILoopType.EDefaultLoopTypes.LOOP);
    private static final AnimationBuilder SHOT_ANIM = new AnimationBuilder().addAnimation("animation.plunderporo.shoot", ILoopType.EDefaultLoopTypes.PLAY_ONCE);

    public PlunderPoroEntity(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
        this.setTame(false);
    }

    public static AttributeSupplier setAttributes(){
        return TamableAnimal.createMobAttributes().add(Attributes.MAX_HEALTH, 20)
        .add(Attributes.MOVEMENT_SPEED, 0.24)
        .add(Attributes.ATTACK_DAMAGE, 7)
        .add(Attributes.ATTACK_SPEED, 1.2)
        .add(Attributes.ARMOR, 3)
        .add(Attributes.ARMOR_TOUGHNESS, 1).build();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(5, new RangedAttackGoal(this, 1, 70, 13));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Monster.class, 3.0F, 1D, 1.4D));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(!entityData.get(ATTACK)){
            if(entityData.get(STATE)){
                event.getController().setAnimation(SIT_ANIM);
                return PlayState.CONTINUE;
            }
            if (event.isMoving()) {
                event.getController().setAnimation(WALK_ANIM);
                return PlayState.CONTINUE;
            }
            event.getController().setAnimation(IDLE_ANIM);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    public <E extends IAnimatable> PlayState predicate2(AnimationEvent<E> event) {
        if(entityData.get(ATTACK)){
            event.getController().setAnimation(SHOT_ANIM);
            return PlayState.CONTINUE;
        }
        event.getController().clearAnimationCache();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<IAnimatable>(this, "controller", 0, this::predicate));
        data.addAnimationController(new AnimationController<IAnimatable>(this, "attacks", 0, this::predicate2));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(STATE, false);
        entityData.define(ATTACK, false);
    }

    @Override @Nullable
    public PoroEntity getBreedOffspring(ServerLevel p_149088_, AgeableMob p_149089_) {
        return null;
    }

    public void tick() {
        super.tick();
        if(entityData.get(ATTACK)){
            ticks++;
            if(ticks==12){
                ticks=0;
                entityData.set(ATTACK, false);
            }
        }
    }

    public InteractionResult mobInteract(Player playerIn, InteractionHand hand) {
        if (this.level.isClientSide) {
            boolean flag = this.isOwnedBy(playerIn);
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        }else{
            if(this.isOwnedBy(playerIn)){
                this.setOrderedToSit(!entityData.get(STATE));
            }
            return super.mobInteract(playerIn, hand);
        } 
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.entityData.set(STATE, sit);
        super.setOrderedToSit(sit);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(STATE, compound.getBoolean("Sitting"));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float p_32142_) {
        AbstractArrow bulletEntity; //= this.getArrow(new ItemStack(Items.ARROW), (float) distanceFactor);
        bulletEntity = new BulletEntity(level, this);
        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333D) - bulletEntity.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = (double)Math.sqrt(d0 * d0 + d2 * d2);
        bulletEntity.setSoundEvent(SoundEvents.BLASTFURNACE_FIRE_CRACKLE);
        bulletEntity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.GENERIC_EXPLODE, 0.3F, 4.0F);
        bulletEntity.setCritArrow(true);
        bulletEntity.setBaseDamage(bulletEntity.getBaseDamage() + 0D);
        this.level.addFreshEntity(bulletEntity);
    }

    protected AbstractArrow getArrow(ItemStack p_32156_, float p_32157_) {
        return ProjectileUtil.getMobArrow(this, p_32156_, p_32157_);
    }

    public class RangedAttackGoal extends Goal {
        private final PlunderPoroEntity entityHost;
        private final PlunderPoroEntity rangedAttackEntityHost;
        private LivingEntity attackTarget;
        private int rangedAttackTime = -1;
        private final double entityMoveSpeed;
        private int seeTime;
        private final int attackIntervalMin;
        private final int maxRangedAttackTime;
        private final float attackRadius;
        private final float maxAttackDistance;
        private boolean strafingClockwise;
        private boolean strafingBackwards;
        private int strafingTime = -1;
     
        public RangedAttackGoal(PlunderPoroEntity attacker, double movespeed, int maxAttackTime, float maxAttackDistanceIn) {
            this(attacker, movespeed, maxAttackTime, maxAttackTime, maxAttackDistanceIn);
        }
     
        public RangedAttackGoal(PlunderPoroEntity attacker, double movespeed, int minAttackTime, int maxAttackTime, float maxAttackDistanceIn) {
            if (!(attacker instanceof LivingEntity)) {
                throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
            } else {
                this.rangedAttackEntityHost = attacker;
                this.entityHost = (PlunderPoroEntity)attacker;
                this.entityMoveSpeed = movespeed;
                this.attackIntervalMin = minAttackTime;
                this.maxRangedAttackTime = maxAttackTime;
                this.attackRadius = maxAttackDistanceIn;
                this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
                this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            }
        }
     
        public boolean canUse() {
            LivingEntity livingentity = this.entityHost.getTarget();
            if (livingentity != null && livingentity.isAlive()) {
                this.attackTarget = livingentity;
                return true;
            } else {
                return false;
            }
        }
     
        public boolean canContinueToUse() {
            return this.canUse() || !this.entityHost.getNavigation().isDone();
        }
     
        public void stop() {
            this.attackTarget = null;
            this.seeTime = 0;
            this.rangedAttackTime = -1;
        }
     
        public void tick() {
            double d0 = this.entityHost.distanceToSqr(this.attackTarget.getX(), this.attackTarget.getY(), this.attackTarget.getZ());
            boolean flag = this.entityHost.getSensing().hasLineOfSight(this.attackTarget);
            if (flag) {
                ++this.seeTime;
            } else {
                this.seeTime = 0;
            }

            if (!(d0 > (double)this.maxAttackDistance) && this.seeTime >= 20){
                this.entityHost.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.entityHost.getNavigation().moveTo(this.attackTarget, this.entityMoveSpeed);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if ((double)this.entityHost.getRandom().nextFloat() < 0.3D) this.strafingClockwise = !this.strafingClockwise;
                if ((double)this.entityHost.getRandom().nextFloat() < 0.3D) this.strafingBackwards = !this.strafingBackwards;
                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (d0 > (double)(this.maxAttackDistance * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (d0 < (double)(this.maxAttackDistance * 0.25F)) {
                    this.strafingBackwards = true;
                }
                this.entityHost.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.entityHost.lookAt(this.attackTarget, 30.0F, 30.0F);
            } else {
                this.entityHost.getLookControl().setLookAt(this.attackTarget, 30.0F, 30.0F);
            }       
                 
            if (--this.rangedAttackTime == 0) {
                if (!flag) return;
                double f = Math.sqrt(d0) / this.attackRadius;
                float lvt_5_1_ = (float) Math.fma(f, 0.1F, 1.0F);
                entityData.set(ATTACK, true);
                this.rangedAttackEntityHost.performRangedAttack(this.attackTarget, lvt_5_1_);
                this.rangedAttackTime = (int) Math.floor(f * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
            } else if (this.rangedAttackTime < 0) {
                double f2 = Math.sqrt(d0) / this.attackRadius;
                this.rangedAttackTime = (int) Math.floor(f2 * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
            }
        }
    }

    protected SoundEvent getDeathSound() { return SoundEvents.BAT_DEATH; }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SoundEvents.FOX_HURT; }

    public SoundEvent getAmbientSound() { return SoundEvents.FOX_AMBIENT; }
}