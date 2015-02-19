package com.github.lunatrius.laserlevel.client.renderer.marker;

import com.github.lunatrius.core.client.renderer.GeometryMasks;
import com.github.lunatrius.core.client.renderer.GeometryTessellator;
import com.github.lunatrius.laserlevel.marker.Constants;
import com.github.lunatrius.laserlevel.marker.Marker;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class MarkerContainerList extends MarkerContainer {
    private final int displayList = GLAllocation.generateDisplayLists(1);

    @Override
    public void compile(final List<Marker> markers) {
        this.initialized = true;
        GL11.glNewList(this.displayList, GL11.GL_COMPILE);
        GlStateManager.pushMatrix();

        final GeometryTessellator tessellator = GeometryTessellator.getInstance();
        tessellator.setTranslation(0, 0, 0);
        tessellator.setDelta(Constants.Rendering.BLOCK_DELTA);

        tessellator.startQuads();
        for (final Marker marker : markers) {
            renderMarker(tessellator, GeometryMasks.Quad.ALL, marker);
        }

        tessellator.draw();

        tessellator.startLines();
        for (final Marker marker : markers) {
            renderMarker(tessellator, GeometryMasks.Line.ALL, marker);
            renderGuide(tessellator, marker);
        }

        tessellator.draw();

        GlStateManager.popMatrix();
        GL11.glEndList();

        int error;
        while ((error = GL11.glGetError()) != GL11.GL_NO_ERROR) {
            System.out.println(error);
        }
    }

    @Override
    public void draw() {
        if (this.initialized) {
            GlStateManager.pushMatrix();

            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glLineWidth(1.0f);

            preRender();

            GL11.glCallList(this.displayList);

            GL11.glDisable(GL11.GL_LINE_SMOOTH);

            GlStateManager.popMatrix();

            GlStateManager.resetColor();
        }
    }

    @Override
    public void deleteGlResources() {
        GLAllocation.deleteDisplayLists(this.displayList);
    }
}
