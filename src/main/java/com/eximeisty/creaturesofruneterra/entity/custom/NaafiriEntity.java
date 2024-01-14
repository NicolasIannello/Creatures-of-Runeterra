package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.entity.ModEntities;
import com.eximeisty.creaturesofruneterra.item.ModItems;
import com.eximeisty.creaturesofruneterra.util.ModSounds;
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
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class NaafiriEntity extends TamableAnimal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Boolean> STATE = SynchedEntityData.defineId(NaafiriEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> ATTACK = SynchedEntityData.defineId(NaafiriEntity.class, EntityDataSerializers.INT);
    private int ticks=0;
    private int ticksHound=0;
    private int ticksDagger=0;
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().then("animation.naafiri.idle", Animation.LoopType.LOOP);
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().then("animation.naafiri.walk", Animation.LoopType.LOOP);
    private static final RawAnimation SIT_ANIM = RawAnimation.begin().then("animation.naafiri.sit", Animation.LoopType.LOOP);
    private static final RawAnimation ATTACK_ANIM = RawAnimation.begin().then("animation.naafiri.bite", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation SUMMON_ANIM = RawAnimation.begin().then("animation.naafiri.spawn", Animation.LoopType.PLAY_ONCE);

    public NaafiriEntity(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
        this.setTame(false);
    }

    public static AttributeSupplier setAttributes(){
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.MOVEMENT_SPEED, 0.33)
                .add(Attributes.ATTACK_DAMAGE, 12)
                .add(Attributes.ARMOR, 7)
                .add(Attributes.ARMOR_TOUGHNESS, 7)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0).build();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true){
            @Override
            protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
                double d0 = this.getAttackReachSqr(enemy);
                if(d0 >= 5 && this.mob.getEntityData().get(ATTACK)==0 && ((NaafiriEntity)this.mob).ticksHound==0){
                    this.mob.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
                    this.mob.getEntityData().set(ATTACK, 2);
                }
                if(d0 >= 3 && this.mob.getEntityData().get(ATTACK)==0 && ((NaafiriEntity)this.mob).ticksDagger==0){
                    this.mob.getEntityData().set(ATTACK, 4);
                }
                if (distToEnemySqr <= d0 && this.mob.getEntityData().get(ATTACK)==0) {
                    this.resetAttackCooldown();
                    this.mob.doHurtTarget(enemy);
                    this.mob.getEntityData().set(ATTACK, 1);
                }
            }
        });
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)));
    }

    @Override @Nullable
    public NaafiriEntity getBreedOffspring(ServerLevel p_149088_, AgeableMob p_149089_) {
        return null;
    }

    public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState)  {
        if(entityData.get(ATTACK)==0){
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
        if(entityData.get(ATTACK)==1){
            tAnimationState.getController().setAnimation(ATTACK_ANIM);
            return PlayState.CONTINUE;
        }
        if(entityData.get(ATTACK)==2){
            tAnimationState.getController().setAnimation(SUMMON_ANIM);
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

    public void tick() {
        super.tick();
        if(ticksHound>0 && getTarget()!=null) ticksHound--;
        if(ticksDagger>0 && getTarget()!=null) ticksDagger--;
        if(entityData.get(ATTACK)==1){
            ticks++;
            if(ticks==5) this.level().playSound(null, this.blockPosition(), ModSounds.NAAFIRI_ATTACK.get(), SoundSource.NEUTRAL, 0.5f, 1);
            if(ticks>15){
                ticks=0;
                entityData.set(ATTACK, 0);
            }
        }
        if(entityData.get(ATTACK)==2){
            ticks++;
            if(ticks==10) this.level().playSound(null, this.blockPosition(), ModSounds.NAAFIRI_SPAWN.get(), SoundSource.NEUTRAL, 0.5f, 1);
            if(ticks==55 && !this.level().isClientSide){
                NaafiriHoundEntity hound= ModEntities.NAAFIRI_HOUND.get().spawn(this.level().getServer().getLevel(this.level().dimension()), (ItemStack) null, null, this.getOnPos().north(2).above(2), MobSpawnType.NATURAL, false, false);
                NaafiriHoundEntity hound2= ModEntities.NAAFIRI_HOUND.get().spawn(this.level().getServer().getLevel(this.level().dimension()), (ItemStack) null, null, this.getOnPos().south(2).above(2), MobSpawnType.NATURAL, false, false);
                NaafiriHoundEntity hound3= ModEntities.NAAFIRI_HOUND.get().spawn(this.level().getServer().getLevel(this.level().dimension()), (ItemStack) null, null, this.getOnPos().east(2).above(2), MobSpawnType.NATURAL, false, false);
                NaafiriHoundEntity hound4= ModEntities.NAAFIRI_HOUND.get().spawn(this.level().getServer().getLevel(this.level().dimension()), (ItemStack) null, null, this.getOnPos().west(2).above(2), MobSpawnType.NATURAL, false, false);
                hound.setTarget(getTarget());hound2.setTarget(getTarget());hound3.setTarget(getTarget());hound4.setTarget(getTarget());
                hound.tame((Player)(this).getOwner());hound2.tame((Player)(this).getOwner());hound3.tame((Player)(this).getOwner());hound4.tame((Player)(this).getOwner());
            }
            if(ticks>70){
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.33);
                ticks=0;
                ticksHound=500;
                entityData.set(ATTACK, 0);
            }
        }
        if(entityData.get(ATTACK)==4){
            ticks++;
            if(ticks==8 && !this.level().isClientSide && getTarget()!=null){
                NaafiriDaggerEntity dagger= new NaafiriDaggerEntity(level(), this);
                dagger.setSoundEvent(SoundEvents.BLASTFURNACE_FIRE_CRACKLE);
                dagger.setCritArrow(true);
                dagger.setNoGravity(true);
                double d0 = getTarget().getX() - this.getX();
                double d1 = getTarget().getY(0.3333333333333333D) - dagger.getY();
                double d2 = getTarget().getZ() - this.getZ();
                double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                dagger.shoot(d0, (d1 + d3 * (double)0.2F)*0, d2, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
                this.level().addFreshEntity(dagger);
            }
            if(ticks>15){
                ticks=0;
                ticksDagger=150;
                entityData.set(ATTACK, 0);
            }
        }
    }

    public void setTame(boolean tamed) {
        super.setTame(tamed);
    }

    public InteractionResult mobInteract(Player playerIn, InteractionHand hand) {
        if (this.level().isClientSide) {
            boolean flag = this.isOwnedBy(playerIn) || this.isTame() && !this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        }else{
            ItemStack item = playerIn.getItemInHand(hand);
            if(item.is(ModItems.NAAFIRI.get())){
                item.getAttributeModifiers(EquipmentSlot.MAINHAND).forEach((atr, modifier) -> {
                    if (atr == Attributes.ATTACK_DAMAGE) {
                        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(modifier.getAmount()*4);
                        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(modifier.getAmount()+5);
                        this.getAttribute(Attributes.ARMOR).setBaseValue(modifier.getAmount());
                        this.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(modifier.getAmount());
                    }
                });
                if(!this.level().isClientSide && ticks%10==0) this.level().broadcastEntityEvent(this, (byte)14);
                this.setHealth(getMaxHealth());
                item.shrink(1);
                return InteractionResult.SUCCESS;
            }

            if(this.isTame() && this.isOwnedBy(playerIn)){
                this.setOrderedToSit(!entityData.get(STATE));
            }
            return InteractionResult.SUCCESS;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 14) {
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

    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(STATE, false);
        entityData.define(ATTACK, 0);
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.entityData.set(STATE, sit);
        super.setOrderedToSit(sit);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(STATE, compound.getBoolean("Sitting"));
        this.entityData.set(ATTACK, compound.getInt("attack"));
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("attack", this.entityData.get(ATTACK));
    }

    protected void dropAllDeathLoot(DamageSource p_21192_) {
        super.dropAllDeathLoot(p_21192_);
        //this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), this.getItemInHand(InteractionHand.MAIN_HAND)));
    }

    protected SoundEvent getDeathSound() { return ModSounds.NAAFIRI_DEATH.get(); }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return ModSounds.NAAFIRI_HURT.get(); }
    public SoundEvent getAmbientSound() { return null; }
}