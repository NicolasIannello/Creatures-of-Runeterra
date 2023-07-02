package com.eximeisty.creaturesofruneterra.item.custom;

import com.eximeisty.creaturesofruneterra.entity.custom.MisilEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class MisilItem extends Item {

   public MisilItem(Properties builder) {
      super(builder);
   }

   public MisilEntity createMisil(Level worldIn, ItemStack stack, LivingEntity shooter) {
      MisilEntity misilentity = new MisilEntity(worldIn, shooter);

      return misilentity;
   }

   public boolean isInfinite(ItemStack stack, ItemStack bow, net.minecraft.world.entity.player.Player player) {
      return false;
   }
}