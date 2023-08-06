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

        boolean flag0 = clase.world.getDayTime()>=16000 && clase.world.getDayTime()<=19000;
        boolean flag1 = clase.getArmorInventoryList().toString().equalsIgnoreCase("[1 chainmail_boots, 1 iron_leggings, 1 fiddle_chestplate, 1 fiddle_helmet]");
        boolean flag2 = true;
        boolean flag3 = true;

        if(flag0){
            if(flag1){
                for (int l = 0; l < 4; l++) {
                    int x = l%2==0 ? 2 : -2;
                    int z = !(l%2==0) ? 4 : -4;
                    if(clase.world.getBlockState(clase.getPosition().add(x, +0.5, z))!=Blocks.SOUL_SAND.getDefaultState()) flag2=false;
                    if(clase.world.getBlockState(clase.getPosition().add(z, +0.5, x))!=Blocks.SOUL_SAND.getDefaultState()) flag2=false;
                    if(clase.world.getBlockState(clase.getPosition().add(x, +1.5, z))!=Blocks.SOUL_FIRE.getDefaultState()) flag2=false;
                    if(clase.world.getBlockState(clase.getPosition().add(z, +1.5, x))!=Blocks.SOUL_FIRE.getDefaultState()) flag2=false;
                }
                if(flag2){
                    int x = 2, r = 5;
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < r ; j++) {
                            if(clase.world.getBlockState(clase.getPosition().add(-x+j, -0.5, -4+i))!=Blocks.SOUL_SOIL.getDefaultState()) flag3=false;
                        }
                        if(i<=1){ x++; r+=2;}
                        if(i>=6){ x--; r+=-2;}
                    }
                    if(flag3){
                        //fiddle
                    }
                }
            }
        }

        //clase.remove();
        System.out.println("f0:"+flag0+" f1:"+flag1+" f2:"+flag2+" f3:"+flag3);
    }
}