package com.eximeisty.creaturesofruneterra.block.entity;

import com.eximeisty.creaturesofruneterra.block.ModTiles;
import com.eximeisty.creaturesofruneterra.item.ModItemTier;
import com.eximeisty.creaturesofruneterra.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class DarkinThingyTileEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public final ItemStackHandler itemHandler = new ItemStackHandler(1){
        @Override
        public int getSlotLimit(int slot) {
            switch (slot) {
                case 0: return 1;
                default: return super.getSlotLimit(slot);
            }
        }

        @Override
        protected void onContentsChanged(int slot){
            setChanged();
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    };
    private boolean eye = false;
    private boolean nw = false;
    public int ticks = 0;

    public DarkinThingyTileEntity(BlockPos pos, BlockState state) {
        super(ModTiles.DARKIN_PEDESTAL.get(), pos, state);
    }

    public <T extends GeoAnimatable> PlayState predicate(AnimationState<T> event)  {
        return PlayState.CONTINUE;
	}

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers){
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override @Nullable
    public ClientboundBlockEntityDataPacket  getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithFullMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        CompoundTag tag = pkt.getTag();
        if(tag != null){
            load(tag);
        }
    }

    public void tick() {
        ItemStack item = itemHandler.getStackInSlot(0);
        if(getLevel().dimension()==Level.NETHER){
            if(item.is(Items.IRON_HOE) || item.getItem() instanceof SwordItem){
                BlockEntity tileentity;
                BlockEntity tileentity2;
                if(getLevel().getBlockEntity(worldPosition.east(3))!=null){
                    tileentity = getLevel().getBlockEntity(worldPosition.east(3));
                    tileentity2 = getLevel().getBlockEntity(worldPosition.west(3));
                    this.getPersistentData().putBoolean("ns", false);
                }else{
                    tileentity = getLevel().getBlockEntity(worldPosition.north(3));
                    tileentity2 = getLevel().getBlockEntity(worldPosition.south(3));
                    this.getPersistentData().putBoolean("ns", true);
                }
                this.setChanged();
                this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
                if(tileentity instanceof DarkinThingyTileEntity && tileentity2 instanceof DarkinThingyTileEntity) {
                    if(item.is(Items.IRON_HOE)){
                        if(((DarkinThingyTileEntity) tileentity).itemHandler.getStackInSlot(0).is(Items.NETHER_WART)) nw = true;
                        if(((DarkinThingyTileEntity) tileentity2).itemHandler.getStackInSlot(0).is(Items.NETHER_WART)) nw = true;
                        if(((DarkinThingyTileEntity) tileentity).itemHandler.getStackInSlot(0).is(Items.ENDER_EYE)) eye = true;
                        if(((DarkinThingyTileEntity) tileentity2).itemHandler.getStackInSlot(0).is(Items.ENDER_EYE)) eye = true;
                        if(nw && eye) {
                            nw = false; eye = false;
                            ticker(null, null, null);
                            if(ticks > 100) ticker(tileentity, tileentity2, new ItemStack(ModItems.RHAAST.get()));
                        }
                    }else if(item.getItem() instanceof SwordItem ? ((SwordItem)item.getItem()).getTier()==ModItemTier.DARKIN : false){
                        ItemStack tileItem = ((DarkinThingyTileEntity) tileentity).itemHandler.getStackInSlot(0);
                        ItemStack tileItem2 = ((DarkinThingyTileEntity) tileentity2).itemHandler.getStackInSlot(0);
                        if(tileItem.is(tileItem2.getItem()) && tileItem2.getItem() instanceof SwordItem) {
                            ticker(null, null, null);
                            if(ticks > 100) {
                                int damage = (int) ((SwordItem) tileItem.getItem()).getDamage() + 1;
                                ItemStack darkinweapon = new ItemStack(ModItems.RHAAST.get());
                                item.getAllEnchantments().forEach(darkinweapon::enchant);
                                darkinweapon.getAttributeModifiers(EquipmentSlot.MAINHAND).forEach((atr, modifier) -> {
                                    if (atr == Attributes.ATTACK_DAMAGE) {
                                        darkinweapon.addAttributeModifier(atr, new AttributeModifier(modifier.getId(), modifier.getName(), damage, modifier.getOperation()), EquipmentSlot.MAINHAND);
                                    } else {
                                        darkinweapon.addAttributeModifier(atr, modifier, EquipmentSlot.MAINHAND);
                                    }
                                });
                                ticker(tileentity, tileentity2, darkinweapon);
                            }
                        }
                    }
                }
            }
        }
    }

    public void ticker(@Nullable BlockEntity tile, @Nullable BlockEntity tile2, @Nullable ItemStack item){
        ticks++;
        if(ticks % 20 == 0) getLevel().playSound(null, worldPosition, SoundEvents.BLAZE_SHOOT, SoundSource.BLOCKS, 1F, 1F);
        if(item!=null && tile!=null && tile2!=null){
            ticks=0;
            setAndDestroyPedestals(tile, tile2, item);
        }
        this.getPersistentData().putInt("ticks", ticks);
        this.setChanged();
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }

    public void setAndDestroyPedestals(BlockEntity tile, BlockEntity tile2, ItemStack item){
        manageItem(item, getLevel(), worldPosition, false);
        ((DarkinThingyTileEntity)tile).manageItem(ItemStack.EMPTY, getLevel(), tile.getBlockPos(), false);
        ((DarkinThingyTileEntity)tile2).manageItem(ItemStack.EMPTY, getLevel(), tile2.getBlockPos(), false);
        getLevel().destroyBlock(tile.getBlockPos(), false);
        getLevel().destroyBlock(tile2.getBlockPos(), false);
    }

    public void manageItem(ItemStack stack, Level worldIn, BlockPos pos, boolean drop) {
        if(drop) worldIn.addFreshEntity(new ItemEntity(worldIn, pos.getX(), pos.getY()+1, pos.getZ(), itemHandler.getStackInSlot(0)));
        if(stack.isEmpty()) {
            itemHandler.setStackInSlot(0, ItemStack.EMPTY);
        }else{
            int i = stack.getCount();
            stack.shrink(i-1);
            itemHandler.setStackInSlot(0, stack);
        }
    }

    @Override
    public void load(CompoundTag compound) {
        itemHandler.deserializeNBT(compound.getCompound("inv"));
        super.load(compound);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.put("inv", itemHandler.serializeNBT());
        super.saveAdditional(compound);
    }
}