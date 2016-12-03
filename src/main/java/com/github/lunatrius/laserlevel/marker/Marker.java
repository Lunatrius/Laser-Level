package com.github.lunatrius.laserlevel.marker;

import com.github.lunatrius.core.util.math.MBlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class Marker {
    public final MBlockPos pos;
    public int dimension;
    public boolean enabled;
    public int spacing;
    public int rgb;
    public int markerLength;

    private boolean sides[] = new boolean[EnumFacing.VALUES.length];

    public Marker(final BlockPos pos, final int dimension, final int spacing, final int rgb) {
        this.pos = new MBlockPos(pos);
        this.dimension = dimension;
        this.enabled = true;
        this.spacing = spacing;
        this.rgb = rgb;
        this.markerLength = Constants.Rendering.DEFAULT_LENGTH;

        for (final EnumFacing side : EnumFacing.VALUES) {
            this.sides[side.ordinal()] = true;
        }
    }

    public void setRed(final int red) {
        this.rgb = (this.rgb & ~0xFF0000) | (MathHelper.clamp(red, 0x00, 0xFF) << 16);
    }

    public int getRed() {
        return this.rgb >> 16 & 0xFF;
    }

    public void setGreen(final int green) {
        this.rgb = (this.rgb & ~0x00FF00) | (MathHelper.clamp(green, 0x00, 0xFF) << 8);
    }

    public int getGreen() {
        return this.rgb >> 8 & 0xFF;
    }

    public void setBlue(final int blue) {
        this.rgb = (this.rgb & ~0x0000FF) | MathHelper.clamp(blue, 0x00, 0xFF);
    }

    public int getBlue() {
        return this.rgb & 0xFF;
    }

    public void setEnabled(final EnumFacing side, final boolean enabled) {
        this.sides[side.ordinal()] = enabled;
    }

    public boolean isEnabled(final EnumFacing side) {
        return this.sides[side.ordinal()];
    }
}
