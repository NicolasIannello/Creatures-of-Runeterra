package com.eximeisty.creaturesofruneterra.item.custom;

import com.eximeisty.creaturesofruneterra.item.client.fiddleScythe.FiddleScytheRenderer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class FiddleScythe extends SwordItem implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);
    public String controllerName = "controller";
    // protected static final UUID REACH = UUID.fromString("f7e4f5c8-2463-4660-bfd3-0de7ac8f7720");
    // private AttributeModifier reachModifier, damageModifier, speedModifier;
    // private Multimap<Attribute, AttributeModifier> attributeModifiers;

    public FiddleScythe(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn.setISTER(()-> FiddleScytheRenderer::new));
        // reachModifier = new AttributeModifier(REACH, "reach_distance", 2, AttributeModifier.Operation.ADDITION);
        // damageModifier = new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (float)attackDamageIn+tier.getAttackDamage(), AttributeModifier.Operation.ADDITION);
        // speedModifier = new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", attackSpeedIn, AttributeModifier.Operation.ADDITION);
    }

    //public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        // ItemStack item = entityIn.getHeldEquipment().iterator().next();
        // if(item==stack && !((PlayerEntity)entityIn).getAttribute(ForgeMod.REACH_DISTANCE.get()).hasModifier(reachModifier)){
        //     ((PlayerEntity)entityIn).getAttribute(ForgeMod.REACH_DISTANCE.get()).applyNonPersistentModifier(reachModifier);
        // }else if(!(item==stack)){
        //     ((PlayerEntity)entityIn).getAttribute(ForgeMod.REACH_DISTANCE.get()).removeModifier(reachModifier);
        // }
    //}

    // public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerentity, Hand handIn) {
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
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        attacker.heal(1);
        target.addPotionEffect(new EffectInstance(Effects.WITHER, 20*4));
        return super.hitEntity(stack, target, attacker);
    }

    public <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @Override @SuppressWarnings({ "unchecked", "rawtypes" })
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, controllerName, 1, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
    
    // public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
    //     Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
    //     builder.put(ForgeMod.REACH_DISTANCE.get(), reachModifier);
    //     builder.put(Attributes.ATTACK_DAMAGE, damageModifier);
    //     builder.put(Attributes.ATTACK_SPEED, speedModifier);
    //     this.attributeModifiers = builder.build();
    //     return equipmentSlot == EquipmentSlotType.MAINHAND ? attributeModifiers : super.getAttributeModifiers(equipmentSlot);
    // }
}