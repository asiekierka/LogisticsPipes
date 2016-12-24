package logisticspipes.network.packets.debug;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;

import net.minecraftforge.fml.client.FMLClientHandler;

import logisticspipes.network.PacketHandler;
import logisticspipes.network.abstractpackets.ModernPacket;
import logisticspipes.proxy.MainProxy;
import network.rs485.logisticspipes.util.LPDataInput;
import network.rs485.logisticspipes.util.LPDataOutput;

public class PipeDebugLogAskForTarget extends ModernPacket {

	public PipeDebugLogAskForTarget(int id) {
		super(id);
	}

	@Override
	public void readData(LPDataInput input) {}

	@Override
	public void processPacket(EntityPlayer player) {
		RayTraceResult box = FMLClientHandler.instance().getClient().objectMouseOver;
		if (box != null && box.typeOfHit == Type.BLOCK) {
			MainProxy.sendPacketToServer(PacketHandler.getPacket(PipeDebugLogResponse.class).setPos(box.getBlockPos()));
		}
	}

	@Override
	public void writeData(LPDataOutput output) {}

	@Override
	public ModernPacket template() {
		return new PipeDebugLogAskForTarget(getId());
	}

	@Override
	public boolean isCompressable() {
		return true;
	}
}
