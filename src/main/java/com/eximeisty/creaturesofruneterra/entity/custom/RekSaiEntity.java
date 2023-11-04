package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.entity.ModEntities;
import com.eximeisty.creaturesofruneterra.util.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class RekSaiEntity extends PathfinderMob implements GeoEntity {
    public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(RekSaiEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> RUN = SynchedEntityData.defineId(RekSaiEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> HEAL = SynchedEntityData.defineId(RekSaiEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private double velocidad=0.4;
    private float damage=(float)1;
    private double grabTicks=1;
    private double deathTicks=0;
    private final ServerBossEvent bossInfo = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(false);
    private float jumpDamage=0;
    private boolean spawnAnim=false;
    private boolean spawn=false;
    // private int lastAttack=0;
    // private int run=150;
    // private static final EntityDataAccessor<Integer> SPAWN = EntityentityData.createKey(RekSaiEntity.class, DataSerializers.VARINT);
    private final CoRPartEntity head;
    private final CoRPartEntity body;
    private final CoRPartEntity leg;
    private final CoRPartEntity tail;
    private final CoRPartEntity tail2;
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().then("animation.Reksai.walk", Animation.LoopType.LOOP);
    private static final RawAnimation RUN_ANIM = RawAnimation.begin().then("animation.Reksai.run", Animation.LoopType.LOOP);
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().then("animation.Reksai.idle", Animation.LoopType.LOOP);
    private static final RawAnimation IDLE2_ANIM = RawAnimation.begin().then("animation.Reksai.idle2", Animation.LoopType.LOOP);
    private static final RawAnimation ATTACK1_ANIM = RawAnimation.begin().then("animation.Reksai.attack1", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation SPIN_ANIM = RawAnimation.begin().then("animation.Reksai.spin", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation ATTACK12_ANIM = RawAnimation.begin().then("animation.Reksai.attack1", Animation.LoopType.PLAY_ONCE).then("animation.Reksai.attack2", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation SPIN2_ANIM = RawAnimation.begin().then("animation.Reksai.spin", Animation.LoopType.PLAY_ONCE).then("animation.Reksai.spin", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation SLAM_ANIM = RawAnimation.begin().then("animation.Reksai.slam", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation RDOWN_ANIM = RawAnimation.begin().then("animation.Reksai.RDown", Animation.LoopType.LOOP);
    private static final RawAnimation RTRANS_ANIM = RawAnimation.begin().then("animation.Reksai.RTrans", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation R2_ANIM = RawAnimation.begin().then("animation.Reksai.R2", Animation.LoopType.LOOP);
    private static final RawAnimation BURROW_ANIM = RawAnimation.begin().then("animation.Reksai.burrow", Animation.LoopType.PLAY_ONCE).then("animation.Reksai.charge", Animation.LoopType.LOOP);
    private static final RawAnimation CHARGE_ANIM = RawAnimation.begin().then("animation.Reksai.charge", Animation.LoopType.LOOP);
    private static final RawAnimation SALIR_ANIM = RawAnimation.begin().then("animation.Reksai.salir", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation THROW_ANIM = RawAnimation.begin().then("animation.Reksai.throw", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation APPEAR_ANIM = RawAnimation.begin().then("animation.Reksai.appear", Animation.LoopType.PLAY_ONCE);
    private static final Predicate<LivingEntity> NOT_THIS = (p_213797_0_) -> {
        if(p_213797_0_ instanceof XerSaiDunebreakerEntity || p_213797_0_ instanceof XerSaiHatchlingEntity || (!(p_213797_0_ instanceof Player) && (p_213797_0_.isInWaterOrBubble() || p_213797_0_.level().getBlockState(p_213797_0_.getOnPos())== Blocks.WATER.defaultBlockState()))) return false;
        if(p_213797_0_ instanceof CoRPartEntity) if( ((CoRPartEntity)p_213797_0_).getParent() instanceof RekSaiEntity ) return false; 
        return true;
    };

    public RekSaiEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);
        head=new CoRPartEntity(ModEntities.WIVHIV.get(),this.level());
        body=new CoRPartEntity(ModEntities.WVIHV.get(),this.level());
        leg=new CoRPartEntity(ModEntities.WVIIHVI.get(),this.level());
        tail=new CoRPartEntity(ModEntities.WIIIHIII.get(),this.level());
        tail2=new CoRPartEntity(ModEntities.WIVHIV.get(),this.level());
    }

    public static AttributeSupplier setAttributes(){
        return PathfinderMob.createMobAttributes().add(Attributes.MAX_HEALTH, 600)//600
        .add(Attributes.MOVEMENT_SPEED, 0.4)//0.4
        .add(Attributes.ATTACK_DAMAGE, 2)//15?
        .add(Attributes.FOLLOW_RANGE, 80)//30?
        .add(Attributes.ATTACK_KNOCKBACK, 4)//?
        .add(Attributes.KNOCKBACK_RESISTANCE, 10)
        .add(Attributes.ARMOR, 8)
        .add(Attributes.ARMOR_TOUGHNESS, 8)
        .add(Attributes.ATTACK_SPEED, 0.3).build();
    }

    @Override
    protected void registerGoals(){
        super.registerGoals();
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>( this, Player.class, false ));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, velocidad,50));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.targetSelector.addGoal(6, (new HurtByTargetGoal(this)).setAlertOthers(XerSaiHatchlingEntity.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>( this, Mob.class, 0, false, false, NOT_THIS));
    }

    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> event)  {
        if(entityData.get(STATE)==0 || event.getController().getAnimationState()== AnimationController.State.STOPPED){
            if (event.isMoving()) {
                if(entityData.get(RUN)==0){
                    event.getController().setAnimation(WALK_ANIM);
                }else{
                    event.getController().setAnimation(RUN_ANIM);
                }
                return PlayState.CONTINUE;
            }
            if(entityData.get(RUN)==0){
                event.getController().setAnimation(IDLE_ANIM);
            }else{
                event.getController().setAnimation(IDLE2_ANIM);
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    public <T extends GeoAnimatable> PlayState predicate2(AnimationState<T> event)  {
        if (entityData.get(STATE)==7) {
            event.getController().setAnimation(ATTACK1_ANIM);
            return PlayState.CONTINUE;
        }
        if (entityData.get(STATE)==9) {
            event.getController().setAnimation(SPIN_ANIM);
            return PlayState.CONTINUE;
        }
        if (entityData.get(STATE)==8) {
            event.getController().setAnimation(ATTACK12_ANIM);
            return PlayState.CONTINUE;
        }
        if (entityData.get(STATE)==10) {
            event.getController().setAnimation(SPIN2_ANIM);
            return PlayState.CONTINUE;
        }
        if (entityData.get(STATE)==11) {
            event.getController().setAnimation(SLAM_ANIM);
            return PlayState.CONTINUE;
        }
        if (entityData.get(STATE)==1) {
            event.getController().setAnimation(RDOWN_ANIM);
            return PlayState.CONTINUE;
        }
        if (entityData.get(STATE)==6){
            event.getController().setAnimation(RTRANS_ANIM);
            return PlayState.CONTINUE;
        }
        if (entityData.get(STATE)==2) {
            event.getController().setAnimation(R2_ANIM);
            return PlayState.CONTINUE;
        }
        if (entityData.get(STATE)==3) {
            event.getController().setAnimation(BURROW_ANIM);
            return PlayState.CONTINUE;
        }
        if (entityData.get(STATE)==4) {
            event.getController().setAnimation(CHARGE_ANIM);
            return PlayState.CONTINUE;
        }
        if (entityData.get(STATE)==5) {
            event.getController().setAnimation(SALIR_ANIM);
            return PlayState.CONTINUE;
        }
        if (entityData.get(STATE)==12) {
            event.getController().setAnimation(THROW_ANIM);
            return PlayState.CONTINUE;
        }
        if (entityData.get(STATE)==20) {
            event.getController().setAnimation(APPEAR_ANIM);
            return PlayState.CONTINUE;
        }
        event.getController().forceAnimationReset();
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

    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(STATE, 20);
        entityData.define(RUN, 0);
        entityData.define(HEAL, false);
    }
    
    public void tick() {
        super.tick();
        if(!spawn){
            head.setParent(this);
            this.level().addFreshEntity(head);
            body.setParent(this);
            this.level().addFreshEntity(body);
            leg.setParent(this);
            this.level().addFreshEntity(leg);
            tail.setParent(this);
            this.level().addFreshEntity(tail);
            tail2.setParent(this);
            this.level().addFreshEntity(tail2);
            spawn=true;
        }
        if(entityData.get(STATE)==4){
            this.leg.setPos(this.getLookAngle().x*-3+this.getX(), this.getY(), this.getLookAngle().z*-3+this.getZ());
            this.tail.setPos(this.getLookAngle().x*-8.5+this.getX(), this.getY(), this.getLookAngle().z*-8.5+this.getZ());
            this.tail2.setPos(this.getLookAngle().x*-13+this.getX(), this.getY(), this.getLookAngle().z*-13+this.getZ());
            this.head.setPos(this.getLookAngle().x*8+this.getX(), this.getY(), this.getLookAngle().z*8+this.getZ());
            this.body.setPos(this.getLookAngle().x*3.5+this.getX(), this.getY(), this.getLookAngle().z*3.5+this.getZ());
        }else{
            if(entityData.get(RUN)==0){
                this.head.setPos(this.getLookAngle().x*8+this.getX(), this.getY()+10, this.getLookAngle().z*8+this.getZ());
                this.body.setPos(this.getLookAngle().x*3.5+this.getX(), this.getY()+9, this.getLookAngle().z*3.5+this.getZ());
            }else{
                this.head.setPos(this.getLookAngle().x*8+this.getX(), this.getY()+8, this.getLookAngle().z*8+this.getZ());
                this.body.setPos(this.getLookAngle().x*3.5+this.getX(), this.getY()+7, this.getLookAngle().z*3.5+this.getZ());
            }
            this.leg.setPos(this.getLookAngle().x*-3+this.getX(), this.getY(), this.getLookAngle().z*-3+this.getZ());
            this.tail.setPos(this.getLookAngle().x*-8.5+this.getX(), this.getY()+7, this.getLookAngle().z*-8.5+this.getZ());
            if(entityData.get(STATE)!=2) this.tail2.setPos(this.getLookAngle().x*-13+this.getX(), this.getY()+8, this.getLookAngle().z*-13+this.getZ());
        }
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
        if(!spawnAnim){
            grabTicks++;
            if(grabTicks==3) this.level().playSound(null, this.getOnPos(), ModSounds.REKSAI_APP.get(), SoundSource.HOSTILE, 3, 1);
            if(grabTicks==35){
                grabTicks=1.0D;
                entityData.set(STATE, 0);
                spawnAnim=true;
            }
        }
        if(this.getHealth()<this.getMaxHealth()/2 && entityData.get(RUN)==0){
            velocidad=0.6;
            this.level().playSound(null, this.getOnPos(), ModSounds.REKSAI_ANGRY.get(), SoundSource.HOSTILE, 3, 1);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
            entityData.set(RUN, 1);
        }
        if(this.getHealth()<=1 || deathTicks>0){
            deathTicks++;
            if(deathTicks==3) this.level().playSound(null, this.getOnPos(), ModSounds.REKSAI_ESCAPE.get(), SoundSource.HOSTILE, 3, 1);
            entityData.set(STATE, 3);
            if(deathTicks==30){
                this.head.discard();
                this.body.discard();
                this.leg.discard();
                this.tail.discard();
                this.tail2.discard();
                this.discard();
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
        private int waterTick=0;
        private double lastX;
        private double lastY;
        private double lastZ;
        private double attackerLastX;
        private double attackerLastZ;
        private double attackerLastY;
        private double sumaX, sumaZ;

        public MeleeAttackGoal(RekSaiEntity creature, double speedIn, boolean useLongMemory) {
            this.attacker = creature;
            this.speedTowardsTarget = speedIn;
            this.longMemory = useLongMemory;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }
        
        public boolean canUse() { 
            LivingEntity livingentity = this.attacker.getTarget();
            if (livingentity == null) {
                if(entityData.get(STATE)==4 || entityData.get(STATE)==2 || entityData.get(STATE)==1 || entityData.get(STATE)==3) entityData.set(STATE, 5);
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else if(!spawnAnim) {
                return false;
            } else {
                if(entityData.get(STATE)==0){
                    if(this.attacker.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ())>120){
                        this.path = this.attacker.getNavigation().createPath(livingentity, 0);
                    }else{
                        return true;//this.attacker.getNavigation().clearPath();
                    }
                }
                if(entityData.get(STATE)==4) return true;//this.path = this.attacker.getNavigation().pathfind(this.lastX, this.lastY, this.lastZ, 0);
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
                ticks=0;
                this.attacker.entityData.set(STATE, 0);
                this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
                return false;
            } else if (!livingentity.isAlive()) {
                ticks=0;
                this.attacker.entityData.set(STATE, 0);
                this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
                return false;
            } else if (!this.longMemory) {
                //return !this.attacker.getNavigation().noPath();
                return this.attacker.entityData.get(STATE)==4 ? true : !this.attacker.getNavigation().isDone();
            } else if (!this.attacker.isWithinRestriction(livingentity.blockPosition())) {
                return false;
            } else {                
                return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative();
            }
        }
        
        public void start() {
            if(entityData.get(STATE)==0 /*|| entityData.get(STATE)==4*/) this.attacker.getNavigation().moveTo(this.path, this.speedTowardsTarget);
            //if(entityData.get(STATE)==0 || entityData.get(STATE)==4) this.attacker.getNavigation().setPath(this.path, this.speedTowardsTarget);
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
            if(charge>0) --charge;
            if(cd>0) --cd;
            if(slam>0) --slam;
            if(throwcd>0) --throwcd;
            lastHit++;

            LivingEntity livingentity = this.attacker.getTarget();
            this.attacker.setRot((float)(this.attacker.getLookAngle().x*livingentity.getX()), (float)(this.attacker.getLookAngle().z*livingentity.getZ()));

            double d0 = this.attacker.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());

            if ((this.longMemory || this.attacker.getSensing().hasLineOfSight(livingentity)) && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRandom().nextFloat() < 0.05F)) {
                this.targetX = livingentity.getX(); this.targetY = livingentity.getY(); this.targetZ = livingentity.getZ();
            }
//            if(livingentity instanceof Player && livingentity.isInWaterOrBubble() || livingentity.getLevel().getBlockState(new BlockPos((int)targetX,(int)targetY-1,(int)targetZ))==Blocks.WATER.defaultBlockState()){
//                if(waterTick<=150) waterTick++;
//            }else{
//                if(waterTick>=150){
//                    entityData.set(HEAL, false);
//                    entityData.set(STATE, 5);
//                    this.attacker.setPos(attackerLastX, attackerLastY, attackerLastZ);//??????
//                    ticks=0;
//                }
//                waterTick=0;
//            }
//
//            if(waterTick>=150){
//                this.goHealing();
//            }else{
                this.checkAndPerformAttack(livingentity, d0);
            //}
        }

//        public void goHealing(){
//            ticks++;
//            if(ticks==30){
//                entityData.set(HEAL, true);
//                this.attackerLastX=this.attacker.getX(); this.attackerLastZ=this.attacker.getZ(); this.attackerLastY=this.attacker.getY();
//            }
//            if(entityData.get(STATE)==0){
//                entityData.set(STATE, 3);
//            }else if(entityData.get(STATE)==3 && ticks>30){
//                this.attacker.setPos(attackerLastX, -75, attackerLastZ);//??????
//                this.attacker.heal(0.5F);
//            }
//        }
        
        public void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            if(entityData.get(STATE)==0 && lastHit>300){
                entityData.set(STATE, 3);
                leap=true;
            }
            if(distToEnemySqr<100 && entityData.get(STATE)==0 && this.attacker.onGround() && throwcd<=0){
                entityData.set(STATE, 12);                
            }
            if(entityData.get(STATE)==12){
                ticks++;
                if(ticks>15/2 && ticks<23/2){
                    double posx=this.attacker.getLookAngle().x*10+this.attacker.getX(); double posz=this.attacker.getLookAngle().z*10+this.attacker.getZ(); double posy=this.attacker.getY();
                    AABB bb= new AABB(posx+5, posy, posz+5, posx-5, posy+9, posz-5).minmax(this.attacker.leg.getBoundingBox());
                    this.attackBB(bb, this.attacker.damage*4, false, 0, true);
                }
                if(ticks>35/2){
                    entityData.set(STATE, 0);
                    ticks=0;
                    throwcd=(int)(Math.random() * 300 + 150);
                }
            }
            if(distToEnemySqr<100 && entityData.get(STATE)==0 && this.attacker.onGround() && cd<=0){
                //lastAttack=0;
                int chance=(int)(Math.random() * 5); int running=0;
                if(entityData.get(RUN)==1) if(chance==0 || chance==2 || chance==4) running=2;
                if(chance<=2){
                    entityData.set(STATE, 8+running);
                }else{
                    entityData.set(STATE, 7+running);
                }
                return;
            }
            if(entityData.get(STATE)==7 || entityData.get(STATE)==8){
                ticks++;
                if((ticks>15/2 && ticks<20/2) || (ticks>40/2 && ticks<45/2)){
                    double posx=this.attacker.getLookAngle().x*8+this.attacker.getX(); double posz=this.attacker.getLookAngle().z*8+this.attacker.getZ(); double posy=this.attacker.getY();
                    AABB bb= new AABB(posx+7, posy, posz+7, posx-7, posy+5, posz-7).minmax(this.attacker.leg.getBoundingBox());
                    this.attackBB(bb, this.attacker.damage*10, false, 0, false);
                }
                if((ticks==25/2 && entityData.get(STATE)==7) || ticks==55/2){
                    entityData.set(STATE, 0);
                    ticks=0;
                    cd=(int)(Math.random() * 20 + 20);
                }
                return;
            }
            if(entityData.get(STATE)==9 || entityData.get(STATE)==10){
                ticks++;
                if((ticks>=3/2 && ticks<=8/2) || (ticks>=13/2 && ticks<=18/2)){
                    AABB bb= this.attacker.getBoundingBox().expandTowards(8, 0, 8).expandTowards(-8, 7, -8).minmax(this.attacker.leg.getBoundingBox());
                    this.attackBB(bb, this.attacker.damage*15, true, 0.4F, false);
                }
                if((ticks==10/2 && entityData.get(STATE)==9) || ticks==20/2){
                    entityData.set(STATE, 0);
                    ticks=0;
                    cd=(int)(Math.random() * 20 + 20);
                }
                return;
            }
            if(distToEnemySqr<400 && entityData.get(STATE)==0 && this.attacker.onGround() && slam<=0){
                //lastAttack=0;
                this.lastX = this.attacker.getTarget().getX();
                this.lastY = this.attacker.getTarget().getY();
                this.lastZ = this.attacker.getTarget().getZ();
                entityData.set(STATE, 11);
                this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
                return;
            }
            if(entityData.get(STATE)==11){
                ticks++;
                if(ticks>=25/2 && ticks<=30/2){
                    double posx=this.attacker.getLookAngle().x*23+this.attacker.getX(); double posz=this.attacker.getLookAngle().z*23+this.attacker.getZ(); double posy=this.attacker.getY();
                    AABB bb= new AABB(posx+3, posy, posz+3, posx-3, posy+5, posz-3).minmax(this.attacker.leg.getBoundingBox());
                    this.attackBB(bb, this.attacker.damage*25, true, 1F, false);
                }
                if(ticks==40/2){
                    entityData.set(STATE, 0);
                    ticks=0;
                    slam=(int)(Math.random() * 60 + 80);
                    this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
                }
                return;
            }
            if(distToEnemySqr>550 && entityData.get(STATE)==0 && charge<=0 && this.attacker.onGround()){
                entityData.set(STATE, 3);
                this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
                return;
            }
            if(entityData.get(STATE)==3){
                ++ticks;
                if(ticks==20/2){
                    this.lastX = this.attacker.getTarget().getX();
                    this.lastY = this.attacker.getTarget().getY();
                    this.lastZ = this.attacker.getTarget().getZ();
                    if ((this.attacker.getY()+10.0D<this.lastY && leap==false) || leap){
                        leap=true;
                    }else{
                        if((this.lastX-this.attacker.getX())<=10 && (this.lastX-this.attacker.getX())>=-10){
                            if(this.lastZ<this.attacker.getZ()){
                                this.lastZ-=20;
                            }else{
                                this.lastZ+=20;
                            }
                        }else{
                            double m= (this.lastZ-this.attacker.getZ())/(this.lastX-this.attacker.getX());
                            double b= this.attacker.getZ()-(m*this.attacker.getX());
                            if(this.lastX<this.attacker.getX()){
                                this.lastX-=20;
                            }else{
                                this.lastX+=20;
                            }
                            this.lastZ= m*this.lastX+b;
                        } 
                    }
                }
                if(ticks==30/2){
                    ticks=0;
                    if(leap==true){
                        charge=(int)(Math.random() * 150 + 30);
                        Vec3 vector3d = this.attacker.getDeltaMovement();
                        double vectorY=((this.attacker.getTarget().getY() - this.attacker.getY())/10)+0.5;
                        Vec3 vector3d1 = new Vec3(this.attacker.getTarget().getX() - this.attacker.getX(), 0D, this.attacker.getTarget().getZ() - this.attacker.getZ());
                        if (vector3d1.lengthSqr() > 1.0E-7D) {
                            vector3d1 = vector3d1.scale(0.2D).add(vector3d.scale(0.2D));
                        }
                        this.attacker.setDeltaMovement(vector3d1.x, vectorY, vector3d1.z);
                        this.attacker.level().playSound(null, this.attacker.blockPosition(), ModSounds.REKSAI_JUMP.get(), SoundSource.HOSTILE, 3, 1);
                        entityData.set(STATE, 2);
                    }else{
                        entityData.set(STATE, 4);
                        this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(1.35);
                    }
                }
                return;
            }
            if(entityData.get(STATE)==4){
                ++ticks;
                AABB bb= this.attacker.leg.getBoundingBox();
                this.breakBB(bb);
                this.attackBB(bb, this.attacker.damage*25, true, 1, false);
                if(this.attacker.distanceToSqr(this.lastX, this.attacker.getY(), this.lastZ)<=30 || ticks>60){
                    this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
                    entityData.set(STATE, 5);
                    ticks=0;
                }
                if(ticks==1){
                    sumaX=(this.lastX-this.attacker.getX())/30;
                    sumaZ=(this.lastZ-this.attacker.getZ())/30;
                }
                this.attacker.setPos(this.attacker.getX()+sumaX, this.attacker.getY(), this.attacker.getZ()+sumaZ);
                this.attacker.setRot((float)this.attacker.getLookAngle().x, (float)this.attacker.getLookAngle().z);
                return;
            }
            if(entityData.get(STATE)==5){
                ++ticks;
                if(ticks==20/2){
                    charge=(int)(Math.random() * 150 + 30);
                    ticks=0;
                    entityData.set(STATE, 0);
                }
                return;
            }
            if(entityData.get(STATE)==2 || entityData.get(STATE)==1 || entityData.get(STATE)==6){
                AABB bb= this.attacker.leg.getBoundingBox().minmax(this.attacker.tail2.getBoundingBox());
                this.breakBB(bb);
                //???this.attackBB(bb, this.attacker.damage*5, true, 5);
                if(this.attacker.fallDistance>0){
                    if(ticks==0){
                        entityData.set(STATE, 6);
                    }else if(ticks==20/2){
                        entityData.set(STATE, 1);
                    }
                    ++ticks;
                }
                if(distToEnemySqr<30 && grab==false){
                    grab=true;
                    enemy.hurt(damageSources().mobAttack(this.attacker), this.attacker.damage*20);
                    enemy.stopRiding();
                    enemy.startRiding(this.attacker, true);
                    lastHit=0;
                }
                if(this.attacker.hasPassenger(enemy) && grab==true){//if(enemy.isRidingSameEntity(this.attacker)==false && grab==true){
                    enemy.startRiding(this.attacker, true);
                }
                if((this.attacker.onGround() || this.attacker.isInWater()) && ticks>1){
                    if(grab){
                        enemy.stopRiding();
                        enemy.setPos(this.attacker.getLookAngle().x*-8+this.attacker.getX(), this.attacker.getY(), this.attacker.getLookAngle().z*-8+this.attacker.getZ());
                        enemy.hurt(damageSources().mobAttack(this.attacker), this.attacker.damage*(jumpDamage+15));
                        lastHit=0;
                    }
                    grab=false;
                    ticks=0;
                    jumpDamage=0;
                    charge=(int)(Math.random() * 5 + 30);
                    leap=false;
                    entityData.set(STATE, 0);
                    this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
                }else{
                    jumpDamage=this.attacker.fallDistance;
                }
                if(ticks>800/2){
                    enemy.stopRiding();
                    grab=false;
                    ticks=0;
                    jumpDamage=0;
                    leap=false;
                    entityData.set(STATE, 0);
                    this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
                }
            }
            return;
        }

        protected void attackBB(AABB bb, float damage, boolean canKnockback, float knockbackStrenght, boolean motion){
            this.attacker.level().getEntities(null, bb).stream().forEach(livingEntity -> {///?????????
                if(!livingEntity.is(this.attacker) && !(livingEntity instanceof CoRPartEntity)) {
                    livingEntity.hurt(damageSources().mobAttack(this.attacker), damage);
                    lastHit=0;
                    if(motion) livingEntity.setDeltaMovement(this.attacker.getLookAngle().x*4, 4.5, this.attacker.getLookAngle().z*4);
                    if(!livingEntity.level().isClientSide && canKnockback) livingEntity.push(this.attacker.getLookAngle().x*4, knockbackStrenght, this.attacker.getLookAngle().z*4);
                }
            });
        }

        protected void breakBB(AABB bb){
            BlockPos.betweenClosedStream(bb).forEach(pos ->{
                if( this.attacker.level().getBlockState(pos)!=Blocks.AIR.defaultBlockState() && this.attacker.level().getBlockState(pos)!=Blocks.WATER.defaultBlockState() && this.attacker.level().getBlockState(pos)!=Blocks.LAVA.defaultBlockState() && this.attacker.level().getBlockState(pos)!=Blocks.BEDROCK.defaultBlockState()){
                    this.attacker.level().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                }
            });
        }

        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return (double)(this.attacker.getBbWidth() * 2.0F * this.attacker.getBbWidth() * 2.0F + attackTarget.getBbWidth());
        }
    }

    public void positionRider(Entity passenger, Entity.MoveFunction p_19958_) {
        if (this.hasPassenger(passenger)) {
            double y, y2;
            if(entityData.get(STATE)==6 || grabTicks>1.0D && grabTicks<=14.0D){
                y = this.getY()+13.0D-grabTicks;
                y2=y;
                grabTicks+=0.5D;
            }else if(entityData.get(STATE)==1){
                y = this.getY();
                y2=y+4D;
                if(grabTicks>1.0D){
                    grabTicks=1.0D;
                }
            }else{
                y = this.getY()+13.0D;
                y2=y;
                if(grabTicks>1.0D){
                    grabTicks=1.0D;
                }
            }
            p_19958_.accept(passenger, this.getLookAngle().x*-8+this.getX(), y, this.getLookAngle().z*-8+this.getZ());
            this.tail2.setPos(this.getLookAngle().x*-8+this.getX(), y2, this.getLookAngle().z*-8+this.getZ());
        }
    }

    public boolean hurt(DamageSource source, float amount) {
        super.hurt(source, amount);
        return false;
    }

    @Override
    public int getExperienceReward(){ return 75+this.level().random.nextInt(25); }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn){ return ModSounds.REKSAI_HIT.get(); }
    protected SoundEvent getDeathSound(){ return ModSounds.REKSAI_ESCAPE.get(); }
    @Override
    public boolean causeFallDamage(float p_146828_, float p_146829_, DamageSource p_146830_) { return false; }
    @Override
    public boolean addEffect(MobEffectInstance effectInstanceIn, @Nullable Entity p_147209_) { return false; }
    @Override
    public boolean shouldRiderSit() { return false; }
    @Override
    public boolean canChangeDimensions() { return false; }
    @Override
    public boolean isPersistenceRequired() { return true; }
    @Override
    public boolean requiresCustomPersistence() { return true; }
    @Override
    protected boolean shouldDespawnInPeaceful() { return true; }
    @Override
    public boolean canBeLeashed(Player p_21418_) { return false; }
    @Override
    public boolean isPushable() { return false; }
    @Override
    public boolean canBeCollidedWith() { return false; }
    @Override
    public boolean canCollideWith(Entity p_20303_) { return false; }
}