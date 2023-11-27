package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.entity.ModEntities;
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
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PoroEntity extends TamableAnimal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Boolean> STATE = SynchedEntityData.defineId(PoroEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ATTACK = SynchedEntityData.defineId(PoroEntity.class, EntityDataSerializers.BOOLEAN);
    private double velocidad=0.2;
    private int ticks=0;
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().then("animation.poro.idle", Animation.LoopType.LOOP);
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().then("animation.poro.walk", Animation.LoopType.LOOP);
    private static final RawAnimation SIT_ANIM = RawAnimation.begin().then("animation.poro.sit", Animation.LoopType.LOOP);
    private static final RawAnimation ATTACK_ANIM = RawAnimation.begin().then("animation.poro.attack", Animation.LoopType.PLAY_ONCE);

    public PoroEntity(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
        this.setTame(false);
    }

    public static AttributeSupplier setAttributes(){
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 2)
                .add(Attributes.ARMOR, 2)
        .add(Attributes.ARMOR_TOUGHNESS, 0)
        .add(Attributes.KNOCKBACK_RESISTANCE, 0).build();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true){
            @Override
            public boolean canUse() {
                if(this.mob.getItemInHand(InteractionHand.MAIN_HAND)== ItemStack.EMPTY) return false;
                return super.canUse();
            }

            @Override
            protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
                double d0 = this.getAttackReachSqr(enemy);
                if (distToEnemySqr <= d0 && !this.mob.getEntityData().get(ATTACK)) {
                    this.resetAttackCooldown();
                    this.mob.doHurtTarget(enemy);
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.getEntityData().set(ATTACK, true);
                }
            }
        });
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D){
            @Override
            public boolean canUse() {
                if (this.mob.getItemInHand(InteractionHand.MAIN_HAND)!=ItemStack.EMPTY) return false; 
                return super.canUse();
            }
        });
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)));
    }

    @Override @Nullable
    public PoroEntity getBreedOffspring(ServerLevel p_149088_, AgeableMob p_149089_) {
        return null;
    }

    public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState)  {
        if(!entityData.get(ATTACK)){
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

    public void tick() {
        super.tick();
        if(entityData.get(ATTACK)){
            ticks++;
            if(ticks==15){
                ticks=0;
                entityData.set(ATTACK, false);
            }
        }
    }

    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            velocidad=0.25;
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(15.0D);
            this.setHealth(15.0F);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(5.0D);
        }
     }

    public InteractionResult mobInteract(Player playerIn, InteractionHand hand) {
        ItemStack itemstack = playerIn.getItemInHand(hand);
        Item item = itemstack.getItem();

        if (this.level.isClientSide) {
            boolean flag = this.isOwnedBy(playerIn) || this.isTame() || item == Items.COOKIE && !this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        }else{
            if(!this.isTame() && item == Items.COOKIE) {
                if (!playerIn.getAbilities().instabuild) itemstack.shrink(1);
    
                if (this.random.nextInt(5) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, playerIn)) {
                   this.tame(playerIn);
                    this.navigation.stop();
                    this.setOrderedToSit(true);
                   this.level.broadcastEntityEvent(this, (byte)7);
                } else {
                   this.level.broadcastEntityEvent(this, (byte)6);
                }
                return InteractionResult.SUCCESS;
            }
            if(this.isTame() && this.isOwnedBy(playerIn)){
                if(item == Items.ANVIL){
                    if (!playerIn.getAbilities().instabuild) itemstack.shrink(1);
                    switchEntity(ModEntities.FABLEDPORO.get(), playerIn);
                }
                if(item == Items.HEART_OF_THE_SEA){
                    if (!playerIn.getAbilities().instabuild) itemstack.shrink(1);
                    switchEntity(ModEntities.PLUNDERPORO.get(), playerIn);
                }
                if(item == Items.CHEST){
                    if (!playerIn.getAbilities().instabuild) itemstack.shrink(1);
                    switchEntity(ModEntities.PATCHEDPOROBOT.get(), playerIn);
                }
                if(item == Items.GOLD_BLOCK){
                    if (!playerIn.getAbilities().instabuild) itemstack.shrink(1);
                    switchEntity(ModEntities.EXALTEDPORO.get(), playerIn);
                }
                if(itemstack!=ItemStack.EMPTY){
                    if(item.canEquip(itemstack, EquipmentSlot.HEAD, this)){
                        this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), this.getItemBySlot(EquipmentSlot.HEAD)));
                        this.setItemSlotAndDropWhenKilled(EquipmentSlot.HEAD, itemstack.copy());
                        itemstack.shrink(1);
                        return InteractionResult.SUCCESS;
                    }else if(itemstack.isRepairable()){
                        this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), this.getItemInHand(InteractionHand.MAIN_HAND)));
                        this.setItemInHand(InteractionHand.MAIN_HAND, itemstack.copy());
                        itemstack.shrink(1);
                        return InteractionResult.SUCCESS;
                    }
                }else if(playerIn.isCrouching()){
                    this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), this.getItemInHand(InteractionHand.MAIN_HAND)));
                    this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), this.getItemBySlot(EquipmentSlot.HEAD)));
                    this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                    this.setItemSlotAndDropWhenKilled(EquipmentSlot.HEAD, ItemStack.EMPTY);
                }
                this.setOrderedToSit(!entityData.get(STATE));
            }
            return super.mobInteract(playerIn, hand);
        }
    }

    public void switchEntity(EntityType<?> poro, Player owner){
        this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), this.getItemInHand(InteractionHand.MAIN_HAND)));
        this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), this.getItemBySlot(EquipmentSlot.HEAD)));
        TamableAnimal entity= (TamableAnimal)poro.spawn(this.level.getServer().getLevel(this.level.dimension()), (ItemStack) null, null, this.getOnPos().above(), MobSpawnType.NATURAL, false, false);
        entity.tame(owner);
        this.discard();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(STATE, false);
        entityData.define(ATTACK, false);
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.entityData.set(STATE, sit);
        super.setOrderedToSit(sit);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(STATE, compound.getBoolean("Sitting"));
    }

    protected void dropAllDeathLoot(DamageSource p_21192_) {
        super.dropAllDeathLoot(p_21192_);
        this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), this.getItemInHand(InteractionHand.MAIN_HAND)));
    }

    protected SoundEvent getDeathSound() { return SoundEvents.BAT_DEATH; }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SoundEvents.FOX_HURT; }
    public SoundEvent getAmbientSound() { return SoundEvents.FOX_AMBIENT; }
}