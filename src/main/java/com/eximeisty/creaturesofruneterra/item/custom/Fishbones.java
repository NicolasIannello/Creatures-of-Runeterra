package com.eximeisty.creaturesofruneterra.item.custom;

import java.util.List;
import java.util.function.Predicate;

import com.eximeisty.creaturesofruneterra.entity.custom.MisilEntity;
import com.eximeisty.creaturesofruneterra.item.ModItems;
import com.eximeisty.creaturesofruneterra.item.client.fishbones.FishbonesRenderer;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
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
    protected static final Predicate<ItemStack> MISIL = (stack) -> stack.isItemEqual( new ItemStack(ModItems.MISIL.get()));
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
    private static final AnimationBuilder RELOAD_ANIM = new AnimationBuilder().addAnimation("animation.fishbones.reload", false).addAnimation("animation.fishbones.charged", true);
    private static final AnimationBuilder FIRE_ANIM = new AnimationBuilder().addAnimation("animation.fishbones.fire", false).addAnimation("animation.fishbones.idle", true);

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
            boolean flag = playerentity.abilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemstack = playerentity.findAmmo(stack);
            if(isCharged(stack)){
                playerentity.getCooldownTracker().setCooldown(this, 10);
            }else{
                playerentity.getCooldownTracker().setCooldown(this, 30);
            }
            if (!itemstack.isEmpty() || flag || isCharged(stack)) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(ModItems.MISIL.get());
                }
                boolean flag1 = playerentity.abilities.isCreativeMode || (itemstack.getItem() instanceof MisilItem && ((MisilItem)itemstack.getItem()).isInfinite(itemstack, stack, playerentity));
                if (!worldIn.isRemote) {
                    if(isCharged(stack)==true){
                        MisilItem misilitem = (MisilItem)(itemstack.getItem() instanceof MisilItem ? itemstack.getItem() : ModItems.MISIL.get());
                        MisilEntity misilentity = misilitem.createMisil(worldIn, itemstack, playerentity);

                        misilentity= customMisil(misilentity);
                        misilentity.setDirectionAndMovement(playerentity, playerentity.rotationPitch, playerentity.rotationYaw, 0.0F,1.0F * 3.0F, 1.0F);
                        misilentity.setDamage(15);
                        misilentity.setKnockbackStrength(1);
                        misilentity.ticksExisted = 35;
                        misilentity.setNoGravity(true);

                        stack.damageItem(1, playerentity, (player) -> {
                            player.sendBreakAnimation(playerentity.getActiveHand());
                        });
                        /*if (flag1 || playerentity.abilities.isCreativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
                            abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                        }*/
                        worldIn.addEntity(misilentity);
                    }
                }
                if(isCharged(stack)) worldIn.playSound(playerentity, playerentity.getPosition(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1, 1);
                if(isCharged(stack)==false){
                    worldIn.playSound(playerentity, playerentity.getPosition(), SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.PLAYERS, 1, 1);
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
                if(isCharged(stack)==true){
                    GeckoLibNetwork.syncAnimation(target, this, id, 3);
                }else{
                    GeckoLibNetwork.syncAnimation(target, this, id, 2);
                }
            }
            setCharged(stack, !isCharged(stack));
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
        
        if(isCharged(itemstack)){
            playerIn.setActiveHand(handIn);
            return ActionResult.resultConsume(itemstack);
        }else if (!playerIn.abilities.isCreativeMode && !flag) {
           return ActionResult.resultFail(itemstack);
        }else {
            playerIn.setActiveHand(handIn);
            return ActionResult.resultConsume(itemstack);
        }
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
        controller.markNeedsReload();
        if (state == 2) {
			controller.setAnimation(RELOAD_ANIM);
		}
        if (state == 3) {
			controller.setAnimation(FIRE_ANIM);
		}
    }

    @Override //@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent(quotes[quote]));
	}

    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return ModItems.GEMSTONE.get()==repair.getItem() || super.getIsRepairable(toRepair, repair);
    }
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
    public AbstractArrowEntity customArrow(AbstractArrowEntity arrow) {
        return arrow;
    }  
    public MisilEntity customMisil(MisilEntity misil) {
        return misil;
    }   
    public int func_230305_d_() {
        return 15;
    }
    @Override
    public Predicate<ItemStack> getInventoryAmmoPredicate() {
        return MISIL;
    }
}