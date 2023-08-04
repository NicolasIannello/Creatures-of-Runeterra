package com.eximeisty.creaturesofruneterra.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ArmorStandEntity;

@Mixin(ArmorStandEntity.class)
public class MixinArmorStandEntity {
    
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/entity/item/ArmorStandEntity;tick()V", cancellable = false)
    private void onTick(CallbackInfo info){
        ArmorStandEntity clase = (ArmorStandEntity)((Object)this);

        boolean flag1 = clase.getArmorInventoryList().toString().contains("fiddle_chestplate") && clase.getArmorInventoryList().toString().contains("fiddle_helmet");
        boolean flag2 = true;

        int x = 2, r = 5;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < r ; j++) {
                if(clase.world.getBlockState(clase.getPosition().add(-x+j, -0.5, -4+i))!=Blocks.SOUL_SOIL.getDefaultState()) flag2=false;
            }
            if(i<=1){ x++; r+=2;}
            if(i>=6){ x--; r+=-2;}
        }

        System.out.println("f1:"+flag1+" f2:"+flag2);
    }
}