package lunatrius.laserlevel.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import lunatrius.laserlevel.CommonProxy;
import lunatrius.laserlevel.TileEntityLaserLevel;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerTileEntitySpecialRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaserLevel.class, new TileEntityLaserLevelRenderer());
	}
}
