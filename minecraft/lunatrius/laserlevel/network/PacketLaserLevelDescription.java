package lunatrius.laserlevel.network;

import lunatrius.laserlevel.TileEntityLaserLevel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketLaserLevelDescription extends PacketLaserLevel {
	byte offset;
	byte spacer;
	byte axis;

	public PacketLaserLevelDescription(TileEntityLaserLevel tileEntity) {
		super(tileEntity);
		this.packetType = TYPE_DESCRIPTION;
		this.offset = tileEntity.offset;
		this.spacer = tileEntity.spacer;
		this.axis = tileEntity.axis;
	}

	@Override
	protected void readData(DataInputStream data) throws IOException {
		super.readData(data);

		this.offset = data.readByte();
		this.spacer = data.readByte();
		this.axis = data.readByte();
	}

	@Override
	protected void writeData(DataOutputStream data) throws IOException {
		super.writeData(data);

		data.writeByte(this.offset);
		data.writeByte(this.spacer);
		data.writeByte(this.axis);
	}

	public byte getOffset() {
		return this.offset;
	}

	public byte getSpacer() {
		return this.spacer;
	}

	public byte getAxis() {
		return this.axis;
	}
}
