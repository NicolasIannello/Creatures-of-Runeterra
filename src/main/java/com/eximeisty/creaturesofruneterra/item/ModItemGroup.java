package com.eximeisty.creaturesofruneterra.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {
    public static final ItemGroup COR_GROUP= new ItemGroup("corModTab") {

        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.DUNEBREAKER_HORN.get());
        }
    };
}
