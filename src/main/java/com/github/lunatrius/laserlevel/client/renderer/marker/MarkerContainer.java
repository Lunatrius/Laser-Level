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

        tessellator.drawCuboid(marker.pos, sides, marker.rgb | Constants.Rendering.ALPHA_QUADS << 24);

        final EnumFacing[] values = EnumFacing.VALUES;

        for (final EnumFacing side : values) {
            if (!marker.isEnabled(side)) {
                continue;
            }

            for (int next = marker.spacing; next <= marker.markerLength; next += marker.spacing) {
                final MBlockPos pos = marker.pos.offset(side, next);
                tessellator.drawCuboid(pos, sides, marker.rgb | Constants.Rendering.ALPHA_QUADS << 24);
            }
        }
    }

    protected void renderGuide(final GeometryTessellator tessellator, final Marker marker) {
        if (!marker.enabled) {
            return;
        }

        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        int hi, lo;

        hi = marker.isEnabled(EnumFacing.EAST) ? marker.markerLength : 0;
        lo = marker.isEnabled(EnumFacing.WEST) ? -marker.markerLength : 0;

        if (hi != 0 || lo != 0) {
            drawMarkerLine(worldRenderer, marker, hi, 0, 0, lo, 0, 0);
        }

        hi = marker.isEnabled(EnumFacing.UP) ? marker.markerLength : 0;
        lo = marker.isEnabled(EnumFacing.DOWN) ? -marker.markerLength : 0;

        if (hi != 0 || lo != 0) {
            drawMarkerLine(worldRenderer, marker, 0, hi, 0, 0, lo, 0);
        }

        hi = marker.isEnabled(EnumFacing.SOUTH) ? marker.markerLength : 0;
        lo = marker.isEnabled(EnumFacing.NORTH) ? -marker.markerLength : 0;

        if (hi != 0 || lo != 0) {
            drawMarkerLine(worldRenderer, marker, 0, 0, hi, 0, 0, lo);
        }
    }

    private void drawMarkerLine(final WorldRenderer worldRenderer, final Marker marker, final int x0, final int y0, final int z0, final int x1, final int y1, final int z1) {
        final double x = marker.pos.x + 0.5;
        final double y = marker.pos.y + 0.5;
        final double z = marker.pos.z + 0.5;
        final int r = marker.getRed();
        final int g = marker.getGreen();
        final int b = marker.getBlue();
        final int a = Constants.Rendering.ALPHA_LINES;

        worldRenderer.pos(x + x0, y + y0, z + z0).color(r, g, b, a).endVertex();
        worldRenderer.pos(x + x1, y + y1, z + z1).color(r, g, b, a).endVertex();
    }

    public abstract void compile(List<Marker> markers);

    public abstract void draw();

    public abstract void deleteGlResources();
}
