package com.github.lunatrius.laserlevel.client.renderer;

import com.github.lunatrius.core.util.vector.Vector3d;
import com.github.lunatrius.laserlevel.client.renderer.marker.MarkerContainer;
import com.github.lunatrius.laserlevel.client.renderer.marker.MarkerContainerList;
import com.github.lunatrius.laserlevel.client.renderer.marker.MarkerContainerVbo;
import com.github.lunatrius.laserlevel.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.profiler.Profiler;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class RenderMarkers {
    public static final RenderMarkers INSTANCE = new RenderMarkers();

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final Profiler profiler = this.minecraft.mcProfiler;
    private final Vector3d playerPosition = new Vector3d();
    private MarkerContainer markerContainer;
    private boolean dirty;

    private RenderMarkers() {
        init();
    }

    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        final EntityPlayerSP player = this.minecraft.thePlayer;
        if (player != null) {
            this.playerPosition.x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks;
            this.playerPosition.y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks;
            this.playerPosition.z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks;

            this.profiler.startSection("laserlevel");

            GlStateManager.pushMatrix();

            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

            render();

            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();

            GlStateManager.popMatrix();

            this.profiler.endSection();
        }
    }

    private void render() {
        this.profiler.startSection("setup");
        this.markerContainer.setTranslation(-this.playerPosition.x, -this.playerPosition.y, -this.playerPosition.z);

        if (this.dirty) {
            /*
            this.profiler.endStartSection("sort");
            List<Marker> list = new ArrayList<Marker>();
            list.addAll(ClientProxy.MARKERS);
            Reference.logger.trace("---");
            Collections.sort(list, new MarkerComparator());
             */
            this.profiler.endStartSection("compile");
            this.markerContainer.compile(ClientProxy.MARKERS);

            this.dirty = false;
        }

        this.profiler.endStartSection("draw");
        this.markerContainer.draw();

        this.profiler.endSection();
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
