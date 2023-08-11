package com.eximeisty.creaturesofruneterra.entity.custom;

import java.util.EnumSet;
import java.util.function.Predicate;

import com.eximeisty.creaturesofruneterra.util.ModSoundEvents;

import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class FiddleDummyEntity extends CreatureEntity implements IAnimatable, IAnimationTickable {
    public static final DataParameter<Integer> STATE = EntityDataManager.createKey(FiddlesticksEntity.class, DataSerializers.VARINT);
    private AnimationFactory factory = new AnimationFactory(this);
    private int parent;
    private int ticks=0;
    private static final AnimationBuilder CHANNEL_ANIM = new AnimationBuilder().addAnimation("animation.Fiddlesticks.channel", false).addAnimation("animation.Fiddlesticks.channelloop", true);
    private static final AnimationBuilder DEATH_ANIM = new AnimationBuilder().addAnimation("animation.Fiddlesticks.channeldummy", false).addAnimation("animation.Fiddlesticks.deathloop", true);
    private static final Predicate<LivingEntity> NOT_THIS = (p_213797_0_) -> {
        if(!(p_213797_0_ instanceof PlayerEntity) && (p_213797_0_ instanceof FiddlesticksEntity || p_213797_0_ instanceof FiddleDummyEntity || p_213797_0_ instanceof WaterMobEntity || (p_213797_0_.isInWaterOrBubbleColumn() || p_213797_0_.getEntityWorld().getBlockState(new BlockPos(p_213797_0_.getPosX(),p_213797_0_.getPosY()-1,p_213797_0_.getPosZ()))==Blocks.WATER.getDefaultState()))) return false;
        return true;
    };

    public FiddleDummyEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 20)
        .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0)
        .createMutableAttribute(Attributes.FOLLOW_RANGE, 80);
    }

    @Override
    protected void registerGoals(){
        super.registerGoals();
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>( this, PlayerEntity.class, false ));
        this.goalSelector.addGoal(2, new FiddleDummyEntity.MeleeAttackGoal(this, 1D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1D,50));
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>( this, MobEntity.class, 0, false, false, NOT_THIS));
    }

    protected void registerData() {
        super.registerData();
        dataManager.register(STATE, 0);
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        AnimationBuilder anim= (dataManager.get(STATE)==0) ? CHANNEL_ANIM : DEATH_ANIM;
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
        if(ticks==400) dataManager.set(STATE, 1); 
        if(ticks>460){
            this.onKillCommand();
        }
        if(this.getHealth()<=1) dataManager.set(STATE, 1);
    }

    @Override
    protected void updateLeashedState() {
        super.updateLeashedState();
        this.clearLeashed(true, false);
        return;
    }

    public void setParent(FiddlesticksEntity fiddle){
        parent = fiddle.getEntityId();
    }

    public FiddlesticksEntity getParent(){
        return (FiddlesticksEntity)this.world.getEntityByID(parent);
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("id", parent);
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
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
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }
        
        public boolean shouldExecute() { 
            LivingEntity livingentity = this.attacker.getAttackTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                this.path = this.attacker.getNavigator().pathfind(livingentity, 0);
                if (this.path != null) {
                    return true;
                } else {
                    return this.getAttackReachSqr(livingentity) >= this.attacker.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());
                }
            }
        }
        
        public boolean shouldContinueExecuting() {
            LivingEntity livingentity = this.attacker.getAttackTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else if (!this.longMemory) {
                return !this.attacker.getNavigator().noPath();
            } else if (!this.attacker.isWithinHomeDistanceFromPosition(livingentity.getPosition())) {
                return false;
            } else {                
                return !(livingentity instanceof PlayerEntity) || !livingentity.isSpectator() && !((PlayerEntity)livingentity).isCreative();
            }
        }
        
        public void startExecuting() {
            this.attacker.getNavigator().setPath(this.path, this.speedTowardsTarget);
            this.attacker.setAggroed(true);
        }
        
        public void resetTask() {
            LivingEntity livingentity = this.attacker.getAttackTarget();
            if (!EntityPredicates.CAN_AI_TARGET.test(livingentity)) {
                this.attacker.setAttackTarget((LivingEntity)null);
            }
            this.attacker.setAggroed(false);
            this.attacker.getNavigator().clearPath();
        }
        
        public void tick() {
            LivingEntity livingentity = this.attacker.getAttackTarget();
            double d0 = this.attacker.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());

            if ((this.longMemory || this.attacker.getEntitySenses().canSee(livingentity)) && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
                this.targetX = livingentity.getPosX(); this.targetY = livingentity.getPosY(); this.targetZ = livingentity.getPosZ();
            }

            this.checkAndPerformAttack(livingentity, d0);
        }

        public void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            ticks++;
            if(ticks%10==0) {
                this.attacker.world.addEntity(new FiddleProyectileEntity(this.attacker.world, this.attacker, this.attacker.getAttackTarget(), Direction.DOWN.getAxis(), getParent()));
                this.attacker.world.playSound(null, this.attacker.getPosition(), SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.AMBIENT, (float)(Math.random() * 5)+5, (float)(Math.random() * 2)+1);
            }
        }
        
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return (double)(this.attacker.getWidth() * 2.0F * this.attacker.getWidth() * 2.0F + attackTarget.getWidth());
        }
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player){ return 5+this.world.rand.nextInt(5); }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn){ return ModSoundEvents.FIDDLESTICKS_HURT.get(); }
    @Override
    public boolean addPotionEffect(EffectInstance effectInstanceIn) { return false; }
    @Override
    protected boolean canBeRidden(Entity entityIn) { return false; }
    @Override
    public boolean canPassengerSteer() { return false; }
    @Override
    public boolean shouldRiderSit() { return false; }
    @Override
    public boolean canBeRiddenInWater(Entity rider) { return true; }
    @Override
    public boolean canChangeDimension() { return false; }
    @Override
    public boolean canDespawn(double distanceToClosestPlayer) { return false; }
    @Override
    public boolean preventDespawn() { return true; }
    @Override
    public boolean isNoDespawnRequired() { return true; }
    @Override
    public int tickTimer() { return ticksExisted; }
}