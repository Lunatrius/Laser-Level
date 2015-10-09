package com.github.lunatrius.laserlevel.client.renderer.marker;

import com.github.lunatrius.core.client.renderer.GeometryTessellator;
import com.github.lunatrius.core.util.MBlockPos;
import com.github.lunatrius.laserlevel.marker.Constants;
import com.github.lunatrius.laserlevel.marker.Marker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.EnumFacing;

import java.util.List;

public abstract class MarkerContainer {
    protected boolean initialized = false;
    private double x;
    private double y;
    private double z;

    public void setTranslation(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    protected void preRender() {
        GlStateManager.translate(this.x, this.y, this.z);
    }

    protected void renderMarker(final GeometryTessellator tessellator, final int sides, final Marker marker) {
        if (!marker.enabled) {
            return;
        }

        tessellator.drawCuboid(marker.pos, sides, marker.rgb, Constants.Rendering.ALPHA_QUADS);

        final EnumFacing[] values = EnumFacing.VALUES;

        for (final EnumFacing side : values) {
            if (!marker.isEnabled(side)) {
                continue;
            }

            for (int next = marker.spacing; next <= marker.markerLength; next += marker.spacing) {
                final MBlockPos pos = marker.pos.offset(side, next);
                tessellator.drawCuboid(pos, sides, marker.rgb, Constants.Rendering.ALPHA_QUADS);
            }
        }
    }

    protected void renderGuide(final GeometryTessellator tessellator, final Marker marker) {
        if (!marker.enabled) {
            return;
        }

        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.setColorRGBA_I(marker.rgb, Constants.Rendering.ALPHA_LINES);

        int hi, lo;

        hi = marker.isEnabled(EnumFacing.EAST) ? marker.markerLength : 0;
        lo = marker.isEnabled(EnumFacing.WEST) ? marker.markerLength : 0;

        if (hi != 0 || lo != 0) {
            worldRenderer.addVertex(marker.pos.x + 0.5 + hi, marker.pos.y + 0.5, marker.pos.z + 0.5);
            worldRenderer.addVertex(marker.pos.x + 0.5 - lo, marker.pos.y + 0.5, marker.pos.z + 0.5);
        }

        hi = marker.isEnabled(EnumFacing.UP) ? marker.markerLength : 0;
        lo = marker.isEnabled(EnumFacing.DOWN) ? marker.markerLength : 0;

        if (hi != 0 || lo != 0) {
            worldRenderer.addVertex(marker.pos.x + 0.5, marker.pos.y + 0.5 + hi, marker.pos.z + 0.5);
            worldRenderer.addVertex(marker.pos.x + 0.5, marker.pos.y + 0.5 - lo, marker.pos.z + 0.5);
        }

        hi = marker.isEnabled(EnumFacing.SOUTH) ? marker.markerLength : 0;
        lo = marker.isEnabled(EnumFacing.NORTH) ? marker.markerLength : 0;

        if (hi != 0 || lo != 0) {
            worldRenderer.addVertex(marker.pos.x + 0.5, marker.pos.y + 0.5, marker.pos.z + 0.5 + hi);
            worldRenderer.addVertex(marker.pos.x + 0.5, marker.pos.y + 0.5, marker.pos.z + 0.5 - lo);
        }
    }

    public abstract void compile(List<Marker> markers);

    public abstract void draw();

    public abstract void deleteGlResources();
}
