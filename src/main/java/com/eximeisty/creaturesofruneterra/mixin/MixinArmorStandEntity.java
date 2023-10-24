package com.eximeisty.creaturesofruneterra.mixin;

import com.eximeisty.creaturesofruneterra.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStand.class)
public class MixinArmorStandEntity {
    private int tick=0;
    
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/world/entity/decoration/ArmorStand;tick()V", cancellable = false)
    private void onTick(CallbackInfo info){
        ArmorStand clase = (ArmorStand)((Object)this);

        boolean flag0 = clase.level.getDayTime()>=15000 && clase.level.getDayTime()<=20000 && clase.level.canSeeSky(clase.blockPosition());
        boolean flag1 = clase.getArmorSlots().toString().equalsIgnoreCase("[1 chainmail_boots, 1 iron_leggings, 1 fiddle_chestplate, 1 fiddle_helmet]");
        boolean flag2 = true;
        boolean flag3 = true;

        if(flag0 && flag1){
            for (int l = 0; l < 4; l++) {
                int x = l%2==0 ? 2 : -2;
                int z = !(l%2==0) ? 4 : -4;
                if(clase.level.getBlockState(clase.blockPosition().offset(x, 0, z))!= Blocks.SOUL_SAND.defaultBlockState()) flag2=false;
                if(clase.level.getBlockState(clase.blockPosition().offset(z, 0, x))!=Blocks.SOUL_SAND.defaultBlockState()) flag2=false;
                if(clase.level.getBlockState(clase.blockPosition().offset(x, 1, z))!=Blocks.SOUL_FIRE.defaultBlockState()) flag2=false;
                if(clase.level.getBlockState(clase.blockPosition().offset(z, 1, x))!=Blocks.SOUL_FIRE.defaultBlockState()) flag2=false;
            }
            if(flag2){
                int x = 2, r = 5;
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < r ; j++) {
                        if(clase.level.getBlockState(clase.blockPosition().offset(-x+j, -1, -4+i))!=Blocks.SOUL_SOIL.defaultBlockState()) flag3=false;
                    }
                    if(i<=1){ x++; r+=2; }
                    if(i>=6){ x--; r+=-2; }
                }
                if(flag3){
                    tick++;
                    if(tick%10==0) clase.level.playSound(null, clase.blockPosition().offset((int)((Math.random() * 20)-10), 0, (int)(Math.random() * 20)-10), SoundEvents.SOUL_ESCAPE, SoundSource.AMBIENT, (float)(Math.random() * 5)+5, (float)(Math.random() * 2)+1);
                    if(tick%50==0 && !clase.level.isClientSide){
                        int lightings = (int)(Math.random() * 3)+1;
                        for (int i = 0; i < lightings; i++) { 
                            int lx = (int)clase.getRandomX(8), lz = (int)clase.getRandomZ(8);
                            if( lx==clase.getX() && lz==clase.getZ() ) lx++;
                            EntityType.LIGHTNING_BOLT.spawn(clase.level.getServer().getLevel(clase.level.dimension()), (ItemStack) null, null, new BlockPos(lx, clase.getBlockY(), lz), MobSpawnType.EVENT.EVENT, false, false);
                        }
                    }
                    if(tick>215 && !clase.level.isClientSide){
                        EntityType.LIGHTNING_BOLT.spawn(clase.level.getServer().getLevel(clase.level.dimension()), (ItemStack) null, null, clase.blockPosition(), MobSpawnType.EVENT, false, false);
                        ModEntities.FIDDLESTICKS.get().spawn(clase.level.getServer().getLevel(clase.level.dimension()), (ItemStack) null, null, clase.blockPosition(), MobSpawnType.EVENT, false, false);
                        clase.discard();
                    }
                }
            }
        }

        if(!(flag0 && flag1 && flag2 && flag3) && tick>0) tick=0;
    }
}