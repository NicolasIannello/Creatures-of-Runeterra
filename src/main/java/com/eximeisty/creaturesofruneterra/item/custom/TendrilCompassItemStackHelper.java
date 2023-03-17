package com.eximeisty.creaturesofruneterra.item.custom;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class TendrilCompassItemStackHelper {
	
	private static final String destinationName = "destination";
	private static final String dimensionName = "destination_dimension";
	private static final String posName = "destination_pos";
	private static final String directionDataName = "directionData";
	private static final String rotationName = "rotation";
	private static final String rotaName = "rota";
	private static final String lastUpdateTickName = "last_update_tick";
	
	public static void setDimensionAndPos( ItemStack stack, World world, BlockPos pos ) {
		CompoundNBT compound = new CompoundNBT();
		compound.putString( dimensionName, Objects.requireNonNull(world.getDimensionType().toString()));
		compound.put( posName, NBTUtil.writeBlockPos(stack.getAttachedEntity().getPosition()) );
		stack.getOrCreateTag().put( destinationName, compound );
	}
	
	static boolean isDimensionEqual( ItemStack stack, World world, LivingEntity entity ) {
		return world.getDimensionKey().toString().contains("nether");
	}

	static BlockPos findObj(World world, LivingEntity entity){
		BlockPos obj=null;
		for (int i = (int)entity.getPosX(); i < (int)entity.getPosX()+10; i++) {
			if( world.getBlockState(new BlockPos(i, entity.getPosY(), entity.getPosZ()))==Blocks.ANCIENT_DEBRIS.getDefaultState()){
				obj=new BlockPos(i, entity.getPosY(), entity.getPosZ());
			}
		}
		return obj;
	}
	
	static void setRotationRotaAndLastUpdateTick( ItemStack stack, double rotation, double rota, long lastUpdateTick ) {
		CompoundNBT compound = new CompoundNBT();
		compound.putDouble( rotationName, rotation );
		compound.putDouble( rotaName, rota );
		compound.putLong( lastUpdateTickName, lastUpdateTick );
		stack.getOrCreateTag().put( directionDataName, compound );
	}
	
	static double getRotation( ItemStack stack ) {
		return stack.getOrCreateTag().getCompound( directionDataName ).getDouble( rotationName );
	}
	
	static double getRota( ItemStack stack ) {
		return stack.getOrCreateTag().getCompound( directionDataName ).getDouble( rotaName );
	}
	
	static long getLastUpdateTick( ItemStack stack ) {
		return stack.getOrCreateTag().getCompound( directionDataName ).getLong( lastUpdateTickName );
	}
}