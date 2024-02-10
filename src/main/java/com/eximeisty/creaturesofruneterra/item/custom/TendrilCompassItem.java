package com.eximeisty.creaturesofruneterra.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class TendrilCompassItem extends Item {

    public TendrilCompassItem(Properties properties) {
        super(properties);
    }

    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.nullToEmpty("Points towards the nearest ancient debri in a 12 block radius"));
    }
}