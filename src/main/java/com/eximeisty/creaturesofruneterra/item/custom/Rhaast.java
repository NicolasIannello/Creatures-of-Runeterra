package com.eximeisty.creaturesofruneterra.item.custom;

import com.eximeisty.creaturesofruneterra.item.ModArmorMaterial;
import com.eximeisty.creaturesofruneterra.item.ModItems;
import com.eximeisty.creaturesofruneterra.item.client.rhaast.RhaastRenderer;
import com.eximeisty.creaturesofruneterra.util.KeyBindings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Rhaast extends SwordItem implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);
    protected static final UUID REACH = UUID.fromString("f7e4f5c8-2463-4660-bfd3-0de7ac8f7247");
    private int ticks = 0;

    public Rhaast(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn.setISTER(()-> RhaastRenderer::new));
    }

    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(ticks>0){
            ticks++;
            if(ticks>118){
                ((PlayerEntity)entityIn).getCooldownTracker().setCooldown(this, 300);
                ticks = 0;
            }else{
                boolean flag = (worldIn.getBlockState(entityIn.getPosition().down()) == Blocks.BEDROCK.getDefaultState()) || (worldIn.getBlockState(entityIn.getPosition().up()) == Blocks.BEDROCK.getDefaultState()) || (worldIn.getBlockState(entityIn.getPosition().east()) == Blocks.BEDROCK.getDefaultState()) || (worldIn.getBlockState(entityIn.getPosition().west()) == Blocks.BEDROCK.getDefaultState());
                if (!flag) {
                    boolean onAirOrFluid = (worldIn.getBlockState(entityIn.getPosition().down()) == Blocks.AIR.getDefaultState()) || (worldIn.getBlockState(entityIn.getPosition().down()) == Blocks.WATER.getDefaultState()) || (worldIn.getBlockState(entityIn.getPosition().down()) == Blocks.LAVA.getDefaultState());
                    double y = onAirOrFluid ? -1 : entityIn.getLookVec().y / 2;
                    entityIn.setSwimming(true);
                    entityIn.noClip = true;
                    entityIn.setMotion(new Vector3d(entityIn.getLookVec().x / 4, y, entityIn.getLookVec().z / 4));
                }
            }
        }

        if (worldIn.isRemote && KeyBindings.ITEM_HABILITY.isKeyDown() && entityIn instanceof PlayerEntity ? ( ((PlayerEntity)entityIn).getHeldItem(Hand.MAIN_HAND).isItemEqual(new ItemStack(ModItems.RHAAST.get())) || ((PlayerEntity)entityIn).getHeldItem(Hand.OFF_HAND).isItemEqual(new ItemStack((ModItems.RHAAST.get())))) : false) {
            if (stack.getDamage() == 0 && entityIn instanceof PlayerEntity ? ((PlayerEntity) entityIn).getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() instanceof ArmorItem ? ((ArmorItem) ((PlayerEntity) entityIn).getItemStackFromSlot(EquipmentSlotType.HEAD).getItem()).getArmorMaterial() != ModArmorMaterial.DARKIN : ((PlayerEntity) entityIn).getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty() : false) {
                UUID id[] = {UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B")};
                EquipmentSlotType es[] = {EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
                Item armor[] = {ModItems.RHAAST_HELMET.get(), ModItems.RHAAST_CHESTPLATE.get(), ModItems.RHAAST_LEGGINGS.get(), ModItems.RHAAST_BOOTS.get()};

                for (int i = 0; i < 4; i++) {
                    ItemStack helmet = ((PlayerEntity) entityIn).getItemStackFromSlot(es[i]).copy();
                    ItemStack Rhelmet = new ItemStack(armor[i]);
                    Rhelmet.addAttributeModifier(Attributes.ARMOR, new AttributeModifier(id[i], "Armor modifier", helmet.isEmpty() ? ((ArmorItem) Rhelmet.getItem()).getDamageReduceAmount() : ((ArmorItem) helmet.getItem()).getDamageReduceAmount() + ((ArmorItem) Rhelmet.getItem()).getDamageReduceAmount(), AttributeModifier.Operation.ADDITION), es[i]);
                    Rhelmet.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(id[i], "Armor modifier", helmet.isEmpty() ? ((ArmorItem) Rhelmet.getItem()).getToughness() : ((ArmorItem) helmet.getItem()).getToughness() + ((ArmorItem) Rhelmet.getItem()).getToughness(), AttributeModifier.Operation.ADDITION), es[i]);
                    Rhelmet.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(id[i], "Armor modifier", helmet.isEmpty() ? 0 : 0.1, AttributeModifier.Operation.ADDITION), es[i]);
                    ((RhaastArmorItem) Rhelmet.getItem()).addSaveData(Rhelmet, helmet);
                    Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(helmet);
                    EnchantmentHelper.setEnchantments(map,Rhelmet);
                    Rhelmet.addEnchantment(Enchantments.BINDING_CURSE, 1);
                    ((PlayerEntity) entityIn).setItemStackToSlot(es[i], Rhelmet);
                }
                stack.setDamage(stack.getDamage() + 40);
            }
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerentity, Hand handIn) {
        ItemStack stack = playerentity.getHeldItem(handIn);
        if(stack.getDamage()>75) return ActionResult.resultConsume(stack);

        playerentity.getCooldownTracker().setCooldown(this, 60);
        ticks++;
        playerentity.hurtTime = 100;
        stack.setDamage(stack.getDamage()+25);

        return ActionResult.resultConsume(stack);
    }
    
    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(!target.isAlive() && attacker.getItemStackFromSlot(EquipmentSlotType.CHEST).isItemEqual(new ItemStack(ModItems.RHAAST_CHESTPLATE.get()))) attacker.heal(8);
        stack.setDamage(stack.getDamage()-3);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if(stack.getDamage()<95) stack.setDamage(stack.getDamage()+5);
        return true;
    }

    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        String quote = "["+ Minecraft.getInstance().gameSettings.keyBindUseItem.getKey().toString().replace("keyboard.", "").replace("."," ").replace("key","")+"] to phase. ["+ KeyBindings.ITEM_HABILITY.getKey().toString().replace("keyboard.", "").replace("."," ").replace("key","")+"] to become Rhaast";
        tooltip.add(new TranslationTextComponent(quote));
    }

    public <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @Override @SuppressWarnings({ "unchecked", "rawtypes" })
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 1, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
    
}