package com.eximeisty.creaturesofruneterra.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;

@Mixin(ArmorStandEntity.class)
public class MixinArmorStandEntity {
    private int tick=0;
    
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/entity/item/ArmorStandEntity;tick()V", cancellable = false)
    private void onTick(CallbackInfo info){
        ArmorStandEntity clase = (ArmorStandEntity)((Object)this);

        boolean flag0 = clase.world.isNightTime();//clase.world.getTimeOfDay(0)>=0.35288608/*15000*/ && clase.world.getTimeOfDay(0)<=0.59869206/*20000*/ && clase.world.canSeeSky(clase.getPosition());
        boolean flag1 = clase.getArmorInventoryList().toString().equalsIgnoreCase("[1 chainmail_boots, 1 iron_leggings, 1 fiddle_chestplate, 1 fiddle_helmet]");
        boolean flag2 = true;
        boolean flag3 = true;

        if(flag0 && flag1){
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
                    if(i<=1){ x++; r+=2; }
                    if(i>=6){ x--; r+=-2; }
                }
                if(flag3){
                    tick++;
                    if(tick%10==0) clase.world.playSound(null, clase.getPosition().add((Math.random() * 20)-10, 0, (Math.random() * 20)-10), SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.AMBIENT, (float)(Math.random() * 5)+5, (float)(Math.random() * 2)+1);
                    if(tick%50==0 && !clase.world.isRemote){
                        int lightings = (int)(Math.random() * 3)+1;
                        for (int i = 0; i < lightings; i++) { 
                            double lx = clase.getPosXRandom(8), lz = clase.getPosZRandom(8);
                            if( lx==clase.getPosX() && lz==clase.getPosZ() ) lx++;
                            EntityType.LIGHTNING_BOLT.spawn(clase.world.getServer().func_241755_D_(), null, null, new BlockPos(lx, clase.getPosY(), lz), SpawnReason.EVENT, false, false);
                        }
                    }
                    if(tick>215 && !clase.world.isRemote){
                        EntityType.LIGHTNING_BOLT.spawn(clase.world.getServer().func_241755_D_(), null, null, clase.getPosition(), SpawnReason.EVENT, false, false);
                        ModEntityTypes.FIDDLESTICKS.get().spawn(clase.world.getServer().func_241755_D_(), null, null, clase.getPosition(), SpawnReason.EVENT, false, false);
                        clase.remove();
                    }
                }
            }
        }

        if(!(flag0 && flag1 && flag2 && flag3) && tick>0) tick=0;
    }
}