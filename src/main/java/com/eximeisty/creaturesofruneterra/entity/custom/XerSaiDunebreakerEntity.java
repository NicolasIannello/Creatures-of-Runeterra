package com.eximeisty.creaturesofruneterra.entity.custom;

import java.util.EnumSet;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class XerSaiDunebreakerEntity extends CreatureEntity implements IAnimatable {
    private static final DataParameter<Integer> STATE = EntityDataManager.createKey(XerSaiDunebreakerEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(XerSaiDunebreakerEntity.class, DataSerializers.BYTE);
    private AnimationFactory factory = new AnimationFactory(this);
    private int AttackCD=0;
    private int HeavyCD=100;
    private int ticks=0;

    public XerSaiDunebreakerEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }
    
    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 65)//60?
        .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.35)
        .createMutableAttribute(Attributes.ATTACK_DAMAGE, 15)//15?
        .createMutableAttribute(Attributes.FOLLOW_RANGE, 20)//30?
        .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 0)//?
        .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 10)
        .createMutableAttribute(Attributes.ATTACK_SPEED, 0.3);
    }

    @Override
    protected void registerGoals(){
        super.registerGoals();
        this.goalSelector.addGoal( 1, new NearestAttackableTargetGoal<>( this, PlayerEntity.class, true ));
        this.goalSelector.addGoal(2, new XerSaiDunebreakerEntity.MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(9, new RandomWalkingGoal(this, 0.35,80));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp(XerSaiHatchlingEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.xersai_dunebreaker.walk", true));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.xersai_dunebreaker.idle", true));
        return PlayState.CONTINUE;
    }

    public <E extends IAnimatable> PlayState predicate2(AnimationEvent<E> event) {
        if ( dataManager.get(STATE) == 1 ) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.xersai_dunebreaker.attack", false));
			return PlayState.CONTINUE;
		}
        if ( dataManager.get(STATE) == 2 ) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.xersai_dunebreaker.heavy", false));
			return PlayState.CONTINUE;
		}
        event.getController().clearAnimationCache();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data){
        data.addAnimationController(new AnimationController<IAnimatable>(this, "controller", 0, this::predicate));
        data.addAnimationController(new AnimationController<IAnimatable>(this, "attacks", 0, this::predicate2));
    }

    @Override
    public AnimationFactory getFactory(){
        return this.factory;
    }

    @Override
    protected void updateLeashedState() {
        super.updateLeashedState();
        this.clearLeashed(true, true);
        return;
    }

    protected PathNavigator createNavigator(World worldIn) {
        return new ClimberPathNavigator(this, worldIn);
    }
    
    protected void registerData() {
        super.registerData();
        this.dataManager.register(CLIMBING, (byte)0);
        dataManager.register(STATE, 0);
    }
    
    public void tick() {
        super.tick();
        if (!this.world.isRemote) {
           this.setBesideClimbableBlock(this.collidedHorizontally);
        }
    }
    
    public boolean isOnLadder() {
        return this.isBesideClimbableBlock();
    }
    
    public boolean isBesideClimbableBlock() {
        return (this.dataManager.get(CLIMBING) & 1) != 0;
    }
    
    public void setBesideClimbableBlock(boolean climbing) {
        byte b0 = this.dataManager.get(CLIMBING);
        if (climbing) {
           b0 = (byte)(b0 | 1);
        } else {
           b0 = (byte)(b0 & -2);
        }
        this.dataManager.set(CLIMBING, b0);
    }

    public class MeleeAttackGoal extends Goal {
        protected final CreatureEntity attacker;
        private final double speedTowardsTarget;
        private final boolean longMemory;
        private Path path;
        private double targetX;
        private double targetY;
        private double targetZ;

        public MeleeAttackGoal(CreatureEntity creature, double speedIn, boolean useLongMemory) {
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
                if(this.attacker.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ())>=10){
                    this.path = this.attacker.getNavigator().pathfind(livingentity, 0);
                }else{
                    return true;
                }
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
            if(AttackCD>=0) --AttackCD;
            if(HeavyCD>=0) --HeavyCD;

            LivingEntity livingentity = this.attacker.getAttackTarget();
            this.attacker.getLookController().setLookPositionWithEntity(livingentity, 30.0F, 30.0F);
            double d0 = this.attacker.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());
            if ((this.longMemory || this.attacker.getEntitySenses().canSee(livingentity)) && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
                this.targetX = livingentity.getPosX(); this.targetY = livingentity.getPosY(); this.targetZ = livingentity.getPosZ();
            }

            this.checkAndPerformAttack(livingentity, d0);
        }
        
        public void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            this.attacker.getLookController().setLookPositionWithEntity(this.attacker.getAttackTarget(), 30.0F, 30.0F);
            if (distToEnemySqr<=15 && AttackCD<=0 && ticks==0) {  
                ++ticks;
                if(HeavyCD<=0){
                    dataManager.set(STATE, 2);
                    HeavyCD=200;
                }else{
                    dataManager.set(STATE, 1);
                }
            }

            //Anim Notifys
            if(ticks>0){
                if(dataManager.get(STATE)==1){
                    if(ticks==17 && distToEnemySqr<=22){
                        this.attacker.attackEntityAsMob(enemy);
                    }
                    if(ticks==31){
                        dataManager.set(STATE, 0);
                    }
                }
                if(dataManager.get(STATE)==2){
                    if(ticks==1){
                        this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).applyPersistentModifier(new AttributeModifier("speed",-0.35,AttributeModifier.Operation.ADDITION)); 
                        this.attacker.getAttribute(Attributes.ATTACK_DAMAGE).applyPersistentModifier(new AttributeModifier("damage",30,AttributeModifier.Operation.ADDITION)); 
                    }
                    if(distToEnemySqr<=22 && ticks==30 ){
                        this.attacker.attackEntityAsMob(enemy);
                    }
                    if(ticks==45){
                        dataManager.set(STATE, 0);
                        this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).applyPersistentModifier(new AttributeModifier("speed",0.35,AttributeModifier.Operation.ADDITION)); 
                        this.attacker.getAttribute(Attributes.ATTACK_DAMAGE).applyPersistentModifier(new AttributeModifier("damage",-30,AttributeModifier.Operation.ADDITION)); 
                    }
                }
                ++ticks;
                if(dataManager.get(STATE)==0){
                    this.resetAttackCD();
                    ticks=0;
                }
            }
        }

        protected void resetAttackCD() {
            AttackCD = 20;
        }
        
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return (double)(this.attacker.getWidth() * 2.0F * this.attacker.getWidth() * 2.0F + attackTarget.getWidth());
        }
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player){ return 10+this.world.rand.nextInt(30); }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) { return false; }
    @Override
    protected boolean canBeRidden(Entity entityIn) { return false; }
    @Override
    public boolean canChangeDimension() { return false; }
    @Override
    public boolean canDespawn(double distanceToClosestPlayer) { return false; }
    @Override
    public boolean preventDespawn() { return true; }
    @Override
    public boolean isNoDespawnRequired() { return true; }

    protected SoundEvent getAmbientSound() { return SoundEvents.ENTITY_ZOGLIN_AMBIENT; }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SoundEvents.ENTITY_STRIDER_HURT; }

    protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_STRIDER_DEATH; }
}