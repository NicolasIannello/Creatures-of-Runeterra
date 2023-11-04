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
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class DunebreakerShield extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public int cd=0;
    public boolean shoot=false;
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().then("animation.dunebreaker_shield.idle", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation DERECHA_ANIM = RawAnimation.begin().then("animation.dunebreaker_shield.derecha", Animation.LoopType.LOOP);
    private static final RawAnimation IZQUIERDA_ANIM = RawAnimation.begin().then("animation.dunebreaker_shield.izquierda", Animation.LoopType.LOOP);
    private static final RawAnimation ATTACKD_ANIM = RawAnimation.begin().then("animation.dunebreaker_shield.attackD", Animation.LoopType.PLAY_ONCE).then("animation.dunebreaker_shield.derecha", Animation.LoopType.LOOP);
    private static final RawAnimation ATTACKI_ANIM = RawAnimation.begin().then("animation.dunebreaker_shield.attackI", Animation.LoopType.PLAY_ONCE).then("animation.dunebreaker_shield.izquierda", Animation.LoopType.LOOP);

    public DunebreakerShield(Properties properties) {
        super(properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "dbs_controller", state -> PlayState.CONTINUE )
                .triggerableAnim("der", DERECHA_ANIM).triggerableAnim("izq", IZQUIERDA_ANIM)
                .triggerableAnim("idle", IDLE_ANIM).triggerableAnim("attackd", ATTACKD_ANIM)
                .triggerableAnim("attacki", ATTACKI_ANIM));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }


    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerentity, InteractionHand handIn) {
        ItemStack itemstack = playerentity.getItemInHand(handIn);
        playerentity.startUsingItem(handIn);
        if (!worldIn.isClientSide) {
            if(playerentity.getItemInHand(InteractionHand.MAIN_HAND).getDisplayName().getString().contains("Dunebreaker")){
                triggerAnim(playerentity, GeoItem.getOrAssignId(itemstack, (ServerLevel)worldIn), "dbs_controller", "der");
            }else{
                triggerAnim(playerentity, GeoItem.getOrAssignId(itemstack, (ServerLevel)worldIn), "dbs_controller", "izq");
            }
        }
        return InteractionResultHolder.consume(itemstack);
    }

    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(cd==97){
            if (!worldIn.isClientSide) {
                DBShieldEntity shieldEntity = new DBShieldEntity(worldIn, (LivingEntity) entityIn);
                shieldEntity.shootFromRotation(entityIn, entityIn.getRotationVector().x, entityIn.getRotationVector().y, 0.0F, 1.0F * 3.0F, 1.0F);
                shieldEntity.setBaseDamage(2);
                shieldEntity.tickCount = 35;
                shieldEntity.setKnockback(2);
                shieldEntity.setNoGravity(true);
                shieldEntity.setPierceLevel((byte) 4);

                if (((LivingEntity) entityIn).getItemInHand(InteractionHand.MAIN_HAND).getDisplayName().getString().contains("Dunebreaker")) {
                    triggerAnim(entityIn, GeoItem.getOrAssignId(stack, (ServerLevel) worldIn), "dbs_controller", "attackd");
                } else {
                    triggerAnim(entityIn, GeoItem.getOrAssignId(stack, (ServerLevel) worldIn), "dbs_controller", "attacki");
                }
                worldIn.addFreshEntity(shieldEntity);
            }
        }
        if(cd>0) cd--;
    }

    public void onUseTick(Level worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        if(Minecraft.getInstance().mouseHandler.isLeftPressed() && cd==0){
            cd=100;
        }
    }

    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (!worldIn.isClientSide) {
            triggerAnim(entityLiving, GeoItem.getOrAssignId(stack, (ServerLevel)worldIn), "dbs_controller", "idle");
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
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private DunebreakerShieldRenderer renderer = null;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new DunebreakerShieldRenderer();

                return this.renderer;
            }
        });
    }
}