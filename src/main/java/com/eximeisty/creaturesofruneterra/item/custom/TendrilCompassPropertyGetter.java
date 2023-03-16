package com.eximeisty.creaturesofruneterra.item.custom;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.security.SecureRandom;
import java.util.Objects;


public class TendrilCompassPropertyGetter implements IItemPropertyGetter {
	
	@OnlyIn( Dist.CLIENT )
	@Override
	public float call(
		@Nonnull ItemStack stack,
		@Nullable ClientWorld clientWorld,
		@Nullable LivingEntity livingEntity ) {
		
		if( livingEntity == null && !stack.isOnItemFrame() ) {
			return 0.0F;
		} else {
			boolean isLivingEntityNotNull = livingEntity != null;
			Entity entity = Objects.requireNonNull( isLivingEntityNotNull ? livingEntity : stack.getItemFrame() );
			World world = clientWorld;
			if( world == null ) {
				world = entity.getEntityWorld();
			}
			double angel;
			if( TendrilCompassItemStackHelper.isDimensionEqual( stack, world, livingEntity ) ) {
				double rotation = isLivingEntityNotNull ? entity.rotationYaw /*yRot*/ : getFrameRotation( (ItemFrameEntity)entity );
				rotation = MathHelper.positiveModulo( rotation / 360.0D, 1.0D );
				double d2 = getSpawnToAngle( stack, entity ) / ( (float)Math.PI * 2.0F );
				angel = 0.5D - ( rotation - 0.25D - d2 );
			} else {
				angel = new SecureRandom().nextDouble();
			}
			if( isLivingEntityNotNull ) {
				angel = wobble( stack, world, angel );
			}
			return MathHelper.positiveModulo( (float)angel, 1.0F );
		}
	}
	
	@OnlyIn( Dist.CLIENT )
	private double wobble( ItemStack stack, World worldIn, double angel ) {
		double rotation = TendrilCompassItemStackHelper.getRotation( stack );
		
		if( worldIn.getGameTime() != TendrilCompassItemStackHelper.getLastUpdateTick( stack ) ) {
			double rota = TendrilCompassItemStackHelper.getRota( stack );
			double d0 = angel - rotation;
			d0 = MathHelper.positiveModulo( d0 + 0.5D, 1.0D ) - 0.5D;
			rota += d0 * 0.1D;
			rota *= 0.8D;
			rotation = MathHelper.positiveModulo( rotation + rota, 1.0D );
			TendrilCompassItemStackHelper.setRotationRotaAndLastUpdateTick( stack, rotation, rota, worldIn.getGameTime() );
		}
		return rotation;
	}
	
	@OnlyIn( Dist.CLIENT )
	private double getFrameRotation( ItemFrameEntity itemFrameEntity ) {
		//return MathHelper.wrapDegrees( 180 + itemFrameEntity.getDirection().get2DDataValue() * 90 );
        return MathHelper.wrapDegrees( 180 + itemFrameEntity.rotationYaw * 90 );
	}
	
	@OnlyIn( Dist.CLIENT )
	private double getSpawnToAngle( ItemStack stack, Entity entity ) {
		BlockPos blockpos = TendrilCompassItemStackHelper.getDestinationPos( stack );
		return StrictMath.atan2( blockpos.getZ() - entity.getPosZ(), blockpos.getX() - entity.getPosX() );
	}
}