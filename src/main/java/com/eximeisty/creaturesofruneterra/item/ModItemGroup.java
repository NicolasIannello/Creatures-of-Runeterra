package com.eximeisty.creaturesofruneterra.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroup {
    public static final CreativeModeTab  COR_GROUP= new CreativeModeTab("corModTab") {

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.GEMSTONE.get());
        }
    };
}