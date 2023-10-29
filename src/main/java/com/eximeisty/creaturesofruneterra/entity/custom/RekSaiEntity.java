package com.eximeisty.creaturesofruneterra.entity.custom;

import java.util.EnumSet;
import java.util.function.Predicate;

import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
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
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.BossInfo;
import net.minecraft.world.server.ServerBossInfo;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class RekSaiEntity extends CreatureEntity implements IAnimatable, IAnimationTickable {
    public static final DataParameter<Integer> STATE = EntityDataManager.createKey(RekSaiEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> RUN = EntityDataManager.createKey(RekSaiEntity.class, DataSerializers.VARINT);
    public static final DataParameter<Boolean> HEAL = EntityDataManager.createKey(RekSaiEntity.class, DataSerializers.BOOLEAN);
    private AnimationFactory factory = new AnimationFactory(this); 
    private double velocidad=0.4;
    private float damage=(float)1;
    private double grabTicks=1;
    private double deathTicks=0;
    // private int healticks=0;
    // private double LastX=0;
    // private double LastY=0;
    // private double LastZ=0;
    private final ServerBossInfo bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);
    private float jumpDamage=0;
    private boolean spawnAnim=false;
    private boolean spawn=false;
    // private int lastAttack=0;
    // private int run=150;
    // private static final DataParameter<Integer> SPAWN = EntityDataManager.createKey(RekSaiEntity.class, DataSerializers.VARINT);
    private final CoRPartEntity head;
    private final CoRPartEntity body;
    private final CoRPartEntity leg;
    private final CoRPartEntity tail;
    private final CoRPartEntity tail2;
    private static final AnimationBuilder WALK_ANIM = new AnimationBuilder().addAnimation("animation.Reksai.walk", true);
    private static final AnimationBuilder RUN_ANIM = new AnimationBuilder().addAnimation("animation.Reksai.run", true);
    private static final AnimationBuilder IDLE_ANIM = new AnimationBuilder().addAnimation("animation.Reksai.idle", true);
    private static final AnimationBuilder IDLE2_ANIM = new AnimationBuilder().addAnimation("animation.Reksai.idle2", true);
    private static final AnimationBuilder ATTACK1_ANIM = new AnimationBuilder().addAnimation("animation.Reksai.attack1", false);
    private static final AnimationBuilder SPIN_ANIM = new AnimationBuilder().addAnimation("animation.Reksai.spin", false);
    private static final AnimationBuilder ATTACK12_ANIM = new AnimationBuilder().addAnimation("animation.Reksai.attack1", false).addAnimation("animation.Reksai.attack2", false);
    private static final AnimationBuilder SPIN2_ANIM = new AnimationBuilder().addAnimation("animation.Reksai.spin", false).addAnimation("animation.Reksai.spin", false);
    private static final AnimationBuilder SLAM_ANIM = new AnimationBuilder().addAnimation("animation.Reksai.slam", false);
    private static final AnimationBuilder RDOWN_ANIM = new AnimationBuilder().addAnimation("animation.Reksai.RDown", true);
    private static final AnimationBuilder RTRANS_ANIM = new AnimationBuilder().addAnimation("animation.Reksai.RTrans", false);
    private static final AnimationBuilder R2_ANIM = new AnimationBuilder().addAnimation("animation.Reksai.R2", true);
    private static final AnimationBuilder BURROW_ANIM = new AnimationBuilder().addAnimation("animation.Reksai.burrow", false).addAnimation("animation.Reksai.charge", true);
    private static final AnimationBuilder CHARGE_ANIM = new AnimationBuilder().addAnimation("animation.Reksai.charge", true);
    private static final AnimationBuilder SALIR_ANIM = new AnimationBuilder().addAnimation("animation.Reksai.salir", false);
    private static final AnimationBuilder THROW_ANIM = new AnimationBuilder().addAnimation("animation.Reksai.throw", false);
    private static final AnimationBuilder APPEAR_ANIM = new AnimationBuilder().addAnimation("animation.Reksai.appear", false);
    private static final Predicate<LivingEntity> NOT_THIS = (p_213797_0_) -> {
        if(p_213797_0_ instanceof XerSaiDunebreakerEntity || p_213797_0_ instanceof XerSaiHatchlingEntity || ( (!(p_213797_0_ instanceof PlayerEntity) || p_213797_0_ instanceof WaterMobEntity) && (p_213797_0_.isInWaterOrBubbleColumn() || p_213797_0_.getEntityWorld().getBlockState(new BlockPos(p_213797_0_.getPosX(),p_213797_0_.getPosY()-1,p_213797_0_.getPosZ()))==Blocks.WATER.getDefaultState()))) return false;
        if(p_213797_0_ instanceof CoRPartEntity) if( ((CoRPartEntity)p_213797_0_).getParent() instanceof RekSaiEntity ) return false; 
        return true;
    };

    public RekSaiEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        head=new CoRPartEntity(ModEntityTypes.WIVHIV.get(),this.world);
        body=new CoRPartEntity(ModEntityTypes.WVIHV.get(),this.world);
        leg=new CoRPartEntity(ModEntityTypes.WVIIHVI.get(),this.world);
        tail=new CoRPartEntity(ModEntityTypes.WIIIHIII.get(),this.world);
        tail2=new CoRPartEntity(ModEntityTypes.WIVHIV.get(),this.world);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 600)//600
        .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.4)//0.4
        .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2)//15?
        .createMutableAttribute(Attributes.FOLLOW_RANGE, 80)//30?
        .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 4)//?
        .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 10)
        .createMutableAttribute(Attributes.ARMOR, 8)
        .createMutableAttribute(Attributes.ARMOR_TOUGHNESS, 8)
        .createMutableAttribute(Attributes.ATTACK_SPEED, 0.3);
    }

    @Override
    protected void registerGoals(){
        super.registerGoals();
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>( this, PlayerEntity.class, false ));
        this.goalSelector.addGoal(2, new RekSaiEntity.MeleeAttackGoal(this, 1D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, velocidad,50));
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.targetSelector.addGoal(6, (new HurtByTargetGoal(this)).setCallsForHelp(XerSaiHatchlingEntity.class));
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
        if(dataManager.get(STATE)==0 || event.getController().getAnimationState()==AnimationState.Stopped){
            if (event.isMoving()) {
                if(dataManager.get(RUN)==0){
                    event.getController().setAnimation(WALK_ANIM);
                }else{
                    event.getController().setAnimation(RUN_ANIM);
                }
                return PlayState.CONTINUE;
            }
            if(dataManager.get(RUN)==0){
                event.getController().setAnimation(IDLE_ANIM);
            }else{
                event.getController().setAnimation(IDLE2_ANIM);
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    public <E extends IAnimatable> PlayState predicate2(AnimationEvent<E> event) {
        if (dataManager.get(STATE)==7) {
            event.getController().setAnimation(ATTACK1_ANIM);
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==9) {
            event.getController().setAnimation(SPIN_ANIM);
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==8) {
            event.getController().setAnimation(ATTACK12_ANIM);
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==10) {
            event.getController().setAnimation(SPIN2_ANIM);
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==11) {
            event.getController().setAnimation(SLAM_ANIM);
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==1) {
            event.getController().setAnimation(RDOWN_ANIM);
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==6){
            event.getController().setAnimation(RTRANS_ANIM);
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==2) {
            event.getController().setAnimation(R2_ANIM);
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==3) {
            event.getController().setAnimation(BURROW_ANIM);
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==4) {
            event.getController().setAnimation(CHARGE_ANIM);
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==5) {
            event.getController().setAnimation(SALIR_ANIM);
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==12) {
            event.getController().setAnimation(THROW_ANIM);
            return PlayState.CONTINUE;
        }
        if (dataManager.get(STATE)==20) {
            event.getController().setAnimation(APPEAR_ANIM);
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

    protected void registerData() {
        super.registerData();
        dataManager.register(STATE, 20);
        dataManager.register(RUN, 0);
        dataManager.register(HEAL, false);
    }
    
    public void tick() {
        super.tick();
        if(!spawn){
            head.setParent(this);
            this.world.addEntity(head);
            body.setParent(this);
            this.world.addEntity(body);
            leg.setParent(this);
            this.world.addEntity(leg);
            tail.setParent(this);
            this.world.addEntity(tail);
            tail2.setParent(this);
            this.world.addEntity(tail2);
            spawn=true;
        }
        if(dataManager.get(STATE)==4){
            this.leg.setPosition(this.getLookVec().x*-3+this.getPosX(), this.getPosY(), this.getLookVec().z*-3+this.getPosZ());
            this.tail.setPosition(this.getLookVec().x*-8.5+this.getPosX(), this.getPosY(), this.getLookVec().z*-8.5+this.getPosZ());
            this.tail2.setPosition(this.getLookVec().x*-13+this.getPosX(), this.getPosY(), this.getLookVec().z*-13+this.getPosZ());
            this.head.setPosition(this.getLookVec().x*8+this.getPosX(), this.getPosY(), this.getLookVec().z*8+this.getPosZ());
            this.body.setPosition(this.getLookVec().x*3.5+this.getPosX(), this.getPosY(), this.getLookVec().z*3.5+this.getPosZ());
        }else{
            if(dataManager.get(RUN)==0){
                this.head.setPosition(this.getLookVec().x*8+this.getPosX(), this.getPosY()+10, this.getLookVec().z*8+this.getPosZ());
                this.body.setPosition(this.getLookVec().x*3.5+this.getPosX(), this.getPosY()+9, this.getLookVec().z*3.5+this.getPosZ());
            }else{
                this.head.setPosition(this.getLookVec().x*8+this.getPosX(), this.getPosY()+8, this.getLookVec().z*8+this.getPosZ());
                this.body.setPosition(this.getLookVec().x*3.5+this.getPosX(), this.getPosY()+7, this.getLookVec().z*3.5+this.getPosZ());
            }
            this.leg.setPosition(this.getLookVec().x*-3+this.getPosX(), this.getPosY(), this.getLookVec().z*-3+this.getPosZ());
            this.tail.setPosition(this.getLookVec().x*-8.5+this.getPosX(), this.getPosY()+7, this.getLookVec().z*-8.5+this.getPosZ());
            if(dataManager.get(STATE)!=2) this.tail2.setPosition(this.getLookVec().x*-13+this.getPosX(), this.getPosY()+8, this.getLookVec().z*-13+this.getPosZ());
        }
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        if(!spawnAnim){
            grabTicks++;
            if(grabTicks==3) this.world.playSound(null, this.getPosition(), ModSoundEvents.REKSAI_APP.get(), SoundCategory.HOSTILE, 3, 1);
            if(grabTicks==35){
                grabTicks=1.0D;
                dataManager.set(STATE, 0);
                spawnAnim=true;
            }
        }
        if(this.getHealth()<this.getMaxHealth()/2 && dataManager.get(RUN)==0){
            velocidad=0.6;
            this.world.playSound(null, this.getPosition(), ModSoundEvents.REKSAI_ANGRY.get(), SoundCategory.HOSTILE, 3, 1);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
            dataManager.set(RUN, 1);
        }
        if(this.getHealth()<=1){
            deathTicks++;
            if(deathTicks==3) this.world.playSound(null, this.getPosition(), ModSoundEvents.REKSAI_ESCAPE.get(), SoundCategory.HOSTILE, 3, 1);
            dataManager.set(STATE, 3);
            if(deathTicks==30){
                this.head.remove();
                this.body.remove();
                this.leg.remove();
                this.tail.remove();
                this.tail2.remove();
                this.remove();
            }else{
                this.setHealth(1);
            }
        }
    }

    public class MeleeAttackGoal extends Goal {
        protected final RekSaiEntity attacker;
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
        private int throwcd=(int)(Math.random() * 300 + 150);
        private int ticks=0;
        private int lastHit=0;
        // private int waterTick=0;
        private double lastX;
        private double lastY;
        private double lastZ;
        // private double attackerLastX;
        // private double attackerLastZ;
        // private double attackerLastY;
        private double sumaX, sumaZ;

        public MeleeAttackGoal(RekSaiEntity creature, double speedIn, boolean useLongMemory) {
            this.attacker = creature;
            this.speedTowardsTarget = speedIn;
            this.longMemory = useLongMemory;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }
        
        public boolean shouldExecute() { 
            LivingEntity livingentity = this.attacker.getAttackTarget();
            if (livingentity == null) {
                if(dataManager.get(STATE)==4 || dataManager.get(STATE)==2 || dataManager.get(STATE)==1 || dataManager.get(STATE)==3) dataManager.set(STATE, 5);
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else if(!spawnAnim) {
                return false;
            } else {
                if(dataManager.get(STATE)==0){
                    if(this.attacker.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ())>120){
                        this.path = this.attacker.getNavigator().pathfind(livingentity, 0);
                    }else{
                        return true;//this.attacker.getNavigator().clearPath();
                    }
                }
                if(dataManager.get(STATE)==4) return true;//this.path = this.attacker.getNavigator().pathfind(this.lastX, this.lastY, this.lastZ, 0);
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
                //return !this.attacker.getNavigator().noPath();
                return this.attacker.dataManager.get(STATE)==4 ? true : !this.attacker.getNavigator().noPath();
            } else if (!this.attacker.isWithinHomeDistanceFromPosition(livingentity.getPosition())) {
                return false;
            } else {                
                return !(livingentity instanceof PlayerEntity) || !livingentity.isSpectator() && !((PlayerEntity)livingentity).isCreative();
            }
        }
        
        public void startExecuting() {
            if(dataManager.get(STATE)==0 /*|| dataManager.get(STATE)==4*/) this.attacker.getNavigator().setPath(this.path, this.speedTowardsTarget);
            //if(dataManager.get(STATE)==0 || dataManager.get(STATE)==4) this.attacker.getNavigator().setPath(this.path, this.speedTowardsTarget);
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
            if(throwcd>0) --throwcd;
            lastHit++;

            LivingEntity livingentity = this.attacker.getAttackTarget();
            this.attacker.setRotation((float)(this.attacker.getLookVec().x*livingentity.getPosX()), (float)(this.attacker.getLookVec().z*livingentity.getPosZ()));

            double d0 = this.attacker.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());

            if ((this.longMemory || this.attacker.getEntitySenses().canSee(livingentity)) && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
                this.targetX = livingentity.getPosX(); this.targetY = livingentity.getPosY(); this.targetZ = livingentity.getPosZ();
            }
            // if(livingentity instanceof PlayerEntity && livingentity.isInWaterOrBubbleColumn() || livingentity.getEntityWorld().getBlockState(new BlockPos(targetX,targetY-1,targetZ))==Blocks.WATER.getDefaultState()){
            //     System.out.println("water: "+waterTick);
            //     if(waterTick<=150) waterTick++;
            // }else{
            //     if(waterTick>=150){
            //         System.out.println("out");
            //         waterTick-=50;
            //         if(waterTick<=0){
            //             waterTick=0;
            //             dataManager.set(HEAL, false);
            //             dataManager.set(STATE, 5);
            //             this.attacker.setPositionAndUpdate(this.attacker.LastX, this.attacker.LastY, this.attacker.LastZ);
            //             ticks=0;
            //             this.attacker.healticks=0;
            //         }
            //     }
            //     waterTick=0;
            // }

            // if(waterTick>=150){
            //     this.goHealing();
            // }else{
                this.checkAndPerformAttack(livingentity, d0);
            //}
        }

        // public void goHealing(){
        //     if(!dataManager.get(HEAL))dataManager.set(HEAL, true);
        //     ticks++;
        //     if(ticks==30){
        //         dataManager.set(HEAL, true);
        //         this.attackerLastX=this.attacker.getPosX(); this.attackerLastZ=this.attacker.getPosZ(); this.attackerLastY=this.attacker.getPosY();
        //     }
        //     if(dataManager.get(STATE)==0){
        //         dataManager.set(STATE, 3);
        //     }else if(dataManager.get(STATE)==3 && ticks>30){
        //         this.attacker.setPositionAndUpdate(attackerLastX, -15, attackerLastZ);
        //         this.attacker.heal(0.5F);
        //     }
        // }
        
        public void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            if(dataManager.get(STATE)==0 && lastHit>300){
                dataManager.set(STATE, 3);
                leap=true;
            }
            if(distToEnemySqr<100 && dataManager.get(STATE)==0 && this.attacker.isOnGround() && throwcd<=0){
                dataManager.set(STATE, 12);                
            }
            if(dataManager.get(STATE)==12){
                ticks++;
                if(ticks>15 && ticks<23){
                    double posx=this.attacker.getLookVec().x*10+this.attacker.getPosX(); double posz=this.attacker.getLookVec().z*10+this.attacker.getPosZ(); double posy=this.attacker.getPosY();
                    AxisAlignedBB bb= new AxisAlignedBB(posx+5, posy, posz+5, posx-5, posy+9, posz-5).union(this.attacker.leg.getBoundingBox());
                    this.attackBB(bb, this.attacker.damage*4, false, 0, true);
                }
                if(ticks>35){
                    dataManager.set(STATE, 0);
                    ticks=0;
                    throwcd=(int)(Math.random() * 300 + 150);
                }
            }
            if(distToEnemySqr<100 && dataManager.get(STATE)==0 && this.attacker.isOnGround() && cd<=0){
                //lastAttack=0;
                int chance=(int)(Math.random() * 5); int running=0;
                if(dataManager.get(RUN)==1) if(chance==0 || chance==2 || chance==4) running=2;
                if(chance<=2){
                    dataManager.set(STATE, 8+running);
                }else{
                    dataManager.set(STATE, 7+running);
                }
                return;
            }
            if(dataManager.get(STATE)==7 || dataManager.get(STATE)==8){
                ticks++;
                if((ticks>15 && ticks<20) || (ticks>40 && ticks<45)){
                    double posx=this.attacker.getLookVec().x*8+this.attacker.getPosX(); double posz=this.attacker.getLookVec().z*8+this.attacker.getPosZ(); double posy=this.attacker.getPosY();
                    AxisAlignedBB bb= new AxisAlignedBB(posx+7, posy, posz+7, posx-7, posy+5, posz-7).union(this.attacker.leg.getBoundingBox());
                    this.attackBB(bb, this.attacker.damage*10, false, 0, false);
                }
                if((ticks==25 && dataManager.get(STATE)==7) || ticks==55){
                    dataManager.set(STATE, 0);
                    ticks=0;
                    cd=(int)(Math.random() * 20 + 20);
                }
                return;
            }
            if(dataManager.get(STATE)==9 || dataManager.get(STATE)==10){
                ticks++;
                if((ticks>=3 && ticks<=8) || (ticks>=13 && ticks<=18)){
                    AxisAlignedBB bb= this.attacker.getBoundingBox().expand(8, 0, 8).expand(-8, 7, -8).union(this.attacker.leg.getBoundingBox());
                    this.attackBB(bb, this.attacker.damage*15, true, 0.4F, false);
                }
                if((ticks==10 && dataManager.get(STATE)==9) || ticks==20){
                    dataManager.set(STATE, 0);
                    ticks=0;
                    cd=(int)(Math.random() * 20 + 20);
                }
                return;
            }
            if(distToEnemySqr<400 && dataManager.get(STATE)==0 && this.attacker.isOnGround() && slam<=0){
                //lastAttack=0;
                this.lastX = this.attacker.getAttackTarget().getPosX();
                this.lastY = this.attacker.getAttackTarget().getPosY();
                this.lastZ = this.attacker.getAttackTarget().getPosZ();
                dataManager.set(STATE, 11);
                this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
                return;
            }
            if(dataManager.get(STATE)==11){
                ticks++;
                if(ticks>=25 && ticks<=30){
                    double posx=this.attacker.getLookVec().x*23+this.attacker.getPosX(); double posz=this.attacker.getLookVec().z*23+this.attacker.getPosZ(); double posy=this.attacker.getPosY();
                    AxisAlignedBB bb= new AxisAlignedBB(posx+3, posy, posz+3, posx-3, posy+5, posz-3).union(this.attacker.leg.getBoundingBox());
                    this.attackBB(bb, this.attacker.damage*25, true, 1F, false);
                }
                if(ticks==40){
                    dataManager.set(STATE, 0);
                    ticks=0;
                    slam=(int)(Math.random() * 60 + 80);
                    this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
                }
                return;
            }
            if(distToEnemySqr>550 && dataManager.get(STATE)==0 && charge<=0 && this.attacker.isOnGround()){
                dataManager.set(STATE, 3);
                this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
                return;
            }
            if(dataManager.get(STATE)==3){
                ++ticks;
                if(ticks==20){
                    this.lastX = this.attacker.getAttackTarget().getPosX();
                    this.lastY = this.attacker.getAttackTarget().getPosY();
                    this.lastZ = this.attacker.getAttackTarget().getPosZ();
                    if ((this.attacker.getPosY()+10.0D<this.lastY && leap==false) || leap){
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
                        this.attacker.world.playSound(null, this.attacker.getPosition(), ModSoundEvents.REKSAI_JUMP.get(), SoundCategory.HOSTILE, 3, 1);
                        dataManager.set(STATE, 2);
                    }else{
                        dataManager.set(STATE, 4);
                        this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(1.35);
                    }
                }
                return;
            }
            if(dataManager.get(STATE)==4){
                ++ticks;
                AxisAlignedBB bb= this.attacker.leg.getBoundingBox();
                this.breakBB(bb);
                this.attackBB(bb, this.attacker.damage*25, true, 1, false);
                if(this.attacker.getDistanceSq(this.lastX, this.attacker.getPosY(), this.lastZ)<=30 || ticks>60){
                    this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
                    dataManager.set(STATE, 5);
                    ticks=0;
                }
                if(ticks==1){
                    sumaX=(this.lastX-this.attacker.getPosX())/30;
                    sumaZ=(this.lastZ-this.attacker.getPosZ())/30;
                }
                this.attacker.setPositionAndRotation(this.attacker.getPosX()+sumaX, this.attacker.getPosY(), this.attacker.getPosZ()+sumaZ, (float)this.attacker.getLookVec().x, (float)this.attacker.getLookVec().z);
                return;
            }
            if(dataManager.get(STATE)==5){
                ++ticks;
                if(ticks==20){
                    charge=(int)(Math.random() * 150 + 30);
                    ticks=0;
                    dataManager.set(STATE, 0);
                }
                return;
            }
            if(dataManager.get(STATE)==2 || dataManager.get(STATE)==1 || dataManager.get(STATE)==6){
                AxisAlignedBB bb= this.attacker.leg.getBoundingBox().union(this.attacker.tail2.getBoundingBox());
                this.breakBB(bb);
                //???this.attackBB(bb, this.attacker.damage*5, true, 5);
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
                    enemy.attackEntityFrom(DamageSource.causeMobDamage(this.attacker), this.attacker.damage*20);
                    enemy.stopRiding();
                    enemy.startRiding(this.attacker, true);
                    lastHit=0;
                }
                if(enemy.isRidingSameEntity(this.attacker)==false && grab==true){
                    enemy.startRiding(this.attacker, true);
                }
                if((this.attacker.isOnGround() || this.attacker.isInWater()) && ticks>1){
                    if(grab){
                        enemy.stopRiding();
                        enemy.setPositionAndUpdate(this.attacker.getLookVec().x*-8+this.attacker.getPosX(), this.attacker.getPosY(), this.attacker.getLookVec().z*-8+this.attacker.getPosZ());
                        enemy.attackEntityFrom(DamageSource.causeMobDamage(this.attacker), this.attacker.damage*(jumpDamage+15));
                        lastHit=0;
                    }
                    grab=false;
                    ticks=0;
                    jumpDamage=0;
                    charge=(int)(Math.random() * 5 + 30);
                    leap=false;
                    dataManager.set(STATE, 0);
                    this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
                }else{
                    jumpDamage=this.attacker.fallDistance;
                }
                if(ticks>800){
                    enemy.stopRiding();
                    grab=false;
                    ticks=0;
                    jumpDamage=0;
                    leap=false;
                    dataManager.set(STATE, 0);
                    this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
                }
            }
            return;
        }

        protected void attackBB(AxisAlignedBB bb, float damage, boolean canKnockback, float knockbackStrenght, boolean motion){
            this.attacker.world.getEntitiesWithinAABB(LivingEntity.class, bb).stream().forEach(livingEntity -> {
                if(!livingEntity.isEntityEqual(this.attacker) && !(livingEntity instanceof CoRPartEntity)) {
                    livingEntity.attackEntityFrom(DamageSource.causeMobDamage(this.attacker), damage);
                    lastHit=0;
                    if(motion) livingEntity.setMotion(this.attacker.getLookVec().x*4, 4.5, this.attacker.getLookVec().z*4);
                    if(!livingEntity.world.isRemote && canKnockback) livingEntity.applyKnockback((float)this.attacker.getLookVec().x*4, knockbackStrenght, this.attacker.getLookVec().z*4);
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

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        if (this.isPassenger(passenger)) {
            double y, y2;
            if(dataManager.get(STATE)==6 || grabTicks>1.0D && grabTicks<=14.0D){
                y = this.getPosY()+13.0D-grabTicks;
                y2=y;
                grabTicks+=0.5D;
            }else if(dataManager.get(STATE)==1){
                y = this.getPosY();
                y2=y+4D;
                if(grabTicks>1.0D){
                    grabTicks=1.0D;
                }
            }else{
                y = this.getPosY()+13.0D;
                y2=y;
                if(grabTicks>1.0D){
                    grabTicks=1.0D;
                }
            }
            passenger.setPosition(this.getLookVec().x*-8+this.getPosX(), y, this.getLookVec().z*-8+this.getPosZ());
            this.tail2.setPosition(this.getLookVec().x*-8+this.getPosX(), y2, this.getLookVec().z*-8+this.getPosZ());
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        super.attackEntityFrom(source, amount);
        return false;
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player){ return 75+this.world.rand.nextInt(25); }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn){ return ModSoundEvents.REKSAI_HIT.get(); }
    //protected SoundEvent getDeathSound(){ return ModSoundEvents.REKSAI_ESCAPE.get(); }
    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) { return false; }
    @Override
    public boolean addPotionEffect(EffectInstance effectInstanceIn) { return false; }
    @Override
    protected boolean canBeRidden(Entity entityIn) { return false; }
    @Override
    public boolean canPassengerSteer() { return false; }
    @Override
    public boolean shouldRiderSit() { return false; }
    @Override
    public boolean canBePushed() { return false; }
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
    public boolean canBeCollidedWith() { return false; }
    @Override
    public void onCollideWithPlayer(PlayerEntity entityIn) { }
    @Override
    public boolean hitByEntity(Entity entityIn) { return true; }
    @Override
    protected void collideWithEntity(Entity p_82167_1_) { }
    @Override
    protected void collideWithNearbyEntities() { }
    @Override
    public int tickTimer() { return ticksExisted; }
}