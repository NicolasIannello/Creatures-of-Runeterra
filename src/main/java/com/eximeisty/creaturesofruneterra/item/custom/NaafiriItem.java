package com.eximeisty.creaturesofruneterra.item.custom;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class NaafiriItem extends SwordItem {

    public NaafiriItem(IItemTier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
    }

    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        String quote = "Right click on a tamed wolf to transform it into Naafiri. Naafiri Health: AttackDamage*4 Naafiri Damage: AttackDamage+5";
        tooltip.add(new TranslationTextComponent(quote));
    }
}
