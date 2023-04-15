package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtTargetGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class PoroEntity extends TameableEntity implements IAnimatable{
    private AnimationFactory factory = new AnimationFactory(this);
    private static final DataParameter<Boolean> STATE = EntityDataManager.createKey(PoroEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ATTACK = EntityDataManager.createKey(PoroEntity.class, DataSerializers.BOOLEAN);
    private double velocidad=0.2;
    private int ticks=0;

    public PoroEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setTamed(false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 5)
        .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2)
        .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2)
        .createMutableAttribute(Attributes.ARMOR, 2)
        .createMutableAttribute(Attributes.ARMOR_TOUGHNESS, 0)
        .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true){
            @Override
            public boolean shouldExecute() {
                if(this.attacker.getHeldItemMainhand()==ItemStack.EMPTY) return false;
                return super.shouldExecute();
            }

            @Override
            protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
                double d0 = this.getAttackReachSqr(enemy);
                if (distToEnemySqr <= d0 && !this.attacker.getDataManager().get(ATTACK)) {
                   this.resetSwingCooldown();
                   this.attacker.swingArm(Hand.MAIN_HAND);
                   this.attacker.attackEntityAsMob(enemy);
                   this.attacker.getDataManager().set(ATTACK, true);
                }
            }
        });
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D){
            @Override
            public boolean shouldExecute() {
                if (this.creature.getHeldItemMainhand()!=ItemStack.EMPTY) return false; 
                return super.shouldExecute();
            }
        });
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)));
    }

    @Override
    public AgeableEntity createChild(ServerWorld world, AgeableEntity mate) {
        return null;
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(dataManager.get(STATE)){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.poro.sit", true));
            return PlayState.CONTINUE;
        }
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.poro.walk", true));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.poro.idle", true));
        return PlayState.CONTINUE;
    }

    public <E extends IAnimatable> PlayState predicate2(AnimationEvent<E> event) {
        if(dataManager.get(ATTACK)){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.poro.attack", false));
            return PlayState.CONTINUE;
        }
        event.getController().clearAnimationCache();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<IAnimatable>(this, "controller", 0, this::predicate));
        data.addAnimationController(new AnimationController<IAnimatable>(this, "attacks", 0, this::predicate2));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public void tick() {
        super.tick();
        if(dataManager.get(ATTACK)){
            ticks++;
            if(ticks==15){
                ticks=0;
                dataManager.set(ATTACK, false);
            }
        }
    }

    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            velocidad=0.25;
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(15.0D);
            this.setHealth(15.0F);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(5.0D);
        }
     }
    
    public ActionResultType getEntityInteractionResult(PlayerEntity playerIn, Hand hand) {
        ItemStack itemstack = playerIn.getHeldItem(hand);
        Item item = itemstack.getItem();

        if (this.world.isRemote) {
            boolean flag = this.isOwner(playerIn) || this.isTamed() || item == Items.COOKIE && !this.isTamed();
            return flag ? ActionResultType.CONSUME : ActionResultType.PASS;
        }else{
            if(!this.isTamed() && item == Items.COOKIE) {
                if (!playerIn.abilities.isCreativeMode) itemstack.shrink(1);
    
                if (this.rand.nextInt(5) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, playerIn)) {
                   this.setTamedBy(playerIn);
                   this.navigator.clearPath();
                   this.setSitting(true);
                   this.world.setEntityState(this, (byte)7);
                } else {
                   this.world.setEntityState(this, (byte)6);
                }
                return ActionResultType.SUCCESS;
            }
            if(this.isTamed() && this.isOwner(playerIn)){
                if(item == Items.ANVIL){
                    if (!playerIn.abilities.isCreativeMode) itemstack.shrink(1);
                    FabledPoroEntity fableporo=new FabledPoroEntity(ModEntityTypes.FABLEDPORO.get(), this.world);
                    switchEntity(fableporo, playerIn);
                }
                if(item == Items.HEART_OF_THE_SEA){
                    if (!playerIn.abilities.isCreativeMode) itemstack.shrink(1);
                    PlunderPoroEntity plunderporo=new PlunderPoroEntity(ModEntityTypes.PLUNDERPORO.get(), this.world);
                    switchEntity(plunderporo, playerIn);
                }
                if(item == Items.CHEST){
                    if (!playerIn.abilities.isCreativeMode) itemstack.shrink(1);
                    PatchedPorobotEntity patchedporobot=new PatchedPorobotEntity(ModEntityTypes.PATCHEDPOROBOT.get(), this.world);
                    switchEntity(patchedporobot, playerIn);
                }
                if(itemstack!=ItemStack.EMPTY){
                    if(item.canEquip(itemstack, EquipmentSlotType.HEAD, this)){
                        this.entityDropItem(this.getItemStackFromSlot(EquipmentSlotType.HEAD));
                        this.setItemStackToSlot(EquipmentSlotType.HEAD, itemstack);
                        itemstack.shrink(1);
                        return ActionResultType.SUCCESS;
                    }else if(itemstack.isRepairable()){
                        this.entityDropItem(this.getHeldItemMainhand());
                        this.setHeldItem(Hand.MAIN_HAND, itemstack);
                        itemstack.shrink(1);
                        return ActionResultType.SUCCESS;
                    }
                }
                this.setSitting(!dataManager.get(STATE));
            }
            return super.getEntityInteractionResult(playerIn, hand);
        } 
    }

    public void switchEntity(TameableEntity poro, PlayerEntity owner){
        this.entityDropItem(this.getItemStackFromSlot(EquipmentSlotType.HEAD));
        this.entityDropItem(this.getHeldItemMainhand());
        poro.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
        poro.setTamedBy(owner);
        poro.setTamed(true);
        poro.setOwnerId(owner.getUniqueID());
        this.world.addEntity(poro);
        this.remove();
    }

    protected void registerData() {
        super.registerData();
        dataManager.register(STATE, false);
        dataManager.register(ATTACK, false);
    }

    @Override
    public void setSitting(boolean sit) {
        this.dataManager.set(STATE, sit);
        super.setSitting(sit);
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.dataManager.set(STATE, compound.getBoolean("Sitting"));
    }

    protected void dropInventory() {
        super.dropInventory();
        this.entityDropItem(this.getItemStackFromSlot(EquipmentSlotType.HEAD));
        this.entityDropItem(this.getHeldItemMainhand());
    }

    protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_BAT_DEATH; }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return SoundEvents.ENTITY_FOX_HURT; }
    public SoundEvent getAmbientSound() { return SoundEvents.ENTITY_FOX_AMBIENT; }
}