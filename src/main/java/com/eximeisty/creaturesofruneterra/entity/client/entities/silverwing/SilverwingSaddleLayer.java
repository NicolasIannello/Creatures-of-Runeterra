package com.eximeisty.creaturesofruneterra.entity.client.entities.silverwing;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.SilverwingEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class SilverwingSaddleLayer extends GeoLayerRenderer<SilverwingEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/silverwing_saddle.png");
    private static final ResourceLocation MODEL = new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/silverwing.geo.json");

    public SilverwingSaddleLayer(IGeoRenderer entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, SilverwingEntity animatable, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(animatable.isHorseSaddled()){
            RenderType armorRenderType = RenderType.getEntityCutout(TEXTURE);
            float size = animatable.getDataManager().get(SilverwingEntity.SIZE);
            matrixStackIn.push();
            matrixStackIn.scale(4F/size, 4F/size, 4F/size);
            this.getRenderer().render(this.getEntityModel().getModel(MODEL), animatable, partialTicks, armorRenderType, matrixStackIn, bufferIn, bufferIn.getBuffer(armorRenderType), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
            matrixStackIn.pop();
        }
    }
}
