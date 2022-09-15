package com.eximeisty.creaturesofruneterra.item.custom;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class SaiElytraItem extends SaiArmorItem{
    private AnimationFactory factory = new AnimationFactory(this);

    public SaiElytraItem(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builder) {
        super(materialIn, slot, builder);
        DispenserBlock.registerDispenseBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		LivingEntity livingEntity = event.getExtraDataOfType(LivingEntity.class).get(0);
        if(livingEntity.isOnGround()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.new", true));
        }else{
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.air", true));
        }
        return PlayState.CONTINUE;
    }

    @Override @SuppressWarnings({ "unchecked", "rawtypes" })
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 1, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }   

    public static boolean isUsable(ItemStack stack) {
        return stack.getDamage() < stack.getMaxDamage() - 1;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        EquipmentSlotType equipmentslottype = MobEntity.getSlotForItemStack(itemstack);
        ItemStack itemstack1 = playerIn.getItemStackFromSlot(equipmentslottype);
        if (itemstack1.isEmpty()) {
        playerIn.setItemStackToSlot(equipmentslottype, itemstack.copy());
        itemstack.setCount(0);
        return ActionResult.func_233538_a_(itemstack, worldIn.isRemote());
        } else {
        return ActionResult.resultFail(itemstack);
        }
    }

    @Override
    public boolean canElytraFly(ItemStack stack, net.minecraft.entity.LivingEntity entity) {
        return SaiElytraItem.isUsable(stack);
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, net.minecraft.entity.LivingEntity entity, int flightTicks) {
        if (!entity.world.isRemote && (flightTicks + 1) % 20 == 0) {
        stack.damageItem(1, entity, e -> e.sendBreakAnimation(net.minecraft.inventory.EquipmentSlotType.CHEST));
        }
        return true;
    }
}