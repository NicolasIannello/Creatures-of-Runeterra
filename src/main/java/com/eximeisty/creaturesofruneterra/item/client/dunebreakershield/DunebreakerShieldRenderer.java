package com.eximeisty.creaturesofruneterra.item.client.dunebreakershield;

import com.eximeisty.creaturesofruneterra.item.custom.DunebreakerShield;
import software.bernie.geckolib.renderer.GeoItemRenderer;


public class DunebreakerShieldRenderer extends GeoItemRenderer<DunebreakerShield> {

    public DunebreakerShieldRenderer() {
        super(new DunebreakerShieldModel());
    }

//    @Override @SuppressWarnings({"rawtypes","resource","unchecked"})
//    public void render(DunebreakerShield animatable, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn, ItemStack itemStack) {
//		this.currentItemStack = itemStack;
//		GeoModel model = modelProvider.getModel(modelProvider.getModelLocation(animatable));
//		AnimationEvent itemEvent = new AnimationEvent(animatable, 0, 0, Minecraft.getInstance().getRenderPartialTicks(),false, Collections.singletonList(itemStack));
//		modelProvider.setLivingAnimations(animatable, this.getUniqueID(animatable), itemEvent);
//		stack.push();
//		stack.translate(0, 0.01f, 0);
//		stack.translate(0.5, 0.5, 0.5);
//		Minecraft.getInstance().textureManager.bindTexture(getTextureLocation(animatable));
//		Color renderColor = getRenderColor(animatable, 0, stack, bufferIn, null, packedLightIn);
//        getRenderType(animatable, 0, stack, bufferIn, null, packedLightIn,getTextureLocation(animatable));
//        RenderType renderType = RenderType.getItemEntityTranslucentCull(getTextureLocation(animatable));
//		render(model, animatable, 0, renderType, stack, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY,(float) renderColor.getRed() / 255f, (float) renderColor.getGreen() / 255f,(float) renderColor.getBlue() / 255f, (float) renderColor.getAlpha() / 255);
//		stack.pop();
//	}
}
