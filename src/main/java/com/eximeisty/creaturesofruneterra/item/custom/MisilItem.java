package com.eximeisty.creaturesofruneterra.item.custom;

import com.eximeisty.creaturesofruneterra.entity.custom.MisilEntity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MisilItem extends Item {

   public MisilItem(Properties builder) {
      super(builder);
   }

   public MisilEntity createMisil(World worldIn, ItemStack stack, LivingEntity shooter) {
      MisilEntity misilentity = new MisilEntity(worldIn, shooter);
      return misilentity;
   }

   public boolean isInfinite(ItemStack stack, ItemStack bow, net.minecraft.entity.player.PlayerEntity player) {
      return false;
   }
}