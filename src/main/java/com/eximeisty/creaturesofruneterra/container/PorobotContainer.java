package com.eximeisty.creaturesofruneterra.container;

import java.util.Optional;

import com.eximeisty.creaturesofruneterra.entity.custom.PatchedPorobotEntity;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PorobotContainer extends RecipeBookMenu<CraftingContainer> {
    public final PatchedPorobotEntity poro;
    private final Player playerEntity;
    private final IItemHandler playerInventory;
    private final CraftingContainer craftMatrix = new CraftingContainer(this, 3, 3);
    private final ResultContainer craftResult = new ResultContainer();
    private final ContainerLevelAccess worldPosCallable;

    public PorobotContainer(int windowId, Level world, int id, Inventory playerInventory, Player player) {
        super(ModContainers.POROBOT_CONTAINER.get(), windowId);

        this.poro=(PatchedPorobotEntity)world.getEntity(id);
        this.worldPosCallable = ContainerLevelAccess.create(world, poro.blockPosition());
        this.playerEntity= player;
        this.playerInventory= new InvWrapper(playerInventory);
        layoutPlayerInventorySlots(8, 86);

        if(poro!=null){
            poro.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h ->{
                int x=0, y=0, index=0;
                for (int j = 0; j < 3; j++) {
                    for (int i = 0; i < 5; i++) {
                        addSlot(new SlotItemHandler(h, i+index, 53+x, 17+y));
                        x+=18;
                    }
                    x=0; y+=18; index+=5;
                }
                y=0;
                addSlot(new SlotItemHandler(h, index, 17, 17));
                for (int j = 0; j < 2; j++) {
                    for (int i = 0; i < 2; i++) {
                        addSlot(new SlotItemHandler(h, i+index+1, 8+x, 35+y));
                        x+=18;
                    }
                    x=0; y+=18; index+=2;
                }
                y=0; index++; 
                addSlot(new SlotItemHandler(h, index, 152, 35));
                index++;
                addSlot(new ResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, index, 197, 133){
                    @OnlyIn(Dist.CLIENT)
                    public boolean isEnabled() {
                        return getSlot(20+9+3*9).getItem().getItem()== Items.CRAFTING_TABLE;
                    }
                });
                for(int i = 0; i < 3; ++i) {
                    for(int j = 0; j < 3; ++j) {
                        addSlot(new Slot(this.craftMatrix, j + i * 3, 179+x, 17+y){
                            @OnlyIn(Dist.CLIENT)
                            public boolean isEnabled() {
                                return getSlot(20+9+3*9).getItem().getItem()==Items.CRAFTING_TABLE;
                            }
                        });
                        x+=18;
                    }
                    x=0; y+=18; //index+=3;
                }
                this.addSlot(new SlotItemHandler(h, index+1, 179+18, 17){
                    @OnlyIn(Dist.CLIENT)
                    public boolean isEnabled() {
                        return getSlot(20+9+3*9).getItem().getItem()==Items.FURNACE;
                    }
                });
                this.addSlot(new SlotItemHandler(h, index+2, 179+18, 17+18*2){
                    @OnlyIn(Dist.CLIENT)
                    public boolean isEnabled() {
                        return getSlot(20+9+3*9).getItem().getItem()==Items.FURNACE;
                    }
                });
                this.addSlot(new SlotItemHandler(h, index+3, 197, 133){
                    @OnlyIn(Dist.CLIENT)
                    public boolean isEnabled() {
                        return getSlot(20+9+3*9).getItem().getItem()==Items.FURNACE;
                    }
                });
            });
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return playerEntity.distanceTo(poro)<=3;
    }

    @Override
    public void removed(Player playerIn) {
        Inventory playerinventory = playerIn.getInventory();
        if (!playerinventory.getSelected().isEmpty()) {
           playerIn.drop(playerinventory.getSelected(), false);
           playerinventory.setPickedItem(ItemStack.EMPTY);
        }
//        this.worldPosCallable.execute((p_217068_2_, p_217068_3_) -> {
//            this.clearContainer(playerIn, this);
//        });
        poro.playersUsing--;
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }
    
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = 34;

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerEntity, sourceStack);
        return copyOfSourceStack;
    }

    protected static void updateCraftingResult(AbstractContainerMenu p_150547_, Level world, Player player, CraftingContainer inventory, ResultContainer inventoryResult) {
        if (!world.isClientSide) {
           ServerPlayer serverplayerentity = (ServerPlayer)player;
           ItemStack itemstack = ItemStack.EMPTY;
            Optional<CraftingRecipe> optional = world.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, inventory, world);
            if (optional.isPresent()) {
                CraftingRecipe icraftingrecipe = optional.get();
                if (inventoryResult.setRecipeUsed(world, serverplayerentity, icraftingrecipe)) {
                    itemstack = icraftingrecipe.assemble(inventory);
                }
            }
  
            inventoryResult.setItem(21+9+3*9, itemstack);
            serverplayerentity.connection.send(new ClientboundContainerSetSlotPacket(p_150547_.containerId, p_150547_.incrementStateId() ,21+9+3*9, itemstack));
        }
    }

//    public void onCraftMatrixChanged(IInventory inventoryIn) {
//        this.worldPosCallable.consume((p_217069_1_, p_217069_2_) -> {
//           updateCraftingResult(this.windowId, p_217069_1_, this.playerEntity, this.craftMatrix, this.craftResult);
//        });
//    }

    public void slotsChanged(Container pInventory) {
        this.worldPosCallable.execute((p_39386_, p_39387_) -> {
            updateCraftingResult(this, p_39386_, this.playerEntity, this.craftMatrix, this.craftResult);
        });
    }
    
//    public void fillStackedContents(RecipeItemHelper itemHelperIn) {
//        this.craftMatrix.fillStackedContents(itemHelperIn);
//    }

//    public void clear() {
//        this.craftMatrix.clear();
//        this.craftResult.clear();
//    }
    
//    public boolean matches(IRecipe<? super CraftingInventory> recipeIn) {
//        return recipeIn.matches(this.craftMatrix, this.playerEntity.world);
//    }

//    public int getOutputSlot() {
//        return 21+9+3*9;
//    }

//    public int getWidth() {
//        return this.craftMatrix.getWidth();
//    }
//
//    public int getHeight() {
//        return this.craftMatrix.getHeight();
//    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents pItemHelper) {
        this.craftMatrix.fillStackedContents(pItemHelper);

    }

    @Override
    public void clearCraftingContent() {
        this.craftMatrix.clearContent();
        this.craftResult.clearContent();

    }

    @Override
    public boolean recipeMatches(Recipe<? super CraftingContainer> pRecipe) {
        return pRecipe.matches(this.craftMatrix, this.playerEntity.level);
    }

    @Override
    public int getResultSlotIndex() {
        return 21+9+3*9;
    }

    @Override
    public int getGridWidth() {
        return this.craftMatrix.getWidth();
    }

    @Override
    public int getGridHeight() {
        return this.craftMatrix.getHeight();
    }

    @OnlyIn(Dist.CLIENT)
    public int getSize() {
        return 34;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    @Override
    public boolean shouldMoveToInventory(int p_150635_) {
        return false;
    }

//    @OnlyIn(Dist.CLIENT)
//    public RecipeBookCategory func_241850_m() {
//        return RecipeBookCategory.CRAFTING;
//    }
}