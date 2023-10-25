package com.eximeisty.creaturesofruneterra.item.custom;

import com.eximeisty.creaturesofruneterra.entity.custom.HexcoreEntity;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HexcoreItem extends Item {

    public HexcoreItem(Properties p_41383_) {
        super(p_41383_);
    }

    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        ItemStack itemstack = p_41433_.getItemInHand(p_41434_);
        if (!p_41432_.isClientSide) {
            HexcoreEntity hexcore = new HexcoreEntity(p_41432_, p_41433_);
            hexcore.setItem(itemstack);
            hexcore.shootFromRotation(p_41433_, p_41433_.getXRot(), p_41433_.getYRot(), 0.0F, 1.5F, 1.0F);
            p_41432_.addFreshEntity(hexcore);
        }

        p_41433_.awardStat(Stats.ITEM_USED.get(this));
        if (!p_41433_.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, p_41432_.isClientSide());
    }
}
