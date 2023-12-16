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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
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

import java.util.Iterator;
import java.util.function.Consumer;

public class AtlasG extends PickaxeItem implements IAnimatable, ISyncable {
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public String controllerName = "controller";
    private boolean hand=false;
    private int dashTicks=0;
    private int soundTicks=0;
    private boolean pound=false;
    private boolean dash=false;
    private int poundTicks=0;
    private double y;
    private Vec3 motion;
    private static final AnimationBuilder CHARGE_ANIM = new AnimationBuilder().addAnimation("animation.atlasg.charge", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("animation.atlasg.full", ILoopType.EDefaultLoopTypes.LOOP);
    private static final AnimationBuilder CHARGE2_ANIM = new AnimationBuilder().addAnimation("animation.atlasg.charge2", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("animation.atlasg.full2", ILoopType.EDefaultLoopTypes.LOOP);
    private static final AnimationBuilder IDLE_ANIM = new AnimationBuilder().addAnimation("animation.atlasg.idle", ILoopType.EDefaultLoopTypes.LOOP);;
    private static final AnimationBuilder IDLE2_ANIM = new AnimationBuilder().addAnimation("animation.atlasg.idle2", ILoopType.EDefaultLoopTypes.LOOP);;

    public AtlasG(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder);
        GeckoLibNetwork.registerSyncable(this);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Material material = state.getMaterial();
        return  Blocks.OBSIDIAN.defaultBlockState()==state || Blocks.NETHERITE_BLOCK.defaultBlockState()==state || Blocks.ANCIENT_DEBRIS.defaultBlockState()==state ? 200 : material != Material.METAL && material != Material.HEAVY_METAL && material != Material.STONE ? 15 : this.speed;
    }

    public <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if(event.getController().getCurrentAnimation()!=null){
            return PlayState.CONTINUE;
        }else{
            event.getController().setAnimation(IDLE_ANIM);
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

    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        Iterator<ItemStack> item = entityIn.getHandSlots().iterator();
        Iterator<ItemStack> item2 = entityIn.getHandSlots().iterator();
        if(item.next()==stack && hand) hand=false;
        if(item.next()==stack && !hand) hand=true;
        if (!worldIn.isClientSide && !isCharged(stack)) {
            final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerLevel) worldIn);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entityIn);
            if(hand){
                GeckoLibNetwork.syncAnimation(target, this, id, 2);
            }else{
                GeckoLibNetwork.syncAnimation(target, this, id, 4);
            }
        }
        if (!worldIn.isClientSide && isCharged(stack)) {
            final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerLevel) worldIn);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entityIn);
            if(hand){
                GeckoLibNetwork.syncAnimation(target, this, id, 1);
            }else{
                GeckoLibNetwork.syncAnimation(target, this, id, 3);
            }
        }
        if(getState(stack)==3){
            if(dashTicks==0) y = entityIn.getY()+5;
            if(dash){
                dashTicks++;
                attackBB(entityIn.getBoundingBox().expandTowards(0.5, 0, 0.5).expandTowards(-0.5, 0, -0.5), entityIn);
                breakBB(entityIn.getBoundingBox().expandTowards(1, 0, 1).expandTowards(-1, 0, -1).move(entityIn.getLookAngle().x * 1.5, 0, entityIn.getLookAngle().z * 1.5), worldIn);
            }
            if(entityIn.fallDistance>=7) pound = dash = true;
            if(entityIn.isOnGround() && (pound || poundTicks>0)){
                poundTicks++;
                breakBB(entityIn.getBoundingBox().expandTowards(1, -2, 1).expandTowards(-1, 0, -1).contract(0, 2, 0), worldIn);
                breakBB(entityIn.getBoundingBox().expandTowards(2, -1, 2).expandTowards(-2, 0, -2).contract(0, 2, 0), worldIn);
                attackBB(entityIn.getBoundingBox().expandTowards(2, 0, 2).expandTowards(-2, 0, -2), entityIn);
                pound=false;
                if(poundTicks>=3) poundTicks=0;
            }
            if(y<entityIn.getY() || entityIn.isInWater()) {
                y = entityIn.getY() + 5; pound = dash = false;
            }
            if(y < entityIn.getY() || (dashTicks>15 && entityIn.isOnGround()) || (item2.next()!=stack && item2.next()!=stack)) {
                dashTicks = poundTicks = 0; dash = pound = false;
                setState(stack, 1);
            }
        }
    }

    public void breakBB(AABB bb, Level worldIn){
        BlockPos.betweenClosedStream(bb).forEach(pos->{
            if(worldIn.getBlockState(pos)!=Blocks.AIR.defaultBlockState() && worldIn.getBlockState(pos)!=Blocks.WATER.defaultBlockState() && worldIn.getBlockState(pos)!=Blocks.LAVA.defaultBlockState() && !(worldIn.getBlockEntity(pos) instanceof BaseContainerBlockEntity)){
                if(worldIn.getBlockState(pos).getDestroySpeed(worldIn, pos)>0 && worldIn.getBlockState(pos).getDestroySpeed(worldIn, pos)<=80) worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());//worldIn.destroyBlock(pos, true, player);
            }
        });
    }

    public void attackBB(AABB bb, Entity player){
        player.level.getEntities(null, bb).stream().forEach(livingEntity -> {
            if(!livingEntity.is(player)) livingEntity.hurt(DamageSource.playerAttack((Player)player), 15);
            if(motion==null) motion = player.getDeltaMovement();
            if(!livingEntity.level.isClientSide) livingEntity.setDeltaMovement(motion.add(0,0.1,0));//livingEntity.setDeltaMovement(player.getLookAngle().x*2, 0.2, player.getLookAngle().z*2);
        });
    }

    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerentity, InteractionHand handIn) {
        ItemStack stack = playerentity.getItemInHand(handIn);
        playerentity.startUsingItem(handIn);
        if(isCharged(stack)){
            worldIn.playSound(playerentity, playerentity.blockPosition(), SoundEvents.PISTON_EXTEND, SoundSource.PLAYERS, 5, 0.9f);
            setState(stack, 3);
            dash=true;
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
            final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerLevel) worldIn);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerentity);
            if(handIn==InteractionHand.MAIN_HAND){
                GeckoLibNetwork.syncAnimation(target, this, id, 3);
            }else{
                GeckoLibNetwork.syncAnimation(target, this, id, 1);
            }
        }
        setCharged(stack, !isCharged(stack));
        return InteractionResultHolder.consume(stack);
    }

    public void playSounds(Level worldIn, Player player){
        if(soundTicks==0) worldIn.playSound(player, player.blockPosition(), SoundEvents.PISTON_CONTRACT, SoundSource.PLAYERS, 3, 1);
        if(soundTicks==10) worldIn.playSound(player, player.blockPosition(), SoundEvents.PISTON_CONTRACT, SoundSource.PLAYERS, 3, 1.5f);
        if(soundTicks==20) worldIn.playSound(player, player.blockPosition(), SoundEvents.PISTON_CONTRACT, SoundSource.PLAYERS, 3, 0.7f);
        if(soundTicks==30) worldIn.playSound(player, player.blockPosition(), SoundEvents.PISTON_CONTRACT, SoundSource.PLAYERS, 3, 1f);
        if(soundTicks==40) worldIn.playSound(player, player.blockPosition(), SoundEvents.PISTON_CONTRACT, SoundSource.PLAYERS, 3, 1.2f);
        if(soundTicks==50) worldIn.playSound(player, player.blockPosition(), SoundEvents.PISTON_CONTRACT, SoundSource.PLAYERS, 3, 0.3f);
        soundTicks++;
        if(soundTicks>50){
            soundTicks=0;
        }else{
            playSounds(worldIn, player);
        }
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
        controller.markNeedsReload();
        if (state == 1) controller.setAnimation(CHARGE2_ANIM);
        if (state == 2) controller.setAnimation(IDLE2_ANIM);
        if (state == 3) controller.setAnimation(CHARGE_ANIM);
        if (state == 4) controller.setAnimation(IDLE_ANIM);
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