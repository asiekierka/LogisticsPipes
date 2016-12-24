/**
 * "LogisticsPipes" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package logisticspipes.pipes;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import logisticspipes.blocks.LogisticsSecurityTileEntity;
import logisticspipes.blocks.powertile.LogisticsPowerJunctionTileEntity;
import logisticspipes.interfaces.IInventoryUtil;
import logisticspipes.modules.ModuleItemSink;
import logisticspipes.modules.abstractmodules.LogisticsModule;
import logisticspipes.modules.abstractmodules.LogisticsModule.ModulePositionType;
import logisticspipes.pipes.basic.CoreRoutedPipe;
import logisticspipes.proxy.SimpleServiceLocator;
import logisticspipes.routing.pathfinder.IPipeInformationProvider.ConnectionPipeType;
import logisticspipes.textures.Textures;
import logisticspipes.textures.Textures.TextureType;
import logisticspipes.transport.PipeTransportLogistics;
import logisticspipes.utils.InventoryHelper;
import logisticspipes.utils.OrientationsUtil;
import logisticspipes.utils.item.ItemIdentifier;
import logisticspipes.utils.item.ItemIdentifierStack;
import logisticspipes.utils.tuples.Pair;
import network.rs485.logisticspipes.world.WorldCoordinatesWrapper;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;

import net.minecraft.util.EnumFacing;

public class PipeItemsBasicLogistics extends CoreRoutedPipe {

	private ModuleItemSink itemSinkModule;

	public PipeItemsBasicLogistics(Item item) {
		super(new PipeTransportLogistics(true) {

			@Override
			public boolean canPipeConnect(TileEntity tile, EnumFacing dir) {
				if (super.canPipeConnect(tile, dir)) {
					return true;
				}
				if (tile instanceof LogisticsSecurityTileEntity) {
					EnumFacing ori = OrientationsUtil.getOrientationOfTilewithTile(container, tile);
					if (ori == null || ori == EnumFacing.UNKNOWN || ori == EnumFacing.DOWN || ori == EnumFacing.UP) {
						return false;
					}
					return true;
				}
				return false;
			}
		}, item);
		itemSinkModule = new ModuleItemSink();
		itemSinkModule.registerHandler(this, this);
	}

	@Override
	public TextureType getNonRoutedTexture(EnumFacing connection) {
		if (isSecurityProvider(connection)) {
			return Textures.LOGISTICSPIPE_SECURITY_TEXTURE;
		}
		return super.getNonRoutedTexture(connection);
	}

	@Override
	public boolean isLockedExit(EnumFacing orientation) {
		if (isPowerJunction(orientation) || isSecurityProvider(orientation)) {
			return true;
		}
		return super.isLockedExit(orientation);
	}

	private boolean isPowerJunction(EnumFacing ori) {
		TileEntity tilePipe = container.getTile(ori);
		if (tilePipe == null || !container.canPipeConnect(tilePipe, ori)) {
			return false;
		}

		if (tilePipe instanceof LogisticsPowerJunctionTileEntity) {
			return true;
		}
		return false;
	}

	private boolean isSecurityProvider(EnumFacing ori) {
		TileEntity tilePipe = container.getTile(ori);
		if (tilePipe == null || !container.canPipeConnect(tilePipe, ori)) {
			return false;
		}
		if (tilePipe instanceof LogisticsSecurityTileEntity) {
			return true;
		}
		return false;
	}

	@Override
	public TextureType getCenterTexture() {
		return Textures.LOGISTICSPIPE_TEXTURE;
	}

	@Override
	public LogisticsModule getLogisticsModule() {
		return itemSinkModule;
	}

	@Override
	public ItemSendMode getItemSendMode() {
		return ItemSendMode.Normal;
	}

	@Override
	public void setTile(TileEntity tile) {
		super.setTile(tile);
		itemSinkModule.registerPosition(ModulePositionType.IN_PIPE, 0);
	}

	@Override
	public IInventoryUtil getPointedInventory(boolean forExtraction) {
		IInventoryUtil inv = super.getPointedInventory(forExtraction);
		if (inv == null) {
			Optional<Pair<IInventory, EnumFacing>> first = new WorldCoordinatesWrapper(container).getConnectedAdjacentTileEntities(ConnectionPipeType.ITEM)
					.filter(adjacent -> adjacent.tileEntity instanceof IInventory)
					.map(adjacentInventory -> new Pair<>(InventoryHelper.getInventory((IInventory) adjacentInventory.tileEntity), adjacentInventory.direction))
					.filter(inventoryDirectionPair -> inventoryDirectionPair.getValue1() != null).findFirst();
			if (first.isPresent()) {
				Pair<IInventory, EnumFacing> p = first.get();
				inv = SimpleServiceLocator.inventoryUtilFactory.getInventoryUtil(p.getValue1(), p.getValue2().getOpposite());
			}
		}
		return inv;
	}

	@Override
	public Set<ItemIdentifier> getSpecificInterests() {
		if (itemSinkModule.isDefaultRoute()) {
			return null;
		}

		Set<ItemIdentifier> l1 = new TreeSet<>();
		Collection<ItemIdentifier> current = itemSinkModule.getSpecificInterests();
		if (current != null) {
			l1.addAll(current);
		}
		return l1;
	}

	@Override
	public boolean hasGenericInterests() {
		return itemSinkModule.isDefaultRoute();
	}
}
