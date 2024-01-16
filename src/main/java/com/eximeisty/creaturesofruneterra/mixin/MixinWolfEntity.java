package com.eximeisty.creaturesofruneterra.mixin;

import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import com.eximeisty.creaturesofruneterra.item.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Wolf.class)
public class MixinWolfEntity {

    @Inject(at = @At("HEAD"), method = "mobInteract", cancellable = true)
    private void onTick(Player p_30412_, InteractionHand p_30413_, CallbackInfoReturnable<Boolean> cir) {
        Wolf wolf = (Wolf)((Object)this);
        if(wolf.isAlive() && wolf.isTame() && wolf.isOwnedBy(p_30412_) && p_30412_.getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.NAAFIRI.get()) && !wolf.level.isClientSide){
            ItemStack dagger = p_30412_.getItemInHand(InteractionHand.MAIN_HAND);
            TamableAnimal entity= (TamableAnimal) ModEntityTypes.NAAFIRI.get().spawn(wolf.level.getServer().getLevel(wolf.level.dimension()), (ItemStack) null, null, wolf.getOnPos().above(), MobSpawnType.NATURAL, false, false);
            entity.tame(p_30412_);
            dagger.getAttributeModifiers(EquipmentSlot.MAINHAND).forEach((atr, modifier) -> {
                if (atr == Attributes.ATTACK_DAMAGE) {
                    entity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(modifier.getAmount()*5);
                    entity.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(modifier.getAmount()+7);
                    entity.getAttribute(Attributes.ARMOR).setBaseValue(modifier.getAmount());
                    entity.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(modifier.getAmount());
                }
            });
            dagger.shrink(1);
            wolf.discard();
        }
    }

}