package com.eximeisty.creaturesofruneterra.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class NaafiriItem extends SwordItem {

    public NaafiriItem(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
    }

    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        String quote = "Right click on a tamed wolf to transform it into Naafiri. Naafiri Health: AttackDamage*4 Naafiri Damage: AttackDamage+5";
        tooltip.add(Component.nullToEmpty(quote));
    }
}
