package com.eximeisty.creaturesofruneterra.entity.custom;

import java.util.EnumSet;
import java.util.function.Predicate;

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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.BossInfo;
import net.minecraft.world.server.ServerBossInfo;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class FiddlesticksEntity extends CreatureEntity implements IAnimatable, IAnimationTickable {
    public static final DataParameter<Integer> STATE = EntityDataManager.createKey(FiddlesticksEntity.class, DataSerializers.VARINT);
    private AnimationFactory factory = new AnimationFactory(this); 
    //private float damage=(float)1;
    private final ServerBossInfo bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS)).setDarkenSky(true).setCreateFog(true);
    private static final AnimationBuilder WALK_ANIM = new AnimationBuilder().addAnimation("animation.Fiddlesticks.walk", true);
    private static final Predicate<LivingEntity> NOT_THIS = (p_213797_0_) -> {
        if(!(p_213797_0_ instanceof PlayerEntity) && (p_213797_0_ instanceof WaterMobEntity || (p_213797_0_.isInWaterOrBubbleColumn() || p_213797_0_.getEntityWorld().getBlockState(new BlockPos(p_213797_0_.getPosX(),p_213797_0_.getPosY()-1,p_213797_0_.getPosZ()))==Blocks.WATER.getDefaultState()))) return false;
        return true;
    };
    public FiddlesticksEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 400)
        .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.5)
        .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2)
        .createMutableAttribute(Attributes.FOLLOW_RANGE, 80)
        .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 4)
        .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 2)
        .createMutableAttribute(Attributes.ARMOR, 4)
        .createMutableAttribute(Attributes.ARMOR_TOUGHNESS, 4)
        .createMutableAttribute(Attributes.ATTACK_SPEED, 0.3);
    }

    @Override
    protected void registerGoals(){
        super.registerGoals();
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>( this, PlayerEntity.class, false ));
        this.goalSelector.addGoal(2, new FiddlesticksEntity.MeleeAttackGoal(this, 1D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 0.4,50));
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>( this, MobEntity.class, 0, false, false, NOT_THIS));
    }

    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(dataManager.get(STATE)==0){
            if (event.isMoving()) {
                event.getController().setAnimation(WALK_ANIM);
                return PlayState.CONTINUE;
            }
        }
        return PlayState.STOP;
    }

    public <E extends IAnimatable> PlayState predicate2(AnimationEvent<E> event) {
        
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
        this.clearLeashed(true, false);
        return;
    }

    protected void registerData() {
        super.registerData();
        dataManager.register(STATE, 0);
    }
    
    public void tick() {
        super.tick();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    public class MeleeAttackGoal extends Goal {
        protected final FiddlesticksEntity attacker;
        private final double speedTowardsTarget;
        private final boolean longMemory;
        private Path path;
        private double targetX;
        private double targetY;
        private double targetZ;

        public MeleeAttackGoal(FiddlesticksEntity creature, double speedIn, boolean useLongMemory) {
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
            //} else if(!spawnAnim) {
            //    return false;
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
            this.attacker.setRotation((float)(this.attacker.getLookVec().x*livingentity.getPosX()), (float)(this.attacker.getLookVec().z*livingentity.getPosZ()));

            double d0 = this.attacker.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());

            if ((this.longMemory || this.attacker.getEntitySenses().canSee(livingentity)) && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
                this.targetX = livingentity.getPosX(); this.targetY = livingentity.getPosY(); this.targetZ = livingentity.getPosZ();
            }

            this.checkAndPerformAttack(livingentity, d0);
        }

        public void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        }

        protected void attackBB(AxisAlignedBB bb, float damage, boolean canKnockback, float knockbackStrenght, boolean motion){
            this.attacker.world.getEntitiesWithinAABB(LivingEntity.class, bb).stream().forEach(livingEntity -> {
                if(!livingEntity.isEntityEqual(this.attacker) && !(livingEntity instanceof CoRPartEntity)) {
                    livingEntity.attackEntityFrom(DamageSource.causeMobDamage(this.attacker), damage);
                    if(motion) livingEntity.setMotion(this.attacker.getLookVec().x*4, 4.5, this.attacker.getLookVec().z*4);
                    if(!livingEntity.world.isRemote && canKnockback) livingEntity.applyKnockback(knockbackStrenght, livingEntity.getPosX()+this.attacker.getPosX(), livingEntity.getPosZ()+this.attacker.getPosZ());
                }
            });
        }

        protected void breakBB(AxisAlignedBB bb){
            BlockPos.getAllInBox(bb).forEach(pos->{
                if( this.attacker.world.getBlockState(pos)!=Blocks.AIR.getDefaultState() && this.attacker.world.getBlockState(pos)!=Blocks.WATER.getDefaultState() && this.attacker.world.getBlockState(pos)!=Blocks.LAVA.getDefaultState() && this.attacker.world.getBlockState(pos)!=Blocks.BEDROCK.getDefaultState()){
                    this.attacker.world.setBlockState(pos, Blocks.AIR.getDefaultState());
                }
            });
        }
        
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return (double)(this.attacker.getWidth() * 2.0F * this.attacker.getWidth() * 2.0F + attackTarget.getWidth());
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        super.attackEntityFrom(source, amount);
        return false;
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player){ return 75+this.world.rand.nextInt(25); }
    //protected SoundEvent getHurtSound(DamageSource damageSourceIn){ return ModSoundEvents.REKSAI_HIT.get(); }
    // @Override
    // public boolean onLivingFall(float distance, float damageMultiplier) { return false; }
    @Override
    public boolean addPotionEffect(EffectInstance effectInstanceIn) { return false; }
    @Override
    protected boolean canBeRidden(Entity entityIn) { return false; }
    @Override
    public boolean canPassengerSteer() { return false; }
    @Override
    public boolean shouldRiderSit() { return false; }
    // @Override
    // public boolean canBePushed() { return false; }
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
    // @Override
    // public boolean canBeCollidedWith() { return false; }
    // @Override
    // public void onCollideWithPlayer(PlayerEntity entityIn) { }
    // @Override
    // public boolean hitByEntity(Entity entityIn) { return true; }
    // @Override
    // protected void collideWithEntity(Entity p_82167_1_) { }
    // @Override
    // protected void collideWithNearbyEntities() { }
    @Override
    public int tickTimer() { return ticksExisted; }
}