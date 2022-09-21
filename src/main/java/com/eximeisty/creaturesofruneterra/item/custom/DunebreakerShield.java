package com.eximeisty.creaturesofruneterra.item.custom;

import com.eximeisty.creaturesofruneterra.entity.custom.DBShieldEntity;
import com.eximeisty.creaturesofruneterra.item.client.dunebreakershield.DunebreakerShieldRenderer;

import net.minecraft.block.DispenserBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class DunebreakerShield extends Item implements IAnimatable , ISyncable{
    private AnimationFactory factory = new AnimationFactory(this);
    public String controllerName = "controller";
    public int cd=0;

    public DunebreakerShield(Properties properties) {
        super(properties.setISTER(()-> DunebreakerShieldRenderer::new));
        GeckoLibNetwork.registerSyncable(this);
        DispenserBlock.registerDispenseBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
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

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        if (!worldIn.isRemote) {
            final int id = GeckoLibUtil.guaranteeIDForStack(itemstack, (ServerWorld) worldIn);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerIn);
            if(playerIn.getHeldItemMainhand().getDisplayName().getString().contains("Dunebreaker")){
                GeckoLibNetwork.syncAnimation(target, this, id, 1);
            }else{
                GeckoLibNetwork.syncAnimation(target, this, id, 2);
            }
        }
        return ActionResult.resultConsume(itemstack);
    }

    @SuppressWarnings("resource")
    public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        if(cd>0) cd--;
        if(Minecraft.getInstance().gameSettings.keyBindAttack.isKeyDown() && cd==0){
            // PlayerEntity playerentity = (PlayerEntity)livingEntityIn;
            // playerentity.getCooldownTracker().setCooldown(this, 400);
            cd=10;
            if (!worldIn.isRemote) {
                DBShieldEntity shieldEntity = new DBShieldEntity(worldIn, livingEntityIn);
                shieldEntity.setDirectionAndMovement(livingEntityIn, livingEntityIn.rotationPitch, livingEntityIn.rotationYaw, 0.0F,1.0F * 3.0F, 1.0F);
                shieldEntity.setDamage(2);
                shieldEntity.setKnockbackStrength(2);
                shieldEntity.ticksExisted = 35;
                shieldEntity.setNoGravity(true);
                shieldEntity.setPierceLevel((byte)4);
                
                final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerWorld) worldIn);
                final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntityIn);
                if(livingEntityIn.getHeldItemMainhand().getDisplayName().getString().contains("Dunebreaker")){
                    GeckoLibNetwork.syncAnimation(target, this, id, 3);
                }else{
                    GeckoLibNetwork.syncAnimation(target, this, id, 4);
                }
                worldIn.addEntity(shieldEntity);
            }
        }
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (!worldIn.isRemote) {
            final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerWorld) worldIn);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entityLiving);
            GeckoLibNetwork.syncAnimation(target, this, id, 0);
        }
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
		controller.markNeedsReload();
        if (state == 0) controller.setAnimation(new AnimationBuilder().addAnimation("animation.dunebreaker_shield.idle", false));
        if (state == 1) controller.setAnimation(new AnimationBuilder().addAnimation("animation.dunebreaker_shield.derecha", true));
        if (state == 2) controller.setAnimation(new AnimationBuilder().addAnimation("animation.dunebreaker_shield.izquierda", true));
        if (state == 3) controller.setAnimation(new AnimationBuilder().addAnimation("animation.dunebreaker_shield.attackD", false).addAnimation("animation.dunebreaker_shield.derecha", true));
        if (state == 4) controller.setAnimation(new AnimationBuilder().addAnimation("animation.dunebreaker_shield.attackI", false).addAnimation("animation.dunebreaker_shield.izquierda", true));
    }

    @Override
	public boolean isShield(ItemStack stack, LivingEntity entity) {
		return true;
	}
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }
}