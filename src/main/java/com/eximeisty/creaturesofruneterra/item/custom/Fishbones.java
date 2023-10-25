package com.eximeisty.creaturesofruneterra.item.custom;


import com.eximeisty.creaturesofruneterra.entity.custom.MisilEntity;
import com.eximeisty.creaturesofruneterra.item.ModItems;
import com.eximeisty.creaturesofruneterra.item.client.fishbones.FishbonesRenderer;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
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

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Fishbones extends ProjectileWeaponItem implements GeoItem {
    protected static final Predicate<ItemStack> MISIL = (stack) -> stack.sameItem(new ItemStack(ModItems.MISIL.get()));
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String controllerName = "controller";
    final int quote = (int) (Math.random() * 8);
    final String[] quotes = {"Fishbones, you know what we oughta' do?",
            "Do the laundry, wash dishes and pay some bills. Stupid dumb rocket launcher",
            "Hey Fishbones, think we can wreak havoc forever?",
            "Hey Fishbones, should we blow something up?",
            "What'samatter, Fishbones?",
            "want to blow something up! Great idea, Fishbones!",
            "Just you and me Fishbones!",
            "FISHBONES! We're doin' it!"
    };
    private static final RawAnimation RELOAD_ANIM = RawAnimation.begin().then("animation.fishbones.reload", Animation.LoopType.PLAY_ONCE).then("animation.fishbones.charged", Animation.LoopType.LOOP);
    private static final RawAnimation FIRE_ANIM = RawAnimation.begin().then("animation.fishbones.fire", Animation.LoopType.PLAY_ONCE).then("animation.fishbones.idle", Animation.LoopType.LOOP);
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().then("animation.fishbones.idle", Animation.LoopType.LOOP);

    public Fishbones(Properties properties) {
        super(properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public int getDefaultProjectileRange() {
        return 0;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "fishbones_controller", state -> PlayState.CONTINUE)
                .triggerableAnim("reload", RELOAD_ANIM).triggerableAnim("fire", FIRE_ANIM).triggerableAnim("idle", IDLE_ANIM));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(!worldIn.isClientSide) {
            if(!isCharged(stack)) {
                triggerAnim(entityIn, GeoItem.getOrAssignId(stack, (ServerLevel) worldIn), "fishbones_controller", "idle");
            }else{
                triggerAnim(entityIn, GeoItem.getOrAssignId(stack, (ServerLevel) worldIn), "fishbones_controller", "reload");
            }
        }
    }

    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player) {
            Player playerentity = (Player)entityLiving;
            boolean flag = playerentity.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
            ItemStack itemstack = playerentity.getProjectile(stack);
            if(isCharged(stack)){
                playerentity.getCooldowns().addCooldown(this, 10);
            }else{
                playerentity.getCooldowns().addCooldown(this, 30);
            }
            if (!itemstack.isEmpty() || flag || isCharged(stack)) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(ModItems.MISIL.get());
                }
                boolean flag1 = playerentity.getAbilities().instabuild || (itemstack.getItem() instanceof MisilItem && ((MisilItem)itemstack.getItem()).isInfinite(itemstack, stack, playerentity));
                if (!worldIn.isClientSide) {
                    if(isCharged(stack)==true){
                        MisilItem misilitem = (MisilItem)(itemstack.getItem() instanceof MisilItem ? itemstack.getItem() : ModItems.MISIL.get());
                        MisilEntity misilentity = misilitem.createMisil(worldIn, itemstack, playerentity);

                        misilentity= customMisil(misilentity);
                        misilentity.shootFromRotation(playerentity, playerentity.getRotationVector().x, playerentity.getRotationVector().y, 0.0F,1.0F * 3.0F, 1.0F);
                        misilentity.setBaseDamage(15);
                        misilentity.setXRot(playerentity.getRotationVector().x);
                        misilentity.setYRot(playerentity.getRotationVector().y);
                        //misilentity.setKnockbackStrength(1);
                        misilentity.tickCount = 35;
                        misilentity.setNoGravity(true);

                        stack.hurtAndBreak(1, playerentity, (player) -> {
                            player.broadcastBreakEvent(playerentity.getUsedItemHand());
                        });
                        /*if (flag1 || playerentity.abilities.isCreativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
                            abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                        }*/
                        worldIn.addFreshEntity(misilentity);
                    }
                }
                if(isCharged(stack)) worldIn.playSound(playerentity, playerentity.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1, 1);
                if(isCharged(stack)==false){
                    worldIn.playSound(playerentity, playerentity.blockPosition(), SoundEvents.PISTON_CONTRACT, SoundSource.PLAYERS, 1, 1);
                    if (!flag1 && !playerentity.getAbilities().instabuild) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            playerentity.getInventory().removeItem(itemstack);
                        }
                    }
                    playerentity.awardStat(Stats.ITEM_USED.get(this));
                }
            }
            if (!worldIn.isClientSide) {
                if(isCharged(stack)==true){
                    triggerAnim(playerentity, GeoItem.getOrAssignId(stack, (ServerLevel)worldIn), "fishbones_controller", "fire");
//                else{
//                    triggerAnim(playerentity, GeoItem.getOrAssignId(stack, (ServerLevel)worldIn), "fishbones_controller_controller", "reload");
                }
            }
            setCharged(stack, !isCharged(stack));
        }
    }

    public static boolean isCharged(ItemStack stack) {
        CompoundTag compoundnbt = stack.getTag();
        return compoundnbt != null && compoundnbt.getBoolean("Charged");
    }
  
     public static void setCharged(ItemStack stack, boolean chargedIn) {
        CompoundTag compoundnbt = stack.getOrCreateTag();
        compoundnbt.putBoolean("Charged", chargedIn);
    }

    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        boolean flag = !playerIn.getProjectile(itemstack).isEmpty();
        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, flag);
        if (ret != null) return ret;
        
        if(isCharged(itemstack)){
            playerIn.startUsingItem(handIn);
            return InteractionResultHolder.consume(itemstack);
        }else if (!playerIn.getAbilities().instabuild && !flag) {
           return InteractionResultHolder.fail(itemstack);
        }else {
            playerIn.startUsingItem(handIn);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        		tooltip.add(Component.translatable(quotes[quote]));
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public MisilEntity customMisil(MisilEntity misil) {
        return misil;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return MISIL;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private FishbonesRenderer renderer = null;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new FishbonesRenderer();

                return this.renderer;
            }
        });
    }
}