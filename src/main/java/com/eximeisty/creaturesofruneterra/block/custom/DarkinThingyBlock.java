package com.eximeisty.creaturesofruneterra.block.custom;

import com.eximeisty.creaturesofruneterra.block.ModTiles;
import com.eximeisty.creaturesofruneterra.block.entity.DarkinThingyTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
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
	private float y = 1, x = 0;

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

	public void animateTick(BlockState p_221789_, Level world, BlockPos pos, RandomSource p_221792_) {
		super.animateTick(p_221789_, world, pos, p_221792_);
		int blockTick = world.getBlockEntity(pos).getPersistentData().getInt("ticks");
		if(blockTick%15==0 || blockTick==1){
			y=1; x=0;
		}else if(blockTick>0){
			y += 0.1; x += 0.3;
			if(world.getBlockEntity(pos).getPersistentData().getBoolean("ns")){
				for (float i = 0; i < 3 ; i+=0.5) {
					world.addParticle(ParticleTypes.DRAGON_BREATH, pos.north(3).getX()+0.5, pos.north(3).getY()+y+(i/4), pos.north(3).getZ()+x+i, 0, 0, 0);
					world.addParticle(ParticleTypes.DRAGON_BREATH, pos.south(3).getX()+0.5, pos.south(3).getY()+y+(i/4), pos.south(3).getZ()-x-i, 0, 0, 0);
				}
			}else{
				for (float i = 0; i < 3 ; i+=0.5) {
					world.addParticle(ParticleTypes.DRAGON_BREATH, pos.west(3).getX()+x+i, pos.west(3).getY()+y+(i/4), pos.west(3).getZ()+0.5, 0, 0, 0);
					world.addParticle(ParticleTypes.DRAGON_BREATH, pos.east(3).getX()-x-i, pos.west(3).getY()+y+(i/4), pos.west(3).getZ()+0.5, 0, 0, 0);
				}
			}
		}
	}

	public void onRemove(BlockState p_51538_, Level p_51539_, BlockPos p_51540_, BlockState p_51541_, boolean p_51542_) {
		p_51539_.addFreshEntity(new ItemEntity(p_51539_, p_51540_.getX(), p_51540_.getY()+1, p_51540_.getZ(), ((DarkinThingyTileEntity)p_51539_.getBlockEntity(p_51540_)).itemHandler.getStackInSlot(0)));
		super.onRemove(p_51538_, p_51539_, p_51540_, p_51541_, p_51542_);
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