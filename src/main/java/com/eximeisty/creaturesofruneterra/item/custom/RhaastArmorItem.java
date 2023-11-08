package com.eximeisty.creaturesofruneterra.item.custom;

import com.eximeisty.creaturesofruneterra.item.client.armor.rhaast.RhaastArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class RhaastArmorItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public final ItemStackHandler itemHandler = new ItemStackHandler(1);

    public RhaastArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(stack.getMaxDamage()>stack.getDamageValue() && entityIn instanceof LivingEntity ? ((LivingEntity)entityIn).getItemBySlot(this.getEquipmentSlot()).getItem()==this.asItem() : false) {
            stack.setDamageValue(stack.getDamageValue() + 1);
        }
        if(stack.getMaxDamage()<=stack.getDamageValue() && entityIn instanceof LivingEntity ? ((LivingEntity)entityIn).getItemBySlot(this.getEquipmentSlot()).getItem()==this.asItem() : false) entityIn.setItemSlot(getEquipmentSlot(), readSaveData(((LivingEntity) entityIn).getItemBySlot(this.getEquipmentSlot())));
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

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private RhaastArmorRenderer renderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null) this.renderer = new RhaastArmorRenderer();

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    private PlayState predicate(AnimationState animationState) {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public boolean isValidRepairItem(ItemStack p_40392_, ItemStack p_40393_) {
        return false;
    }
}