package com.eximeisty.creaturesofruneterra.entity.custom;

import java.util.EnumSet;

import net.minecraft.block.Blocks;
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
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.BossInfo;
import net.minecraft.world.server.ServerBossInfo;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class RekSaiEntity extends CreatureEntity implements IAnimatable {
    private static final DataParameter<Integer> STATE = EntityDataManager.createKey(RekSaiEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(RekSaiEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> RUN = EntityDataManager.createKey(RekSaiEntity.class, DataSerializers.VARINT);
    private AnimationFactory factory = new AnimationFactory(this);
    private static double velocidad=0.4;
    private double grabTicks=1;
    private final ServerBossInfo bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);
    private float dañoSalto=0;

    public RekSaiEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 6)//60?
        .createMutableAttribute(Attributes.MOVEMENT_SPEED, velocidad)
        .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2)//15?
        .createMutableAttribute(Attributes.FOLLOW_RANGE, 600)//30?
        .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 0)//?
        .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 10)
        .createMutableAttribute(Attributes.ARMOR, 0)
        .createMutableAttribute(Attributes.ARMOR_TOUGHNESS, 0)
        .createMutableAttribute(Attributes.ATTACK_SPEED, 0.3);
    }

    @Override
    protected void registerGoals(){
        super.registerGoals();
        this.goalSelector.addGoal( 1, new NearestAttackableTargetGoal<>( this, PlayerEntity.class, false ));
        this.goalSelector.addGoal(2, new RekSaiEntity.MeleeAttackGoal(this, 1D, false));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp(XerSaiHatchlingEntity.class));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
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
        if (event.isMoving()) {
            if(dataManager.get(RUN)==0){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Reksai.walk", true));
            }else{
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Reksai.run", true));
            }
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Reksai.idle", true));
        return PlayState.CONTINUE;
    }

    public <E extends IAnimatable> PlayState predicate2(AnimationEvent<E> event) {
        if (dataManager.get(STATE)==7) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Reksai.attack1", false));
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==9) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Reksai.spin", false));
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==8) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Reksai.attack1", false).addAnimation("animation.Reksai.attack2", false));
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==10) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Reksai.spin", false).addAnimation("animation.Reksai.spin", false));
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==11) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Reksai.slam", false));
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==1) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Reksai.RDown", true));
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==6){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Reksai.RTrans", false));
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==2) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Reksai.R2", true));
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==3) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Reksai.burrow", false));
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==4) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Reksai.charge", true));
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==5) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Reksai.salir", false));
            return PlayState.CONTINUE;
        }
        event.getController().clearAnimationCache();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data){
        //<IAnimatable> antes no estaba
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

    protected PathNavigator createNavigator(World worldIn) {
        return new ClimberPathNavigator(this, worldIn);
    }
    
    protected void registerData() {
        super.registerData();
        this.dataManager.register(CLIMBING, (byte)0);
        dataManager.register(STATE, 0);
        dataManager.register(RUN, 0);
    }
    
    public void tick() {
        super.tick();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
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
        if (climbing && dataManager.get(STATE)<1) {
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
        private boolean leap=false;
        private boolean grab=false;
        private Path path;
        private double targetX;
        private double targetY;
        private double targetZ;
        private int charge=(int)(Math.random() * 60 + 40);
        private int cd=40;
        private int slam=(int)(Math.random() * 60 + 80);
        private int ticks=0;
        private double lastX;
        private double lastY;
        private double lastZ;

        public MeleeAttackGoal(CreatureEntity creature, double speedIn, boolean useLongMemory) {
            this.attacker = creature;
            this.speedTowardsTarget = speedIn;
            this.longMemory = useLongMemory;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
        }
        
        public boolean shouldExecute() { 
            LivingEntity livingentity = this.attacker.getAttackTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                if(dataManager.get(STATE)<1){
                    if(this.attacker.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ())>100){
                        this.path = this.attacker.getNavigator().pathfind(livingentity, 0);
                    }
                    this.attacker.getLookController().setLookPositionWithEntity(livingentity, 30.0F, 30.0F);
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
            if(charge>0) --charge;
            if(cd>0) --cd;
            if(slam>0) --slam;

            LivingEntity livingentity = this.attacker.getAttackTarget();
            if(dataManager.get(STATE)<1 || (dataManager.get(STATE)>=7 && dataManager.get(STATE)<=10)){
                this.attacker.getLookController().setLookPositionWithEntity(livingentity, 30.0F, 30.0F);
            }else{
                this.attacker.getLookController().setLookPosition(this.lastX, this.lastY, this.lastZ, 30.0F, 30.0F);
            }
            double d0 = this.attacker.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());

            if ((this.longMemory || this.attacker.getEntitySenses().canSee(livingentity)) && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
                this.targetX = livingentity.getPosX(); this.targetY = livingentity.getPosY(); this.targetZ = livingentity.getPosZ();
            }
            this.checkAndPerformAttack(livingentity, d0);
        }
        
        public void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            if(distToEnemySqr<300 && dataManager.get(STATE)==0 && this.attacker.isOnGround() && slam<=0){
                this.lastX = this.attacker.getAttackTarget().getPosX();
                this.lastY = this.attacker.getAttackTarget().getPosY();
                this.lastZ = this.attacker.getAttackTarget().getPosZ();
                dataManager.set(STATE, 11);
                this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).applyPersistentModifier(new AttributeModifier("speed",-velocidad,AttributeModifier.Operation.ADDITION)); 
            }
            if(dataManager.get(STATE)==11){
                ticks++;
                if(ticks>=25 && ticks<=30){
                    double posx=this.attacker.getLookVec().x*23+this.attacker.getPosX(); double posz=this.attacker.getLookVec().z*23+this.attacker.getPosZ(); double posy=this.attacker.getPosY();
                    AxisAlignedBB bb= new AxisAlignedBB(posx+3, posy, posz+3, posx-3, posy+5, posz-3).union(this.attacker.getBoundingBox());
                    this.attackBB(bb, 10, true, 3F);
                }
                if(ticks==40){
                    dataManager.set(STATE, 0);
                    ticks=0;
                    slam=(int)(Math.random() * 60 + 80);
                    this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).applyPersistentModifier(new AttributeModifier("speed",velocidad,AttributeModifier.Operation.ADDITION)); 
                }
            }
            if(distToEnemySqr<100 && dataManager.get(STATE)==0 && this.attacker.isOnGround() && cd<=0){
                int chance=(int)(Math.random() * 5);
                int running=0;
                if(dataManager.get(RUN)==1){
                    running=2;
                }
                if(chance<=2){
                    dataManager.set(STATE, 8+running);
                }else{
                    dataManager.set(STATE, 7+running);
                }
            }
            if(dataManager.get(STATE)==7 || dataManager.get(STATE)==8){
                ticks++;
                if((ticks>15 && ticks<20) || (ticks>40 && ticks<45)){
                    double posx=this.attacker.getLookVec().x*8+this.attacker.getPosX(); double posz=this.attacker.getLookVec().z*8+this.attacker.getPosZ(); double posy=this.attacker.getPosY();
                    AxisAlignedBB bb= new AxisAlignedBB(posx+7, posy, posz+7, posx-7, posy+5, posz-7);
                    this.attackBB(bb, 10, false, 0);
                }
                if((ticks==25 && dataManager.get(STATE)==7) || ticks==55){
                    dataManager.set(STATE, 0);
                    ticks=0;
                    cd=20;
                }
            }
            if(dataManager.get(STATE)==9 || dataManager.get(STATE)==10){
                ticks++;
                if((ticks>=3 && ticks<=8) || (ticks>=13 && ticks<=18)){
                    AxisAlignedBB bb= new AxisAlignedBB(this.attacker.getBoundingBox().minX-6.0D,this.attacker.getBoundingBox().minY,this.attacker.getBoundingBox().minZ-6.0D,this.attacker.getBoundingBox().maxX+6.0D,this.attacker.getBoundingBox().maxY,this.attacker.getBoundingBox().maxZ+6.0D);
                    this.attackBB(bb, 10, true, 0.4F);
                }
                if((ticks==10 && dataManager.get(STATE)==9) || ticks==20){
                    dataManager.set(STATE, 0);
                    ticks=0;
                    cd=10;
                }
            }
            if(distToEnemySqr>550 && dataManager.get(STATE)==0 && charge<=0 && this.attacker.isOnGround()){
                dataManager.set(STATE, 3);
                this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).applyPersistentModifier(new AttributeModifier("speed",-velocidad,AttributeModifier.Operation.ADDITION)); 
            }
            if(dataManager.get(STATE)==3){//burrow
                ++ticks;
                if(ticks==20){
                    this.lastX = this.attacker.getAttackTarget().getPosX();
                    this.lastY = this.attacker.getAttackTarget().getPosY();
                    this.lastZ = this.attacker.getAttackTarget().getPosZ();
                    if (this.attacker.getPosY()+10.0D<this.lastY && leap==false){
                        leap=true;
                    }else{
                        if((this.lastX-this.attacker.getPosX())<=10 && (this.lastX-this.attacker.getPosX())>=-10){
                            if(this.lastZ<this.attacker.getPosZ()){
                                this.lastZ-=20;
                            }else{
                                this.lastZ+=20;
                            }
                        }else{
                            double m= (this.lastZ-this.attacker.getPosZ())/(this.lastX-this.attacker.getPosX());
                            double b= this.attacker.getPosZ()-(m*this.attacker.getPosX());
                            if(this.lastX<this.attacker.getPosX()){
                                this.lastX-=20;
                            }else{
                                this.lastX+=20;
                            }
                            this.lastZ= m*this.lastX+b;
                        } 
                    }
                }
                if(ticks==30){
                    ticks=0;
                    if(leap==true){
                        charge=(int)(Math.random() * 150 + 30);
                        Vector3d vector3d = this.attacker.getMotion();
                        double vectorY=((this.attacker.getAttackTarget().getPosY() - this.attacker.getPosY())/10)+0.5;
                        Vector3d vector3d1 = new Vector3d(this.attacker.getAttackTarget().getPosX() - this.attacker.getPosX(), 0D, this.attacker.getAttackTarget().getPosZ() - this.attacker.getPosZ());
                        if (vector3d1.lengthSquared() > 1.0E-7D) {
                            vector3d1 = vector3d1.scale(0.2D).add(vector3d.scale(0.2D));
                        }
                        this.attacker.setMotion(vector3d1.x, vectorY, vector3d1.z);
                        dataManager.set(STATE, 2);
                    }else{
                        dataManager.set(STATE, 4);
                        this.attacker.getNavigator().getPathToPos(new BlockPos(this.lastX, this.lastY, this.lastZ), 0);
                        this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).applyPersistentModifier(new AttributeModifier("speed",1.5,AttributeModifier.Operation.ADDITION)); 
                    }
                }
            }
            if(dataManager.get(STATE)==4){//charge
                ticks++;
                AxisAlignedBB bb= new AxisAlignedBB(this.attacker.getBoundingBox().minX-1.0D,this.attacker.getBoundingBox().minY,this.attacker.getBoundingBox().minZ-1.0D,this.attacker.getBoundingBox().maxX+1.0D,this.attacker.getBoundingBox().maxY,this.attacker.getBoundingBox().maxZ+1.0D);
                this.breakBB(bb);
                this.attackBB(bb, 10, true, 5);
                if(this.attacker.getDistanceSq(this.lastX, this.attacker.getPosY(), this.lastZ)<=30 || ticks==100){
                    this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).applyPersistentModifier(new AttributeModifier("speed",-1.5+velocidad,AttributeModifier.Operation.ADDITION)); 
                    dataManager.set(STATE, 5);
                    ticks=0;
                }
            }
            if(dataManager.get(STATE)==5){//salir
                ++ticks;
                if(ticks==20){
                    charge=(int)(Math.random() * 150 + 30);
                    ticks=0;
                    dataManager.set(STATE, 0);
                }
            }
            if(dataManager.get(STATE)==2 || dataManager.get(STATE)==1 || dataManager.get(STATE)==6){//aire
                AxisAlignedBB bb= new AxisAlignedBB(this.attacker.getBoundingBox().minX-1.0D,this.attacker.getBoundingBox().minY,this.attacker.getBoundingBox().minZ-1.0D,this.attacker.getBoundingBox().maxX+1.0D,this.attacker.getBoundingBox().maxY,this.attacker.getBoundingBox().maxZ+1.0D);
                this.breakBB(bb);
                this.attackBB(bb, 10, true, 5);
                if(this.attacker.fallDistance>0){
                    if(ticks==0){
                        dataManager.set(STATE, 6);
                    }else if(ticks==20){
                        dataManager.set(STATE, 1);
                    }
                    ++ticks;
                }
                if(distToEnemySqr<30 && grab==false){
                    grab=true;
                    enemy.attackEntityFrom(DamageSource.causeMobDamage(this.attacker), 2.0F);
                    enemy.stopRiding();
                    enemy.startRiding(this.attacker, true);
                }
                if(enemy.isRidingSameEntity(this.attacker)==false && grab==true){
                    enemy.startRiding(this.attacker, true);
                }
                if(this.attacker.isOnGround() && ticks>1){
                    enemy.stopRiding();
                    enemy.setPositionAndUpdate(this.attacker.getLookVec().x*-8+this.attacker.getPosX(), this.attacker.getPosY(), this.attacker.getLookVec().z*-8+this.attacker.getPosZ());
                    enemy.attackEntityFrom(DamageSource.causeMobDamage(this.attacker), dañoSalto);
                    grab=false;
                    ticks=0;
                    dañoSalto=0;
                    charge=(int)(Math.random() * 5 + 30);
                    leap=false;
                    dataManager.set(STATE, 0);
                    this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).applyPersistentModifier(new AttributeModifier("speed",velocidad,AttributeModifier.Operation.ADDITION)); 
                }else{
                    dañoSalto=this.attacker.fallDistance;
                }
            }
        }

        protected void attackBB(AxisAlignedBB bb, float damage, boolean canKnockback, float knockbackStrenght){
            this.attacker.world.getEntitiesWithinAABB(LivingEntity.class, bb).stream().forEach(livingEntity -> {
                if(!livingEntity.isEntityEqual(this.attacker)) livingEntity.attackEntityFrom(DamageSource.causeMobDamage(this.attacker), damage);
                if(!livingEntity.world.isRemote && canKnockback) livingEntity.applyKnockback(knockbackStrenght, livingEntity.getPosX()+this.attacker.getPosX(), livingEntity.getPosZ()+this.attacker.getPosZ());
            });
        }

        protected void breakBB(AxisAlignedBB bb){
            BlockPos.getAllInBox(bb).forEach(pos->{
                if( this.attacker.world.getBlockState(pos)!=Blocks.AIR.getDefaultState() && this.attacker.world.getBlockState(pos)!=Blocks.WATER.getDefaultState() && this.attacker.world.getBlockState(pos)!=Blocks.LAVA.getDefaultState()){
                    this.attacker.world.setBlockState(pos, Blocks.AIR.getDefaultState());
                }
            });
        }
        
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return (double)(this.attacker.getWidth() * 2.0F * this.attacker.getWidth() * 2.0F + attackTarget.getWidth());
        }
    }

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        if (this.isPassenger(passenger)) {
            //passenger.setMotion(0, passenger.getMotion().y, 0); ??
            double y;
            if(dataManager.get(STATE)==6 || grabTicks>1.0D && grabTicks<=14.0D){
                y = this.getPosY()+13.0D-grabTicks;
                grabTicks+=0.5D;
            }else if(dataManager.get(STATE)==1){
                y = this.getPosY();
                if(grabTicks>1.0D){
                    grabTicks=1.0D;
                }
            }else{
                y = this.getPosY()+13.0D;
                if(grabTicks>1.0D){
                    grabTicks=1.0D;
                }
            }
            passenger.setPosition(this.getLookVec().x*-8+this.getPosX(), y, this.getLookVec().z*-8+this.getPosZ());
        }
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }
    @Override
    public boolean addPotionEffect(EffectInstance effectInstanceIn) {
        return false;
    }
    @Override
    protected boolean canBeRidden(Entity entityIn) {
        return false;
    }
    @Override
    public boolean canPassengerSteer() {
        return false;
    }
    @Override
    public boolean shouldRiderSit() {
        return false;
    }
    @Override
    public boolean canBePushed() {
        return false;
    }
    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        return true;
    }
    @Override
    public boolean canChangeDimension() {
        return false;
    }
    @Override
    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }
    @Override
    public boolean preventDespawn() {
        return true;
    }
    @Override
    public boolean isNoDespawnRequired() {
        return true;
    }
}