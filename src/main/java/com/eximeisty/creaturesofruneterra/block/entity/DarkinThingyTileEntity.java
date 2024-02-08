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
    private boolean offeringCheck1 = false;
    private boolean offeringCheck2 = false;
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
            if (item.is(Items.IRON_HOE)) {
                recipe(Items.NETHER_WART, Items.ENDER_EYE, new ItemStack(ModItems.RHAAST.get()));
            }else if (item.is(Items.WOODEN_SWORD)) {
                recipe(Items.BONE_BLOCK, Items.WITHER_SKELETON_SKULL, new ItemStack(ModItems.NAAFIRI.get()));
            }else if (item.getItem() instanceof SwordItem swordItem && swordItem.getTier()==ModItemTier.DARKIN) {
                upgrade(item, new ItemStack(item.getItem()));
            }
        }
    }

    public void recipe(Item offering1, Item offering2, ItemStack result){
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
            if(((DarkinThingyTileEntity) tileentity).itemHandler.getStackInSlot(0).is(offering1)) offeringCheck1 = true;
            if(((DarkinThingyTileEntity) tileentity2).itemHandler.getStackInSlot(0).is(offering1)) offeringCheck1 = true;
            if(((DarkinThingyTileEntity) tileentity).itemHandler.getStackInSlot(0).is(offering2)) offeringCheck2 = true;
            if(((DarkinThingyTileEntity) tileentity2).itemHandler.getStackInSlot(0).is(offering2)) offeringCheck2 = true;
            if(offeringCheck1 && offeringCheck2) {
                offeringCheck1 = false; offeringCheck2 = false;
                ticker(null, null, null);
                if(ticks > 100) ticker(tileentity, tileentity2, result);
            }
        }
    }

    public void upgrade(ItemStack item, ItemStack result){
        BlockEntity tileentity;
        BlockEntity tileentity2;
        if (getLevel().getBlockEntity(worldPosition.east(3)) != null) {
            tileentity = getLevel().getBlockEntity(worldPosition.east(3));
            tileentity2 = getLevel().getBlockEntity(worldPosition.west(3));
            this.getPersistentData().putBoolean("ns", false);
        } else {
            tileentity = getLevel().getBlockEntity(worldPosition.north(3));
            tileentity2 = getLevel().getBlockEntity(worldPosition.south(3));
            this.getPersistentData().putBoolean("ns", true);
        }
        this.setChanged();
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
        if (tileentity instanceof DarkinThingyTileEntity && tileentity2 instanceof DarkinThingyTileEntity) {
            ItemStack tileItem = ((DarkinThingyTileEntity) tileentity).itemHandler.getStackInSlot(0);
            ItemStack tileItem2 = ((DarkinThingyTileEntity) tileentity2).itemHandler.getStackInSlot(0);
            if (tileItem.is(tileItem2.getItem()) && tileItem2.getItem() instanceof SwordItem) {
                tileItem.getAttributeModifiers(EquipmentSlot.MAINHAND).forEach((atr, modifier) ->{
                    if (atr == Attributes.ATTACK_DAMAGE) {
                        item.getAttributeModifiers(EquipmentSlot.MAINHAND).forEach((atr2, modifier2) ->{
                            if (atr2 == Attributes.ATTACK_DAMAGE) {
                                if(modifier.getAmount()>modifier2.getAmount()){
                                    ticker(null, null, null);
                                    if (ticks > 100) {
                                        ItemStack darkinweapon = result;
                                        item.getAllEnchantments().forEach(darkinweapon::enchant);
                                        darkinweapon.getAttributeModifiers(EquipmentSlot.MAINHAND).forEach((d_atr, d_modifier) -> {
                                            if (d_atr == Attributes.ATTACK_DAMAGE) {
                                                darkinweapon.addAttributeModifier(d_atr, new AttributeModifier(d_modifier.getId(), d_modifier.getName(), modifier.getAmount()+1, d_modifier.getOperation()), EquipmentSlot.MAINHAND);
                                            } else {
                                                darkinweapon.addAttributeModifier(d_atr, d_modifier, EquipmentSlot.MAINHAND);
                                            }
                                        });
                                        ticker(tileentity, tileentity2, darkinweapon);
                                    }
                                }
                            }
                        });
                    }
                });
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
        int chance=(int)(Math.random() * 5);
        if(chance==0 || chance==4) getLevel().destroyBlock(tile.getBlockPos(), false);
        if(chance==1 || chance==4) getLevel().destroyBlock(tile2.getBlockPos(), false);
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