package com.eximeisty.creaturesofruneterra.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class RhaastArmorItem extends GeoArmorItem implements IAnimatable {
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public final ItemStackHandler itemHandler = new ItemStackHandler(1);

    public RhaastArmorItem(ArmorMaterial material, EquipmentSlot type, Properties properties) {
        super(material, type, properties);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(stack.getMaxDamage()>stack.getDamageValue() && entityIn instanceof LivingEntity ? ((LivingEntity)entityIn).getItemBySlot(this.getSlot()).getItem()==this.asItem() : false) {
            stack.setDamageValue(stack.getDamageValue() + 1);
        }
        if(stack.getMaxDamage()<=stack.getDamageValue() && entityIn instanceof LivingEntity ? ((LivingEntity)entityIn).getItemBySlot(this.getSlot()).getItem()==this.asItem() : false) entityIn.setItemSlot(getSlot(), readSaveData(((LivingEntity) entityIn).getItemBySlot(this.getSlot())));
    }

    public ItemStack readSaveData(ItemStack stack) {
        CompoundTag compound = stack.getTag();
        itemHandler.deserializeNBT(compound.getCompound("inv"));
        return itemHandler.getStackInSlot(0);
    }

    public void addSaveData(ItemStack stack, ItemStack armor) {
        CompoundTag compound = stack.getOrCreateTag();
        itemHandler.insertItem(0, armor, false);
        compound.put("inv", itemHandler.serializeNBT());
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
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

    @Override
    public boolean isValidRepairItem(ItemStack p_40392_, ItemStack p_40393_) {
        return false;
    }
}