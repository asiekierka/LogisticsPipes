package logisticspipes.network.packets.debuggui;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextComponentString;

import lombok.Getter;
import lombok.Setter;

import logisticspipes.commands.chathelper.LPChatListener;
import logisticspipes.commands.commands.debug.DebugGuiController;
import logisticspipes.network.PacketHandler;
import logisticspipes.network.abstractpackets.ModernPacket;
import logisticspipes.network.packets.gui.OpenChatGui;
import logisticspipes.proxy.MainProxy;
import logisticspipes.utils.string.ChatColor;
import network.rs485.logisticspipes.util.LPDataInput;
import network.rs485.logisticspipes.util.LPDataOutput;

public class DebugTargetResponse extends ModernPacket {

	@Getter
	@Setter
	private TargetMode mode;
	@Getter
	@Setter
	private int[] additions = new int[0];

	public DebugTargetResponse(int id) {
		super(id);
	}

	@Override
	public void readData(LPDataInput input) {
		mode = TargetMode.values()[input.readByte()];
		additions = input.readIntArray();
	}

	@Override
	public void processPacket(final EntityPlayer player) {
		if (mode == TargetMode.None) {
			player.addChatComponentMessage(new TextComponentString(ChatColor.RED + "No Target Found"));
		} else if (mode == TargetMode.Block) {
			int x = additions[0];
			int y = additions[1];
			int z = additions[2];
			player.addChatComponentMessage(new TextComponentString("Checking Block at: x:" + x + " y:" + y + " z:" + z));
			Block id = player.worldObj.getBlock(x, y, z);
			player.addChatComponentMessage(new TextComponentString("Found Block with Id: " + id.getClass()));
			final TileEntity tile = player.worldObj.getTileEntity(x, y, z);
			if (tile == null) {
				player.addChatComponentMessage(new TextComponentString(ChatColor.RED + "No TileEntity found"));
			} else {
				LPChatListener.addTask(() -> {
					player.addChatComponentMessage(new TextComponentString(
							ChatColor.GREEN + "Starting debuging of TileEntity: " + ChatColor.BLUE + ChatColor.UNDERLINE + tile.getClass().getSimpleName()));
					DebugGuiController.instance().startWatchingOf(tile, player);
					MainProxy.sendPacketToPlayer(PacketHandler.getPacket(OpenChatGui.class), player);
					return true;
				}, player);
				player.addChatComponentMessage(new TextComponentString(
						ChatColor.AQUA + "Start debuging of TileEntity: " + ChatColor.BLUE + ChatColor.UNDERLINE + tile.getClass().getSimpleName()
								+ ChatColor.AQUA + "? " + ChatColor.RESET + "<" + ChatColor.GREEN + "yes" + ChatColor.RESET + "/" + ChatColor.RED + "no"
								+ ChatColor.RESET + ">"));
				MainProxy.sendPacketToPlayer(PacketHandler.getPacket(OpenChatGui.class), player);
			}
		} else if (mode == TargetMode.Entity) {
			int entityId = additions[0];
			final Entity entity = player.worldObj.getEntityByID(entityId);
			if (entity == null) {
				player.addChatComponentMessage(new TextComponentString(ChatColor.RED + "No Entity found"));
			} else {
				LPChatListener.addTask(() -> {
					player.addChatComponentMessage(new TextComponentString(
							ChatColor.GREEN + "Starting debuging of Entity: " + ChatColor.BLUE + ChatColor.UNDERLINE + entity.getClass().getSimpleName()));
					DebugGuiController.instance().startWatchingOf(entity, player);
					MainProxy.sendPacketToPlayer(PacketHandler.getPacket(OpenChatGui.class), player);
					return true;
				}, player);
				player.addChatComponentMessage(new TextComponentString(
						ChatColor.AQUA + "Start debuging of Entity: " + ChatColor.BLUE + ChatColor.UNDERLINE + entity.getClass().getSimpleName()
								+ ChatColor.AQUA + "? " + ChatColor.RESET + "<" + ChatColor.GREEN + "yes" + ChatColor.RESET + "/" + ChatColor.RED + "no"
								+ ChatColor.RESET + ">"));
				MainProxy.sendPacketToPlayer(PacketHandler.getPacket(OpenChatGui.class), player);
			}
		}
	}

	@Override
	public void writeData(LPDataOutput output) {
		output.writeByte(mode.ordinal());
		output.writeIntArray(additions);
	}

	@Override
	public ModernPacket template() {
		return new DebugTargetResponse(getId());
	}

	@Override
	public boolean isCompressable() {
		return true;
	}

	public enum TargetMode {
		Block,
		Entity,
		None
	}
}
