package com.eximeisty.creaturesofruneterra.block.entity.client.drill;

import com.eximeisty.creaturesofruneterra.block.entity.DrillTileEntity;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class DrillRenderer extends GeoBlockRenderer<DrillTileEntity> {

    public DrillRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn, new DrillModel());
    }

    /*@Override
	public RenderType getRenderType(DrillTileEntity animatable, float partialTicks, MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
		return RenderType.getEntityTranslucent(getTextureLocation(animatable));
	}*/
}