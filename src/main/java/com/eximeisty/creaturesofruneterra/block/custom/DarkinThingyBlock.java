package com.eximeisty.creaturesofruneterra.block.custom;

import com.eximeisty.creaturesofruneterra.block.ModTiles;
import com.eximeisty.creaturesofruneterra.block.entity.DarkinThingyTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class DarkinThingyBlock extends DirectionalBlock implements EntityBlock {

	public DarkinThingyBlock(Properties builder) {
        super(builder);
    }

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return ModTiles.DARKIN_PEDESTAL.get().create(blockPos, blockState);
	}

    @Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState();
	}

	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult pHit) {
		BlockEntity tileentity = worldIn.getBlockEntity(pos);
		if (!worldIn.isClientSide && tileentity instanceof DarkinThingyTileEntity) {
			ItemStack itemstack = player.getItemInHand(hand).copy();
			if(!player.isCreative()) player.getItemInHand(hand).shrink(1);
			((DarkinThingyTileEntity)tileentity).manageItem(itemstack, worldIn, pos, true);
		}
		return InteractionResult.CONSUME;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide() ? null : (level1, pos, state1, t) -> {
			if (t instanceof DarkinThingyTileEntity tile) {
				tile.tick();
			}
		};
	}
}