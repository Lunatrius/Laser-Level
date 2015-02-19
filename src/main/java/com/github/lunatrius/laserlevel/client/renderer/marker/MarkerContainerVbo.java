package com.github.lunatrius.laserlevel.client.renderer.marker;

import com.github.lunatrius.core.client.renderer.GeometryMasks;
import com.github.lunatrius.core.client.renderer.GeometryTessellator;
import com.github.lunatrius.core.client.renderer.vertex.VertexFormats;
import com.github.lunatrius.laserlevel.marker.Constants;
import com.github.lunatrius.laserlevel.marker.Marker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.util.List;

public class MarkerContainerVbo extends MarkerContainer {
    private VertexBuffer vertexBufferQuads = new VertexBuffer(VertexFormats.ABSTRACT);
    private VertexBuffer vertexBufferLines = new VertexBuffer(VertexFormats.ABSTRACT);

    @Override
    public void compile(final List<Marker> markers) {
        this.initialized = true;

        final GeometryTessellator tessellator = GeometryTessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        tessellator.setTranslation(0, 0, 0);
        tessellator.setDelta(Constants.Rendering.BLOCK_DELTA);

        tessellator.startQuads();
        for (final Marker marker : markers) {
            renderMarker(tessellator, GeometryMasks.Quad.ALL, marker);
        }

        final ByteBuffer byteBuffer = worldRenderer.getByteBuffer();
        final int byteIndex = worldRenderer.finishDrawing();
        this.vertexBufferQuads.bufferData(byteBuffer, byteIndex);

        tessellator.startLines();
        for (final Marker marker : markers) {
            renderMarker(tessellator, GeometryMasks.Line.ALL, marker);
            renderGuide(tessellator, marker);
        }

        final ByteBuffer byteBuffer2 = worldRenderer.getByteBuffer();
        final int byteIndex2 = worldRenderer.finishDrawing();
        this.vertexBufferLines.bufferData(byteBuffer2, byteIndex2);
    }

    @Override
    public void draw() {
        if (this.initialized) {
            GlStateManager.pushMatrix();

            GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
            GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glLineWidth(1.0f);

            preRender();

            this.vertexBufferQuads.bindBuffer();
            setupArrayPointers();
            this.vertexBufferQuads.drawArrays(GL11.GL_QUADS);

            this.vertexBufferLines.bindBuffer();
            setupArrayPointers();
            this.vertexBufferLines.drawArrays(GL11.GL_LINES);

            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
            GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);

            GlStateManager.popMatrix();

            OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
            GlStateManager.resetColor();
        }
    }

    @Override
    public void deleteGlResources() {
        this.vertexBufferQuads.deleteGlBuffers();
        this.vertexBufferLines.deleteGlBuffers();
    }

    private void setupArrayPointers() {
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 16, 0);
        GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 16, 12);
    }
}
