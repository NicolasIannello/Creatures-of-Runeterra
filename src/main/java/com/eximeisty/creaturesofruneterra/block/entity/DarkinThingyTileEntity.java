package com.eximeisty.creaturesofruneterra.block.entity;

import com.eximeisty.creaturesofruneterra.block.ModTiles;
import com.eximeisty.creaturesofruneterra.item.ModItemTier;
import com.eximeisty.creaturesofruneterra.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.Map;

public class DarkinThingyTileEntity extends TileEntity implements IAnimatable, ITickableTileEntity {
    private final AnimationFactory factory = new AnimationFactory(this);
//    public final ItemStackHandler itemHandler = new ItemStackHandler(1){
//        @Override
//        public int getSlotLimit(int slot) {
//            switch (slot) {
//                case 0: return 1;
//                default: return super.getSlotLimit(slot);
//            }
//        }
//
//        @Override
//        protected void onContentsChanged(int slot){
//            markDirty();
//            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 2);
//        }
//    };
    public ItemStack itemTile = ItemStack.EMPTY;
    private boolean offeringCheck1 = false;
    private boolean offeringCheck2 = false;
    public int ticks = 0;

    public DarkinThingyTileEntity() {
        super(ModTiles.DARKIN_PEDESTAL.get());
    }

    private <E extends TileEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<DarkinThingyTileEntity>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        writeItem(nbt);
        return new SUpdateTileEntityPacket(this.pos, -1, nbt);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
//        super.onDataPacket(net, pkt);
//        handleUpdateTag(this.world.getBlockState(this.getPos()), pkt.getNbtCompound());
        readItem(pkt.getNbtCompound());
    }

    @Override
    public void tick() {
        ItemStack item = itemTile;
        if(world.getDimensionKey()==World.THE_NETHER){
            if (item.isItemEqual(new ItemStack(Items.IRON_HOE))) {
                recipe(Items.NETHER_WART, Items.ENDER_EYE, new ItemStack(ModItems.RHAAST.get()));
            }else if (item.isItemEqual(new ItemStack(Items.WOODEN_SWORD))) {
                recipe(Items.BONE_BLOCK, Items.WITHER_SKELETON_SKULL, new ItemStack(ModItems.NAAFIRI.get()));
            }else if (item.getItem() instanceof SwordItem && ((SwordItem)item.getItem()).getTier()==ModItemTier.DARKIN) {
                upgrade(item, new ItemStack(item.getItem()));
            }
        }
    }

    public void recipe(Item offering1, Item offering2, ItemStack result){
        TileEntity tileentity;
        TileEntity tileentity2;
        if(world.getTileEntity(pos.east(3))!=null){
            tileentity = world.getTileEntity(pos.east(3));
            tileentity2 = world.getTileEntity(pos.west(3));
            this.getTileData().putBoolean("ns", false);
        }else{
            tileentity = world.getTileEntity(pos.north(3));
            tileentity2 = world.getTileEntity(pos.south(3));
            this.getTileData().putBoolean("ns", true);
        }
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 2);
        if(tileentity instanceof DarkinThingyTileEntity && tileentity2 instanceof DarkinThingyTileEntity) {
            if(((DarkinThingyTileEntity) tileentity).itemTile.isItemEqual(new ItemStack(offering1))) offeringCheck1 = true;
            if(((DarkinThingyTileEntity) tileentity2).itemTile.isItemEqual(new ItemStack(offering1))) offeringCheck1 = true;
            if(((DarkinThingyTileEntity) tileentity).itemTile.isItemEqual(new ItemStack(offering2))) offeringCheck2 = true;
            if(((DarkinThingyTileEntity) tileentity2).itemTile.isItemEqual(new ItemStack(offering2))) offeringCheck2 = true;
            if(offeringCheck1 && offeringCheck2) {
                offeringCheck1 = false; offeringCheck2 = false;
                ticker(null, null, null);
                if(ticks > 100) ticker(tileentity, tileentity2, result);
            }
        }
    }

    public void upgrade(ItemStack item, ItemStack result){
        TileEntity tileentity;
        TileEntity tileentity2;
        if (world.getTileEntity(pos.east(3)) != null) {
            tileentity = world.getTileEntity(pos.east(3));
            tileentity2 = world.getTileEntity(pos.west(3));
            this.getTileData().putBoolean("ns", false);
        } else {
            tileentity = world.getTileEntity(pos.north(3));
            tileentity2 = world.getTileEntity(pos.south(3));
            this.getTileData().putBoolean("ns", true);
        }
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 2);
        if (tileentity instanceof DarkinThingyTileEntity && tileentity2 instanceof DarkinThingyTileEntity) {
            ItemStack tileItem = ((DarkinThingyTileEntity) tileentity).itemTile;
            ItemStack tileItem2 = ((DarkinThingyTileEntity) tileentity2).itemTile;
            if (tileItem.isItemEqual(new ItemStack(tileItem2.getItem())) && tileItem2.getItem() instanceof SwordItem) {
                tileItem.getAttributeModifiers(EquipmentSlotType.MAINHAND).forEach((atr, modifier) ->{
                    if (atr == Attributes.ATTACK_DAMAGE) {
                        item.getAttributeModifiers(EquipmentSlotType.MAINHAND).forEach((atr2, modifier2) ->{
                            if (atr2 == Attributes.ATTACK_DAMAGE) {
                                if(modifier.getAmount()>modifier2.getAmount()){
                                    ticker(null, null, null);
                                    if (ticks > 100) {
                                        ItemStack darkinweapon = result;
                                        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(item);
                                        EnchantmentHelper.setEnchantments(map,darkinweapon);
                                        darkinweapon.getAttributeModifiers(EquipmentSlotType.MAINHAND).forEach((d_atr, d_modifier) -> {
                                            if (d_atr == Attributes.ATTACK_DAMAGE) {
                                                darkinweapon.addAttributeModifier(d_atr, new AttributeModifier(d_modifier.getID(), d_modifier.getName(), modifier.getAmount()+1, d_modifier.getOperation()), EquipmentSlotType.MAINHAND);
                                            } else {
                                                darkinweapon.addAttributeModifier(d_atr, d_modifier, EquipmentSlotType.MAINHAND);
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

    public void ticker(@Nullable TileEntity tile, @Nullable TileEntity tile2, @Nullable ItemStack item){
        ticks++;
        if(ticks % 20 == 0) world.playSound(null, pos, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.BLOCKS, 1F, 1F);
        if(item!=null && tile!=null && tile2!=null){
            ticks=0;
            setAndDestroyPedestals(tile, tile2, item);
        }
        this.getTileData().putInt("ticks", ticks);
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 2);
    }

    public void setAndDestroyPedestals(TileEntity tile, TileEntity tile2, ItemStack item){
        manageItem(item, world, pos, false);
        ((DarkinThingyTileEntity)tile).manageItem(ItemStack.EMPTY, world, tile.getPos(), false);
        ((DarkinThingyTileEntity)tile2).manageItem(ItemStack.EMPTY, world, tile2.getPos(), false);
        int chance=(int)(Math.random() * 5);
        if(chance==0 || chance==4) world.destroyBlock(tile.getPos(), false);
        if(chance==1 || chance==4) world.destroyBlock(tile2.getPos(), false);
    }

    public void manageItem(ItemStack stack, World worldIn, BlockPos pos, boolean drop) {
        if(drop) worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY()+1, pos.getZ(), itemTile));
        if(stack.isEmpty()) {
            itemTile = ItemStack.EMPTY;
        }else{
            int i = stack.getCount();
            stack.shrink(i-1);
            itemTile = stack;
        }
        markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 2);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        //itemHandler.deserializeNBT(nbt.getCompound("inv"));
        super.read(state, nbt);
        readItem(nbt);
    }

    private void readItem(CompoundNBT nbt){
        itemTile = ItemStack.read(nbt.getCompound("Item"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        //compound.put("inv", itemHandler.serializeNBT());
        super.write(compound);
        writeItem(compound);
        return compound;
    }

    private void writeItem(CompoundNBT nbt){
        nbt.put("Item", itemTile.write(new CompoundNBT()));
    }
}