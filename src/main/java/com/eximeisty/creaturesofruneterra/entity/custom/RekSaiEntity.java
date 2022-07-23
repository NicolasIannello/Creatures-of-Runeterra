package com.eximeisty.creaturesofruneterra.entity.custom;

import java.util.EnumSet;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.EntityPredicates;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
// LogManager.getLogger().log(Level.DEBUG, "hi");

public class RekSaiEntity extends CreatureEntity implements IAnimatable {
    private static final DataParameter<Integer> STATE = EntityDataManager.createKey(XerSaiDunebreakerEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(XerSaiDunebreakerEntity.class, DataSerializers.BYTE);
    private AnimationFactory factory = new AnimationFactory(this);
    
    public RekSaiEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 6)//60?
        .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25)
        .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2)//15?
        .createMutableAttribute(Attributes.FOLLOW_RANGE, 20)//30?
        .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 0)//?
        .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 10)
        .createMutableAttribute(Attributes.ATTACK_SPEED, 0.3);
    }

    @Override
    protected void registerGoals(){
        super.registerGoals();
        this.goalSelector.addGoal( 1, new NearestAttackableTargetGoal<>( this, PlayerEntity.class, true ));
        this.goalSelector.addGoal(2, new RekSaiEntity.MeleeAttackGoal(this, 1.0D, false));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp(XerSaiHatchlingEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Reksai.walk", true));
            return PlayState.CONTINUE;
        }
        event.getController().clearAnimationCache();
        return PlayState.STOP;
    }

    @Override @SuppressWarnings("unchecked")
    public void registerControllers(AnimationData data){
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
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
        private int delayCounter;        
        private int failedPathFindingPenalty = 0;
        private boolean canPenalize = false;

        public MeleeAttackGoal(CreatureEntity creature, double speedIn, boolean useLongMemory) {
            this.attacker = creature;
            this.speedTowardsTarget = speedIn;
            this.longMemory = useLongMemory;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }
        
        public boolean shouldExecute() { 
            LivingEntity livingentity = this.attacker.getAttackTarget();
            if (livingentity == null) {
                LogManager.getLogger().log(Level.DEBUG, "1 living entity should "+livingentity);
                return false;
            } else if (!livingentity.isAlive()) {
                LogManager.getLogger().log(Level.DEBUG, "2 living isn't alive should "+!livingentity.isAlive());
                return false;
            } else {
                if (canPenalize) {
                    if (--this.delayCounter <= 0) {
                        this.path = this.attacker.getNavigator().pathfind(livingentity, 0);
                        this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
                        return this.path != null;
                    } else {
                        return true;
                    }
                }
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
            this.delayCounter = 0;
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
            this.attacker.getLookController().setLookPositionWithEntity(livingentity, 30.0F, 30.0F);
            double d0 = this.attacker.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());
            this.delayCounter = Math.max(this.delayCounter - 1, 0);
            if ((this.longMemory || this.attacker.getEntitySenses().canSee(livingentity)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
                this.targetX = livingentity.getPosX();
                this.targetY = livingentity.getPosY();
                this.targetZ = livingentity.getPosZ();
                this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
                if (this.canPenalize) {
                    this.delayCounter += failedPathFindingPenalty;
                    if (this.attacker.getNavigator().getPath() != null) {
                        net.minecraft.pathfinding.PathPoint finalPathPoint = this.attacker.getNavigator().getPath().getFinalPathPoint();
                        if (finalPathPoint != null && livingentity.getDistanceSq(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1){
                            failedPathFindingPenalty = 0;
                        }else {
                            failedPathFindingPenalty += 10;
                        }
                    }else {
                        failedPathFindingPenalty += 10;
                    }
                }
                if (d0 > 1024.0D) {
                    this.delayCounter += 10;
                } else if (d0 > 256.0D) {
                    this.delayCounter += 5;
                }
                if (!this.attacker.getNavigator().tryMoveToEntityLiving(livingentity, this.speedTowardsTarget)) {
                    this.delayCounter += 15;
                }
            }
            this.checkAndPerformAttack(livingentity, d0);
        }
        
        public void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            this.attacker.getLookController().setLookPositionWithEntity(this.attacker.getAttackTarget(), 30.0F, 30.0F);

        }

        protected void resetAttackCD() {
            //AttackCD = 50;
        }
        
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return (double)(this.attacker.getWidth() * 2.0F * this.attacker.getWidth() * 2.0F + attackTarget.getWidth());
        }
    }
}