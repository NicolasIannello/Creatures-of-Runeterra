package com.eximeisty.creaturesofruneterra.entity.client.entities.dbshield;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.DBShieldEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class DBShieldRenderer extends GeoProjectilesRenderer<DBShieldEntity> {

    public DBShieldRenderer(EntityRendererManager renderManager) {
        super(renderManager, new DBShieldModel());
    }

    @Override
    public ResourceLocation getEntityTexture(DBShieldEntity entity) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/dbshield.png");
    }

    @Override
	public void renderEarly(DBShieldEntity animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        stackIn.scale(2.5F, 2.5F, 2.5F);
        stackIn.translate(0F, -0.5F, 0F);
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);    
    }
}