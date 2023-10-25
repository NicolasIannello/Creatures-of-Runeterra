package com.eximeisty.creaturesofruneterra.screen;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.container.PorobotContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


public class PorobotScreen extends AbstractContainerScreen<PorobotContainer> {
    private final ResourceLocation GUI = new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/gui/porobot_gui.png");
    private final ResourceLocation GUI_CRAFTING = new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/gui/porobot_gui_crafting.png");
    private final ResourceLocation GUI_FURNACE = new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/gui/porobot_gui_furnace.png");

    public PorobotScreen(PorobotContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(GuiGraphics matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        boolean furnace = false;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        switch (this.menu.getSlot(20+9+3*9).getItem().getItem().toString()) {
            case "crafting_table": RenderSystem.setShaderTexture(0, GUI_CRAFTING); break;
            case "furnace": RenderSystem.setShaderTexture(0, GUI_FURNACE); furnace=true; break;
            default: RenderSystem.setShaderTexture(0, GUI); break;

        }
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(matrixStack, i, j, 0, 0, 239, 168);
        if(furnace){
            if (this.menu.poro.isLit()) {
                int k = this.menu.poro.getBurnLeftScaled();
                this.blit(matrixStack, i+196, j+34+18-k, 0, 168+18-k, 18, k);
            }
            int l = this.menu.poro.getCookProgressionScaled();
            this.blit(matrixStack, i+190, j+77, 18, 168, 31, l);
        }
    }
}