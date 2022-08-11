package com.eximeisty.creaturesofruneterra.item.custom;

import com.eximeisty.creaturesofruneterra.item.client.DunebreakerShieldRenderer;

import net.minecraft.block.DispenserBlock;
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
import software.bernie.geckolib3.core.AnimationState;
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

    public DunebreakerShield(Properties properties) {
        super(properties.setISTER(()-> DunebreakerShieldRenderer::new));
        GeckoLibNetwork.registerSyncable(this);
        DispenserBlock.registerDispenseBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
    }

    public <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if(event.getController().getAnimationState()==AnimationState.Stopped){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.dunebreaker_shield.idle", true));
        }
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

    public void tick(World worldIn, PlayerEntity playerIn, Hand handIn, ItemStack itemstack){
        if (!worldIn.isRemote) {
            final int id = GeckoLibUtil.guaranteeIDForStack(itemstack, (ServerWorld) worldIn);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerIn);
            GeckoLibNetwork.syncAnimation(target, this, id, 1);
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        if (!worldIn.isRemote) {
            final int id = GeckoLibUtil.guaranteeIDForStack(itemstack, (ServerWorld) worldIn);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerIn);
            GeckoLibNetwork.syncAnimation(target, this, id, 1);
        }
        return ActionResult.resultConsume(itemstack);
    }

    @Override
    public void onAnimationSync(int id, int state) {
        if (state == 1) {
			final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
			controller.markNeedsReload();
			controller.setAnimation(new AnimationBuilder().addAnimation("animation.dunebreaker_shield.guard", false));
		}
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