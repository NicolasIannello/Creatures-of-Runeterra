package com.eximeisty.creaturesofruneterra.entity.custom;

import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
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
    private double velocidad=0.2;

    public PoroEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setTamed(false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 5)
        .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
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

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<IAnimatable>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            velocidad=0.3;
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(10.0D);
            this.setHealth(10.0F);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(5.0D);
        }
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
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
                if(item == Items.IRON_BLOCK){
                    this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
                    this.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
                    itemstack.shrink(1);
                    return ActionResultType.SUCCESS;
                }
                this.setSitting(!dataManager.get(STATE));
            }
            return super.getEntityInteractionResult(playerIn, hand);
        } 
    }

    public void switchEntity(TameableEntity poro, PlayerEntity owner){
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
    }

    @Override
    public void setSitting(boolean sit) {
        this.dataManager.set(STATE, sit);
        if(sit){
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
        }else{
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(velocidad);
        }
        super.setSitting(sit);
    }
}