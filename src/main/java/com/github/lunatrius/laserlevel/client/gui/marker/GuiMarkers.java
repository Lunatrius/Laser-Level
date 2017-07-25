package com.github.lunatrius.laserlevel.client.gui.marker;

import com.github.lunatrius.core.client.gui.GuiScreenBase;
import com.github.lunatrius.laserlevel.LaserLevel;
import com.github.lunatrius.laserlevel.json.ConfigurationHandler;
import com.github.lunatrius.laserlevel.marker.Marker;
import com.github.lunatrius.laserlevel.reference.Names;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;

import java.io.IOException;

public class GuiMarkers extends GuiScreenBase {
    private GuiMarkersSlot guiMarkersSlot;

    protected GuiButton btnAdd = null;
    protected GuiButton btnDelete = null;
    protected GuiButton btnEdit = null;
    protected GuiButton btnDone = null;

    private final String strTitle = I18n.format(Names.Gui.GuiMarkers.TITLE);

    public GuiMarkers(final GuiScreen guiScreen) {
        super(guiScreen);
    }

    @Override
    public void initGui() {
        super.initGui();

        int id = -1;

        this.btnAdd = new GuiButton(++id, this.width / 2 - 154, this.height - 25 - 22, 150, 20, I18n.format(Names.Gui.GuiMarkers.ADD));
        this.buttonList.add(this.btnAdd);

        this.btnDelete = new GuiButton(++id, this.width / 2 + 4, this.height - 25 - 22, 150, 20, I18n.format(Names.Gui.GuiMarkers.DELETE));
        this.buttonList.add(this.btnDelete);

        this.btnEdit = new GuiButton(++id, this.width / 2 - 154, this.height - 25, 150, 20, I18n.format(Names.Gui.GuiMarkers.EDIT));
        this.buttonList.add(this.btnEdit);

        this.btnDone = new GuiButton(++id, this.width / 2 + 4, this.height - 25, 150, 20, I18n.format(Names.Gui.GuiMarkers.DONE));
        this.buttonList.add(this.btnDone);

        this.guiMarkersSlot = new GuiMarkersSlot(this);

        this.btnDelete.enabled = false;
        this.btnEdit.enabled = false;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.guiMarkersSlot.handleMouseInput();
    }

    @Override
    protected void actionPerformed(final GuiButton guiButton) {
        if (guiButton.enabled) {
            if (guiButton.id == this.btnAdd.id) {
                final Marker marker = new Marker(new BlockPos(this.mc.player), this.mc.player.dimension, 1, 0x0000BF);
                LaserLevel.proxy.addMarker(marker);
                this.guiMarkersSlot.selectedIndex = -1;
                this.btnDelete.enabled = false;
                this.btnEdit.enabled = false;
            } else if (guiButton.id == this.btnDelete.id) {
                LaserLevel.proxy.removeMarker(this.guiMarkersSlot.selectedIndex);
                this.guiMarkersSlot.selectedIndex = -1;
                this.btnDelete.enabled = false;
                this.btnEdit.enabled = false;
            } else if (guiButton.id == this.btnEdit.id) {
                if (this.guiMarkersSlot.selectedIndex != -1) {
                    final Marker marker = LaserLevel.proxy.getMarker(this.guiMarkersSlot.selectedIndex);
                    this.mc.displayGuiScreen(new GuiMarkerEdit(this, marker));
                }
            } else if (guiButton.id == this.btnDone.id) {
                this.mc.displayGuiScreen(this.parentScreen);
            } else {
                this.guiMarkersSlot.actionPerformed(guiButton);
            }
        }
    }

    @Override
    public void drawScreen(final int x, final int y, final float partialTicks) {
        this.guiMarkersSlot.drawScreen(x, y, partialTicks);

        drawCenteredString(this.fontRenderer, this.strTitle, this.width / 2, 4, 0x00FFFFFF);

        super.drawScreen(x, y, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        ConfigurationHandler.save();
    }
}
