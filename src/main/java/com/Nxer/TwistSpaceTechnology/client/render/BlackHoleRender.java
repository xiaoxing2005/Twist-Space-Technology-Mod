package com.Nxer.TwistSpaceTechnology.client.render;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import com.Nxer.TwistSpaceTechnology.common.tile.TileBlackHoleCore;

import cpw.mods.fml.client.registry.ClientRegistry;

public class BlackHoleRender extends TileEntitySpecialRenderer {

    private IModelCustom model;
    private ResourceLocation textures;

    public BlackHoleRender(IModelCustom model, ResourceLocation textures) {
        this.model = model;
        this.textures = textures;
        ClientRegistry.bindTileEntitySpecialRenderer(TileBlackHoleCore.class, this);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof TileBlackHoleCore tileBlackHoleCore)) return;
        TileBlackHoleCore.renderBlackHole(tileBlackHoleCore,x,y,z,timeSinceLastTick,model,textures,this);
    }

    public void BindTexture(ResourceLocation textures) {
        this.bindTexture(textures);
    }
}
