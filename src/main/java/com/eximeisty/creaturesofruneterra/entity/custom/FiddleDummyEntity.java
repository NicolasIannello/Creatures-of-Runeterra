package com.eximeisty.creaturesofruneterra.entity.custom;

import java.util.EnumSet;
import java.util.function.Predicate;

import com.eximeisty.creaturesofruneterra.util.ModSoundEvents;

import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.Path;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class FiddleDummyEntity extends PathfinderMob implements IAnimatable, IAnimationTickable {
    public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(FiddlesticksEntity.class, EntityDataSerializers.INT);
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private int parent;
    private int ticks=0;
    private static final AnimationBuilder CHANNEL_ANIM = new AnimationBuilder().addAnimation("animation.Fiddlesticks.channel", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("animation.Fiddlesticks.channelloop", ILoopType.EDefaultLoopTypes.LOOP);
    private static final AnimationBuilder DEATH_ANIM = new AnimationBuilder().addAnimation("animation.Fiddlesticks.channeldummy", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("animation.Fiddlesticks.deathloop", ILoopType.EDefaultLoopTypes.LOOP);
    private static final Predicate<LivingEntity> NOT_THIS = (p_213797_0_) -> {
        if(!(p_213797_0_ instanceof Player) && (p_213797_0_ instanceof FiddlesticksEntity || p_213797_0_ instanceof FiddleDummyEntity || p_213797_0_ instanceof WaterAnimal || (p_213797_0_.isInWaterOrBubble() || p_213797_0_.getLevel().getBlockState(new BlockPos(p_213797_0_.getX(),p_213797_0_.getY()-1,p_213797_0_.getZ()))== Blocks.WATER.defaultBlockState()))) return false;
        return true;
    };

    public FiddleDummyEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier setAttributes(){
        return PathfinderMob.createMobAttributes().add(Attributes.MAX_HEALTH, 20)
        .add(Attributes.MOVEMENT_SPEED, 0)
        .add(Attributes.FOLLOW_RANGE, 80).build();
    }

    @Override
    protected void registerGoals(){
        super.registerGoals();
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>( this, Player.class, false ));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1D,50));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>( this, Mob.class, 0, false, false, NOT_THIS));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(STATE, 0);
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        AnimationBuilder anim= (entityData.get(STATE)==0) ? CHANNEL_ANIM : DEATH_ANIM;
        event.getController().setAnimation(anim);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data){
        data.addAnimationController(new AnimationController<IAnimatable>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory(){
        return this.factory;
    }

    public void tick() {
        super.tick();
        ticks++;
        if(ticks==400) entityData.set(STATE, 1); 
        if(ticks>460){
            this.kill();
        }
        if(this.getHealth()<=1) entityData.set(STATE, 1);
    }

    @Override
    protected void tickLeash() {
        super.tickLeash();
        this.dropLeash(true, false);
        return;
    }

    public void setParent(FiddlesticksEntity fiddle){
        parent = fiddle.getId();
    }

    public FiddlesticksEntity getParent(){
        return (FiddlesticksEntity)this.level.getEntity(parent);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("id", parent);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        parent = compound.getInt("id");
    }

    public class MeleeAttackGoal extends Goal {
        protected final FiddleDummyEntity attacker;
        private final double speedTowardsTarget;
        private final boolean longMemory;
        private Path path;
        private double targetX;
        private double targetY;
        private double targetZ;
        private int ticks=0;

        public MeleeAttackGoal(FiddleDummyEntity creature, double speedIn, boolean useLongMemory) {
            this.attacker = creature;
            this.speedTowardsTarget = speedIn;
            this.longMemory = useLongMemory;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }
        
        public boolean canUse() {
            LivingEntity livingentity = this.attacker.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                this.path = this.attacker.getNavigation().createPath(livingentity, 0);
                if (this.path != null) {
                    return true;
                } else {
                    return this.getAttackReachSqr(livingentity) >= this.attacker.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
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
            this.attacker.getNavigation().isDone();
        }
        
        public void tick() {
            LivingEntity livingentity = this.attacker.getTarget();
            double d0 = this.attacker.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());

            if ((this.longMemory || this.attacker.getSensing().hasLineOfSight(livingentity)) && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRandom().nextFloat() < 0.05F)) {
                this.targetX = livingentity.getX(); this.targetY = livingentity.getY(); this.targetZ = livingentity.getZ();
            }

            this.checkAndPerformAttack(livingentity, d0);
        }

        public void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            ticks++;
            if(ticks%10==0) {
                this.attacker.level.addFreshEntity(new FiddleProyectileEntity(this.attacker.level, this.attacker, this.attacker.getTarget(), Direction.DOWN.getAxis(), getParent()));
                this.attacker.level.playSound(null, this.attacker.blockPosition(), SoundEvents.SOUL_ESCAPE, SoundSource.AMBIENT, (float)(Math.random() * 5)+5, (float)(Math.random() * 2)+1);
            }
        }
        
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return (double)(this.attacker.getBbWidth() * 2.0F * this.attacker.getBbWidth() * 2.0F + attackTarget.getBbWidth());
        }
    }

    @Override
    public int getExperienceReward(){ return 5+this.level.random.nextInt(5); }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn){ return ModSoundEvents.FIDDLESTICKS_HURT.get(); }
    @Override
    public boolean addEffect(MobEffectInstance effectInstanceIn, @Nullable Entity p_147209_) { return false; }
//    @Override
//    protected boolean canBeRidden(Entity entityIn) { return false; }
//    @Override
//    public boolean canPassengerSteer() { return false; }
    @Override
    public boolean shouldRiderSit() { return false; }
//    @Override
//    public boolean canBeRiddenInWater(Entity rider) { return true; }
    @Override
    public boolean canChangeDimensions() { return false; }
//    @Override
//    public boolean canDespawn(double distanceToClosestPlayer) { return false; }
    @Override
    public boolean isPersistenceRequired() { return true; }
    @Override
    public boolean requiresCustomPersistence() { return true; }
    @Override
    public int tickTimer() { return tickCount; }
}