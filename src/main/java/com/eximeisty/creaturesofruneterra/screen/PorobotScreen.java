package com.eximeisty.creaturesofruneterra.screen;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.container.PorobotContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class PorobotScreen extends ContainerScreen<PorobotContainer>{
    private final ResourceLocation GUI = new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/gui/porobot_gui.png");
    private final ResourceLocation GUI_CRAFTING = new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/gui/porobot_gui_crafting.png");
    private final ResourceLocation GUI_FURNACE = new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/gui/porobot_gui_furnace.png");

    public PorobotScreen(PorobotContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override @Deprecated
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        switch (this.container.getSlot(20+9+3*9).getStack().getItem().toString()) {
            case "crafting_table": this.minecraft.getTextureManager().bindTexture(GUI_CRAFTING); break;
            case "furnace": this.minecraft.getTextureManager().bindTexture(GUI_FURNACE); break;
            default: this.minecraft.getTextureManager().bindTexture(GUI); break;
        }
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i, j, 0, 0, 239, 168);
    }
}