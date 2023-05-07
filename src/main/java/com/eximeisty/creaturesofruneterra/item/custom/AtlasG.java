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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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
        if (!worldIn.isRemote && !isCharged(stack)) {
            final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerWorld) worldIn);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entityIn);
            GeckoLibNetwork.syncAnimation(target, this, id, 2);
        }
        if(getState(stack)==3){
            dashTicks++;
            entityIn.world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(entityIn.getBoundingBox().minX-1.0D,entityIn.getBoundingBox().minY,entityIn.getBoundingBox().minZ-1.0D,entityIn.getBoundingBox().maxX+1.0D,entityIn.getBoundingBox().maxY,entityIn.getBoundingBox().maxZ+1.0D)).stream().forEach(livingEntity -> {
                if(!livingEntity.isEntityEqual(entityIn)) livingEntity.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity)entityIn), 10);
                if(!livingEntity.world.isRemote) livingEntity.applyKnockback(1.5F, livingEntity.getPosX()-entityIn.getPosX(), livingEntity.getPosZ()-entityIn.getPosZ());
            });
            BlockPos.getAllInBox(new AxisAlignedBB(entityIn.getBoundingBox().minX-0.5D,entityIn.getBoundingBox().minY,entityIn.getBoundingBox().minZ-0.5D,entityIn.getBoundingBox().maxX+0.5D,entityIn.getBoundingBox().maxY,entityIn.getBoundingBox().maxZ+0.5D))
            .forEach(pos->{
                if(entityIn.world.getBlockState(pos)!=Blocks.AIR.getDefaultState() && entityIn.world.getBlockState(pos)!=Blocks.WATER.getDefaultState() && entityIn.world.getBlockState(pos)!=Blocks.LAVA.getDefaultState()){
                    if(entityIn.world.getBlockState(pos).getBlockHardness(worldIn, pos)>=0 && entityIn.world.getBlockState(pos).getBlockHardness(worldIn, pos)<=80) entityIn.world.destroyBlock(pos, true, entityIn);
                }
            });
            if(dashTicks>=20 &&/*&& entityIn.isOnGround()*/!(entityIn.getMotion().x<=-0.1 || entityIn.getMotion().x>=0.1 || entityIn.getMotion().y<=-0.1 || entityIn.getMotion().y>=0.1)) setState(stack, 1);
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerentity, Hand handIn) {
        ItemStack stack = playerentity.getHeldItem(handIn);
        playerentity.setActiveHand(handIn);
        if(isCharged(stack)){
            setState(stack, 3);
            playerentity.getCooldownTracker().setCooldown(this, 20);
            playerentity.setMotion(playerentity.getLookVec().x*2, 0.1, playerentity.getLookVec().z*2);
            if (!worldIn.isRemote){
                stack.damageItem(50, playerentity, (player) -> {
                    player.sendBreakAnimation(playerentity.getActiveHand());
                });
            }
        }else{
            dashTicks=0;
            setState(stack, 2);
            playerentity.getCooldownTracker().setCooldown(this, 25);
            playerentity.addStat(Stats.ITEM_USED.get(this));
        }
        if (!worldIn.isRemote && !isCharged(stack)) {
            final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerWorld) worldIn);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerentity);
            GeckoLibNetwork.syncAnimation(target, this, id, 1);
        }
        setCharged(stack, !isCharged(stack));
        return ActionResult.resultConsume(stack);
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
        controller.markNeedsReload();
        if(hand){
            if (state == 1) controller.setAnimation(CHARGE2_ANIM);
            if (state == 2) controller.setAnimation(IDLE2_ANIM);
        }else{
            if (state == 1) controller.setAnimation(CHARGE_ANIM);
            if (state == 2) controller.setAnimation(IDLE_ANIM);
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

    public static int getState(ItemStack stack) {
        CompoundNBT compoundnbt = stack.getTag();
        return compoundnbt.getInt("State");
    }
  
    public static void setState(ItemStack stack, int chargedIn) {
        CompoundNBT compoundnbt = stack.getOrCreateTag();
        compoundnbt.putInt("State", chargedIn);
    }
}