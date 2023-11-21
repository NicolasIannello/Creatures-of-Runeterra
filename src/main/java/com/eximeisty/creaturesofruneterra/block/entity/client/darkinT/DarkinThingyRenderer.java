package com.eximeisty.creaturesofruneterra.block.entity.client.darkinT;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.block.entity.DarkinThingyTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import javax.annotation.Nullable;


public class DarkinThingyRenderer extends GeoBlockRenderer<DarkinThingyTileEntity> {
    private float z = 0;
    private ResourceLocation rl=new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/block/darkin_pedestal.png");
    private IRenderTypeBuffer rtb;
    private DarkinThingyTileEntity tile;

    public DarkinThingyRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn, new DarkinThingyModel());
    }

    @Override
    public void renderEarly(DarkinThingyTileEntity animatable, MatrixStack stackIn, float partialTicks,
                            @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn,
                            int packedOverlayIn, float red, float green, float blue, float alpha) {
        super.renderEarly(animatable,stackIn,partialTicks,renderTypeBuffer,vertexBuilder,packedLightIn,packedOverlayIn,red,green,blue,alpha);
        rtb=renderTypeBuffer;
        tile=animatable;
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack poseStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        if (bone.getName().equals("item")) {
            z += 0.5;
            if(z>360) z=0;
            poseStack.push();
            poseStack.rotate(Vector3f.YP.rotationDegrees(z));
            poseStack.translate(0D, 1.5D, 0D);
            Minecraft.getInstance().getItemRenderer().renderItem(null,((DarkinThingyTileEntity)tile.getTileEntity()).itemTile, ItemCameraTransforms.TransformType.GROUND, false,poseStack, rtb, tile.getWorld(), packedLight, packedOverlay);
            poseStack.pop();
            buffer = rtb.getBuffer(RenderType.getEntityTranslucent(rl));
        }

        super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}