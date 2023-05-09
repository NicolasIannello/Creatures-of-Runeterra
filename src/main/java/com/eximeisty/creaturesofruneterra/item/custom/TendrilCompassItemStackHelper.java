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
	private static BlockPos obj=null;
	private static double range=6;
	
	public static void setDimensionAndPos( ItemStack stack, World world, BlockPos pos ) {
		CompoundNBT compound = new CompoundNBT();
		compound.putString( dimensionName, Objects.requireNonNull(world.getDimensionType().toString()));
		compound.put( posName, NBTUtil.writeBlockPos(stack.getAttachedEntity().getPosition()) );
		stack.getOrCreateTag().put( destinationName, compound );
	}
	
	static boolean isDimensionEqual( ItemStack stack, World world, LivingEntity entity ) {
		return world.getDimensionKey().toString().contains("nether");
	}

	// static BlockPos findObj(World world, LivingEntity entity){
	// 	BlockPos obj=null;
	// 	double lastDistance=1000, X=entity.getPosX(), Y=entity.getPosY(), Z=entity.getPosZ();
	// 	for(double i = Y+6; i>=Y-6; i--){
	// 		for(double j = X+6; j>=X-6; j--){
	// 			for(double n = Z+6; n>=Z-6; n--){
	// 				if( world.getBlockState(new BlockPos(j, i, n))==Blocks.ANCIENT_DEBRIS.getDefaultState() && entity.getDistanceSq(j, i, n)<lastDistance){
	// 					obj=new BlockPos(j, i, n);
	// 					lastDistance=entity.getDistanceSq(j, i, n);
	// 				}
	// 			}
	// 		}
	// 	}
	// 	return obj;
	// }

	static BlockPos findObj(World world, LivingEntity entity){
		if(obj!=null) if(world.getBlockState(obj)!=Blocks.ANCIENT_DEBRIS.getDefaultState() || entity.getDistanceSq(obj.getX(), obj.getY(), obj.getZ())>100) obj=null;

		double X=entity.getPosX(), Y=entity.getPosY(), Z=entity.getPosZ();
		for(double i = Y+range; i>=Y+range-3; i--){
			for(double j = X+6; j>=X-6; j--){
				for(double n = Z+6; n>=Z-6; n--){
					if( world.getBlockState(new BlockPos(j, i, n))==Blocks.ANCIENT_DEBRIS.getDefaultState()){
						if(obj==null){
							obj=new BlockPos(j, i, n);
						}else if(entity.getDistanceSq(j, i, n)<entity.getDistanceSq(obj.getX(), obj.getY(), obj.getZ())) {
							obj=new BlockPos(j, i, n);
						}
					}
				}
			}
		}
		range+=-3;
		if(range<-6) range=6;

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