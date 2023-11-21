package com.eximeisty.creaturesofruneterra.block.custom;

import com.eximeisty.creaturesofruneterra.block.ModTiles;
import com.eximeisty.creaturesofruneterra.block.entity.DarkinThingyTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class DarkinThingyBlock extends DirectionalBlock {
	private float y = 1, x = 0;

	public DarkinThingyBlock(Properties builder) {
        super(builder);
    }

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModTiles.DARKIN_PEDESTAL.get().create();
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

	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (!worldIn.isRemote && tileentity instanceof DarkinThingyTileEntity) {
			ItemStack itemstack = player.getHeldItem(handIn).copy();
			if(!player.isCreative()) player.getHeldItem(handIn).shrink(1);
			((DarkinThingyTileEntity)tileentity).manageItem(itemstack, worldIn, pos, true);
			worldIn.notifyBlockUpdate(pos, state, state, 2);
		}
		return ActionResultType.SUCCESS;
	}

//	public void animateTick(BlockState stateIn, World world, BlockPos pos, Random rand) {
//		super.animateTick(stateIn, world, pos, rand);
//		int blockTick = world.getTileEntity(pos).getTileData().getInt("ticks");
//		if(blockTick%15==0 || blockTick==1){
//			y=1; x=0;
//		}else if(blockTick>0){
//			y += 0.1; x += 0.3;
//			if(world.getTileEntity(pos).getTileData().getBoolean("ns")){
//				for (float i = 0; i < 3 ; i+=0.5) {
//					world.addParticle(ParticleTypes.DRAGON_BREATH, pos.north(3).getX()+0.5, pos.north(3).getY()+y+(i/4), pos.north(3).getZ()+x+i, 0, 0, 0);
//					world.addParticle(ParticleTypes.DRAGON_BREATH, pos.south(3).getX()+0.5, pos.south(3).getY()+y+(i/4), pos.south(3).getZ()-x-i, 0, 0, 0);
//				}
//			}else{
//				for (float i = 0; i < 3 ; i+=0.5) {
//					world.addParticle(ParticleTypes.DRAGON_BREATH, pos.west(3).getX()+x+i, pos.west(3).getY()+y+(i/4), pos.west(3).getZ()+0.5, 0, 0, 0);
//					world.addParticle(ParticleTypes.DRAGON_BREATH, pos.east(3).getX()-x-i, pos.west(3).getY()+y+(i/4), pos.west(3).getZ()+0.5, 0, 0, 0);
//				}
//			}
//		}
//	}

	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY()+1, pos.getZ(), ((DarkinThingyTileEntity)worldIn.getTileEntity(pos)).itemTile));
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}

}