package com.github.lunatrius.laserlevel.client.gui.marker;

import com.github.lunatrius.core.client.gui.GuiHelper;
import com.github.lunatrius.core.client.gui.GuiNumericField;
import com.github.lunatrius.core.client.gui.GuiScreenBase;
import com.github.lunatrius.laserlevel.client.renderer.RenderMarkers;
import com.github.lunatrius.laserlevel.marker.Constants;
import com.github.lunatrius.laserlevel.marker.Marker;
import com.github.lunatrius.laserlevel.reference.Names;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GuiMarkerEdit extends GuiScreenBase {
    private final Marker marker;

    private GuiNumericField nfMarkerLength = null;
    private GuiNumericField nfX = null;
    private GuiNumericField nfY = null;
    private GuiNumericField nfZ = null;

    private GuiSlider sliderSpacing = null;
    private GuiSlider sliderR = null;
    private GuiSlider sliderG = null;
    private GuiSlider sliderB = null;

    private final GuiCheckBox[] checkBoxes = new GuiCheckBox[EnumFacing.VALUES.length];

    private GuiButton btnToggle = null;
    private GuiButton btnMove = null;
    private GuiButton btnDone = null;

    private final String strTitle = I18n.format(Names.Gui.GuiMarkerEdit.TITLE);
    private final String strMarkerLength = I18n.format(Names.Gui.GuiMarkerEdit.MARKER_LENGTH);
    private final String strX = I18n.format(Names.Gui.GuiMarkerEdit.X);
    private final String strY = I18n.format(Names.Gui.GuiMarkerEdit.Y);
    private final String strZ = I18n.format(Names.Gui.GuiMarkerEdit.Z);

    private final GuiPageButtonList.GuiResponder guiResponder = new GuiPageButtonList.GuiResponder() {
        int prevId;
        float prevValue = -1;

        @Override
        public void setEntryValue(final int id, final boolean value) {
        }

        @Override
        public void setEntryValue(final int id, final float value) {
            if (this.prevId != id || this.prevValue != value) {
                if (GuiMarkerEdit.this.sliderSpacing.id == id) {
                    GuiMarkerEdit.this.marker.spacing = Math.round(value);
                    RenderMarkers.INSTANCE.markDirty();
                } else if (GuiMarkerEdit.this.sliderR.id == id) {
                    GuiMarkerEdit.this.marker.setRed(Math.round(value));
                    RenderMarkers.INSTANCE.markDirty();
                } else if (GuiMarkerEdit.this.sliderG.id == id) {
                    GuiMarkerEdit.this.marker.setGreen(Math.round(value));
                    RenderMarkers.INSTANCE.markDirty();
                } else if (GuiMarkerEdit.this.sliderB.id == id) {
                    GuiMarkerEdit.this.marker.setBlue(Math.round(value));
                    RenderMarkers.INSTANCE.markDirty();
                }
            }

            this.prevId = id;
            this.prevValue = value;
        }

        @Override
        public void setEntryValue(final int id, final String value) {
        }
    };

    private final GuiSlider.FormatHelper formatValue = new GuiSlider.FormatHelper() {
        @Override
        public String getText(final int id, final String name, final float value) {
            return I18n.format(Names.Gui.GuiMarkerEdit.SLIDER_FORMAT, name, Math.round(value));
        }
    };

    public GuiMarkerEdit(final GuiScreen parentScreen, final Marker marker) {
        super(parentScreen);
        this.marker = marker;
    }

    @Override
    public void initGui() {
        super.initGui();

        final int centerX = this.width / 2;
        final int centerY = this.height / 2;
        final int nfOffsetX = 25;
        final int nfHeight = 20;
        int id = -1;

        this.nfMarkerLength = new GuiNumericField(this.mc.fontRendererObj, ++id, centerX + nfOffsetX, centerY - nfHeight / 2 - (nfHeight + 5) * 4);
        this.buttonList.add(this.nfMarkerLength);

        this.nfX = new GuiNumericField(this.mc.fontRendererObj, ++id, centerX + nfOffsetX, centerY - nfHeight / 2 - (nfHeight + 5) * 3);
        this.buttonList.add(this.nfX);

        this.nfY = new GuiNumericField(this.mc.fontRendererObj, ++id, centerX + nfOffsetX, centerY - nfHeight / 2 - (nfHeight + 5) * 2);
        this.buttonList.add(this.nfY);

        this.nfZ = new GuiNumericField(this.mc.fontRendererObj, ++id, centerX + nfOffsetX, centerY - nfHeight / 2 - (nfHeight + 5) * 1);
        this.buttonList.add(this.nfZ);

        final int sliderWidth = 250;
        final int sliderHeight = 20;

        this.sliderSpacing = new GuiSlider(this.guiResponder, ++id, centerX - sliderWidth / 2, centerY - sliderHeight / 2 + (sliderHeight + 5) * 0, I18n.format(Names.Gui.GuiMarkerEdit.SPACING), 1.0f, 128.0f, this.marker.spacing, this.formatValue);
        this.sliderSpacing.width = sliderWidth;
        this.buttonList.add(this.sliderSpacing);

        this.sliderR = new GuiSlider(this.guiResponder, ++id, centerX - sliderWidth / 2, centerY - sliderHeight / 2 + (sliderHeight + 5) * 1, I18n.format(Names.Gui.GuiMarkerEdit.RED), 0, 255, this.marker.getRed(), this.formatValue);
        this.sliderR.width = sliderWidth;
        this.buttonList.add(this.sliderR);

        this.sliderG = new GuiSlider(this.guiResponder, ++id, centerX - sliderWidth / 2, centerY - sliderHeight / 2 + (sliderHeight + 5) * 2, I18n.format(Names.Gui.GuiMarkerEdit.GREEN), 0, 255, this.marker.getGreen(), this.formatValue);
        this.sliderG.width = sliderWidth;
        this.buttonList.add(this.sliderG);

        this.sliderB = new GuiSlider(this.guiResponder, ++id, centerX - sliderWidth / 2, centerY - sliderHeight / 2 + (sliderHeight + 5) * 3, I18n.format(Names.Gui.GuiMarkerEdit.BLUE), 0, 255, this.marker.getBlue(), this.formatValue);
        this.sliderB.width = sliderWidth;
        this.buttonList.add(this.sliderB);

        final EnumFacing[] sides = new EnumFacing[] {
                EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH
        };

        final int cbBaseOffsetX = -120;
        for (int i = 0; i < sides.length; i++) {
            final EnumFacing side = sides[i];
            final int cbOffsetX = side.ordinal() % 2 == 0 ? 0 : 60;
            final int cbHeight = 20;

            this.checkBoxes[side.ordinal()] = new GuiCheckBox(++id, centerX + cbBaseOffsetX + cbOffsetX, centerY + (cbHeight + 5) * (i / 2 - 3) - 5, I18n.format(Names.Gui.GuiMarkerEdit.SIDE_BASE + side.getName()), this.marker.isEnabled(side));
            this.buttonList.add(this.checkBoxes[side.ordinal()]);
        }

        this.btnToggle = new GuiButton(++id, centerX - 4 - 150, this.height - 25, 100, 20, I18n.format(this.marker.enabled ? Names.Gui.GuiMarkerEdit.ON : Names.Gui.GuiMarkerEdit.OFF));
        this.buttonList.add(this.btnToggle);

        this.btnMove = new GuiButton(++id, centerX - 50, this.height - 25, 100, 20, I18n.format(Names.Gui.GuiMarkers.MOVE));
        this.buttonList.add(this.btnMove);

        this.btnDone = new GuiButton(++id, centerX + 4 + 50, this.height - 25, 100, 20, I18n.format(Names.Gui.GuiMarkers.DONE));
        this.buttonList.add(this.btnDone);

        this.nfMarkerLength.setMinimum(0);
        this.nfMarkerLength.setMaximum(Constants.Rendering.MAX_LENGTH);
        this.nfX.setMinimum(Constants.World.MINIMUM_COORD);
        this.nfX.setMaximum(Constants.World.MAXIMUM_COORD);
        this.nfY.setMinimum(Constants.World.MINIMUM_COORD);
        this.nfY.setMaximum(Constants.World.MAXIMUM_COORD);
        this.nfZ.setMinimum(Constants.World.MINIMUM_COORD);
        this.nfZ.setMaximum(Constants.World.MAXIMUM_COORD);

        this.nfMarkerLength.setValue(this.marker.markerLength);
        this.nfX.setValue(this.marker.pos.x);
        this.nfY.setValue(this.marker.pos.y);
        this.nfZ.setValue(this.marker.pos.z);

        updateButtons();
    }

    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == this.nfMarkerLength.id) {
                this.marker.markerLength = this.nfMarkerLength.getValue();
                RenderMarkers.INSTANCE.markDirty();
            } else if (button.id == this.nfX.id) {
                this.marker.pos.x = this.nfX.getValue();
                RenderMarkers.INSTANCE.markDirty();
            } else if (button.id == this.nfY.id) {
                this.marker.pos.y = this.nfY.getValue();
                RenderMarkers.INSTANCE.markDirty();
            } else if (button.id == this.nfZ.id) {
                this.marker.pos.z = this.nfZ.getValue();
                RenderMarkers.INSTANCE.markDirty();
            } else if (button.id == this.btnToggle.id) {
                this.marker.enabled = !this.marker.enabled;
                updateButtons();
                RenderMarkers.INSTANCE.markDirty();
            } else if (button.id == this.btnMove.id) {
                this.marker.pos.set(this.mc.player);
                this.marker.dimension = this.mc.player.dimension;
                this.nfX.setValue(this.marker.pos.x);
                this.nfY.setValue(this.marker.pos.y);
                this.nfZ.setValue(this.marker.pos.z);
                RenderMarkers.INSTANCE.markDirty();
            } else if (button.id == this.btnDone.id) {
                this.mc.displayGuiScreen(this.parentScreen);
            } else {
                for (final EnumFacing side : EnumFacing.VALUES) {
                    final GuiCheckBox checkBox = this.checkBoxes[side.ordinal()];
                    if (button.id == checkBox.id) {
                        this.marker.setEnabled(side, checkBox.isChecked());
                        RenderMarkers.INSTANCE.markDirty();
                    }
                }
            }
        }
        super.actionPerformed(button);
    }

    private void updateButtons() {
        this.btnToggle.displayString = I18n.format(this.marker.enabled ? Names.Gui.GuiMarkerEdit.ON : Names.Gui.GuiMarkerEdit.OFF);

        this.nfMarkerLength.setEnabled(this.marker.enabled);
        this.nfX.setEnabled(this.marker.enabled);
        this.nfY.setEnabled(this.marker.enabled);
        this.nfZ.setEnabled(this.marker.enabled);

        this.sliderSpacing.enabled = this.marker.enabled;
        this.sliderR.enabled = this.marker.enabled;
        this.sliderG.enabled = this.marker.enabled;
        this.sliderB.enabled = this.marker.enabled;

        for (final GuiCheckBox checkBox : this.checkBoxes) {
            checkBox.enabled = this.marker.enabled;
        }

        this.btnMove.enabled = this.marker.enabled;
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        renderBackground(0, 16, this.height - 5 - 20 - 3, this.height);

        final int centerX = this.width / 2;
        final int centerY = this.height / 2;
        final int nfOffsetX = 20;
        final int nfHeight = 20;

        drawCenteredString(this.fontRendererObj, this.strTitle, centerX, 4, 0x00FFFFFF);
        drawString(this.fontRendererObj, this.strMarkerLength, centerX + nfOffsetX - this.fontRendererObj.getStringWidth(this.strMarkerLength), centerY - (nfHeight + 5) * 3 - nfHeight - 5 - this.fontRendererObj.FONT_HEIGHT / 2, 0xFFFFFFFF);
        drawString(this.fontRendererObj, this.strX, centerX + nfOffsetX - this.fontRendererObj.getStringWidth(this.strX), centerY - (nfHeight + 5) * 2 - nfHeight - 5 - this.fontRendererObj.FONT_HEIGHT / 2, 0xFFFFFFFF);
        drawString(this.fontRendererObj, this.strY, centerX + nfOffsetX - this.fontRendererObj.getStringWidth(this.strY), centerY - (nfHeight + 5) * 1 - nfHeight - 5 - this.fontRendererObj.FONT_HEIGHT / 2, 0xFFFFFFFF);
        drawString(this.fontRendererObj, this.strZ, centerX + nfOffsetX - this.fontRendererObj.getStringWidth(this.strZ), centerY - (nfHeight + 5) * 0 - nfHeight - 5 - this.fontRendererObj.FONT_HEIGHT / 2, 0xFFFFFFFF);

        final Tessellator tessellator = Tessellator.getInstance();
        final VertexBuffer buffer = tessellator.getBuffer();
        final int w = 250;
        final int h = 20;
        final int x = centerX - w / 2;
        final int y = centerY - h / 2 + (nfHeight + 5) * 4;

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 0, 1);
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableTexture2D();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        GuiHelper.drawColoredRectangle(buffer, x, y, x + w, y + h, 0, 0x00, 0x00, 0x00, 0xFF);
        GuiHelper.drawColoredRectangle(buffer, x + 1, y + 1, x - 1 + w, y - 1 + h, 0, this.marker.getRed(), this.marker.getGreen(), this.marker.getBlue(), 0xFF);

        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void renderBackground(final int y0, final int y1, final int y2, final int y3) {
        final Tessellator tessellator = Tessellator.getInstance();
        final VertexBuffer buffer = tessellator.getBuffer();
        final double textureWidth = 32.0;
        final double textureHeight = 32.0;
        final int shadowHeight = 4;

        this.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        GuiHelper.drawTexturedRectangle(buffer, 0, y0, this.width, y1, 0.0, textureWidth, textureHeight, 0xFF404040);
        GuiHelper.drawTexturedRectangle(buffer, 0, y2, this.width, y3, 0.0, textureWidth, textureHeight, 0xFF404040);

        tessellator.draw();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 0, 1);
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableTexture2D();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        GuiHelper.drawVerticalGradientRectangle(buffer, 0, y1, this.width, y1 + shadowHeight, 0.0, 0xFF000000, 0x00000000);
        GuiHelper.drawVerticalGradientRectangle(buffer, 0, y2 - shadowHeight, this.width, y2, 0.0, 0x00000000, 0xFF000000);

        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
    }
}
