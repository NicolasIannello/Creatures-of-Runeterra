package com.eximeisty.creaturesofruneterra.entity.custom;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class FabledPoroEntity extends TamableAnimal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Boolean> STATE = SynchedEntityData.defineId(FabledPoroEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FORGE = SynchedEntityData.defineId(FabledPoroEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ATTACK = SynchedEntityData.defineId(FabledPoroEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DAY = SynchedEntityData.defineId(FabledPoroEntity.class, EntityDataSerializers.INT);
    private double velocidad=0.25;
    private int ticks=0;
    public ItemStack forgeItem= ItemStack.EMPTY;
    private static final RawAnimation FORGE_ANIM = RawAnimation.begin().then("animation.fabledporo.forge", Animation.LoopType.LOOP);
    private static final RawAnimation SIT_ANIM = RawAnimation.begin().then("animation.fabledporo.sit", Animation.LoopType.LOOP);
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().then("animation.fabledporo.walk", Animation.LoopType.LOOP);
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().then("animation.fabledporo.idle", Animation.LoopType.LOOP);
    private static final RawAnimation ATTACK_ANIM = RawAnimation.begin().then("animation.fabledporo.attack", Animation.LoopType.PLAY_ONCE);

    public FabledPoroEntity(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
        this.setTame(false);
    }

    public static AttributeSupplier setAttributes(){
        return TamableAnimal.createMobAttributes().add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.MOVEMENT_SPEED, 0.23)
                .add(Attributes.ATTACK_DAMAGE, 7)
                .add(Attributes.ATTACK_SPEED, 1.2)
                .add(Attributes.ATTACK_KNOCKBACK, 1)
                .add(Attributes.ARMOR, 4)
                .add(Attributes.ARMOR_TOUGHNESS, 1).build();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true){
            @Override
            protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
                double d0 = this.getAttackReachSqr(enemy);
                if (distToEnemySqr <= d0 && !this.mob.getEntityData().get(ATTACK) && !this.mob.getEntityData().get(FORGE)) {
                    this.resetAttackCooldown();
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.doHurtTarget(enemy);
                    this.mob.getEntityData().set(ATTACK, true);
                }
            }
        });
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)));
    }

    public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState)  {
        if(!entityData.get(ATTACK)){
            if(entityData.get(FORGE)){
                tAnimationState.getController().setAnimation(FORGE_ANIM);
                return PlayState.CONTINUE;
            }
            if(entityData.get(STATE)){
                tAnimationState.getController().setAnimation(SIT_ANIM);
                return PlayState.CONTINUE;
            }
            if (tAnimationState.isMoving()) {
                tAnimationState.getController().setAnimation(WALK_ANIM);
                return PlayState.CONTINUE;
            }
            tAnimationState.getController().setAnimation(IDLE_ANIM);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    public <T extends GeoAnimatable> PlayState predicate2(AnimationState<T> tAnimationState)  {
        if(entityData.get(ATTACK)){
            tAnimationState.getController().setAnimation(ATTACK_ANIM);
            return PlayState.CONTINUE;
        }
        tAnimationState.getController().forceAnimationReset();
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
        entityData.define(FORGE, false);
        entityData.define(ATTACK, false);
        entityData.define(DAY, -1);
    }

    @Override @Nullable
    public FabledPoroEntity getBreedOffspring(ServerLevel p_149088_, AgeableMob p_149089_) {
        return null;
    }

    public void tick() {
        super.tick();
        if(this.getItemInHand(InteractionHand.MAIN_HAND)!=ItemStack.EMPTY && entityData.get(FORGE)){
            ticks++;
            if(ticks==10 || ticks==30 || ticks==50 || ticks==70 || ticks==90) this.level().playSound(null, this.getOnPos(), SoundEvents.ANVIL_LAND, SoundSource.NEUTRAL, 0.5F, 1);
            if(ticks==100){
                entityData.set(FORGE, false);
                if(!entityData.get(STATE)) this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
                forgeItem.setDamageValue((forgeItem.getDamageValue()-forgeItem.getDamageValue()/2));
                //this.entityDropItem(forgeItem);
                this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), forgeItem));
                this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                ticks=0;
            }
        }
        if(entityData.get(ATTACK)){
            ticks++;
            if(ticks==22){
                ticks=0;
                entityData.set(ATTACK, false);
            }
        }
    }

    public InteractionResult mobInteract(Player playerIn, InteractionHand hand) {
        ItemStack itemstack = playerIn.getItemInHand(hand);

        if (this.level().isClientSide) {
            boolean flag = this.isOwnedBy(playerIn);
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        }else{
            int day=(int)(this.level().getDayTime()/24000);
            if(itemstack.isRepairable()){
                if(entityData.get(DAY)!=day){
                    entityData.set(DAY, day);
                    entityData.set(FORGE, true);
                    this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
                    forgeItem=itemstack.copy();
                    this.setItemInHand(InteractionHand.MAIN_HAND, forgeItem);
                    itemstack.shrink(1);
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
            this.spawnParticles(ParticleTypes.ANGRY_VILLAGER);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void spawnParticles(ParticleOptions particleData) {
        for(int i = 0; i < 5; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(particleData, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
        }
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.entityData.set(STATE, sit);
        super.setOrderedToSit(sit);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("day", this.entityData.get(DAY));
        compound.putBoolean("forge", this.entityData.get(FORGE));
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(STATE, compound.getBoolean("Sitting"));
        this.entityData.set(DAY, compound.getInt("day"));
        this.entityData.set(FORGE, compound.getBoolean("forge"));
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
    }

    protected SoundEvent getAmbientSound() { return SoundEvents.EVOKER_AMBIENT; }
    protected SoundEvent getDeathSound() { return SoundEvents.EVOKER_DEATH; }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SoundEvents.EVOKER_HURT; }
}