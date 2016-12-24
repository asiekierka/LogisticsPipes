package logisticspipes.blocks.powertile;

import logisticspipes.pipes.basic.CoreRoutedPipe;
import logisticspipes.proxy.MainProxy;

import net.minecraft.nbt.NBTTagCompound;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class LogisticsForgePowerProviderTileEntity extends LogisticsPowerProviderTileEntity implements IEnergyStorage {
	public static final int MAX_STORAGE = 10000000;
	public static final int MAX_MAXMODE = 8;
	public static final int MAX_PROVIDE_PER_TICK = 10000; // TODO

	public LogisticsForgePowerProviderTileEntity() {
	}

	public int addEnergy(int amount, boolean simulate) {
		if (MainProxy.isClient(getWorld())) {
			return 0;
		}

		int amtAdded = Math.min((int) Math.floor(LogisticsForgePowerProviderTileEntity.MAX_STORAGE - internalStorage), amount);
		if (!simulate) {
			internalStorage += amtAdded;
			if (internalStorage >= getMaxStorage()) {
				needMorePowerTriggerCheck = false;
			}
		}

		return amtAdded;
	}

	public int freeSpace() {
		return (int) (getMaxStorage() - internalStorage);
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return addEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return 0;
	}

	@Override
	public int getEnergyStored() {
		return (int) Math.floor(internalStorage);
	}

	@Override
	public boolean canExtract() {
		return false;
	}

	@Override
	public boolean canReceive() {
		return true;
	}

	@Override
	public int getMaxEnergyStored() {
		return getMaxStorage();
	}

	@Override
	public int getMaxStorage() {
		maxMode = Math.min(LogisticsForgePowerProviderTileEntity.MAX_MAXMODE, Math.max(1, maxMode));
		return (LogisticsForgePowerProviderTileEntity.MAX_STORAGE / maxMode);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
	}

	@Override
	public String getBrand() {
		return "FU";
	}

	@Override
	protected double getMaxProvidePerTick() {
		return LogisticsForgePowerProviderTileEntity.MAX_PROVIDE_PER_TICK;
	}

	@Override
	protected void handlePower(CoreRoutedPipe pipe, double toSend) {
		pipe.handleRFPowerArival(toSend);
	}

	@Override
	protected int getLaserColor() {
		return LogisticsPowerProviderTileEntity.RF_COLOR;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return super.hasCapability(capability, facing) || capability == CapabilityEnergy.ENERGY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CapabilityEnergy.ENERGY ? CapabilityEnergy.ENERGY.cast(this) : super.getCapability(capability, facing);
	}
}
