package com.eximeisty.creaturesofruneterra.item.custom;

import java.util.List;
import java.util.function.Predicate;

import com.eximeisty.creaturesofruneterra.item.client.fishbones.FishbonesRenderer;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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

public class Fishbones extends ShootableItem implements IAnimatable , ISyncable{
    private AnimationFactory factory = new AnimationFactory(this);
    public String controllerName = "controller";
    final int quote=(int)(Math.random() * 8);
    final String[] quotes = {"Fishbones, you know what we oughta' do?",
                            "Do the laundry, wash dishes and pay some bills. Stupid dumb rocket launcher",
                            "Hey Fishbones, think we can wreak havoc forever?",
                            "Hey Fishbones, should we blow something up?",
                            "What'samatter, Fishbones?",
                            "want to blow something up! Great idea, Fishbones!",
                            "Just you and me Fishbones!",
                            "FISHBONES! We're doin' it!"
                            };

    public Fishbones(Properties properties) {
        super(properties.setISTER(()-> FishbonesRenderer::new));
        GeckoLibNetwork.registerSyncable(this);
    }
    
    public <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if(event.getController().getAnimationState()==AnimationState.Stopped){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.fishbones.idle", true));
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

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity)entityLiving;
            boolean flag = playerentity.abilities.isCreativeMode;
            ItemStack itemstack = playerentity.findAmmo(stack);
            if(isCharged(itemstack)){
                playerentity.getCooldownTracker().setCooldown(this, 10);
            }else{
                playerentity.getCooldownTracker().setCooldown(this, 30);
            }
            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }
                boolean flag1 = playerentity.abilities.isCreativeMode || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, stack, playerentity));
                if (!worldIn.isRemote) {
                    if(isCharged(itemstack)==true){
                        ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
                        AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(worldIn, itemstack, playerentity);
                        abstractarrowentity = customArrow(abstractarrowentity);
                        abstractarrowentity.setDirectionAndMovement(playerentity, playerentity.rotationPitch, playerentity.rotationYaw, 0.0F,1.0F * 3.0F, 1.0F);

                        abstractarrowentity.setDamage(2.5);
                        abstractarrowentity.setIsCritical(true);
                        abstractarrowentity.setKnockbackStrength(5);
                        abstractarrowentity.ticksExisted = 35;
                        abstractarrowentity.setNoGravity(true);

                        stack.damageItem(1, playerentity, (player) -> {
                            player.sendBreakAnimation(playerentity.getActiveHand());
                        });
                        if (flag1 || playerentity.abilities.isCreativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
                            abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                        }
                        worldIn.addEntity(abstractarrowentity);
                    }
                }
                if(isCharged(itemstack)==false){
                    //worldIn.playSound((PlayerEntity)null, playerentity.getPosX(), playerentity.getPosY(), playerentity.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !playerentity.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            playerentity.inventory.deleteStack(itemstack);
                        }
                    }
                    playerentity.addStat(Stats.ITEM_USED.get(this));
                }
            }
            if (!worldIn.isRemote) {
                final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerWorld) worldIn);
                final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entityLiving);
                if(isCharged(itemstack)==true){
                    GeckoLibNetwork.syncAnimation(target, this, id, 3);
                }else{
                    GeckoLibNetwork.syncAnimation(target, this, id, 2);
                }
            }
            setCharged(itemstack, !isCharged(itemstack));
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

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        boolean flag = !playerIn.findAmmo(itemstack).isEmpty();
        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, flag);
        if (ret != null) return ret;
  
        if (!playerIn.abilities.isCreativeMode && !flag) {
           return ActionResult.resultFail(itemstack);
        } else {
            playerIn.setActiveHand(handIn);
            return ActionResult.resultConsume(itemstack);
        }
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
        controller.markNeedsReload();
        if (state == 2) {
			controller.setAnimation(new AnimationBuilder().addAnimation("animation.fishbones.reload", false).addAnimation("animation.fishbones.charged", true));
            System.out.println("reload");
		}
        if (state == 3) {
			controller.setAnimation(new AnimationBuilder().addAnimation("animation.fishbones.fire", false).addAnimation("animation.fishbones.idle", true));
		}
    }

    @Override //@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent(quotes[quote]));
	}

    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return Items.LAPIS_LAZULI==repair.getItem() || super.getIsRepairable(toRepair, repair);
    }
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
    public AbstractArrowEntity customArrow(AbstractArrowEntity arrow) {
        return arrow;
    }   
    public int func_230305_d_() {
        return 15;
    }
    @Override
    public Predicate<ItemStack> getInventoryAmmoPredicate() {
        return ARROWS;
    }
}