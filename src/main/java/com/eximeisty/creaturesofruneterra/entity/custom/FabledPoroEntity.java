package com.eximeisty.creaturesofruneterra.entity.custom;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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

public class FabledPoroEntity extends TameableEntity implements IAnimatable{
    private AnimationFactory factory = new AnimationFactory(this);
    private static final DataParameter<Boolean> STATE = EntityDataManager.createKey(FabledPoroEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FORGE = EntityDataManager.createKey(FabledPoroEntity.class, DataSerializers.BOOLEAN);
    private double velocidad=0.25;
    private int ticks=0;
    public ItemStack forgeItem=ItemStack.EMPTY;

    public FabledPoroEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setTamed(false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 60)
        .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25)
        .createMutableAttribute(Attributes.ATTACK_DAMAGE, 7)
        .createMutableAttribute(Attributes.ATTACK_SPEED, 1.2)
        .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(dataManager.get(FORGE)){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.fabledporo.forge", true));
            return PlayState.CONTINUE;
        }
        if(dataManager.get(STATE)){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.fabledporo.sit", true));
            return PlayState.CONTINUE;
        }
        if(this.isSwingInProgress){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.fabledporo.attack", false));
            return PlayState.CONTINUE;
        }
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.fabledporo.walk", true));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.fabledporo.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<IAnimatable>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    protected void registerData() {
        super.registerData();
        dataManager.register(STATE, false);
        dataManager.register(FORGE, false);
    }

    @Override
    public AgeableEntity createChild(ServerWorld world, AgeableEntity mate) {
        return null;
    }

    public void tick() {
        super.tick();
        if(dataManager.get(FORGE)){
            ticks++;
            if(ticks==100){
                dataManager.set(FORGE, false);
                forgeItem.setDamage(forgeItem.getDamage()-forgeItem.getDamage()/2);
                this.entityDropItem(forgeItem);
                forgeItem=ItemStack.EMPTY;
                ticks=0;
            }
        }
    }

    public ActionResultType getEntityInteractionResult(PlayerEntity playerIn, Hand hand) {
        ItemStack itemstack = playerIn.getHeldItem(hand);

        if (this.world.isRemote) {
            boolean flag = this.isOwner(playerIn);
            return flag ? ActionResultType.CONSUME : ActionResultType.PASS;
        }else{
            if(this.isOwner(playerIn)){
                if(itemstack.isRepairable() && ticks==0){
                    if (!playerIn.abilities.isCreativeMode) itemstack.shrink(1);
                    dataManager.set(FORGE, true);
                    forgeItem=itemstack;
                    return ActionResultType.SUCCESS;
                }
                this.setSitting(!dataManager.get(STATE));
            }
            return super.getEntityInteractionResult(playerIn, hand);
        } 
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