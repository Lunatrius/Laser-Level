package lunatrius.laserlevel;

import cpw.mods.fml.common.registry.BlockProxy;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockLaserLevel extends BlockContainer implements BlockProxy {
	public BlockLaserLevel(int par1) {
		super(par1, Material.redstoneLight);
		disableStats();
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		TileEntity var6 = par1World.getBlockTileEntity(par2, par3, par4);

		if (var6 != null && var6 instanceof TileEntityLaserLevel) {
			par5EntityPlayer.openGui(LaserLevel.instance, 0, par1World, par2, par3, par4);
			return true;
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileEntityLaserLevel();
	}
}
