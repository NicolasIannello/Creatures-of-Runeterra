package com.eximeisty.creaturesofruneterra.item;

import java.util.function.Supplier;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

public enum ModArmorMaterial implements IArmorMaterial{
    SAI("sai", 45, new int[]{8, 15, 20, 7}, 30, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 5.0F, 0.3F, () -> {
        return Ingredient.fromItems(ModItems.REKSAI_PLAQUE.get());
    });
    
    private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionAmountArray;
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyValue<Ingredient> repairMaterial;

    private ModArmorMaterial(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial) {
        this.name = name;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionAmountArray = damageReductionAmountArray;
        this.enchantability = enchantability;
        this.soundEvent = soundEvent;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairMaterial = new LazyValue<>(repairMaterial);
    }

    public int getDurability(EquipmentSlotType slotIn) {
        return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
    }
    public int getDamageReductionAmount(EquipmentSlotType slotIn) {
        return this.damageReductionAmountArray[slotIn.getIndex()];
    }
    public int getEnchantability() {
        return this.enchantability;
    }
    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }
    public Ingredient getRepairMaterial() {
        return this.repairMaterial.getValue();
    }
    @OnlyIn(Dist.CLIENT)
    public String getName() {
        return CreaturesofRuneterra.MOD_ID+":"+this.name;
    }
    public float getToughness() {
        return this.toughness;
    }
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
