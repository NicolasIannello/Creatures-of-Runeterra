package com.eximeisty.creaturesofruneterra.container;

import java.util.Optional;

import com.eximeisty.creaturesofruneterra.entity.custom.PatchedPorobotEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
//import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PorobotContainer extends RecipeBookContainer<CraftingInventory> {
    private final PatchedPorobotEntity poro;
    private final PlayerEntity playerEntity;
    private final IItemHandler playerInventory;
    private final CraftingInventory craftMatrix = new CraftingInventory(this, 3, 3);
    private final CraftResultInventory craftResult = new CraftResultInventory();
    private final IWorldPosCallable worldPosCallable;

    public PorobotContainer(int windowId, World world, int id, PlayerInventory playerInventory, PlayerEntity player) {
        super(ModContainers.POROBOT_CONTAINER.get(), windowId);

        this.poro=(PatchedPorobotEntity)world.getEntityByID(id);
        this.worldPosCallable = IWorldPosCallable.of(world, poro.getPosition());
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
                addSlot(new CraftingResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, index, 197, 133){
                    @OnlyIn(Dist.CLIENT)
                    public boolean isEnabled() {
                        return getSlot(20+9+3*9).getStack().getItem()==Items.CRAFTING_TABLE;
                    }
                });
                for(int i = 0; i < 3; ++i) {
                    for(int j = 0; j < 3; ++j) {
                        addSlot(new Slot(this.craftMatrix, j + i * 3, 179+x, 17+y){
                            @OnlyIn(Dist.CLIENT)
                            public boolean isEnabled() {
                                return getSlot(20+9+3*9).getStack().getItem()==Items.CRAFTING_TABLE;
                            }
                        });
                        x+=18;
                    }
                    x=0; y+=18; index+=3;
                }
            });
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return playerEntity.getDistance(poro)<=3;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        PlayerInventory playerinventory = playerIn.inventory;
        if (!playerinventory.getItemStack().isEmpty()) {
           playerIn.dropItem(playerinventory.getItemStack(), false);
           playerinventory.setItemStack(ItemStack.EMPTY);
        }
        this.worldPosCallable.consume((p_217068_2_, p_217068_3_) -> {
            this.clearContainer(playerIn, p_217068_2_, this.craftMatrix);
        });
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
    private static final int TE_INVENTORY_SLOT_COUNT = 31;

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        Slot sourceSlot = inventorySlots.get(index);
        if (sourceSlot == null || !sourceSlot.getHasStack()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getStack();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!mergeItemStack(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (!mergeItemStack(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        if (sourceStack.getCount() == 0) {
            sourceSlot.putStack(ItemStack.EMPTY);
        } else {
            sourceSlot.onSlotChanged();
        }
        sourceSlot.onTake(playerEntity, sourceStack);
        return copyOfSourceStack;
    }

    protected static void updateCraftingResult(int id, World world, PlayerEntity player, CraftingInventory inventory, CraftResultInventory inventoryResult) {
        if (!world.isRemote) {
           ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)player;
           ItemStack itemstack = ItemStack.EMPTY;
           Optional<ICraftingRecipe> optional = world.getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, inventory, world);
            if (optional.isPresent()) {
                ICraftingRecipe icraftingrecipe = optional.get();
                if (inventoryResult.canUseRecipe(world, serverplayerentity, icraftingrecipe)) {
                    itemstack = icraftingrecipe.getCraftingResult(inventory);
                }
            }
  
            inventoryResult.setInventorySlotContents(21+9+3*9, itemstack);
            serverplayerentity.connection.sendPacket(new SSetSlotPacket(id, 21+9+3*9, itemstack));
        }
    }

    public void onCraftMatrixChanged(IInventory inventoryIn) {
        this.worldPosCallable.consume((p_217069_1_, p_217069_2_) -> {
           updateCraftingResult(this.windowId, p_217069_1_, this.playerEntity, this.craftMatrix, this.craftResult);
        });
    }
    
    public void fillStackedContents(RecipeItemHelper itemHelperIn) {
        this.craftMatrix.fillStackedContents(itemHelperIn);
    }

    public void clear() {
        this.craftMatrix.clear();
        this.craftResult.clear();
    }
    
    public boolean matches(IRecipe<? super CraftingInventory> recipeIn) {
        return recipeIn.matches(this.craftMatrix, this.playerEntity.world);
    }

    public int getOutputSlot() {
        return 21+9+3*9;
    }

    public int getWidth() {
        return this.craftMatrix.getWidth();
    }
    
    public int getHeight() {
        return this.craftMatrix.getHeight();
    }

    @OnlyIn(Dist.CLIENT)
    public int getSize() {
        return 10;
    }

    @OnlyIn(Dist.CLIENT)
    public RecipeBookCategory func_241850_m() {
        return RecipeBookCategory.CRAFTING;
    }
}