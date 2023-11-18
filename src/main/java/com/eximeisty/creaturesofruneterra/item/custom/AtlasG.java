package com.eximeisty.creaturesofruneterra.item.custom;

import com.eximeisty.creaturesofruneterra.item.client.atlasg.AtlasGRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
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

import java.util.Iterator;
import java.util.function.Consumer;

public class AtlasG extends PickaxeItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean hand=false;
    private int dashTicks=0;
    private int soundTicks=0;
    private boolean pound=false;
    private Vec3 motion;
    private static final RawAnimation CHARGE_ANIM = RawAnimation.begin().then("animation.atlasg.charge", Animation.LoopType.PLAY_ONCE).then("animation.atlasg.full", Animation.LoopType.LOOP);
    private static final RawAnimation CHARGE2_ANIM = RawAnimation.begin().then("animation.atlasg.charge2", Animation.LoopType.PLAY_ONCE).then("animation.atlasg.full2", Animation.LoopType.LOOP);
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().then("animation.atlasg.idle", Animation.LoopType.LOOP);
    private static final RawAnimation IDLE2_ANIM = RawAnimation.begin().then("animation.atlasg.idle2", Animation.LoopType.LOOP);

    public AtlasG(Tier p_42961_, int p_42962_, float p_42963_, Properties p_42964_) {
        super(p_42961_, p_42962_, p_42963_, p_42964_);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Material material = state.getMaterial();
        return  Blocks.OBSIDIAN.defaultBlockState()==state || Blocks.NETHERITE_BLOCK.defaultBlockState()==state || Blocks.ANCIENT_DEBRIS.defaultBlockState()==state ? 200 : material != Material.METAL && /*material != Material.ANVIL &&*/ material != Material.STONE ? 15 : this.speed;
    }

    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        Iterator<ItemStack> item = entityIn.getHandSlots().iterator();
        if(item.next()==stack && hand) hand=false;
        if(item.next()==stack && !hand) hand=true;
        if (!worldIn.isClientSide && !isCharged(stack)) {
            if(hand){
                triggerAnim(entityIn, GeoItem.getOrAssignId(stack, (ServerLevel)worldIn), "atlas_controller", "idle2");
            }else{
                triggerAnim(entityIn, GeoItem.getOrAssignId(stack, (ServerLevel)worldIn), "atlas_controller", "idle");
            }
        }
        if(getState(stack)==3){
            dashTicks++;
            attackBB(entityIn.getBoundingBox().expandTowards(0.5, 0, 0.5).expandTowards(-0.5, 0, -0.5), entityIn);
            breakBB(entityIn.getBoundingBox().expandTowards(0.5, 0, 0.5).expandTowards(-0.5, 0, -0.5).move(entityIn.getLookAngle().x*1.5, 0, entityIn.getLookAngle().z*1.5), entityIn, worldIn);
            if(entityIn.fallDistance>=7) pound=true;
            if(dashTicks>=20 && (!entityIn.isOnGround())) dashTicks-=3;
            if(entityIn.isOnGround() && pound){
                breakBB(entityIn.getBoundingBox().expandTowards(1, -2, 1).expandTowards(-1, 0, -1).contract(0, 2, 0), entityIn, worldIn);
                breakBB(entityIn.getBoundingBox().expandTowards(2, -1, 2).expandTowards(-2, 0, -2).contract(0, 2, 0), entityIn, worldIn);
                attackBB(entityIn.getBoundingBox().expandTowards(2, 0, 2).expandTowards(-2, 0, -2), entityIn);
                pound=false;
            }
            if(dashTicks>=20) setState(stack, 1);
        }
    }

    public void breakBB(AABB bb, Entity player, Level worldIn){
        BlockPos.betweenClosedStream(bb).forEach(pos->{
            if(player.level.getBlockState(pos)!=Blocks.AIR.defaultBlockState() && player.level.getBlockState(pos)!=Blocks.WATER.defaultBlockState() && player.level.getBlockState(pos)!=Blocks.LAVA.defaultBlockState()){
                if(player.level.getBlockState(pos).getDestroySpeed(player.level, pos)>=0 && player.level.getBlockState(pos).getDestroySpeed(player.level, pos)<=60) player.level.destroyBlock(pos, true, player);
            }
        });
    }

    public void attackBB(AABB bb, Entity player){
        player.level.getEntities(null, player.getBoundingBox().expandTowards(2, 0, 2).expandTowards(-2, 0, -2)).stream().forEach(livingEntity -> {
            if(!livingEntity.is(player)) livingEntity.hurt(player.level.damageSources().playerAttack((Player) player), 15);
            if(!livingEntity.level.isClientSide) livingEntity.setDeltaMovement(motion.add(0,0.1,0));//livingEntity.setDeltaMovement(player.getLookAngle().x*2, 0.2, player.getLookAngle().z*2);
        });
    }

    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerentity, InteractionHand handIn) {
        ItemStack stack = playerentity.getItemInHand(handIn);
        playerentity.startUsingItem(handIn);
        if(isCharged(stack)){
            worldIn.playSound(playerentity, playerentity.blockPosition(), SoundEvents.PISTON_EXTEND, SoundSource.PLAYERS, 5, 0.9f);
            setState(stack, 3);
            playerentity.getCooldowns().addCooldown(this, 20);
            motion = new Vec3(playerentity.getLookAngle().x*2, 0.1, playerentity.getLookAngle().z*2);
            playerentity.setDeltaMovement(motion);
            if (!worldIn.isClientSide){
                stack.hurtAndBreak(25, playerentity, (player) -> {
                    player.broadcastBreakEvent(playerentity.getUsedItemHand());
                });
            }
        }else{
            playSounds(worldIn, playerentity);
            dashTicks=0;
            setState(stack, 2);
            playerentity.getCooldowns().addCooldown(this, 25);
            playerentity.awardStat(Stats.ITEM_USED.get(this));
        }
        if (!worldIn.isClientSide && !isCharged(stack)) {
            if(handIn==InteractionHand.MAIN_HAND){
                triggerAnim(playerentity, GeoItem.getOrAssignId(stack, (ServerLevel)worldIn), "atlas_controller", "charge");
            }else{
                triggerAnim(playerentity, GeoItem.getOrAssignId(stack, (ServerLevel)worldIn), "atlas_controller", "charge2");
            }
        }
        setCharged(stack, !isCharged(stack));
        return InteractionResultHolder.consume(stack);
    }

    public void playSounds(Level worldIn, Player player){
        if(soundTicks==0) worldIn.playSound(player, player.blockPosition(), SoundEvents.PISTON_CONTRACT, SoundSource.PLAYERS, 3, 1);
        if(soundTicks==10/2) worldIn.playSound(player, player.blockPosition(), SoundEvents.PISTON_CONTRACT, SoundSource.PLAYERS, 3, 1.5f);
        if(soundTicks==20/2) worldIn.playSound(player, player.blockPosition(), SoundEvents.PISTON_CONTRACT, SoundSource.PLAYERS, 3, 0.7f);
        if(soundTicks==30/2) worldIn.playSound(player, player.blockPosition(), SoundEvents.PISTON_CONTRACT, SoundSource.PLAYERS, 3, 1f);
        if(soundTicks==40/2) worldIn.playSound(player, player.blockPosition(), SoundEvents.PISTON_CONTRACT, SoundSource.PLAYERS, 3, 1.2f);
        if(soundTicks==50/2) worldIn.playSound(player, player.blockPosition(), SoundEvents.PISTON_CONTRACT, SoundSource.PLAYERS, 3, 0.3f);
        soundTicks++;
        if(soundTicks>50/2){
            soundTicks=0;
        }else{
            playSounds(worldIn, player);
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

    public static int getState(ItemStack stack) {
        CompoundTag compoundnbt = stack.getTag();
        return compoundnbt.getInt("State");
    }

    public static void setState(ItemStack stack, int chargedIn) {
        CompoundTag compoundnbt = stack.getOrCreateTag();
        compoundnbt.putInt("State", chargedIn);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "atlas_controller", state -> PlayState.CONTINUE)
                .triggerableAnim("charge", CHARGE_ANIM).triggerableAnim("charge2", CHARGE2_ANIM)
                .triggerableAnim("idle", IDLE_ANIM).triggerableAnim("idle2", IDLE2_ANIM));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private AtlasGRenderer renderer = null;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new AtlasGRenderer();

                return this.renderer;
            }
        });
    }
}
