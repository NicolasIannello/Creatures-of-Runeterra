package com.eximeisty.creaturesofruneterra.item.custom;

import java.util.Iterator;

import com.eximeisty.creaturesofruneterra.item.client.atlasg.AtlasGRenderer;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
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

public class AtlasG extends PickaxeItem implements IAnimatable , ISyncable{
    private AnimationFactory factory = new AnimationFactory(this);
    public String controllerName = "controller";
    public boolean hand=false;

    public AtlasG(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder.setISTER(()-> AtlasGRenderer::new));
        GeckoLibNetwork.registerSyncable(this);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Material material = state.getMaterial();
        return  Blocks.OBSIDIAN.getDefaultState()==state || Blocks.NETHERITE_BLOCK.getDefaultState()==state || Blocks.ANCIENT_DEBRIS.getDefaultState()==state ? 200 : material != Material.IRON && material != Material.ANVIL && material != Material.ROCK ? 15 : this.efficiency;
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

    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        Iterator<ItemStack> item = entityIn.getHeldEquipment().iterator();
        if(item.next()==stack && hand) hand=false;
        if(item.next()==stack && !hand) hand=true;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerentity, Hand handIn) {
        ItemStack stack = playerentity.getHeldItem(handIn);
        playerentity.setActiveHand(handIn);
        if(isCharged(stack)){
            playerentity.getCooldownTracker().setCooldown(this, 10);
        }else{
            playerentity.getCooldownTracker().setCooldown(this, 30);
        }
        if (isCharged(stack)) {
            playerentity.setMotion(playerentity.getLookVec().x*4, 0.3, playerentity.getLookVec().z*4);
            if (!worldIn.isRemote) {
                if(isCharged(stack)){
                    stack.damageItem(10, playerentity, (player) -> {
                        player.sendBreakAnimation(playerentity.getActiveHand());
                    });
                }
            }
            if(!isCharged(stack)){
                playerentity.addStat(Stats.ITEM_USED.get(this));
            }
        }
        if (!worldIn.isRemote) {
            final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerWorld) worldIn);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerentity);
            if(isCharged(stack)){
                GeckoLibNetwork.syncAnimation(target, this, id, 1);
            }else{
                GeckoLibNetwork.syncAnimation(target, this, id, 2);
            }
        }
        setCharged(stack, !isCharged(stack));
        return ActionResult.resultConsume(stack);
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
        controller.markNeedsReload();
        if (state == 2) {
			controller.setAnimation(new AnimationBuilder().addAnimation("animation.atlasg.reload", false).addAnimation("animation.atlasg.charged", true));
		}
        if (state == 3) {
			controller.setAnimation(new AnimationBuilder().addAnimation("animation.atlasg.fire", false).addAnimation("animation.atlasg.idle", true));
		}
    }

    public static boolean isCharged(ItemStack stack) {
        CompoundNBT compoundnbt = stack.getTag();
        return compoundnbt != null && compoundnbt.getBoolean("Charged");
    }
  
    public static void setCharged(ItemStack stack, boolean chargedIn) {
        CompoundNBT compoundnbt = stack.getOrCreateTag();
        compoundnbt.putBoolean("Charged", chargedIn);
    }
}