package logisticspipes.proxy.enderchest;

import logisticspipes.proxy.interfaces.IEnderStorageProxy;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import codechicken.enderstorage.common.BlockEnderStorage;
import codechicken.enderstorage.common.TileFrequencyOwner;

public class EnderStorageProxy implements IEnderStorageProxy {

	@Override
	public boolean isEnderChestBlock(Block block) {
		return block instanceof BlockEnderStorage;
	}

	@Override
	public void openEnderChest(World world, BlockPos pos, EntityPlayer player) {
		TileFrequencyOwner tile = (TileFrequencyOwner) world.getTileEntity(pos);
		tile.activate(player, 0);
	}
}
