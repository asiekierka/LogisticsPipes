package logisticspipes.network.packets.pipe;

import net.minecraft.entity.player.EntityPlayer;

import lombok.Getter;
import lombok.Setter;

import logisticspipes.network.abstractpackets.ModernPacket;
import logisticspipes.network.abstractpackets.ModuleCoordinatesPacket;
import logisticspipes.renderer.LogisticsGuiOverrenderer;
import net.minecraft.util.math.BlockPos;
import network.rs485.logisticspipes.util.LPDataInput;
import network.rs485.logisticspipes.util.LPDataOutput;

public class SlotFinderActivatePacket extends ModuleCoordinatesPacket {

	@Getter
	@Setter
	private int targetPosX;
	@Getter
	@Setter
	private int targetPosY;
	@Getter
	@Setter
	private int targetPosZ;
	@Getter
	@Setter
	private int slot;

	public SlotFinderActivatePacket(int id) {
		super(id);
	}

	@Override
	public ModernPacket template() {
		return new SlotFinderActivatePacket(getId());
	}

	@Override
	public void writeData(LPDataOutput output) {
		super.writeData(output);
		output.writeInt(targetPosX);
		output.writeInt(targetPosY);
		output.writeInt(targetPosZ);
		output.writeInt(slot);
	}

	@Override
	public void readData(LPDataInput input) {
		super.readData(input);
		targetPosX = input.readInt();
		targetPosY = input.readInt();
		targetPosZ = input.readInt();
		slot = input.readInt();
	}

	@Override
	public void processPacket(EntityPlayer player) {
		LogisticsGuiOverrenderer.getInstance().setPipePosX(getPosX());
		LogisticsGuiOverrenderer.getInstance().setPipePosY(getPosY());
		LogisticsGuiOverrenderer.getInstance().setPipePosZ(getPosZ());
		LogisticsGuiOverrenderer.getInstance().setTargetPosX(getTargetPosX());
		LogisticsGuiOverrenderer.getInstance().setTargetPosY(getTargetPosY());
		LogisticsGuiOverrenderer.getInstance().setTargetPosZ(getTargetPosZ());
		LogisticsGuiOverrenderer.getInstance().setSlot(getSlot());
		LogisticsGuiOverrenderer.getInstance().setOverlaySlotActive(true);
		LogisticsGuiOverrenderer.getInstance().setPositionInt(getPositionInt());
		LogisticsGuiOverrenderer.getInstance().setPositionType(getType());
	}

	public SlotFinderActivatePacket setTargetPos(BlockPos targetPos) {
		setTargetPosX(targetPos.getX());
		setTargetPosY(targetPos.getY());
		setTargetPosZ(targetPos.getZ());
		return this;
	}
}
