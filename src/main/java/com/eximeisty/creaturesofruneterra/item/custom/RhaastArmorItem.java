package com.eximeisty.creaturesofruneterra.item.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;

public class RhaastArmorItem extends GeoArmorItem implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);
    public final ItemStackHandler itemHandler = new ItemStackHandler(1);

    public RhaastArmorItem(IArmorMaterial material, EquipmentSlotType type, Properties properties) {
        super(material, type, properties);
    }

    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getMaxDamage() > stack.getDamage() && entityIn instanceof LivingEntity ? ((LivingEntity) entityIn).getItemStackFromSlot(this.getEquipmentSlot()).getItem() == this.asItem() : false) {
            stack.setDamage(stack.getDamage() + 1);
        }
        if (stack.getMaxDamage() <= stack.getDamage() && entityIn instanceof LivingEntity ? ((LivingEntity) entityIn).getItemStackFromSlot(this.getEquipmentSlot()).getItem() == this.asItem() : false)
            entityIn.setItemStackToSlot(getEquipmentSlot(), readSaveData(((LivingEntity) entityIn).getItemStackFromSlot(this.getEquipmentSlot())));
    }

    public ItemStack readSaveData(ItemStack stack) {
        CompoundNBT compound = stack.getTag();
        itemHandler.deserializeNBT(compound.getCompound("inv"));
        return itemHandler.getStackInSlot(0);
    }

    public void addSaveData(ItemStack stack, ItemStack armor) {
        CompoundNBT compound = stack.getOrCreateTag();
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
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }
}