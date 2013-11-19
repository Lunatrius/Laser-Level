package lunatrius.laserlevel;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import lunatrius.laserlevel.client.gui.GuiLaserLevel;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CommonProxy implements IGuiHandler {
	public void registerRecipes() {
		GameRegistry.addShapelessRecipe(new ItemStack(LaserLevel.blockLaserLevel, 1), Block.redstoneLampIdle, Block.torchRedstoneActive, Item.compass);
	}

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityLaserLevel.class, "LaserLevel");
	}

	public void registerTileEntitySpecialRenderers() {
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity != null && tileEntity instanceof TileEntityLaserLevel) {
			return new GuiLaserLevel(world, x, y, z);
		}

		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}
}
