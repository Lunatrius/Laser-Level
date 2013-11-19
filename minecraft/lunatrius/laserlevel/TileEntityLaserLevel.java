package lunatrius.laserlevel;

import lunatrius.laserlevel.network.PacketLaserLevel;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityLaserLevel extends TileEntity {
	public final static int RENDER_RANGE = 64;

	public byte offset = 0;
	public byte spacer = 8;
	public byte axis = 0x07;

	@Override
	public void readFromNBT(NBTTagCompound var1) {
		super.readFromNBT(var1);
		this.offset = var1.getByte("Offset");
		this.spacer = var1.getByte("Spacer");
		this.axis = var1.getByte("Axis");
	}

	@Override
	public void writeToNBT(NBTTagCompound var1) {
		super.writeToNBT(var1);
		var1.setByte("Offset", this.offset);
		var1.setByte("Spacer", this.spacer);
		var1.setByte("Axis", this.axis);
	}

	@Override
	public Packet getDescriptionPacket() {
		return PacketLaserLevel.getDescriptionPacket(this);
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return AxisAlignedBB.getAABBPool().getAABB(this.xCoord - RENDER_RANGE, this.yCoord - RENDER_RANGE, this.zCoord - RENDER_RANGE, this.xCoord + RENDER_RANGE + 1, this.yCoord + RENDER_RANGE + 1, this.zCoord + RENDER_RANGE + 1);
	}
}
