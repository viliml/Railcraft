/* 
 * Copyright (c) CovertJaguar, 2014 http://railcraft.info
 * 
 * This code is the property of CovertJaguar
 * and may only be used with explicit written
 * permission unless otherwise specified on the
 * license page at http://railcraft.info/wiki/info:license.
 */
package mods.railcraft.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

public class RenderTools {

    public static final int BOX_BRIGHTNESS = 165;
    public static final float PIXEL = 0.0625f;

    public static void setColor(int color) {
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        GL11.glColor4f(red, green, blue, 1);
    }

    public static boolean renderStandardBlock(RenderBlocks renderblocks, Block block, int x, int y, int z) {
        renderblocks.setRenderBoundsFromBlock(block);
        return renderblocks.renderStandardBlock(block, x, y, z);
    }

    public static boolean renderStandardBlockWithColorMultiplier(RenderBlocks renderblocks, Block block, int x, int y, int z) {
        renderblocks.setRenderBoundsFromBlock(block);
        int mult = block.colorMultiplier(renderblocks.blockAccess, x, y, z);
        float r = (float) (mult >> 16 & 255) / 255.0F;
        float g = (float) (mult >> 8 & 255) / 255.0F;
        float b = (float) (mult & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable) {
            float var9 = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
            float var10 = (r * 30.0F + g * 70.0F) / 100.0F;
            float var11 = (r * 30.0F + b * 70.0F) / 100.0F;
            r = var9;
            g = var10;
            b = var11;
        }

        return renderblocks.renderStandardBlockWithColorMultiplier(block, x, y, z, r, g, b);
    }

    public static void renderBlockSideWithBrightness(RenderBlocks renderblocks, IBlockAccess world, Block block, int i, int j, int k, int side, int brightness) {
        renderblocks.setRenderBoundsFromBlock(block);
        renderblocks.enableAO = false;
        Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        tessellator.setBrightness(brightness);
        if (side == 0)
            renderblocks.renderFaceYNeg(block, i, j, k, block.getIcon(world, i, j, k, 0));
        else if (side == 1)
            renderblocks.renderFaceYPos(block, i, j, k, block.getIcon(world, i, j, k, 1));
        else if (side == 2)
            renderblocks.renderFaceZNeg(block, i, j, k, block.getIcon(world, i, j, k, 2));
        else if (side == 3)
            renderblocks.renderFaceZPos(block, i, j, k, block.getIcon(world, i, j, k, 3));
        else if (side == 4)
            renderblocks.renderFaceXNeg(block, i, j, k, block.getIcon(world, i, j, k, 4));
        else if (side == 5)
            renderblocks.renderFaceXPos(block, i, j, k, block.getIcon(world, i, j, k, 5));
    }

    public static void renderBlockOnInventory(RenderBlocks renderblocks, Block block, int meta, float light) {
        renderBlockOnInventory(renderblocks, block, meta, light, -1);
    }

    public static void renderBlockOnInventory(RenderBlocks renderblocks, Block block, int meta, float light, int side) {
        renderBlockOnInventory(renderblocks, block, meta, light, side, null);
    }

    public static void renderBlockOnInventory(RenderBlocks renderblocks, Block block, int meta, float light, int side, IIcon iconOveride) {
        Tessellator tessellator = Tessellator.instance;
//        boolean flag = block.blockID == Block.grass.blockID;
//        if (renderblocks.useInventoryTint) {
//            int j = block.getRenderColor(meta);
//            if (flag) {
//                j = 0xffffff;
//            }
//            float red = (float) (j >> 16 & 0xff) / 255F;
//            float green = (float) (j >> 8 & 0xff) / 255F;
//            float blue = (float) (j & 0xff) / 255F;
//            GL11.glColor4f(red * light, green * light, blue * light, 1.0F);
//        }
        block.setBlockBoundsForItemRender();
        renderblocks.setRenderBoundsFromBlock(block);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        if (side == 0 || side == -1) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            IIcon icon = iconOveride == null ? block.getIcon(0, meta) : iconOveride;
            if (icon != null)
                renderblocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();
        }
//        if (flag && renderblocks.useInventoryTint) {
//            int k1 = block.getRenderColor(meta);
//            float f6 = (float) (k1 >> 16 & 0xff) / 255F;
//            float f8 = (float) (k1 >> 8 & 0xff) / 255F;
//            float f9 = (float) (k1 & 0xff) / 255F;
//            GL11.glColor4f(f6 * light, f8 * light, f9 * light, 1.0F);
//        }
        if (side == 1 || side == -1) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            IIcon icon = iconOveride == null ? block.getIcon(1, meta) : iconOveride;
            if (icon != null)
                renderblocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();
        }
//        if (flag && renderblocks.useInventoryTint) {
//            GL11.glColor4f(light, light, light, 1.0F);
//        }
        if (side == 2 || side == -1) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1F);
            IIcon icon = iconOveride == null ? block.getIcon(2, meta) : iconOveride;
            if (icon != null)
                renderblocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();
        }
        if (side == 3 || side == -1) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            IIcon icon = iconOveride == null ? block.getIcon(3, meta) : iconOveride;
            if (icon != null)
                renderblocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();
        }
        if (side == 4 || side == -1) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1F, 0.0F, 0.0F);
            IIcon icon = iconOveride == null ? block.getIcon(4, meta) : iconOveride;
            if (icon != null)
                renderblocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();
        }
        if (side == 5 || side == -1) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            IIcon icon = iconOveride == null ? block.getIcon(5, meta) : iconOveride;
            if (icon != null)
                renderblocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();
        }
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    public static IIcon getSafeIcon(IIcon icon) {
        if (icon == null)
            return getMissingIcon();
        return icon;
    }

    public static IIcon getMissingIcon() {
        return ((TextureMap) Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationBlocksTexture)).getAtlasSprite("missingno");
    }

}
