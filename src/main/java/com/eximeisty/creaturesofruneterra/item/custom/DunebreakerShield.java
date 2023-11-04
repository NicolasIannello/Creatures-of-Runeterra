package com.eximeisty.creaturesofruneterra.item.custom;

import com.eximeisty.creaturesofruneterra.entity.custom.DBShieldEntity;

import com.eximeisty.creaturesofruneterra.item.client.dunebreakershield.DunebreakerShieldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Consumer;


public class DunebreakerShield extends Item implements IAnimatable, ISyncable {
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public int cd=0;
    public boolean shoot=false;
    public String controllerName = "controller";
    private static final AnimationBuilder IDLE_ANIM = new AnimationBuilder().addAnimation("animation.dunebreaker_shield.idle", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
    private static final AnimationBuilder DERECHA_ANIM = new AnimationBuilder().addAnimation("animation.dunebreaker_shield.derecha", ILoopType.EDefaultLoopTypes.LOOP);
    private static final AnimationBuilder IZQUIERDA_ANIM = new AnimationBuilder().addAnimation("animation.dunebreaker_shield.izquierda", ILoopType.EDefaultLoopTypes.LOOP);
    private static final AnimationBuilder ATTACKD_ANIM = new AnimationBuilder().addAnimation("animation.dunebreaker_shield.attackD", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("animation.dunebreaker_shield.derecha", ILoopType.EDefaultLoopTypes.LOOP);
    private static final AnimationBuilder ATTACKI_ANIM = new AnimationBuilder().addAnimation("animation.dunebreaker_shield.attackI", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("animation.dunebreaker_shield.izquierda", ILoopType.EDefaultLoopTypes.LOOP);

    public DunebreakerShield(Properties properties) {
        super(properties);
        GeckoLibNetwork.registerSyncable(this);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
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
        return this.factory;
    }

    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        playerIn.startUsingItem(handIn);
        if (!worldIn.isClientSide) {
            final int id = GeckoLibUtil.guaranteeIDForStack(itemstack, (ServerLevel) worldIn);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerIn);
            if(playerIn.getItemInHand(InteractionHand.MAIN_HAND).getDisplayName().getString().contains("Dunebreaker")){
                GeckoLibNetwork.syncAnimation(target, this, id, 1);
            }else{
                GeckoLibNetwork.syncAnimation(target, this, id, 2);
            }
        }
        return InteractionResultHolder.consume(itemstack);
    }

    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(cd==197){
            if (!worldIn.isClientSide) {
                DBShieldEntity shieldEntity = new DBShieldEntity(worldIn, (LivingEntity) entityIn);
                shieldEntity.shootFromRotation(entityIn, entityIn.getRotationVector().x, entityIn.getRotationVector().y, 0.0F, 1.0F * 3.0F, 1.0F);
                shieldEntity.setBaseDamage(2);
                shieldEntity.tickCount = 35;
                shieldEntity.setKnockback(2);
                shieldEntity.setNoGravity(true);
                shieldEntity.setPierceLevel((byte) 4);

                final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerLevel) worldIn);
                final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entityIn);
                if(((LivingEntity)entityIn).getItemInHand(InteractionHand.MAIN_HAND).getDisplayName().getString().contains("Dunebreaker")){
                    GeckoLibNetwork.syncAnimation(target, this, id, 3);
                }else{
                    GeckoLibNetwork.syncAnimation(target, this, id, 4);
                }
                worldIn.addFreshEntity(shieldEntity);
            }
        }
        if(cd>0) cd--;
    }

    public void onUseTick(Level worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        if(Minecraft.getInstance().mouseHandler.isLeftPressed() && cd==0){
            cd=200;
        }
    }

    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (!worldIn.isClientSide) {
            final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerLevel) worldIn);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entityLiving);
            GeckoLibNetwork.syncAnimation(target, this, id, 0);
        }
    }

    public UseAnim getUseAnimation(ItemStack p_43105_) {
        return UseAnim.BLOCK;
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public boolean isValidRepairItem(ItemStack p_43091_, ItemStack p_43092_) {
        return false;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = new DunebreakerShieldRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
        controller.markNeedsReload();
        if (state == 0) controller.setAnimation(IDLE_ANIM);
        if (state == 1) controller.setAnimation(DERECHA_ANIM);
        if (state == 2) controller.setAnimation(IZQUIERDA_ANIM);
        if (state == 3) controller.setAnimation(ATTACKD_ANIM);
        if (state == 4) controller.setAnimation(ATTACKI_ANIM);
    }
}