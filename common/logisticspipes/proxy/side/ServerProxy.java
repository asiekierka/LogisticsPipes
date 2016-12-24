package logisticspipes.proxy.side;

import java.io.File;
import java.util.List;

import logisticspipes.LogisticsPipes;
import logisticspipes.blocks.LogisticsSecurityTileEntity;
import logisticspipes.blocks.LogisticsSolderingTileEntity;
import logisticspipes.blocks.crafting.LogisticsCraftingTableTileEntity;
import logisticspipes.blocks.powertile.LogisticsForgePowerProviderTileEntity;
import logisticspipes.blocks.powertile.LogisticsIC2PowerProviderTileEntity;
import logisticspipes.blocks.powertile.LogisticsPowerJunctionTileEntity;
import logisticspipes.blocks.stats.LogisticsStatisticsTileEntity;
import logisticspipes.items.ItemLogisticsPipe;
import logisticspipes.modules.abstractmodules.LogisticsModule;
import logisticspipes.network.PacketHandler;
import logisticspipes.network.packets.UpdateName;
import logisticspipes.pipes.basic.CoreUnroutedPipe;
import logisticspipes.pipes.basic.LogisticsTileGenericPipe;
import logisticspipes.pipes.basic.LogisticsTileGenericSubMultiBlock;
import logisticspipes.proxy.MainProxy;
import logisticspipes.proxy.SimpleServiceLocator;
import logisticspipes.proxy.interfaces.IProxy;
import logisticspipes.utils.item.ItemIdentifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.server.FMLServerHandler;

public class ServerProxy implements IProxy {

	private Configuration langDatabase;
	private long saveThreadTime = 0;

	public ServerProxy() {
		langDatabase = new Configuration(new File("config/LogisticsPipes-LangDatabase.cfg"));
	}

	@Override
	public String getSide() {
		return "Server";
	}

	@Override
	public World getWorld() {
		return null;
	}

	@Override
	public void registerTileEntities() {
		GameRegistry.registerTileEntity(LogisticsSolderingTileEntity.class, "logisticspipes.blocks.LogisticsSolderingTileEntity");
		GameRegistry.registerTileEntity(LogisticsPowerJunctionTileEntity.class, "logisticspipes.blocks.powertile.LogisticsPowerJuntionTileEntity");
		GameRegistry.registerTileEntity(LogisticsForgePowerProviderTileEntity.class, "logisticspipes.blocks.powertile.LogisticsForgePowerProviderTileEntity");
		GameRegistry.registerTileEntity(LogisticsIC2PowerProviderTileEntity.class, "logisticspipes.blocks.powertile.LogisticsIC2PowerProviderTileEntity");
		GameRegistry.registerTileEntity(LogisticsSecurityTileEntity.class, "logisticspipes.blocks.LogisticsSecurityTileEntity");
		GameRegistry.registerTileEntity(LogisticsCraftingTableTileEntity.class, "logisticspipes.blocks.crafting.LogisticsCraftingTableTileEntity");
		GameRegistry.registerTileEntity(LogisticsTileGenericPipe.class, LogisticsPipes.logisticsTileGenericPipeMapping);
		GameRegistry.registerTileEntity(LogisticsStatisticsTileEntity.class, "logisticspipes.blocks.stats.LogisticsStatisticsTileEntity");
		GameRegistry.registerTileEntity(LogisticsTileGenericSubMultiBlock.class, "logisticspipes.pipes.basic.LogisticsTileGenericSubMultiBlock");
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return null;
	}

	@Override
	public void registerParticles() {
		//Only Client Side
	}

	private String getNameForCategory(String category, ItemIdentifier item) {
		String name = langDatabase.get(category, "name", "").getString();
		if (name.equals("")) {
			saveLangDatabase();
			if (item.isDamageable()) {
				return item.getFriendlyName();
			} else {
				return "LP|UNDEFINED";
			}
		}
		return name;
	}

	private void setNameForCategory(String category, ItemIdentifier item, String newName) {
		langDatabase.get(category, "name", newName).set(newName);
		saveLangDatabase();
	}

	private void saveLangDatabase() {
		saveThreadTime = System.currentTimeMillis() + 30 * 1000;
	}

