package com.eximeisty.creaturesofruneterra.item.client.drill;

import com.eximeisty.creaturesofruneterra.item.custom.DrillItem;
// import com.mojang.blaze3d.matrix.MatrixStack;
// import com.mojang.blaze3d.vertex.IVertexBuilder;

// import net.minecraft.client.renderer.IRenderTypeBuffer;
// import net.minecraft.client.renderer.RenderType;
// import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class DrillItemRenderer extends GeoItemRenderer<DrillItem> {

    public DrillItemRenderer() {
        super(new DrillItemModel());
    }

    /*@Override
	public RenderType getRenderType(DrillItem animatable, float partialTicks, MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
		return RenderType.getEntityTranslucent(getTextureLocation(animatable));
	}*/
}