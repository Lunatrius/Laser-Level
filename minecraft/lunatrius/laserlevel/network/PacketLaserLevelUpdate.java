package lunatrius.laserlevel.network;

import lunatrius.laserlevel.TileEntityLaserLevel;

public class PacketLaserLevelUpdate extends PacketLaserLevelDescription {
	public PacketLaserLevelUpdate(TileEntityLaserLevel tileEntity, byte offset, byte spacer, byte axis) {
		super(tileEntity);
		this.packetType = TYPE_UPDATE;
		this.offset = offset;
		this.spacer = spacer;
		this.axis = axis;
	}
}
