package com.eximeisty.creaturesofruneterra.mixin;

import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import com.eximeisty.creaturesofruneterra.item.ModItems;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WolfEntity.class)
public class MixinWolfEntity {

    @Inject(at = @At("HEAD"), method = "getEntityInteractionResult", cancellable = true)
    private void onTick(PlayerEntity p_30412_, Hand p_30413_, CallbackInfoReturnable<Boolean> cir) {
        WolfEntity wolf = (WolfEntity)((Object)this);
        if(wolf.isAlive() && wolf.isTamed() && wolf.isOwner(p_30412_) && p_30412_.getHeldItem(Hand.MAIN_HAND).isItemEqual(new ItemStack(ModItems.NAAFIRI.get())) && !wolf.world.isRemote){
            ItemStack dagger = p_30412_.getHeldItem(Hand.MAIN_HAND);
            TameableEntity entity= (TameableEntity) ModEntityTypes.NAAFIRI.get().spawn(wolf.world.getServer().getWorld(wolf.world.getDimensionKey()), (ItemStack) null, null, new BlockPos(wolf.getPosX(), wolf.getPosY()+1, wolf.getPosZ()), SpawnReason.NATURAL, false, false);
            entity.setTamedBy(p_30412_);
            dagger.getAttributeModifiers(EquipmentSlotType.MAINHAND).forEach((atr, modifier) -> {
                if (atr == Attributes.ATTACK_DAMAGE) {
                    entity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(modifier.getAmount()*5);
                    entity.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(modifier.getAmount()+7);
                    entity.getAttribute(Attributes.ARMOR).setBaseValue(modifier.getAmount());
                    entity.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(modifier.getAmount());
                }
            });
            dagger.shrink(1);
            wolf.remove();
        }
    }

}