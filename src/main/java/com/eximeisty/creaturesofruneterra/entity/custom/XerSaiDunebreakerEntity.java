package com.eximeisty.creaturesofruneterra.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;

public class XerSaiDunebreakerEntity extends PathfinderMob implements GeoEntity {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(XerSaiDunebreakerEntity.class, EntityDataSerializers.BYTE);
    public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(XerSaiDunebreakerEntity.class, EntityDataSerializers.INT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int AttackCD=0;
    private int HeavyCD=100;
    private int ticks=0;
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().then("animation.xersai_dunebreaker.idle", Animation.LoopType.LOOP);
    private static final RawAnimation  WALK_ANIM = RawAnimation.begin().then("animation.xersai_dunebreaker.walk", Animation.LoopType.LOOP);
    private static final RawAnimation  ATTACK_ANIM = RawAnimation.begin().then("animation.xersai_dunebreaker.attack", Animation.LoopType.PLAY_ONCE );
    private static final RawAnimation  HEAVY_ANIM = RawAnimation.begin().then("animation.xersai_dunebreaker.heavy", Animation.LoopType.PLAY_ONCE);

    public XerSaiDunebreakerEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier setAttributes(){
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 65)
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 15)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.ATTACK_KNOCKBACK, 0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10)
                .add(Attributes.ATTACK_SPEED, 0.3).build();
    }

    @Override
    protected void registerGoals(){
        super.registerGoals();
        this.goalSelector.addGoal( 1, new FloatGoal(this));
        this.goalSelector.addGoal( 2, new NearestAttackableTargetGoal<>( this, Player.class, true ));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(9, new RandomStrollGoal(this, 0.35,80));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this)));//.setAlertOthers(XerSaiHatchlingEntity.class));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, Villager.class, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState)  {
            if (tAnimationState.isMoving()) {
                tAnimationState.getController().setAnimation(WALK_ANIM);
                return PlayState.CONTINUE;
            }
            tAnimationState.getController().setAnimation(IDLE_ANIM);
            return PlayState.CONTINUE;
    }

    public <T extends GeoAnimatable> PlayState predicate2(AnimationState<T> tAnimationState)  {
        if ( entityData.get(STATE) == 1 ) {
            tAnimationState.getController().setAnimation(ATTACK_ANIM);
            return PlayState.CONTINUE;
        }
        if ( entityData.get(STATE) == 2 ) {
            tAnimationState.getController().setAnimation(HEAVY_ANIM);
            return PlayState.CONTINUE;
        }
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

    @Override
    protected void tickLeash() {
        super.tickLeash();
        this.dropLeash(true, true);
    }

    protected PathNavigation createNavigation(Level p_33802_) {
        return new WallClimberNavigation(this, p_33802_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
        this.entityData.define(STATE, 0);
    }

    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }
    }

    public boolean onClimbable() {
        return this.isClimbing();
    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean p_33820_) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (p_33820_) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    public class MeleeAttackGoal extends Goal {
        protected final PathfinderMob attacker;
        private final double speedTowardsTarget;
        private final boolean longMemory;
        private Path path;
        private double targetX;
        private double targetY;
        private double targetZ;

        public MeleeAttackGoal(PathfinderMob creature, double speedIn, boolean useLongMemory) {
            this.attacker = creature;
            this.speedTowardsTarget = speedIn;
            this.longMemory = useLongMemory;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.attacker.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                if(this.attacker.distanceToSqr(livingentity)>=10){
                    this.path = this.attacker.getNavigation().createPath(livingentity, 0);
                }else{
                    return true;
                }
                if (this.path != null) {
                    return true;
                } else {
                    return this.getAttackReachSqr(livingentity) >= this.attacker.distanceToSqr(livingentity);
                }
            }
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = this.attacker.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else if (!this.longMemory) {
                return !this.attacker.getNavigation().isDone();
            } else if (!this.attacker.isWithinRestriction(livingentity.blockPosition())) {
                return false;
            } else {
                return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative();
            }
        }

        public void start() {
            this.attacker.getNavigation().moveTo(this.path, this.speedTowardsTarget);
            this.attacker.setAggressive(true);
        }

        public void stop() {
            LivingEntity livingentity = this.attacker.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
                this.attacker.setTarget((LivingEntity)null);
            }
            this.attacker.setAggressive(false);
            this.attacker.getNavigation().stop();
        }

        public void tick() {
            if(AttackCD>=0) --AttackCD;
            if(HeavyCD>=0) --HeavyCD;

            LivingEntity livingentity = this.attacker.getTarget();
            this.attacker.getLookControl().setLookAt(livingentity);
            double d0 = this.attacker.distanceToSqr(livingentity);
            if ((this.longMemory || this.attacker.getSensing().hasLineOfSight(livingentity)) && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRandom().nextFloat() < 0.05F)) {
                this.targetX = livingentity.getX(); this.targetY = livingentity.getY(); this.targetZ = livingentity.getZ();
            }

            this.checkAndPerformAttack(livingentity, d0);
        }

        public void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            this.attacker.getLookControl().setLookAt(enemy);
            if (distToEnemySqr<=15 && AttackCD<=0 && ticks==0) {
                ++ticks;
                if(HeavyCD<=0){
                    entityData.set(STATE, 2);
                    HeavyCD=200;
                }else{
                    entityData.set(STATE, 1);
                }
            }

            //Anim Notifys
            if(ticks>0){
                if(entityData.get(STATE)==1){
                    if(ticks==17/2+2 && distToEnemySqr<=22){
                        this.attacker.doHurtTarget(enemy);
                    }
                    if(ticks==31/2+2){
                        entityData.set(STATE, 0);
                    }
                }
                if(entityData.get(STATE)==2){
                    if(ticks==1){
                        this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier("speed",-0.35,AttributeModifier.Operation.ADDITION));
                        this.attacker.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("damage",10,AttributeModifier.Operation.ADDITION));
                    }
                    if(distToEnemySqr<=22 && ticks==30/2+2){
                        this.attacker.doHurtTarget(enemy);
                        enemy.lerpMotion(this.attacker.getLookAngle().x*2, 2, this.attacker.getLookAngle().z*2);
                    }
                    if(ticks==45/2+2){
                        entityData.set(STATE, 0);
                        this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier("speed",0.35,AttributeModifier.Operation.ADDITION));
                        this.attacker.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("damage",-10,AttributeModifier.Operation.ADDITION));
                    }
                }
                ++ticks;
                if(entityData.get(STATE)==0){
                    this.resetAttackCD();
                    ticks=0;
                }
            }
        }

        protected void resetAttackCD() {
            AttackCD = 20;
        }

        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return (double)(this.attacker.getBbWidth() * 2.0F * this.attacker.getBbWidth() * 2.0F + attackTarget.getBbWidth());
        }
    }

    @Override
    public int getExperienceReward(){ return 10+this.level.random.nextInt(30); }
    @Override
    public boolean causeFallDamage(float p_146828_, float p_146829_, DamageSource p_146830_) { return false; }
    @Override
    public boolean canBeLeashed(Player p_21418_) { return false; }
    @Override
    public boolean isPersistenceRequired() { return true; }
    @Override
    public boolean requiresCustomPersistence() { return true; }
    @Override
    protected boolean shouldDespawnInPeaceful() { return true; }
    protected SoundEvent getAmbientSound() { return SoundEvents.ZOGLIN_AMBIENT; }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SoundEvents.STRIDER_HURT; }
    protected SoundEvent getDeathSound() { return SoundEvents.STRIDER_DEATH; }
}