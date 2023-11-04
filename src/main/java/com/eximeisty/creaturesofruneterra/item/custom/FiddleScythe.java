package com.eximeisty.creaturesofruneterra.item.custom;

import com.eximeisty.creaturesofruneterra.item.client.fiddleScythe.FiddleScytheRenderer;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;
import java.util.function.Consumer;


public class FiddleScythe extends SwordItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    //public String controllerName = "controller";
     protected static final UUID REACH = UUID.fromString("f7e4f5c8-2463-4660-bfd3-0de7ac8f7720");
     private AttributeModifier reachModifier, damageModifier, speedModifier;
     private Multimap<Attribute, AttributeModifier> attributeModifiers;

    public FiddleScythe(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
        reachModifier = new AttributeModifier(REACH, "reach_distance", 1.5, AttributeModifier.Operation.ADDITION);
        damageModifier = new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (float)attackDamageIn+tier.getAttackDamageBonus(), AttributeModifier.Operation.ADDITION);
        speedModifier = new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeedIn, AttributeModifier.Operation.ADDITION);
    }

//    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
//         ItemStack item = entityIn.getHandSlots().iterator().next();
//         if(item==stack && !((Player)entityIn).getAttribute(ForgeMod.ENTITY_REACH.get()).hasModifier(reachModifier)){
//             ((Player)entityIn).getAttribute(ForgeMod.ENTITY_REACH.get()).addTransientModifier(reachModifier);
//         }else if(!(item==stack)){
//             ((Player)entityIn).getAttribute(ForgeMod.ENTITY_REACH.get()).removeModifier(reachModifier);
//         }
//    }

    // public ActionResult<ItemStack> onItemRightClick(World worldIn, Player playerentity, Hand handIn) {
    //     ItemStack stack = playerentity.getHeldItem(handIn);
    //     playerentity.setActiveHand(handIn);
    //     if(!playerentity.getCooldownTracker().hasCooldown(stack.getItem())){
    //         playerentity.addStat(Stats.ITEM_USED.get(this));
    //         playerentity.getCooldownTracker().setCooldown(this, 500);
    //         if(!worldIn.isRemote){
    //             stack.damageItem(20, playerentity, (player) -> {
    //                 player.sendBreakAnimation(playerentity.getActiveHand());
    //             });
    //         }
    //     }
    //     return ActionResult.resultConsume(stack);
    // }
    
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        attacker.heal(1);
        target.addEffect(new MobEffectInstance(MobEffects.WITHER, 20*4));
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "atlas_controller", state -> PlayState.CONTINUE));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
    
     public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack) {
         ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
         builder.put(ForgeMod.ENTITY_REACH.get(), reachModifier);
         builder.put(Attributes.ATTACK_DAMAGE, damageModifier);
         builder.put(Attributes.ATTACK_SPEED, speedModifier);
         this.attributeModifiers = builder.build();
         return equipmentSlot == EquipmentSlot.MAINHAND ? attributeModifiers : super.getAttributeModifiers(equipmentSlot, stack);
     }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private FiddleScytheRenderer renderer = null;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new FiddleScytheRenderer();

                return this.renderer;
            }
        });
    }
}