	@Override
	public String getName(ItemIdentifier item) {
		String category = "";
		if (item.isDamageable()) {
			category = "itemNames." + Integer.toString(Item.getIdFromItem(item.item));
		} else {
			if (item.itemDamage == 0) {
				category = "itemNames." + Integer.toString(Item.getIdFromItem(item.item));
			} else {
				category = "itemNames." + Integer.toString(Item.getIdFromItem(item.item)) + "." + Integer.toString(item.itemDamage);
			}
		}
		String name = getNameForCategory(category, item);
		if (name.equals("LP|UNDEFINED")) {
			if (item.itemDamage == 0) {
				return item.getFriendlyName();
			} else {
				category = "itemNames." + Integer.toString(Item.getIdFromItem(item.item));
				name = getNameForCategory(category, item);
				if (name.equals("LP|UNDEFINED")) {
					return item.getFriendlyName();
				}
			}
		}
		return name;
	}

	@Override
	public void updateNames(ItemIdentifier item, String name) {
		String category = "";
		if (item.isDamageable()) {
			category = "itemNames." + Integer.toString(Item.getIdFromItem(item.item));
		} else {
			if (item.itemDamage == 0) {
				category = "itemNames." + Integer.toString(Item.getIdFromItem(item.item));
			} else {
				category = "itemNames." + Integer.toString(Item.getIdFromItem(item.item)) + "." + Integer.toString(item.itemDamage);
			}
		}
		setNameForCategory(category, item, name);
	}

	@Override
	public void tick() {
		//Save Language Database
		if (saveThreadTime != 0) {
			if (saveThreadTime < System.currentTimeMillis()) {
				saveThreadTime = 0;
				langDatabase.save();
				LogisticsPipes.log.info("LangDatabase saved");
			}
		}
	}

	@Override
	public void sendNameUpdateRequest(EntityPlayer player) {
		for (String category : langDatabase.getCategoryNames()) {
			if (!category.startsWith("itemNames.")) {
				continue;
			}
			String name = langDatabase.get(category, "name", "").getString();
			if (name.equals("")) {
				String itemPart = category.substring(10);
				String metaPart = "0";
				if (itemPart.contains(".")) {
					String[] itemPartSplit = itemPart.split("\\.");
					itemPart = itemPartSplit[0];
					metaPart = itemPartSplit[1];
				}
				int id = Integer.valueOf(itemPart);
				int meta = Integer.valueOf(metaPart);
				SimpleServiceLocator.serverBufferHandler.addPacketToCompressor(PacketHandler.getPacket(UpdateName.class).setIdent(ItemIdentifier.get(Item.getItemById(id), meta, null)).setName("-"), player);
			}
		}
	}

	@Override
	public int getDimensionForWorld(World world) {
		return world.provider.getDimension();
	}

	@Override
	public LogisticsTileGenericPipe getPipeInDimensionAt(int dimension, BlockPos pos, EntityPlayer player) {
		return ServerProxy.getPipe(DimensionManager.getWorld(dimension), pos);
	}

	// BuildCraft method
	/**
	 * Retrieves pipe at specified coordinates if any.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	protected static LogisticsTileGenericPipe getPipe(World world, BlockPos pos) {
		if (world == null) {
			return null;
		}
		if (!world.isBlockLoaded(pos)) {
			return null;
		}

		final TileEntity tile = world.getTileEntity(pos);
		if (!(tile instanceof LogisticsTileGenericPipe)) {
			return null;
		}

		return (LogisticsTileGenericPipe) tile;
	}

	// BuildCraft method end
	@Override
	public void addLogisticsPipesOverride(IIconRegister par1IIconRegister, int index, String override1, String override2, boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	@SuppressWarnings("rawtypes")
	public void sendBroadCast(String message) {
		MinecraftServer server = FMLServerHandler.instance().getServer();
		if (server != null && server.getPlayerList() != null) {
			List<EntityPlayerMP> list = server.getPlayerList().getPlayerList();
			if (list != null && !list.isEmpty()) {
				list.stream().forEach(player -> player.addChatMessage(new TextComponentString("[LP] Server: " + message)));
			}
		}
	}

	@Override
	public void tickServer() {
		MainProxy.addTick();
	}

	@Override
	public void tickClient() {}

	@Override
	public EntityPlayer getEntityPlayerFromNetHandler(INetHandler handler) {
		if (handler instanceof NetHandlerPlayServer) {
			return ((NetHandlerPlayServer) handler).playerEntity;
		}
		return null;
	}

	@Override
	public void setIconProviderFromPipe(ItemLogisticsPipe item, CoreUnroutedPipe dummyPipe) {}

	@Override
	public LogisticsModule getModuleFromGui() {
		return null;
	}

	@Override
	public IItemRenderer getPipeItemRenderer() {
		return null;
	}

	@Override
	public boolean checkSinglePlayerOwner(String commandSenderName) {
		return false;
	}

	@Override
	public void openFluidSelectGui(int slotId) {}
}
