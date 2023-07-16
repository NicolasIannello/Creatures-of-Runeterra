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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
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
    private float damage=(float)0.1;
    private float velocidad=(float)0.5;
    private final ServerBossInfo bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS)).setDarkenSky(true).setCreateFog(true);
    private static final AnimationBuilder WALK_ANIM = new AnimationBuilder().addAnimation("animation.Fiddlesticks.walk", true);
    private static final AnimationBuilder IDLE_ANIM = new AnimationBuilder().addAnimation("animation.Fiddlesticks.idle", true);
    private static final AnimationBuilder CHANNEL_ANIM = new AnimationBuilder().addAnimation("animation.Fiddlesticks.channel", false).addAnimation("animation.Fiddlesticks.channelloop", true);
    private static final AnimationBuilder ATTACK1_ANIM = new AnimationBuilder().addAnimation("animation.Fiddlesticks.attack1", false);
    private static final AnimationBuilder ATTACK2_ANIM = new AnimationBuilder().addAnimation("animation.Fiddlesticks.attack2", false);
    private static final AnimationBuilder RUN_ANIM = new AnimationBuilder().addAnimation("animation.Fiddlesticks.run", true);
    private static final AnimationBuilder RUNATTACK_ANIM = new AnimationBuilder().addAnimation("animation.Fiddlesticks.runattack", false);
    private static final AnimationBuilder BLIND_ANIM = new AnimationBuilder().addAnimation("animation.Fiddlesticks.blind", false).addAnimation("animation.Fiddlesticks.blindloop", true);
    private static final AnimationBuilder CHANNELREVERT_ANIM = new AnimationBuilder().addAnimation("animation.Fiddlesticks.channelrevert", false);
    private static final AnimationBuilder BLINDREVERT_ANIM = new AnimationBuilder().addAnimation("animation.Fiddlesticks.blindrevert", false);
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
        if(dataManager.get(STATE)<=0){
            if (event.isMoving()) {
                AnimationBuilder anim= (dataManager.get(STATE)==-1) ? RUN_ANIM : WALK_ANIM;
                event.getController().setAnimation(anim);
            }else{
                event.getController().setAnimation(IDLE_ANIM);
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    public <E extends IAnimatable> PlayState predicate2(AnimationEvent<E> event) {
        if (dataManager.get(STATE)>0){    
            switch (dataManager.get(STATE)) {
                case 1: event.getController().setAnimation(ATTACK1_ANIM); break;
                case 2: event.getController().setAnimation(ATTACK2_ANIM); break;
                case 3: event.getController().setAnimation(RUNATTACK_ANIM); break;
                case 4: event.getController().setAnimation(CHANNEL_ANIM); break;
                case 5: event.getController().setAnimation(CHANNELREVERT_ANIM); break;
                case 6: event.getController().setAnimation(BLIND_ANIM); break;
                case 7: event.getController().setAnimation(BLINDREVERT_ANIM); break;
            }
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
        private int channel=(int)(Math.random() * 400 + 100);
        private int blind=(int)(Math.random() * 400 + 300);
        private int run=(int)(Math.random() * 100 + 150);
        private int lastHit=0;
        private int ticks=0;
        private int cd=0;

        public MeleeAttackGoal(FiddlesticksEntity creature, double speedIn, boolean useLongMemory) {
            this.attacker = creature;
            this.speedTowardsTarget = speedIn;
            this.longMemory = useLongMemory;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }
        
        public boolean shouldExecute() { 
            LivingEntity livingentity = this.attacker.getAttackTarget();
            if (livingentity == null) {
                ticks=0;
                this.attacker.dataManager.set(STATE, 0);
                this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
                return false;
            } else if (!livingentity.isAlive()) {
                ticks=0;
                this.attacker.dataManager.set(STATE, 0);
                this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
                return false;
            } else {
                if(dataManager.get(STATE)>0) {
                    this.path=null;
                    return true;
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
                ticks=0;
                this.attacker.dataManager.set(STATE, 0);
                this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
                return false;
            } else if (!livingentity.isAlive()) {
                ticks=0;
                this.attacker.dataManager.set(STATE, 0);
                this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
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
            if (channel>0) --channel;
            if (blind>0) --blind;
            if (run>0) --run;
            lastHit++;

            LivingEntity livingentity = this.attacker.getAttackTarget();
            double d0 = this.attacker.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());

            if ((this.longMemory || this.attacker.getEntitySenses().canSee(livingentity)) && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
                this.targetX = livingentity.getPosX(); this.targetY = livingentity.getPosY(); this.targetZ = livingentity.getPosZ();
            }

            this.checkAndPerformAttack(livingentity, d0);
        }

        public void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            if(dataManager.get(STATE)==0 && lastHit>300){

            }
            if(distToEnemySqr<10 && dataManager.get(STATE)==0 && this.attacker.isOnGround() && cd<=0){
                int chance=(int)(Math.random() * 2)+1;
                dataManager.set(STATE, chance);
                this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
            }
            if(dataManager.get(STATE)>0) ticks++;
            switch (dataManager.get(STATE)) {
                case 1:
                    animNotifys(15, 20, 25, 10, true, 20*this.attacker.damage, 1, 0, this.attacker.getLookVec().x*2, 0, this.attacker.getLookVec().z*2, ModSoundEvents.FIDDLESTICKS_ATTACK.get());
                    break;
                case 2:
                    animNotifys(10, 15, 20, 10, true, 15*this.attacker.damage, 1, 0, this.attacker.getLookVec().x*2, 0, this.attacker.getLookVec().z*2, ModSoundEvents.FIDDLESTICKS_ATTACK.get());
                    break;
            }
        }

        protected void animNotifys(int bbStart, int bbEnd, int reset, int cd, boolean ms, float damage, double growXZ, double growY, double offsetX, double offsetY, double offsetZ, SoundEvent sound){
            AxisAlignedBB bb= this.attacker.getBoundingBox().grow(growXZ, growY, growXZ).offset(offsetX, offsetY, offsetZ);
            if(ticks==2 && sound!=null) this.attacker.world.playSound(null, this.attacker.getPosition(), sound, SoundCategory.HOSTILE, 1, 1);
            if(ticks>bbStart && ticks<bbEnd) attackBB(bb, damage);
            if(ticks>reset) resetState(cd, ms);
        }

        protected void resetState(int cd, boolean ms){
            dataManager.set(STATE, 0);
            ticks=0;
            cd=(int)(Math.random() * 20 + cd);
            if(ms) this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
        }

        protected void attackBB(AxisAlignedBB bb, float damage){
            this.attacker.world.getEntitiesWithinAABB(LivingEntity.class, bb).stream().forEach(livingEntity -> {
                if(!livingEntity.isEntityEqual(this.attacker)) {
                    livingEntity.attackEntityFrom(DamageSource.causeMobDamage(this.attacker), damage);
                    lastHit=0;
                }
            });
        }

        protected void breakBB(AxisAlignedBB bb){
            BlockPos.getAllInBox(bb).forEach(pos->{
                // if( this.attacker.world.getBlockState(pos)!=Blocks.AIR.getDefaultState() && this.attacker.world.getBlockState(pos)!=Blocks.WATER.getDefaultState() && this.attacker.world.getBlockState(pos)!=Blocks.LAVA.getDefaultState() && this.attacker.world.getBlockState(pos)!=Blocks.BEDROCK.getDefaultState()){
                //     this.attacker.world.setBlockState(pos, Blocks.AIR.getDefaultState());
                // }
                if(pos.getY()==this.attacker.getPosY()) this.attacker.world.setBlockState(pos, Blocks.WHITE_CARPET.getDefaultState());
            });
        }
        
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return (double)(this.attacker.getWidth() * 2.0F * this.attacker.getWidth() * 2.0F + attackTarget.getWidth());
        }
    }

    // public boolean attackEntityFrom(DamageSource source, float amount) {
    //     super.attackEntityFrom(source, amount);
    //     return false;
    // }

    @Override
    protected int getExperiencePoints(PlayerEntity player){ return 75+this.world.rand.nextInt(25); }
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