package lunatrius.laserlevel.network;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import lunatrius.laserlevel.TileEntityLaserLevel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketHandler implements IPacketHandler {
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if (packet.channel.equals(PacketLaserLevel.PACKET_CHANNEL) && packet instanceof PacketLaserLevel) {
			PacketLaserLevel packetLaserLevel = (PacketLaserLevel) packet;

			int x = packetLaserLevel.getX();
			int y = packetLaserLevel.getY();
			int z = packetLaserLevel.getZ();
			EntityPlayer entityPlayer = (EntityPlayer) player;
			TileEntityLaserLevel tileEntity = (TileEntityLaserLevel) entityPlayer.worldObj.getBlockTileEntity(x, y, z);

			if (tileEntity == null) {
				return;
			}

			switch (packetLaserLevel.packetType) {
			case PacketLaserLevel.TYPE_DESCRIPTION:
				PacketLaserLevelDescription packetLaserLevelDescription = (PacketLaserLevelDescription) packetLaserLevel;
				tileEntity.offset = packetLaserLevelDescription.getOffset();
				tileEntity.spacer = packetLaserLevelDescription.getSpacer();
				tileEntity.axis = packetLaserLevelDescription.getAxis();
				break;

			case PacketLaserLevel.TYPE_UPDATE:
				PacketLaserLevelUpdate packetLaserLevelUpdate = (PacketLaserLevelUpdate) packetLaserLevel;
				tileEntity.spacer += packetLaserLevelUpdate.getSpacer();
				if (tileEntity.spacer < 0) {
					tileEntity.spacer = 0;
				}
				tileEntity.offset += packetLaserLevelUpdate.getOffset();
				if (tileEntity.spacer > 0) {
					tileEntity.offset %= tileEntity.spacer;
				}
				tileEntity.axis ^= packetLaserLevelUpdate.getAxis();
				break;
			}

			entityPlayer.worldObj.markBlockForUpdate(x, y, z);
		}
	}
}
