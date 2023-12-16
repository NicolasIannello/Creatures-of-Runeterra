package com.eximeisty.creaturesofruneterra.item.custom;

import com.eximeisty.creaturesofruneterra.item.ModArmorMaterials;
import com.eximeisty.creaturesofruneterra.item.ModItems;
import com.eximeisty.creaturesofruneterra.item.client.rhaast.RhaastRenderer;
import com.eximeisty.creaturesofruneterra.util.KeyBinding;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class Rhaast extends SwordItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected static final UUID REACH = UUID.fromString("f7e4f5c8-2463-4660-bfd3-0de7ac8f7247");
    private AttributeModifier reachModifier, damageModifier, speedModifier;
    private Multimap<Attribute, AttributeModifier> attributeModifiers;
    private int ticks = 0;

    public Rhaast(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
        reachModifier = new AttributeModifier(REACH, "reach_distance", 1.5, AttributeModifier.Operation.ADDITION);
        damageModifier = new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (float)attackDamageIn+tier.getAttackDamageBonus(), AttributeModifier.Operation.ADDITION);
        speedModifier = new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeedIn, AttributeModifier.Operation.ADDITION);
    }

    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(ticks>0){
            ticks++;
            if(ticks>118){
                ((Player)entityIn).getCooldowns().addCooldown(this, 300);
                ticks = 0;
            }else{
                boolean flag = (worldIn.getBlockState(entityIn.blockPosition().below()) == Blocks.BEDROCK.defaultBlockState()) || (worldIn.getBlockState(entityIn.blockPosition().above()) == Blocks.BEDROCK.defaultBlockState()) || (worldIn.getBlockState(entityIn.blockPosition().east()) == Blocks.BEDROCK.defaultBlockState()) || (worldIn.getBlockState(entityIn.blockPosition().west()) == Blocks.BEDROCK.defaultBlockState());
                if (!flag) {
                    boolean onAirOrFluid = (worldIn.getBlockState(entityIn.blockPosition().below()) == Blocks.AIR.defaultBlockState()) || (worldIn.getBlockState(entityIn.blockPosition().below()) == Blocks.WATER.defaultBlockState()) || (worldIn.getBlockState(entityIn.blockPosition().below()) == Blocks.LAVA.defaultBlockState());
                    double y = onAirOrFluid ? -1 : entityIn.getLookAngle().y / 2;
                    entityIn.setSwimming(true);
                    entityIn.noPhysics = true;
                    entityIn.setDeltaMovement(new Vec3(entityIn.getLookAngle().x / 4, y, entityIn.getLookAngle().z / 4));
                }
            }
        }

        if (worldIn.isClientSide && KeyBinding.ITEM_HABILITY.isDown() && entityIn instanceof Player ? ( ((Player)entityIn).getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.RHAAST.get()) || ((Player)entityIn).getItemInHand(InteractionHand.OFF_HAND).is(ModItems.RHAAST.get())) : false) {
            if (stack.getDamageValue() == 0 && entityIn instanceof Player ? ((Player) entityIn).getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ArmorItem ? ((ArmorItem) ((Player) entityIn).getItemBySlot(EquipmentSlot.HEAD).getItem()).getMaterial() != ModArmorMaterials.DARKIN : ((Player) entityIn).getItemBySlot(EquipmentSlot.HEAD).isEmpty() : false) {
                UUID id[] = {UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B")};
                EquipmentSlot es[] = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
                Item armor[] = {ModItems.RHAAST_HELMET.get(), ModItems.RHAAST_CHESTPLATE.get(), ModItems.RHAAST_LEGGINGS.get(), ModItems.RHAAST_BOOTS.get()};

                for (int i = 0; i < 4; i++) {
                    ItemStack helmet = ((Player) entityIn).getItemBySlot(es[i]).copy();
                    ItemStack Rhelmet = new ItemStack(armor[i]);
                    Rhelmet.addAttributeModifier(Attributes.ARMOR, new AttributeModifier(id[i], "Armor modifier", helmet.isEmpty() ? ((ArmorItem) Rhelmet.getItem()).getDefense() : ((ArmorItem) helmet.getItem()).getDefense() + ((ArmorItem) Rhelmet.getItem()).getDefense(), AttributeModifier.Operation.ADDITION), es[i]);
                    Rhelmet.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(id[i], "Armor modifier", helmet.isEmpty() ? ((ArmorItem) Rhelmet.getItem()).getToughness() : ((ArmorItem) helmet.getItem()).getToughness() + ((ArmorItem) Rhelmet.getItem()).getToughness(), AttributeModifier.Operation.ADDITION), es[i]);
                    Rhelmet.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(id[i], "Armor modifier", helmet.isEmpty() ? 0 : 0.1, AttributeModifier.Operation.ADDITION), es[i]);
                    ((RhaastArmorItem) Rhelmet.getItem()).addSaveData(Rhelmet, helmet);
                    helmet.getAllEnchantments().forEach(Rhelmet::enchant);
                    Rhelmet.enchant(Enchantments.BINDING_CURSE, 1);
                    ((Player) entityIn).setItemSlot(es[i], Rhelmet);
                }
                stack.setDamageValue(stack.getDamageValue() + 40);
            }
        }
    }

    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerentity, InteractionHand handIn) {
        ItemStack stack = playerentity.getItemInHand(handIn);
        if(stack.getDamageValue()>75) return InteractionResultHolder.consume(stack);

        playerentity.getCooldowns().addCooldown(this, 60);
        ticks++;
        playerentity.invulnerableTime = 100;
        stack.setDamageValue(stack.getDamageValue()+25);

        return InteractionResultHolder.consume(stack);
    }
    
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(!target.isAlive() && attacker.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.RHAAST_CHESTPLATE.get())) attacker.heal(8);
        stack.setDamageValue(stack.getDamageValue()-3);
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack p_43282_, Level p_43283_, BlockState p_43284_, BlockPos p_43285_, LivingEntity p_43286_) {
        if(p_43282_.getDamageValue()<95) p_43282_.setDamageValue(p_43282_.getDamageValue()+5);
        return true;
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

    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        String quote = "["+Minecraft.getInstance().options.keyUse.getKey().toString().replace("keyboard.", "").replace("."," ").replace("key","")+"] to phase. ["+ KeyBinding.ITEM_HABILITY.getKey().toString().replace("keyboard.", "").replace("."," ").replace("key","")+"] to become Rhaast";
        tooltip.add(Component.nullToEmpty(quote));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private RhaastRenderer renderer = null;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new RhaastRenderer();

                return this.renderer;
            }
        });
    }
}