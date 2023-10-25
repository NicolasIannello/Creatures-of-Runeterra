package com.eximeisty.creaturesofruneterra.entity.custom;

import java.util.EnumSet;
import java.util.function.Predicate;

import com.eximeisty.creaturesofruneterra.entity.ModEntities;
import com.eximeisty.creaturesofruneterra.item.ModItems;

import com.eximeisty.creaturesofruneterra.util.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class FiddlesticksEntity extends PathfinderMob implements GeoEntity {
    public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(FiddlesticksEntity.class, EntityDataSerializers.INT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private float damage=(float)1;
    private double deathTicks=0;
    private float velocidad=(float)0.4;
    private final ServerBossEvent bossInfo = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true).setCreateWorldFog(true);
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().then("animation.Fiddlesticks.walk", Animation.LoopType.LOOP);
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().then("animation.Fiddlesticks.idle", Animation.LoopType.LOOP);
    private static final RawAnimation CHANNEL_ANIM = RawAnimation.begin().then("animation.Fiddlesticks.channel", Animation.LoopType.PLAY_ONCE).then("animation.Fiddlesticks.channelloop", Animation.LoopType.LOOP);
    private static final RawAnimation ATTACK1_ANIM = RawAnimation.begin().then("animation.Fiddlesticks.attack1", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation ATTACK2_ANIM = RawAnimation.begin().then("animation.Fiddlesticks.attack2", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation RUN_ANIM = RawAnimation.begin().then("animation.Fiddlesticks.run", Animation.LoopType.LOOP);
    private static final RawAnimation RUNATTACK_ANIM = RawAnimation.begin().then("animation.Fiddlesticks.runattack", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation BLIND_ANIM = RawAnimation.begin().then("animation.Fiddlesticks.blind", Animation.LoopType.PLAY_ONCE).then("animation.Fiddlesticks.blindloop", Animation.LoopType.LOOP);
    private static final RawAnimation CHANNELREVERT_ANIM = RawAnimation.begin().then("animation.Fiddlesticks.channelrevert", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation BLINDREVERT_ANIM = RawAnimation.begin().then("animation.Fiddlesticks.blindrevert", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation DEATH_ANIM = RawAnimation.begin().then("animation.Fiddlesticks.death", Animation.LoopType.PLAY_ONCE).then("animation.Fiddlesticks.deathloop", Animation.LoopType.LOOP);
    private static final Predicate<LivingEntity> NOT_THIS = (p_213797_0_) -> {
        if(!(p_213797_0_ instanceof Player) && (p_213797_0_ instanceof FiddlesticksEntity || p_213797_0_ instanceof FiddleDummyEntity || p_213797_0_ instanceof WaterAnimal || (p_213797_0_.isInWaterOrBubble() || p_213797_0_.level().getBlockState(new BlockPos(p_213797_0_.getBlockX(),p_213797_0_.getBlockY()-1,p_213797_0_.getBlockZ()))== Blocks.WATER.defaultBlockState()))) return false;
        return true;
    };

    public FiddlesticksEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier setAttributes(){
        return PathfinderMob.createMobAttributes().add(Attributes.MAX_HEALTH, 400)
        .add(Attributes.MOVEMENT_SPEED, 0.4)
        .add(Attributes.ATTACK_DAMAGE, 2)
        .add(Attributes.FOLLOW_RANGE, 80)
        .add(Attributes.ATTACK_KNOCKBACK, 4)
        .add(Attributes.KNOCKBACK_RESISTANCE, 2)
        .add(Attributes.ARMOR, 4)
        .add(Attributes.ARMOR_TOUGHNESS, 4)
        .add(Attributes.ATTACK_SPEED, 0.3).build();
    }

    @Override
    protected void registerGoals(){
        super.registerGoals();
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>( this, Player.class, false ));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1D,50){
            @Override
            public boolean canContinueToUse() {
                if(this.mob.getEntityData().get(STATE)==8) return false;
                return !this.mob.getNavigation().isDone() && !this.mob.isVehicle();
            }
        });
        this.goalSelector.addGoal(0, new FloatGoal(this));
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
        if(entityData.get(STATE)<=0){
            if (event.isMoving()) {
                RawAnimation anim= (entityData.get(STATE)==-1) ? RUN_ANIM : WALK_ANIM;
                event.getController().setAnimation(anim);
            }else{
                event.getController().setAnimation(IDLE_ANIM);
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    public <T extends GeoAnimatable> PlayState predicate2(AnimationState<T> event)  {
        if (entityData.get(STATE)>0){
            switch (entityData.get(STATE)) {
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
        this.dropLeash(true, false);
        return;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(STATE, 0);
    }

    public void tick() {
        super.tick();
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
        if(this.getHealth()<=1){
            deathTicks++;
            entityData.set(STATE, 8);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
            if(deathTicks==3) {
                this.level().playSound(null, this.blockPosition(), ModSounds.FIDDLESTICKS_DEATH.get(), SoundSource.HOSTILE, 3, 1);
            }
            if(deathTicks>80){
                this.kill();
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
        private int channel=1000/4;
        private int blind=700/4;
        private int run=400/4;
        private int lastHit=0;
        private int ticks=0;
        private int cd=0;
        private float prevHealth;

        public MeleeAttackGoal(FiddlesticksEntity creature, double speedIn, boolean useLongMemory) {
            this.attacker = creature;
            this.speedTowardsTarget = speedIn;
            this.longMemory = useLongMemory;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
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
            } else {
                if(entityData.get(STATE)>0) {
                    this.path=null;
                    return true;
                }
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
            if (channel>0) --channel;
            if (blind>0) --blind;
            if (run>0) --run;
            if (cd>0) --cd;
            if(entityData.get(STATE)<=0) lastHit++;

            LivingEntity livingentity = this.attacker.getTarget();
            double d0 = this.attacker.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());

            if ((this.longMemory || this.attacker.getSensing().hasLineOfSight(livingentity)) && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRandom().nextFloat() < 0.05F)) {
                this.targetX = livingentity.getX(); this.targetY = livingentity.getY(); this.targetZ = livingentity.getZ();
            }

            this.checkAndPerformAttack(livingentity, d0);
        }

        public void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            boolean flag = entityData.get(STATE)==0 && this.attacker.onGround() && cd<=0;
            if((channel<=0 || lastHit>400/2) && flag && distToEnemySqr>200){
                Vec3 vector3d = new Vec3(this.attacker.getX() - this.attacker.getTarget().getX(), this.attacker.getY(0.5D) - this.attacker.getTarget().getEyeY(), this.attacker.getZ() - this.attacker.getTarget().getZ());
                vector3d = vector3d.normalize();
                BlockPos pos[]= new BlockPos[3];
                BlockPos posFiddle=null;
                int chance=(int)(Math.random() * 4);
                int j=0;
                int a=0,b=0,c=1,d=1;
                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 0:
                            a=(int)this.attacker.getTarget().getLookAngle().x*7;
                            b=(int)this.attacker.getTarget().getLookAngle().z*7;
                            break;
                        case 1:
                            c=-1;d=-1;
                            break;
                        case 2:
                            a=(int)(this.attacker.getTarget().getLookAngle().x+0.9D)*7;
                            b=(int)(this.attacker.getTarget().getLookAngle().z+0.9D)*7;
                            if(a>=1) a+=(this.attacker.getTarget().getLookAngle().x+0.9D-1)*7;
                            if(b>=1) b+=(this.attacker.getTarget().getLookAngle().z+0.9D-1)*7;
                            break;
                        case 3:
                            c=1;d=1;
                            break;
                    }
                    int x = (a)*c + this.attacker.getTarget().getBlockX();
                    int z = (b)*d + this.attacker.getTarget().getBlockZ();
                    int y = this.attacker.getTarget().getBlockY();
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
                    entityData.set(STATE, 4);
                    this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
                    this.attacker.level().playSound(null, this.attacker.blockPosition(), ModSounds.FIDDLESTICKS_CHANNEL.get(), SoundSource.HOSTILE, 2, 1);
                }
            }
            if(distToEnemySqr>=300 && flag && run<=0 && teleportToEntity(this.attacker.getTarget())){
                entityData.set(STATE, -1);
                this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.65);
                this.attacker.level().playSound(null, this.attacker.blockPosition(), ModSounds.FIDDLESTICKS_RUN.get(), SoundSource.HOSTILE, 2, 1);
            }
            if(distToEnemySqr>=30 && flag && blind<=0 && this.attacker.getTarget() instanceof Player){
                entityData.set(STATE, 6);
                this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
            }
            if(distToEnemySqr<15 && flag){
                int chance=(int)(Math.random() * 2)+1;
                entityData.set(STATE, chance);
                this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
            }
            if(entityData.get(STATE)>0) ticks++;
            switch (entityData.get(STATE)) {
                case -1:
                    if(distToEnemySqr<15) {
                        entityData.set(STATE, 3);
                        this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
                    }
                    break;
                case 1:
                    animNotifys(15, 20, 25, true, 20*this.attacker.damage, 2, 0, this.attacker.getLookAngle().x*2, 0, this.attacker.getLookAngle().z*2, ModSounds.FIDDLESTICKS_ATTACK.get(), false);
                    break;
                case 2:
                    animNotifys(10, 15, 20, true, 15*this.attacker.damage, 2, 0, this.attacker.getLookAngle().x*2, 0, this.attacker.getLookAngle().z*2, ModSounds.FIDDLESTICKS_ATTACK.get(), false);
                    break;
                case 3:
                    animNotifys(7, 12, 20, true, 40*this.attacker.damage, 2, 0, this.attacker.getLookAngle().x*2, 0, this.attacker.getLookAngle().z*2, ModSounds.FIDDLESTICKS_ATTACK.get(), true);
                    break;
                case 4:
                    if(ticks%10==0){
                        this.attacker.level().addFreshEntity(new FiddleProyectileEntity(this.attacker.level(), this.attacker, this.attacker.getTarget(), Direction.DOWN.getAxis(), null));
                        this.attacker.level().playSound(null, this.attacker.blockPosition(), SoundEvents.SOUL_ESCAPE, SoundSource.AMBIENT, (float)(Math.random() * 5)+5, (float)(Math.random() * 2)+1);
                    }
                    if(ticks>200 || (prevHealth-100)>this.attacker.getHealth()) {
                        ticks=0;
                        entityData.set(STATE, 5);
                    }
                    break;
                case 5:
                    if(ticks>10) resetState(true, 0, false, false, true);
                    break;
                case 6:
                    animNotifys(7, 70, 80, 90, false, ModSounds.FIDDLESTICKS_CHANNEL.get());
                    break;
                case 7:
                    if(ticks>15) resetState(true, 0, true, false, false);
                    break;
            }
        }

        protected void animNotifys(int state ,int start, int end, int reset, boolean ms, SoundEvent sound){
            start/=2; end/=2; reset/=2;
            double d0 = this.attacker.getX() - this.attacker.getTarget().getX();
            double d1 = this.attacker.getY() - 1 - this.attacker.getTarget().getY();
            double d2 = this.attacker.getZ() - this.attacker.getTarget().getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            float x = (float) (Math.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
            float y = (float) (-(Math.atan2(d1, d3) * (180D / Math.PI)));

            if(ticks==2 && sound!=null) this.attacker.level().playSound(null, this.attacker.blockPosition(), sound, SoundSource.HOSTILE, 1, 1);
            if(ticks>start && ticks<end){
                if(this.attacker.getSensing().hasLineOfSight(this.attacker.getTarget())){
                    this.attacker.getTarget().addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20*30));
                    if(Mth.degreesDifferenceAbs(this.attacker.getTarget().getYRot(), x)<65 && Mth.degreesDifferenceAbs(this.attacker.getTarget().getXRot(), y)<50) this.attacker.getTarget().addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20*30));
                }
            }
            if(ticks>reset) resetState(ms, state, false, false, false);
        }

        protected void animNotifys(int bbStart, int bbEnd, int reset, boolean ms, float damage, double growXZ, double growY, double offsetX, double offsetY, double offsetZ, SoundEvent sound, boolean runAttack){
            bbStart/=2; bbEnd/=2; reset/=2;
            AABB bb= this.attacker.getBoundingBox().inflate(growXZ, growY, growXZ).move(offsetX, offsetY, offsetZ);
            if(ticks==2 && sound!=null) this.attacker.level().playSound(null, this.attacker.blockPosition(), sound, SoundSource.HOSTILE, 1, 1);
            if(ticks>bbStart && ticks<bbEnd) attackBB(bb, damage, runAttack);
            if(ticks>reset) resetState(ms, 0, false, runAttack, false);
        }

        protected void resetState(boolean ms, int value, boolean blindReset, boolean runReset, boolean channelReset){
            entityData.set(STATE, value);
            ticks=0;
            cd=(int)(Math.random() * 15 + 5)/3;
            if(blindReset) blind = (int)(Math.random() * 400 + 400)/4;
            if(runReset) run = (int)(Math.random() * 200 + 200)/4;
            if(channelReset) channel = (int)(Math.random() * 600 + 600)/4;
            if(ms) this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
        }

        protected void attackBB(AABB bb, float damage, boolean runAttack){
            this.attacker.level().getEntities(null, bb).stream().forEach(livingEntity -> {
                if(!livingEntity.is(this.attacker)) {
                    livingEntity.hurt(damageSources().mobAttack(this.attacker), damage);
                    if (runAttack){
                        this.attacker.getTarget().addEffect(new MobEffectInstance(MobEffects.POISON, 20*30));
                        this.attacker.getTarget().addEffect(new MobEffectInstance(MobEffects.WITHER, 20*15));
                    }
                    lastHit=0;
                }
            });
        }

        // protected void breakBB(AABB bb){
        //     BlockPos.getAllInBox(bb).forEach(pos->{
        //         if(pos.getY()==this.attacker.getY()) this.attacker.world.setBlockState(pos, Blocks.WHITE_CARPET.getDefaultState());
        //     });
        // }

        private boolean teleportToEntity(Entity p_70816_1_) {
            Vec3 vector3d = new Vec3(this.attacker.getX() - p_70816_1_.getX(), this.attacker.getY(0.5D) - p_70816_1_.getEyeY(), this.attacker.getZ() - p_70816_1_.getZ());
            vector3d = vector3d.normalize();
            double x = p_70816_1_.getLookAngle().x*-20 + p_70816_1_.getX();
            double z = p_70816_1_.getLookAngle().z*-20 + p_70816_1_.getZ();
            double y = this.attacker.getY() + (double)(this.attacker.random.nextInt(16) - 8) - vector3d.y * 40.0D;
            return teleportTo(x, y, z);
        }

        private boolean teleportTo(double x, double y, double z) {
            BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos(x, y, z);
            while(blockpos$mutable.getY() > this.attacker.level().getMinBuildHeight() && !this.attacker.level().getBlockState(blockpos$mutable).getMaterial().blocksMotion()) blockpos$mutable.move(Direction.DOWN);
            BlockState blockstate = this.attacker.level().getBlockState(blockpos$mutable);
            boolean flag = blockstate.getMaterial().blocksMotion();
            boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
            if (flag && !flag1) {
                net.minecraftforge.event.entity.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory.onEnderTeleport(this.attacker, x, y, z);
                if (event.isCanceled()) return false;
                boolean flag2 = this.attacker.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
                if (flag2 && !this.attacker.isSilent()) {
                    this.attacker.level().playSound((Player)null, this.attacker.xo, this.attacker.yo, this.attacker.zo, SoundEvents.ENDERMAN_TELEPORT, this.attacker.getSoundSource(), 1.0F, 1.0F);
                    this.attacker.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
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
                final int j = Integer.valueOf(i);
                BlockPos.betweenClosedStream(new AABB(blockPos).inflate(1, 4, 1)).forEach(position->{
                    if(this.attacker.level().getBlockState(position.below()).getMaterial().blocksMotion() && this.attacker.level().getBlockState(position)==Blocks.AIR.defaultBlockState() && pos2[j]==null) pos2[j]=position;
                });
                if(pos2[j]!=null) i++;
            }
            if(pos2.length>=1 && teleportTo(posFiddle.getX(), posFiddle.getY(), posFiddle.getZ())){
                for (BlockPos blockPos2 : pos2) {
                    if(blockPos2!=null){
                        FiddleDummyEntity dummy = (FiddleDummyEntity) ModEntities.FIDDLEDUMMY.get().spawn(level().getServer().getLevel(level().dimension()), (ItemStack)null, null, blockPos2, MobSpawnType.NATURAL, false, false);
                        dummy.setParent(this.attacker);
                    }
                }
            }else{
                return false;
            }
            return true;
        }

        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return (double)(this.attacker.getBbWidth() * 2.0F * this.attacker.getBbWidth() * 2.0F + attackTarget.getBbWidth());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        switch(id) {
            default:
                super.handleEntityEvent(id);
                break;
            case 46:
                for(int j = 0; j < 150; ++j) {
                    double d0 = (double)j / 127.0D;
                    float f = (this.random.nextFloat() - 0.5F) * 0.2F;
                    float f1 = (this.random.nextFloat() - 0.5F) * 0.2F;
                    float f2 = (this.random.nextFloat() - 0.5F) * 0.2F;
                    double d1 = Mth.lerp(d0, this.xo, this.getX()) + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth() * 2.0D;
                    double d2 = Mth.lerp(d0, this.yo, this.getY()) + this.random.nextDouble() * (double)this.getBbHeight();
                    double d3 = Mth.lerp(d0, this.zo, this.getZ()) + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth() * 2.0D;
                    this.level().addParticle(ParticleTypes.ASH, d1, d2, d3, (double)f, (double)f1, (double)f2);
                }
                break;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(source.is(DamageTypes.ON_FIRE)) return false;
        return super.hurt(source, amount);
    }

    protected void dropAllDeathLoot(DamageSource p_21192_) {
        super.dropAllDeathLoot(p_21192_);
        this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(ModItems.FIDDLESCYTHE.get(), 1)));
        this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(ModItems.FIDDLE_BEARTRAP.get(), 1)));
        this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(ModItems.FIDDLE_BIRDCAGE.get(), 1)));
    }

    @Override
    public int getExperienceReward(){ return 75+this.level().random.nextInt(25); }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn){ return ModSounds.FIDDLESTICKS_HURT.get(); }
    @Override
    public boolean addEffect(MobEffectInstance effectInstanceIn, @Nullable Entity p_147209_) { return false; }
//    @Override
//    protected boolean canBeRidden(Entity entityIn) { return false; }
    @Override
    public boolean canBeLeashed(Player p_21418_) { return false; }
    @Override
    public boolean shouldRiderSit() { return false; }
//    @Override
//    public boolean canBeRiddenInWater(Entity rider) { return true; }
    @Override
    public boolean canChangeDimensions() { return false; }
//    @Override
//    public boolean canDespawn(double distanceToClosestPlayer) { return false; }
    @Override
    public boolean shouldDespawnInPeaceful() { return true; }
    @Override
    public boolean requiresCustomPersistence() { return true; }
    @Override
    public boolean isPersistenceRequired() { return true; }
}