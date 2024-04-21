package com.Nxer.TwistSpaceTechnology.common.tile;

import com.Nxer.TwistSpaceTechnology.client.render.BlackHoleRender;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

public class TileBlackHoleCore extends TileEntity {

    public double size = 1;
    public float CoreQuality = 0;
    private boolean isGenerate = false;

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536;
    }

    @SideOnly(Side.CLIENT)
    public static void renderBlackHole(TileBlackHoleCore tile, double x, double y, double z, float timeSinceLastTick,
                                       IModelCustom model, ResourceLocation textures, BlackHoleRender Render){
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        double size = tile.size;
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Render.BindTexture(textures);
        GL11.glScaled(size, size, size);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        model.renderAll();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setDouble("size", size);
        nbt.setFloat("CoreQuality", CoreQuality);
        nbt.setBoolean("isGenerate", isGenerate);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        size = nbt.getDouble("size");
        CoreQuality = nbt.getFloat("CoreQuality");
        isGenerate = nbt.getBoolean("isGenerate");
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
    }
}
