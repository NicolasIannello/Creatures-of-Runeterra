package com.eximeisty.creaturesofruneterra.item.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TendrilCompassPropertyGetter implements ItemPropertyFunction {

	@OnlyIn( Dist.CLIENT )
	@Override
	public float call(ItemStack stack, @Nullable ClientLevel clientWorld, @Nullable LivingEntity livingEntity, int pSeed){

		if( livingEntity == null && !stack.isFramed() ) {
			return 0.0F;
		} else {
			boolean isLivingEntityNotNull = livingEntity != null;
			Entity entity = Objects.requireNonNull( isLivingEntityNotNull ? livingEntity : stack.getFrame() );
			Level world = clientWorld;
			if( world == null ) {
				world = entity.getLevel();
			}
			double angel;
			if(TendrilCompassItemStackHelper.isDimensionEqual( stack, world, livingEntity )){
				BlockPos objetivo=TendrilCompassItemStackHelper.findObj(world, livingEntity);
				if(objetivo!=null) {
					double rotation = isLivingEntityNotNull ? entity.getYRot() : getFrameRotation( (ItemFrame)entity );
					rotation = Mth.positiveModulo( rotation / 360.0D, 1.0D );
					double d2 = StrictMath.atan2( objetivo.getZ() - entity.getZ(), objetivo.getX() - entity.getX() ) / ( (float)Math.PI * 2.0F );
					angel = 0.5D - ( rotation - 0.25D - d2 );
					if( isLivingEntityNotNull ) {
						angel = wobble( stack, world, angel );
					}
					return Mth.positiveModulo( (float)angel, 1.0F );
				} else {
					return 2;
				}
			} else {
				return 2;
			}
		}
	}

	@OnlyIn( Dist.CLIENT )
	private double wobble( ItemStack stack, Level worldIn, double angel ) {
		double rotation = TendrilCompassItemStackHelper.getRotation( stack );

		if( worldIn.getGameTime() != TendrilCompassItemStackHelper.getLastUpdateTick( stack ) ) {
			double rota = TendrilCompassItemStackHelper.getRota( stack );
			double d0 = angel - rotation;
			d0 = Mth.positiveModulo( d0 + 0.5D, 1.0D ) - 0.5D;
			rota += d0 * 0.1D;
			rota *= 0.8D;
			rotation = Mth.positiveModulo( rotation + rota, 1.0D );
			TendrilCompassItemStackHelper.setRotationRotaAndLastUpdateTick( stack, rotation, rota, worldIn.getGameTime() );
		}
		return rotation;
	}

	@OnlyIn( Dist.CLIENT )
	private double getFrameRotation( ItemFrame itemFrameEntity ) {
        return Mth.wrapDegrees( 180 + itemFrameEntity.getYRot() * 90 );
	}

	@OnlyIn( Dist.CLIENT )
	private double getSpawnToAngle( ItemStack stack, Entity entity, BlockPos blockpos ) {
		return StrictMath.atan2( blockpos.getZ() - entity.getZ(), blockpos.getX() - entity.getX() );
	}
}