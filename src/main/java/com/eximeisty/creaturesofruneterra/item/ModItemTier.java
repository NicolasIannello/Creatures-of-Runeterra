package com.eximeisty.creaturesofruneterra.item;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModItemTier {

    public static final ForgeTier SAI = new ForgeTier(2, 1400, 1.5f, 2f, 22,
            null, () -> Ingredient.of(ModItems.REKSAI_CLAW.get()));
    public static final ForgeTier ATLAS = new ForgeTier(4, 1600, 10.0F, 5.0F, 10,
            null, () -> Ingredient.of(ModItems.GEMSTONE.get()));
    public static final ForgeTier FIDDLE = new ForgeTier(5, 800, 7.0F, 6.0F, 15,
            null, () -> null);
}