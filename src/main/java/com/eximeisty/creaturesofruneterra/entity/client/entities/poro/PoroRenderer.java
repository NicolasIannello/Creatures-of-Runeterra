package com.eximeisty.creaturesofruneterra.entity.client.entities.poro;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.PoroEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PoroRenderer extends GeoEntityRenderer<PoroEntity> {
    private ResourceLocation rl=new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/poro.png");

    public PoroRenderer(EntityRendererManager renderManager) {
        super(renderManager, new PoroModel());
    }

    @Override
    public ResourceLocation getEntityTexture(PoroEntity entity) {
        return rl;
    }

    // @Override
	// public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
	// 	if (bone.getName().equals("itemBone")){
	// 		stack.push();
	// 		stack.rotate(Vector3f.XP.rotationDegrees(-75));
	// 		stack.rotate(Vector3f.YP.rotationDegrees(0));
	// 		stack.rotate(Vector3f.ZP.rotationDegrees(0));
	// 		stack.translate(0.2D, 0.4D, 0D);
	// 		stack.scale(1.0f, 1.0f, 1.0f);
	// 		Minecraft.getInstance().getItemRenderer().renderItem(poroWeapon, TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb);
	// 		stack.pop();
	// 		bufferIn = rtb.getBuffer(RenderType.getEntityTranslucent(whTexture));
	// 	}
	// 	super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	// }
}