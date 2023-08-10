package com.eximeisty.creaturesofruneterra.item;

import java.util.function.Supplier;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

public enum ModItemTier implements IItemTier{
    SAI(6, 2700, 15.0F, 7.0F, 20, () -> {
        return Ingredient.fromItems(ModItems.REKSAI_CLAW.get());
    }),
    ATLAS(4, 1600, 10.0F, 5.0F, 10, () -> {
        return Ingredient.fromItems(ModItems.GEMSTONE.get());
    }),
    FIDDLE(5, 800, 7.0F, 6.0F, 15, () -> {
        return null;
    });

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final LazyValue<Ingredient> repairMaterial;

    ModItemTier(int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability, Supplier<Ingredient> repairMaterial){
        this.harvestLevel=harvestLevel;
        this.maxUses=maxUses;
        this.efficiency=efficiency;
        this.attackDamage=attackDamage;
        this.enchantability=enchantability;
        this.repairMaterial= new LazyValue<>(repairMaterial);
    }

    @Override
    public int getMaxUses() {
        return maxUses;
    }
    @Override
    public float getEfficiency() {
        return efficiency;
    }
    @Override
    public float getAttackDamage() {
        return attackDamage;
    }
    @Override
    public int getHarvestLevel() {
        return harvestLevel;
    }
    @Override
    public int getEnchantability() {
        return enchantability;
    }
    @Override
    public Ingredient getRepairMaterial() {
        return repairMaterial.getValue();
    }
}