package com.Nxer.TwistSpaceTechnology.client.Widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;

public class Widget implements IDrawable {

    private UITexture texture;
    private int width;
    private int height;

    @Override
    public void draw(float x, float y, float width, float height, float partialTicks) {
        Tessellator tessellator = Tessellator.instance;
        Minecraft.getMinecraft().renderEngine.bindTexture(texture.location);
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(texture.location);
        float x1 = x / 0.5f;
        float y1 = y / 0.5f;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0, y, 0, 0, width + 0.5f * partialTicks);
        tessellator.addVertexWithUV(x1, y, 0, height + 0.5f * partialTicks, width + 0.5f * partialTicks);
        tessellator.addVertexWithUV(x1, 0, 0, height + 0.5f * partialTicks, 0);
        tessellator.addVertexWithUV(0, 0, 0, 0, 0);
        tessellator.draw();
    }

    public Widget setTexture(UITexture texture, int width, int height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
        return this;
    }
}
