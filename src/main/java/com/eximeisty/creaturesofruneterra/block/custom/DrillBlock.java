package com.eximeisty.creaturesofruneterra.block.custom;


import java.util.List;

import javax.annotation.Nullable;

import com.eximeisty.creaturesofruneterra.block.ModTiles;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.util.ITooltipFlag;


public class DrillBlock extends DirectionalBlock{

    public DrillBlock(Properties builder) {
        super(builder);
    }
    
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

    @Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return  ModTiles.DRILL.get().create();
	}

    @Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState();
	}
    
	@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
		tooltip.add(new TranslationTextComponent("Might angry something"));
	}
}