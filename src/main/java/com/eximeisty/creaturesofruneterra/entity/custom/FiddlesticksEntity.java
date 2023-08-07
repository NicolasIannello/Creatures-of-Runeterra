package com.eximeisty.creaturesofruneterra.entity.custom;

import java.util.EnumSet;
import java.util.function.Predicate;

import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import com.eximeisty.creaturesofruneterra.util.ModSoundEvents;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
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
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.Path;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.BossInfo;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
    private float damage=(float)1;
    private double deathTicks=0;
    private float velocidad=(float)0.4;
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
    private static final AnimationBuilder DEATH_ANIM = new AnimationBuilder().addAnimation("animation.Fiddlesticks.death", false).addAnimation("animation.Fiddlesticks.deathloop", true);
    private static final Predicate<LivingEntity> NOT_THIS = (p_213797_0_) -> {
        if(!(p_213797_0_ instanceof PlayerEntity) && (p_213797_0_ instanceof FiddlesticksEntity || p_213797_0_ instanceof FiddleDummyEntity || p_213797_0_ instanceof WaterMobEntity || (p_213797_0_.isInWaterOrBubbleColumn() || p_213797_0_.getEntityWorld().getBlockState(new BlockPos(p_213797_0_.getPosX(),p_213797_0_.getPosY()-1,p_213797_0_.getPosZ()))==Blocks.WATER.getDefaultState()))) return false;
        return true;
    };

    public FiddlesticksEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 400)
        .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.4)
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
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1D,50){
            @Override
            public boolean shouldContinueExecuting() {
                if(this.creature.getDataManager().get(STATE)==8) return false;
                return !this.creature.getNavigator().noPath() && !this.creature.isBeingRidden();
            }
        });
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
                case 8: event.getController().setAnimation(DEATH_ANIM); break;
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
        if(this.getHealth()<=1){
            deathTicks++;
            dataManager.set(STATE, 8);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
            if(deathTicks==3) {
                this.world.playSound(null, this.getPosition(), ModSoundEvents.FIDDLESTICKS_DEATH.get(), SoundCategory.HOSTILE, 3, 1);
            }
            if(deathTicks>80){
                this.onKillCommand();
            }else{
                this.setHealth(1);
            }
        }
    }

    public class MeleeAttackGoal extends Goal {
        protected final FiddlesticksEntity attacker;
        private final double speedTowardsTarget;
        private final boolean longMemory;
        private Path path;
        private double targetX;
        private double targetY;
        private double targetZ;
        private int channel=1000;
        private int blind=700;
        private int run=400;
        private int lastHit=0;
        private int ticks=0;
        private int cd=0;
        private float prevHealth;

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
            if (cd>0) --cd;
            if(dataManager.get(STATE)<=0) lastHit++;

            LivingEntity livingentity = this.attacker.getAttackTarget();
            double d0 = this.attacker.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());

            if ((this.longMemory || this.attacker.getEntitySenses().canSee(livingentity)) && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
                this.targetX = livingentity.getPosX(); this.targetY = livingentity.getPosY(); this.targetZ = livingentity.getPosZ();
            }

            this.checkAndPerformAttack(livingentity, d0);
        }

        public void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            boolean flag = dataManager.get(STATE)==0 && this.attacker.isOnGround() && cd<=0;
            if((channel<=0 || lastHit>400) && flag && distToEnemySqr>200){
                Vector3d vector3d = new Vector3d(this.attacker.getPosX() - this.attacker.getAttackTarget().getPosX(), this.attacker.getPosYHeight(0.5D) - this.attacker.getAttackTarget().getPosYEye(), this.attacker.getPosZ() - this.attacker.getAttackTarget().getPosZ());
                vector3d = vector3d.normalize();
                BlockPos pos[]= new BlockPos[3];
                BlockPos posFiddle=null;
                int chance=(int)(Math.random() * 4);
                int j=0;
                double a=0,b=0,c=1,d=1;
                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 0:
                            a=this.attacker.getAttackTarget().getLookVec().x*7;
                            b=this.attacker.getAttackTarget().getLookVec().z*7;
                            break;
                        case 1:
                            c=-1;d=-1;
                            break;
                        case 2:
                            a=(this.attacker.getAttackTarget().getLookVec().x+0.9D)*7;
                            b=(this.attacker.getAttackTarget().getLookVec().z+0.9D)*7;
                            if(a>=1) a+=(this.attacker.getAttackTarget().getLookVec().x+0.9D-1)*7;
                            if(b>=1) b+=(this.attacker.getAttackTarget().getLookVec().z+0.9D-1)*7;
                            break;
                        case 3:
                            c=1;d=1;
                            break;
                    }
                    double x = (a)*c + this.attacker.getAttackTarget().getPosX();
                    double z = (b)*d + this.attacker.getAttackTarget().getPosZ();
                    double y = this.attacker.getAttackTarget().getPosY();
                    if(i!=chance) { 
                        pos[j]= new BlockPos(x,y,z);
                        j++; 
                    } else { 
                        posFiddle= new BlockPos(x,y,z); 
                    }
                }
                if(spawnDummies(pos, posFiddle)){
                    lastHit=0;
                    prevHealth = this.attacker.getHealth();
                    dataManager.set(STATE, 4);
                    this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
                    this.attacker.world.playSound(null, this.attacker.getPosition(), ModSoundEvents.FIDDLESTICKS_CHANNEL.get(), SoundCategory.HOSTILE, 2, 1);
                }
            }
            if(distToEnemySqr>=300 && flag && run<=0 && teleportToEntity(this.attacker.getAttackTarget())){
                dataManager.set(STATE, -1);
                this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.65);
                this.attacker.world.playSound(null, this.attacker.getPosition(), ModSoundEvents.FIDDLESTICKS_RUN.get(), SoundCategory.HOSTILE, 2, 1);
            }
            if(distToEnemySqr>=30 && flag && blind<=0 && this.attacker.getAttackTarget() instanceof PlayerEntity){
                dataManager.set(STATE, 6);
                this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
            }
            if(distToEnemySqr<15 && flag){
                int chance=(int)(Math.random() * 2)+1;
                dataManager.set(STATE, chance);
                this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
            }
            if(dataManager.get(STATE)>0) ticks++;
            switch (dataManager.get(STATE)) {
                case -1:
                    if(distToEnemySqr<15) {
                        dataManager.set(STATE, 3);
                        this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
                    }
                    break;
                case 1:
                    animNotifys(15, 20, 25, true, 20*this.attacker.damage, 1, 0, this.attacker.getLookVec().x*2, 0, this.attacker.getLookVec().z*2, ModSoundEvents.FIDDLESTICKS_ATTACK.get(), false);
                    break;
                case 2:
                    animNotifys(10, 15, 20, true, 15*this.attacker.damage, 1, 0, this.attacker.getLookVec().x*2, 0, this.attacker.getLookVec().z*2, ModSoundEvents.FIDDLESTICKS_ATTACK.get(), false);
                    break;
                case 3:
                    animNotifys(7, 12, 20, true, 40*this.attacker.damage, 1, 0, this.attacker.getLookVec().x*2, 0, this.attacker.getLookVec().z*2, ModSoundEvents.FIDDLESTICKS_ATTACK.get(), true);
                    break;
                case 4:
                    if(ticks%10==0){ 
                        this.attacker.world.addEntity(new FiddleProyectileEntity(this.attacker.world, this.attacker, this.attacker.getAttackTarget(), Direction.DOWN.getAxis()));
                        this.attacker.world.playSound(null, this.attacker.getPosition(), SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.AMBIENT, (float)(Math.random() * 5)+5, (float)(Math.random() * 2)+1);
                    }
                    if(ticks>200 || (prevHealth-100)>this.attacker.getHealth()) {
                        ticks=0;
                        dataManager.set(STATE, 5);
                    }
                    break;
                case 5:
                    if(ticks>10) resetState(true, 0, false, false, true);
                    break;
                case 6:
                    animNotifys(7, 40, 50, 60, false, ModSoundEvents.FIDDLESTICKS_CHANNEL.get());
                    break;
                case 7:
                    if(ticks>15) resetState(true, 0, true, false, false);
                    break;
            }
        }

        protected void animNotifys(int state ,int start, int end, int reset, boolean ms, SoundEvent sound){
            double d0 = this.attacker.getPosX() - this.attacker.getAttackTarget().getPosX();
            double d1 = this.attacker.getPosY() - 1 - this.attacker.getAttackTarget().getPosY();
            double d2 = this.attacker.getPosZ() - this.attacker.getAttackTarget().getPosZ();
            double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
            float x = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
            float y = (float) (-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
            
            if(ticks==2 && sound!=null) this.attacker.world.playSound(null, this.attacker.getPosition(), sound, SoundCategory.HOSTILE, 1, 1);
            if(ticks>start && ticks<end){
                if(this.attacker.getEntitySenses().canSee(this.attacker.getAttackTarget())){
                    this.attacker.getAttackTarget().addPotionEffect(new EffectInstance(Effects.BLINDNESS, 20*40));
                    if(MathHelper.degreesDifferenceAbs(this.attacker.getAttackTarget().rotationYaw, x)<65 && MathHelper.degreesDifferenceAbs(this.attacker.getAttackTarget().rotationPitch, y)<50) this.attacker.getAttackTarget().addPotionEffect(new EffectInstance(Effects.NAUSEA, 20*40));
                }
            }
            if(ticks>reset) resetState(ms, state, false, false, false);
        }

        protected void animNotifys(int bbStart, int bbEnd, int reset, boolean ms, float damage, double growXZ, double growY, double offsetX, double offsetY, double offsetZ, SoundEvent sound, boolean runAttack){
            AxisAlignedBB bb= this.attacker.getBoundingBox().grow(growXZ, growY, growXZ).offset(offsetX, offsetY, offsetZ);
            if(ticks==2 && sound!=null) this.attacker.world.playSound(null, this.attacker.getPosition(), sound, SoundCategory.HOSTILE, 1, 1);
            if(ticks>bbStart && ticks<bbEnd) attackBB(bb, damage, runAttack);
            if(ticks>reset) resetState(ms, 0, false, runAttack, false);
        }

        protected void resetState(boolean ms, int value, boolean blindReset, boolean runReset, boolean channelReset){
            dataManager.set(STATE, value);
            ticks=0;
            cd=(int)(Math.random() * 15 + 5);
            if(blindReset) blind = (int)(Math.random() * 400 + 400);
            if(runReset) run = (int)(Math.random() * 200 + 200);
            if(channelReset) channel = (int)(Math.random() * 600 + 600);
            if(ms) this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
        }

        protected void attackBB(AxisAlignedBB bb, float damage, boolean runAttack){
            this.attacker.world.getEntitiesWithinAABB(LivingEntity.class, bb).stream().forEach(livingEntity -> {
                if(!livingEntity.isEntityEqual(this.attacker)) {
                    livingEntity.attackEntityFrom(DamageSource.causeMobDamage(this.attacker), damage);
                    if (runAttack){
                        this.attacker.getAttackTarget().addPotionEffect(new EffectInstance(Effects.POISON, 20*30));
                        this.attacker.getAttackTarget().addPotionEffect(new EffectInstance(Effects.WITHER, 20*15));
                    }
                    lastHit=0;
                }
            });
        }

        // protected void breakBB(AxisAlignedBB bb){
        //     BlockPos.getAllInBox(bb).forEach(pos->{
        //         if(pos.getY()==this.attacker.getPosY()) this.attacker.world.setBlockState(pos, Blocks.WHITE_CARPET.getDefaultState());
        //     });
        // }

        private boolean teleportToEntity(Entity p_70816_1_) {
            Vector3d vector3d = new Vector3d(this.attacker.getPosX() - p_70816_1_.getPosX(), this.attacker.getPosYHeight(0.5D) - p_70816_1_.getPosYEye(), this.attacker.getPosZ() - p_70816_1_.getPosZ());
            vector3d = vector3d.normalize();
            double x = p_70816_1_.getLookVec().x*-20 + p_70816_1_.getPosX();
            double z = p_70816_1_.getLookVec().z*-20 + p_70816_1_.getPosZ();
            double y = this.attacker.getPosY() + (double)(this.attacker.rand.nextInt(16) - 8) - vector3d.y * 40.0D;
            return teleportTo(x, y, z);
        }

        private boolean teleportTo(double x, double y, double z) {
            BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(x, y, z);
            while(blockpos$mutable.getY() > 0 && !this.attacker.world.getBlockState(blockpos$mutable).getMaterial().blocksMovement()) blockpos$mutable.move(Direction.DOWN);
            BlockState blockstate = this.attacker.world.getBlockState(blockpos$mutable);
            boolean flag = blockstate.getMaterial().blocksMovement();
            boolean flag1 = blockstate.getFluidState().isTagged(FluidTags.WATER);
            if (flag && !flag1) {
                net.minecraftforge.event.entity.living.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory.onEnderTeleport(this.attacker, x, y, z);
                if (event.isCanceled()) return false;
                boolean flag2 = this.attacker.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
                if (flag2 && !this.attacker.isSilent()) {
                    this.attacker.world.playSound((PlayerEntity)null, this.attacker.prevPosX, this.attacker.prevPosY, this.attacker.prevPosZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.attacker.getSoundCategory(), 1.0F, 1.0F);
                    this.attacker.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
                return flag2;
            } else { 
                return false;
            }
        }

        public boolean spawnDummies(BlockPos pos[], BlockPos posFiddle){
            BlockPos pos2[]= new BlockPos[3];
            int i=0;
            for (BlockPos blockPos : pos) {
                final int j = new Integer(i);
                BlockPos.getAllInBox(new AxisAlignedBB(blockPos).grow(1, 4, 1)).forEach(position->{
                    if(this.attacker.world.getBlockState(position.down()).getMaterial().blocksMovement() && this.attacker.world.getBlockState(position)==Blocks.AIR.getDefaultState() && pos2[j]==null) pos2[j]=position;
                });
                if(pos2[j]!=null) i++;
            }
            if(pos2.length>=1 && teleportTo(posFiddle.getX(), posFiddle.getY(), posFiddle.getZ())){
                for (BlockPos blockPos2 : pos2) {
                    if(blockPos2!=null){
                        FiddleDummyEntity dummy = (FiddleDummyEntity)ModEntityTypes.FIDDLEDUMMY.get().spawn(world.getServer().func_241755_D_(), null, null, blockPos2, SpawnReason.NATURAL, false, false);
                        dummy.setParent(this.attacker);
                    }
                }
            }else{
                return false;
            }
            return true;
        }
        
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return (double)(this.attacker.getWidth() * 2.0F * this.attacker.getWidth() * 2.0F + attackTarget.getWidth());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        switch(id) {
            default:
                super.handleStatusUpdate(id);
                break;
            case 46:
                for(int j = 0; j < 150; ++j) {
                    double d0 = (double)j / 127.0D;
                    float f = (this.rand.nextFloat() - 0.5F) * 0.2F;
                    float f1 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                    float f2 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                    double d1 = MathHelper.lerp(d0, this.prevPosX, this.getPosX()) + (this.rand.nextDouble() - 0.5D) * (double)this.getWidth() * 2.0D;
                    double d2 = MathHelper.lerp(d0, this.prevPosY, this.getPosY()) + this.rand.nextDouble() * (double)this.getHeight();
                    double d3 = MathHelper.lerp(d0, this.prevPosZ, this.getPosZ()) + (this.rand.nextDouble() - 0.5D) * (double)this.getWidth() * 2.0D;
                    this.world.addParticle(ParticleTypes.ASH, d1, d2, d3, (double)f, (double)f1, (double)f2);
                }
                break;
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.isFireDamage()) return false;
        return super.attackEntityFrom(source, amount);
    }

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