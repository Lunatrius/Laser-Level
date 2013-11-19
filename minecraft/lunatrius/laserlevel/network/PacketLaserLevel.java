package lunatrius.laserlevel.network;

import lunatrius.laserlevel.TileEntityLaserLevel;
import net.minecraft.network.packet.Packet250CustomPayload;

import java.io.*;

public abstract class PacketLaserLevel extends Packet250CustomPayload {
	public final static String PACKET_CHANNEL = "LaserLevel";
	public final static byte TYPE_DESCRIPTION = 0;
	public final static byte TYPE_UPDATE = 1;

	protected byte packetType;
	private int x;
	private int y;
	private int z;

	protected PacketLaserLevel(TileEntityLaserLevel tileEntity) {
		this.x = tileEntity.xCoord;
		this.y = tileEntity.yCoord;
		this.z = tileEntity.zCoord;
	}

	@Override
	public void readPacketData(DataInput dataInput) throws IOException {
		super.readPacketData(dataInput);

		ByteArrayInputStream byteArray = new ByteArrayInputStream(this.data);
		DataInputStream data = new DataInputStream(byteArray);

		readData(data);
	}

	@Override
	public void writePacketData(DataOutput dataOutput) throws IOException {
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(byteArray);

		writeData(data);

		this.channel = PACKET_CHANNEL;
		this.data = byteArray.toByteArray();
		this.length = this.data.length;

		super.writePacketData(dataOutput);
	}

	public PacketLaserLevel writePacketData() throws IOException {
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(byteArray);

		writeData(data);

		this.channel = PACKET_CHANNEL;
		this.data = byteArray.toByteArray();
		this.length = this.data.length;

		return this;
	}

	protected void readData(DataInputStream dataInputStream) throws IOException {
		this.packetType = dataInputStream.readByte();
		this.x = dataInputStream.readInt();
		this.y = dataInputStream.readInt();
		this.z = dataInputStream.readInt();
	}

	protected void writeData(DataOutputStream data) throws IOException {
		data.writeByte(this.packetType);
		data.write(this.x);
		data.write(this.y);
		data.write(this.z);
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getZ() {
		return this.z;
	}

	public static PacketLaserLevelDescription getDescriptionPacket(TileEntityLaserLevel tileEntity) {
		PacketLaserLevelDescription packet = new PacketLaserLevelDescription(tileEntity);
		try {
			packet.writePacketData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return packet;
	}

	public static PacketLaserLevelUpdate getUpdatePacket(TileEntityLaserLevel tileEntity, byte offset, byte spacer, byte axis) {
		PacketLaserLevelUpdate packet = new PacketLaserLevelUpdate(tileEntity, offset, spacer, axis);
		try {
			packet.writePacketData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return packet;
	}
}
