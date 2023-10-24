package com.eximeisty.creaturesofruneterra.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

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
	private static int range=6;
	
	public static void setDimensionAndPos(ItemStack stack, Level world, BlockPos pos ) {
		CompoundTag compound = new CompoundTag();
		compound.putString( dimensionName, Objects.requireNonNull(world.dimensionType().toString()));
		compound.put( posName, NbtUtils.writeBlockPos(stack.getEntityRepresentation().getOnPos()) );
		stack.getOrCreateTag().put( destinationName, compound );
	}
	
	static boolean isDimensionEqual( ItemStack stack, Level world, LivingEntity entity ) {
		return world.dimension().toString().contains("nether");
	}

	static BlockPos findObj(Level world, LivingEntity entity){
		if(obj!=null) if(world.getBlockState(obj)!= Blocks.ANCIENT_DEBRIS.defaultBlockState() || entity.distanceToSqr(obj.getX(), obj.getY(), obj.getZ())>100) obj=null;

		int X=entity.getBlockX(), Y=entity.getBlockY(), Z=entity.getBlockZ();
		for(int i = Y+range; i>=Y+range-3; i--){
			for(int j = X+6; j>=X-6; j--){
				for(int n = Z+6; n>=Z-6; n--){
					if( world.getBlockState(new BlockPos(j, i, n))==Blocks.ANCIENT_DEBRIS.defaultBlockState()){
						if(obj==null){
							obj=new BlockPos(j, i, n);
						}else if(entity.distanceToSqr(j, i, n)<entity.distanceToSqr(obj.getX(), obj.getY(), obj.getZ())) {
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
		CompoundTag compound = new CompoundTag();
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