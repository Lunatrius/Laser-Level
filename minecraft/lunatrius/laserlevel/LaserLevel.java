package lunatrius.laserlevel;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import lunatrius.laserlevel.network.PacketHandler;
import lunatrius.laserlevel.network.PacketLaserLevel;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.Configuration;

@Mod(modid = "LaserLevel")
@NetworkMod(channels = {
		PacketLaserLevel.PACKET_CHANNEL
}, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class LaserLevel {
	public static BlockLaserLevel blockLaserLevel = null;
	private int blockLaserLevelId = 511;

	@SidedProxy(clientSide = "lunatrius.laserlevel.client.ClientProxy", serverSide = "lunatrius.laserlevel.CommonProxy")
	public static CommonProxy proxy;

	@Instance("LaserLevel")
	public static LaserLevel instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());

		config.load();
		this.blockLaserLevelId = config.getBlock("laserLevel", this.blockLaserLevelId).getInt(this.blockLaserLevelId);
		config.save();

		blockLaserLevel = (BlockLaserLevel) new BlockLaserLevel(this.blockLaserLevelId).setStepSound(Block.soundGlassFootstep).setLightValue(1.0f).setUnlocalizedName("laserlevel").setCreativeTab(CreativeTabs.tabTools).setTextureName("redstone_lamp_on");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		GameRegistry.registerBlock(blockLaserLevel, ItemBlock.class, "laserLevel");
		// LanguageRegistry.instance().addStringLocalization(blockLaserLevel.getBlockName() + ".name", "en_US", "Laser Level");
		proxy.registerRecipes();
		proxy.registerTileEntities();
		proxy.registerTileEntitySpecialRenderers();
		NetworkRegistry.instance().registerGuiHandler(instance, proxy);
	}
}
