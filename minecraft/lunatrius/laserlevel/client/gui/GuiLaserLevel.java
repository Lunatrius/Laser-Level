package lunatrius.laserlevel.client.gui;

import cpw.mods.fml.common.network.PacketDispatcher;
import lunatrius.laserlevel.TileEntityLaserLevel;
import lunatrius.laserlevel.network.PacketLaserLevel;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class GuiLaserLevel extends GuiScreen {
	private final TileEntityLaserLevel tileEntity;

	private int widthCenter = 0;
	private int heightCenter = 0;

	private GuiButtonToggle btnToggleX;
	private GuiButtonToggle btnToggleY;
	private GuiButtonToggle btnToggleZ;

	private GuiButton btnOffsetDec;
	private GuiButton btnOffsetInc;
	private GuiButton btnSpacerDec;
	private GuiButton btnSpacerInc;

	private GuiButton btnDone;

	private final String strX = StatCollector.translateToLocal("laserlevel.gui.x");
	private final String strY = StatCollector.translateToLocal("laserlevel.gui.y");
	private final String strZ = StatCollector.translateToLocal("laserlevel.gui.z");
	private final String strDecrease = StatCollector.translateToLocal("laserlevel.gui.decrease");
	private final String strIncrease = StatCollector.translateToLocal("laserlevel.gui.increase");
	private final String strDone = StatCollector.translateToLocal("laserlevel.gui.done");
	private final String strOffset = StatCollector.translateToLocal("laserlevel.gui.offset");
	private final String strSpacer = StatCollector.translateToLocal("laserlevel.gui.spacer");

	public GuiLaserLevel(World world, int x, int y, int z) {
		this.tileEntity = (TileEntityLaserLevel) world.getBlockTileEntity(x, y, z);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void initGui() {
		int id = 0;

		this.widthCenter = this.width / 2;
		this.heightCenter = this.height / 2;

		this.btnToggleX = new GuiButtonToggle(id++, this.widthCenter - 40, this.heightCenter - 60, 20, 20, this.strX);
		this.buttonList.add(this.btnToggleX);

		this.btnToggleY = new GuiButtonToggle(id++, this.widthCenter - 10, this.heightCenter - 60, 20, 20, this.strY);
		this.buttonList.add(this.btnToggleY);

		this.btnToggleZ = new GuiButtonToggle(id++, this.widthCenter + 20, this.heightCenter - 60, 20, 20, this.strZ);
		this.buttonList.add(this.btnToggleZ);

		this.btnOffsetDec = new GuiButton(id++, this.widthCenter - 50, this.heightCenter - 20, 20, 20, this.strDecrease);
		this.buttonList.add(this.btnOffsetDec);

		this.btnOffsetInc = new GuiButton(id++, this.widthCenter + 30, this.heightCenter - 20, 20, 20, this.strIncrease);
		this.buttonList.add(this.btnOffsetInc);

		this.btnSpacerDec = new GuiButton(id++, this.widthCenter - 50, this.heightCenter + 20, 20, 20, this.strDecrease);
		this.buttonList.add(this.btnSpacerDec);

		this.btnSpacerInc = new GuiButton(id++, this.widthCenter + 30, this.heightCenter + 20, 20, 20, this.strIncrease);
		this.buttonList.add(this.btnSpacerInc);

		this.btnDone = new GuiButton(id++, this.widthCenter - 25, this.heightCenter + 50, 50, 20, this.strDone);
		this.buttonList.add(this.btnDone);

		if ((this.tileEntity.axis & 0x01) == 0) {
			this.btnToggleX.enabled = false;
		}

		if ((this.tileEntity.axis & 0x02) == 0) {
			this.btnToggleY.enabled = false;
		}

		if ((this.tileEntity.axis & 0x04) == 0) {
			this.btnToggleZ.enabled = false;
		}
	}

	@Override
	protected void actionPerformed(GuiButton guiButton) {
		byte offset = 0;
		byte spacer = 0;
		byte axis = 0;

		if (guiButton.id == this.btnToggleX.id) {
			axis = 0x01;
		} else if (guiButton.id == this.btnToggleY.id) {
			axis = 0x02;
		} else if (guiButton.id == this.btnToggleZ.id) {
			axis = 0x04;
		} else if (guiButton.id == this.btnOffsetDec.id) {
			offset = -1;
		} else if (guiButton.id == this.btnOffsetInc.id) {
			offset = +1;
		} else if (guiButton.id == this.btnSpacerDec.id) {
			spacer = -1;
		} else if (guiButton.id == this.btnSpacerInc.id) {
			spacer = +1;
		} else if (guiButton.id == this.btnDone.id) {
			this.mc.displayGuiScreen(null);
		}

		if ((offset != 0 || spacer != 0 || axis != 0) && (this.tileEntity != null)) {
			PacketDispatcher.sendPacketToServer(PacketLaserLevel.getUpdatePacket(this.tileEntity, offset, spacer, axis));
		}
	}

	@Override
	public void drawScreen(int var1, int var2, float var3) {
		if (this.tileEntity == null) {
			this.mc.displayGuiScreen(null);
			return;
		}

		this.btnToggleX.enabled = (this.tileEntity.axis & 0x01) != 0;
		this.btnToggleY.enabled = (this.tileEntity.axis & 0x02) != 0;
		this.btnToggleZ.enabled = (this.tileEntity.axis & 0x04) != 0;

		this.drawGradientRect(0, 0, this.width, this.height, 0x50101010, 0x60101010);
		this.drawGradientRect(this.widthCenter - 80, this.heightCenter - 80, this.widthCenter + 80, this.heightCenter + 80, 0xC0101010, 0xD0101010);
		this.drawCenteredString(this.fontRenderer, this.strOffset, this.widthCenter, this.heightCenter - 30, 0xFFFFFFFF);
		this.drawCenteredString(this.fontRenderer, Integer.toString(this.tileEntity.offset), this.widthCenter, this.heightCenter - 15, 0xFFFFFFFF);
		this.drawCenteredString(this.fontRenderer, this.strSpacer, this.widthCenter, this.heightCenter + 10, 0xFFFFFFFF);
		this.drawCenteredString(this.fontRenderer, Integer.toString(this.tileEntity.spacer), this.widthCenter, this.heightCenter + 25, 0xFFFFFFFF);

		super.drawScreen(var1, var2, var3);
	}
}
