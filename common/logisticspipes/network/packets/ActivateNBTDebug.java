package logisticspipes.network.packets;

import net.minecraft.entity.player.EntityPlayer;

import logisticspipes.LPConstants;
import logisticspipes.config.Configs;
import logisticspipes.network.abstractpackets.ModernPacket;
import network.rs485.logisticspipes.util.LPDataInput;
import network.rs485.logisticspipes.util.LPDataOutput;

public class ActivateNBTDebug extends ModernPacket {

	public ActivateNBTDebug(int id) {
		super(id);
	}

	@Override
	public void readData(LPDataInput input) {}

	@Override
	public void processPacket(EntityPlayer player) {
		try {
			Configs.TOOLTIP_INFO = true;
		} catch (Exception e1) {
			if (LPConstants.DEBUG) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void writeData(LPDataOutput output) {}

	@Override
	public ModernPacket template() {
		return new ActivateNBTDebug(getId());
	}
}
