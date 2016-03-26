package com.github.lunatrius.laserlevel.client.renderer;

import com.github.lunatrius.core.util.vector.Vector3d;
import com.github.lunatrius.laserlevel.client.renderer.marker.MarkerContainer;
import com.github.lunatrius.laserlevel.client.renderer.marker.MarkerContainerList;
import com.github.lunatrius.laserlevel.client.renderer.marker.MarkerContainerVbo;
import com.github.lunatrius.laserlevel.marker.Marker;
import com.github.lunatrius.laserlevel.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.profiler.Profiler;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class RenderMarkers {
    public static final RenderMarkers INSTANCE = new RenderMarkers();

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final Profiler profiler = this.minecraft.mcProfiler;
    private final Vector3d playerPosition = new Vector3d();
    private final Vector3d prevPlayerPosition = new Vector3d();
    private MarkerContainer markerContainer;
    private boolean dirty;

    private RenderMarkers() {
        init();
    }

    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        final EntityPlayerSP player = this.minecraft.thePlayer;
        if (player != null) {
            this.playerPosition.x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
            this.playerPosition.y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
            this.playerPosition.z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();

            this.profiler.startSection("laserlevel");

            GlStateManager.pushMatrix();

            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

            render(player);

            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();

            GlStateManager.popMatrix();

            this.profiler.endSection();
        }
    }

    private void render(final EntityPlayer player) {
        this.profiler.startSection("setup");
        this.markerContainer.setTranslation(-this.playerPosition.x, -this.playerPosition.y, -this.playerPosition.z);

        if (this.playerPosition.lengthSquaredTo(this.prevPlayerPosition) > 16 * 16) {
            this.prevPlayerPosition.set(this.playerPosition);

            markDirty();
        }

        if (this.dirty) {
            this.profiler.endStartSection("filter");
            final List<Marker> markers = filterMarkers(ClientProxy.MARKERS, player);

            this.profiler.endStartSection("compile");
            this.markerContainer.compile(markers);

            this.dirty = false;
        }

        this.profiler.endStartSection("draw");
        this.markerContainer.draw();

        this.profiler.endSection();
    }

    private List<Marker> filterMarkers(final List<Marker> markers, final EntityPlayer player) {
        final List<Marker> filtered = new ArrayList<Marker>();
        for (final Marker marker : markers) {
            if (marker.dimension != player.dimension) {
                continue;
            }

            final int length = marker.markerLength + 64;
            if (marker.pos.distanceSqToCenter(this.playerPosition.x, this.playerPosition.y, this.playerPosition.z) > length * length) {
                continue;
            }

            filtered.add(marker);
        }

        return filtered;
    }

    private void init() {
        if (this.markerContainer != null) {
            this.markerContainer.deleteGlResources();
        }

        if (OpenGlHelper.useVbo()) {
            this.markerContainer = new MarkerContainerVbo();
        } else {
            this.markerContainer = new MarkerContainerList();
        }

        markDirty();
    }

    public void markDirty() {
        this.dirty = true;
    }
}
