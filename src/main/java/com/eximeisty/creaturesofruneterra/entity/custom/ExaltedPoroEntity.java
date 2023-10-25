package com.eximeisty.creaturesofruneterra.entity.custom;

import com.google.common.collect.Lists;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class ExaltedPoroEntity extends TamableAnimal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Boolean> STATE = SynchedEntityData.defineId(PoroEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> GG = SynchedEntityData.defineId(PoroEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CAST = SynchedEntityData.defineId(PoroEntity.class, EntityDataSerializers.BOOLEAN);
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().then("animation.exaltedporo.idle", Animation.LoopType.LOOP);
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().then("animation.exaltedporo.walk", Animation.LoopType.LOOP);
    private static final RawAnimation SIT_ANIM = RawAnimation.begin().then("animation.exaltedporo.sit", Animation.LoopType.LOOP);
    private static final RawAnimation CAST_ANIM = RawAnimation.begin().then("animation.exaltedporo.cast", Animation.LoopType.PLAY_ONCE);
    private int ticks=0;
    private int cd;
    public int giveGoldCD;
    private Player toCast;
    private List<MobEffect> effects = Lists.newArrayList(ForgeRegistries.MOB_EFFECTS.getValues());

    public ExaltedPoroEntity(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
        this.setTame(false);
    }

    public static AttributeSupplier setAttributes(){
        return TamableAnimal.createMobAttributes().add(Attributes.MAX_HEALTH, 15)
        .add(Attributes.MOVEMENT_SPEED, 0.30)
        .add(Attributes.ATTACK_DAMAGE, 2)
        .add(Attributes.ARMOR, 2)
        .add(Attributes.ARMOR_TOUGHNESS, 0)
        .add(Attributes.KNOCKBACK_RESISTANCE, 0).build();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new GiveGoldGoal(this, 1D, false));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Piglin.class, true));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)));
    }

    public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> event)  {
        if(!entityData.get(CAST)){
            if(entityData.get(STATE)){
                event.getController().setAnimation(SIT_ANIM);
                return PlayState.CONTINUE;
            }
            if (event.isMoving()) {
                event.getController().setAnimation(WALK_ANIM);
                return PlayState.CONTINUE;
            }
            event.getController().setAnimation(IDLE_ANIM);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    public <T extends GeoAnimatable> PlayState predicate2(AnimationState<T> event)  {
        if(entityData.get(CAST)){
            event.getController().setAnimation(CAST_ANIM);
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

    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(STATE, false);
        entityData.define(CAST, false);
        entityData.define(GG, true);
    }

    public void tick() {
        super.tick();
        if(cd>0) cd--;
        if(giveGoldCD>0 && !entityData.get(CAST)) giveGoldCD--;
        if(giveGoldCD<=0 && !entityData.get(GG)) entityData.set(GG, true);
        if(entityData.get(CAST)){
            entityData.set(GG, false);
            ticks++;
            if(!this.level().isClientSide && ticks%10==0) this.level().broadcastEntityEvent(this, (byte)14);
            if(ticks==100){
                ticks=0;
                giveGoldCD=20*40;
                if(!this.level().isClientSide && toCast!=null) toCast.addEffect(new MobEffectInstance(effects.get(new Random().nextInt(effects.size()-1)), 20*60));
                entityData.set(CAST, false);
                toCast=null;
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3);
            }
        }
    }

    public InteractionResult mobInteract(Player playerIn, InteractionHand hand) {
        ItemStack itemstack = playerIn.getItemInHand(hand);
        Item item = itemstack.getItem();

        if (this.level().isClientSide) {
            boolean flag = this.isOwnedBy(playerIn);
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        }else{
            if(item== Items.GOLD_INGOT){
                if(cd<=0){
                    this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
                    itemstack.shrink(1);
                    toCast=playerIn;
                    cd=20*120;
                    entityData.set(CAST, true);
                }else{
                    if(!this.level().isClientSide) this.level().broadcastEntityEvent(this, (byte)13);
                }
                return InteractionResult.SUCCESS;
            }else if(this.isOwnedBy(playerIn)){
                this.setOrderedToSit(!entityData.get(STATE));
            }
            return super.mobInteract(playerIn, hand);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 13) {
            this.spawnParticles(ParticleTypes.ANGRY_VILLAGER,0);
        }else if (id == 14) {
            this.spawnParticles(ParticleTypes.HAPPY_VILLAGER,1);
        }else {
            super.handleEntityEvent(id);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void spawnParticles(ParticleOptions particleData, int y) {
        for(int i = 0; i < 5; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(particleData, this.getRandomX(1.0D), this.getRandomY()+y, this.getRandomZ(1.0D), d0, d1, d2);
        }
    }

    @Override
    public boolean canChangeDimensions() {
        return true;
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.entityData.set(STATE, sit);
        super.setOrderedToSit(sit);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(STATE, compound.getBoolean("Sitting"));
        cd=compound.getInt("cd");
        giveGoldCD=compound.getInt("ggcd");
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("cd", cd);
        compound.putInt("ggcd", giveGoldCD);
    }

    protected void dropAllDeathLoot(DamageSource p_21192_) {
        super.dropAllDeathLoot(p_21192_);
        this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(Items.GOLD_NUGGET, 4)));
    }
    
    public class GiveGoldGoal extends Goal {
        protected final ExaltedPoroEntity attacker;
        private final double speedTowardsTarget;
        private final boolean longMemory;
        private Path path;
        private double targetX;
        private double targetY;
        private double targetZ;
        private int delayCounter;
        private long lastCheckTime;
        private boolean canPenalize = false;

        public GiveGoldGoal(ExaltedPoroEntity creature, double speedIn, boolean useLongMemory) {
            this.attacker = creature;
            this.speedTowardsTarget = speedIn;
            this.longMemory = useLongMemory;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            long i = this.attacker.level().getGameTime();

            if(!entityData.get(GG) || entityData.get(CAST)){
                return false;
            }else if (i - this.lastCheckTime < 20L) {
                return false;
            } else {
                this.lastCheckTime = i;
                LivingEntity livingentity = this.attacker.getTarget();
                if (livingentity == null) {
                    return false;
                } else if (!livingentity.isAlive()) {
                    return false;
                } else {
                if (canPenalize) {
                    if (--this.delayCounter <= 0) {
                        this.path = this.attacker.getNavigation().createPath(livingentity, 0);
                        this.delayCounter = 4 + this.attacker.getRandom().nextInt(7);
                        return this.path != null;
                    } else {
                        return true;
                    }
                    }
                    this.path = this.attacker.getNavigation().createPath(livingentity, 0);
                    if (this.path != null) {
                    return true;
                    } else {
                    return this.getAttackReachSqr(livingentity) >= this.attacker.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                    }
                }
            }
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = this.attacker.getTarget();

            if(!entityData.get(GG) || entityData.get(CAST)){
                return false;
            }else if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
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
        }

        public void stop() {
            LivingEntity livingentity = this.attacker.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
                this.attacker.setTarget((LivingEntity)null);
            }
            this.attacker.getNavigation().stop();
        }

        public void tick() {
            LivingEntity livingentity = this.attacker.getTarget();
            this.attacker.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            double d0 = this.attacker.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
            if ((this.longMemory || this.attacker.getSensing().hasLineOfSight(livingentity)) && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRandom().nextFloat() < 0.05F)) {
                this.targetX = livingentity.getX();
                this.targetY = livingentity.getY();
                this.targetZ = livingentity.getZ();
            }
            this.checkAndPerformAttack(livingentity, d0);
        }

        protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            if (distToEnemySqr <= 15 ) {
                entityData.set(CAST, true);
                this.attacker.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
                this.attacker.level().addFreshEntity(new ItemEntity(this.attacker.level(), this.attacker.getX(), this.attacker.getY(), this.attacker.getZ(), new ItemStack(Items.GOLD_INGOT)));
            }
        }

        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return (double)(this.attacker.getBbWidth() * 2.0F * this.attacker.getBbWidth() * 2.0F + attackTarget.getBbWidth());
        }
    }


    @Override @Nullable
    public PoroEntity getBreedOffspring(ServerLevel p_149088_, AgeableMob p_149089_) {
        return null;
    }
    protected SoundEvent getDeathSound() { return SoundEvents.BAT_DEATH; }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SoundEvents.FOX_HURT; }
    public SoundEvent getAmbientSound() { return SoundEvents.FOX_AMBIENT; }
}