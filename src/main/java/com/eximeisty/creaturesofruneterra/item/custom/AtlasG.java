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
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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
    private boolean hand=false;
    private int dashTicks=0;
    private int soundTicks=0;
    private boolean pound=false;
    private boolean dash=false;
    private int poundTicks=0;
    private double y;
    private Vector3d motion;
    private static final AnimationBuilder CHARGE_ANIM = new AnimationBuilder().addAnimation("animation.atlasg.charge", false).addAnimation("animation.atlasg.full", true);
    private static final AnimationBuilder CHARGE2_ANIM = new AnimationBuilder().addAnimation("animation.atlasg.charge2", false).addAnimation("animation.atlasg.full2", true);
    private static final AnimationBuilder IDLE_ANIM = new AnimationBuilder().addAnimation("animation.atlasg.idle", true);;
    private static final AnimationBuilder IDLE2_ANIM = new AnimationBuilder().addAnimation("animation.atlasg.idle2", true);;

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

    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        Iterator<ItemStack> item = entityIn.getHeldEquipment().iterator();
        Iterator<ItemStack> item2 = entityIn.getHeldEquipment().iterator();
        if(item.next()==stack && hand) hand=false;
        if(item.next()==stack && !hand) hand=true;
        if (!worldIn.isRemote && !isCharged(stack)) {
            final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerWorld) worldIn);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entityIn);
            if(hand){
                GeckoLibNetwork.syncAnimation(target, this, id, 2);
            }else{
                GeckoLibNetwork.syncAnimation(target, this, id, 4);
            }
        }
        if (!worldIn.isRemote && isCharged(stack)) {
            final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerWorld) worldIn);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entityIn);
            if(hand){
                GeckoLibNetwork.syncAnimation(target, this, id, 1);
            }else{
                GeckoLibNetwork.syncAnimation(target, this, id, 3);
            }
        }
        if(getState(stack)==3){
            if(dashTicks==0) y = entityIn.getPosY()+5;
            if(dash){
                dashTicks++;
                attackBB(entityIn.getBoundingBox().expand(0.5, 0, 0.5).expand(-0.5, 0, -0.5), entityIn);
                breakBB(entityIn.getBoundingBox().expand(1, 0, 1).expand(-1, 0, -1).offset(entityIn.getLookVec().x * 1.5, 0, entityIn.getLookVec().z * 1.5), worldIn);
            }
            if(entityIn.fallDistance>=7) pound = dash = true;
            if(entityIn.isOnGround() && (pound || poundTicks>0)){
                poundTicks++;
                breakBB(entityIn.getBoundingBox().expand(1, -2, 1).expand(-1, 0, -1).contract(0, 2, 0), worldIn);
                breakBB(entityIn.getBoundingBox().expand(2, -1, 2).expand(-2, 0, -2).contract(0, 2, 0), worldIn);
                attackBB(entityIn.getBoundingBox().expand(2, 0, 2).expand(-2, 0, -2), entityIn);
                pound=false;
                if(poundTicks>=3) poundTicks=0;
            }
            if(y<entityIn.getPosY() || entityIn.isInWater()) {
                y = entityIn.getPosY() + 5; pound = dash = false;
            }
            if(y < entityIn.getPosY() || (dashTicks>15 && entityIn.isOnGround()) || (item2.next()!=stack && item2.next()!=stack)) {
                dashTicks = poundTicks = 0; dash = pound = false;
                setState(stack, 1);
            }
        }
    }

    public void breakBB(AxisAlignedBB bb, World worldIn){
        BlockPos.getAllInBox(bb).forEach(pos->{
            if(worldIn.getBlockState(pos)!=Blocks.AIR.getDefaultState() && worldIn.getBlockState(pos)!=Blocks.WATER.getDefaultState() && worldIn.getBlockState(pos)!=Blocks.LAVA.getDefaultState()){
                if(worldIn.getBlockState(pos).getBlockHardness(worldIn, pos)>=0 && worldIn.getBlockState(pos).getBlockHardness(worldIn, pos)<=80) worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());//worldIn.destroyBlock(pos, true, player);
            }
        });
    }

    public void attackBB(AxisAlignedBB bb, Entity player){
        player.world.getEntitiesWithinAABB(LivingEntity.class, bb).stream().forEach(livingEntity -> {
            if(!livingEntity.isEntityEqual(player)) livingEntity.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity)player), 15);
            if(motion==null) motion = player.getMotion();
            if(!livingEntity.world.isRemote) livingEntity.setMotion(motion.add(0,0.1,0));//livingEntity.setMotion(player.getLookVec().x*2, 0.2, player.getLookVec().z*2);
        });
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerentity, Hand handIn) {
        ItemStack stack = playerentity.getHeldItem(handIn);
        playerentity.setActiveHand(handIn);
        if(isCharged(stack)){
            worldIn.playSound(playerentity, playerentity.getPosition(), SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.PLAYERS, 5, 0.9f);
            setState(stack, 3);
            dash=true;
            playerentity.getCooldownTracker().setCooldown(this, 20);
            motion = new Vector3d(playerentity.getLookVec().x*2, 0.1, playerentity.getLookVec().z*2);
            playerentity.setMotion(motion);
            if (!worldIn.isRemote){
                stack.damageItem(25, playerentity, (player) -> {
                    player.sendBreakAnimation(playerentity.getActiveHand());
                });
            }
        }else{
            playSounds(worldIn, playerentity);
            dashTicks=0;
            setState(stack, 2);
            playerentity.getCooldownTracker().setCooldown(this, 25);
            playerentity.addStat(Stats.ITEM_USED.get(this));
        }
        if (!worldIn.isRemote && !isCharged(stack)) {
            final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerWorld) worldIn);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerentity);
            if(handIn==Hand.MAIN_HAND){
                GeckoLibNetwork.syncAnimation(target, this, id, 3);
            }else{
                GeckoLibNetwork.syncAnimation(target, this, id, 1);
            }
        }
        setCharged(stack, !isCharged(stack));
        return ActionResult.resultConsume(stack);
    }

    public void playSounds(World worldIn, PlayerEntity player){
        if(soundTicks==0) worldIn.playSound(player, player.getPosition(), SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.PLAYERS, 3, 1);
        if(soundTicks==10) worldIn.playSound(player, player.getPosition(), SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.PLAYERS, 3, 1.5f);
        if(soundTicks==20) worldIn.playSound(player, player.getPosition(), SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.PLAYERS, 3, 0.7f);
        if(soundTicks==30) worldIn.playSound(player, player.getPosition(), SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.PLAYERS, 3, 1f);
        if(soundTicks==40) worldIn.playSound(player, player.getPosition(), SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.PLAYERS, 3, 1.2f);
        if(soundTicks==50) worldIn.playSound(player, player.getPosition(), SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.PLAYERS, 3, 0.3f);
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
        CompoundNBT compoundnbt = stack.getTag();
        return compoundnbt != null && compoundnbt.getBoolean("Charged");
    }
  
    public static void setCharged(ItemStack stack, boolean chargedIn) {
        CompoundNBT compoundnbt = stack.getOrCreateTag();
        compoundnbt.putBoolean("Charged", chargedIn);
    }

    public static int getState(ItemStack stack) {
        CompoundNBT compoundnbt = stack.getTag();
        return compoundnbt.getInt("State");
    }
  
    public static void setState(ItemStack stack, int chargedIn) {
        CompoundNBT compoundnbt = stack.getOrCreateTag();
        compoundnbt.putInt("State", chargedIn);
    }
}