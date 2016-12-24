package logisticspipes.transport;

import logisticspipes.network.PacketHandler;
import logisticspipes.network.abstractpackets.ModernPacket;
import logisticspipes.network.packets.pipe.PipeFluidUpdate;
import logisticspipes.pipes.basic.fluid.FluidRoutedPipe;
import logisticspipes.proxy.MainProxy;
import logisticspipes.proxy.SimpleServiceLocator;
import logisticspipes.utils.SafeTimeTracker;
import logisticspipes.utils.item.ItemIdentifierStack;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class PipeFluidTransportLogistics extends PipeTransportLogistics implements IFluidHandler {

	public FluidTank[] sideTanks = new FluidTank[EnumFacing.VALUES.length];
	public FluidTank internalTank = new FluidTank(getInnerCapacity());

	public FluidStack[] renderCache = new FluidStack[7];

	public PipeFluidTransportLogistics() {
		super(true);
		for (EnumFacing dir : EnumFacing.VALUES) {
			sideTanks[dir.ordinal()] = new FluidTank(getSideCapacity());
		}
	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		if (from.ordinal() < EnumFacing.VALUES.length && getFluidPipe().canReceiveFluid()) {
			return sideTanks[from.ordinal()].fill(resource, doFill);
		} else {
			return 0;
		}
	}

	private FluidRoutedPipe getFluidPipe() {
		return (FluidRoutedPipe) getPipe();
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		return getPipe().isFluidPipe() && getFluidPipe().canReceiveFluid();
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		return false;
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		if (from.ordinal() < EnumFacing.VALUES.length) {
			return sideTanks[from.ordinal()].drain(maxDrain, doDrain);
		} else {
			return null;
		}
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		if (sideTanks[from.ordinal()].getFluid() == null || !(sideTanks[from.ordinal()].getFluid().isFluidEqual(resource))) {
			return new FluidStack(resource.getFluidID(), 0);
		}
		return drain(from, resource.amount, doDrain);
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		if (from.ordinal() < EnumFacing.VALUES.length) {
			return new FluidTankInfo[] { new FluidTankInfo(sideTanks[from.ordinal()]) };
		} else {
			return null;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		for (EnumFacing direction : EnumFacing.VALUES) {
			if (nbttagcompound.hasKey("tank[" + direction.ordinal() + "]")) {
				sideTanks[direction.ordinal()].readFromNBT(nbttagcompound.getCompoundTag("tank[" + direction.ordinal() + "]"));
			}
		}
		if (nbttagcompound.hasKey("tank[middle]")) {
			internalTank.readFromNBT(nbttagcompound.getCompoundTag("tank[middle]"));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		for (EnumFacing direction : EnumFacing.VALUES) {
			NBTTagCompound subTag = new NBTTagCompound();
			sideTanks[direction.ordinal()].writeToNBT(subTag);
			nbttagcompound.setTag("tank[" + direction.ordinal() + "]", subTag);
		}
		NBTTagCompound subTag = new NBTTagCompound();
		internalTank.writeToNBT(subTag);
		nbttagcompound.setTag("tank[middle]", subTag);
	}

	public int getInnerCapacity() {
		return 10000;
	}

	public int getSideCapacity() {
		return 5000;
	}

	@Override
	public void onNeighborBlockChange(int blockId) {
		super.onNeighborBlockChange(blockId);

		for (EnumFacing direction : EnumFacing.VALUES) {
			if (!MainProxy.checkPipesConnections(container, container.getTile(PipeFluidTransportLogistics.orientations[direction.ordinal()]), PipeFluidTransportLogistics.orientations[direction.ordinal()])) {
				if (MainProxy.isServer(getWorld())) {
					FluidStack stack = sideTanks[direction.ordinal()].getFluid();
					if (stack != null) {
						sideTanks[direction.ordinal()].setFluid(null);
						internalTank.fill(stack, true);
					}
				}
				if (renderCache[direction.ordinal()] != null) {
					renderCache[direction.ordinal()].amount = 1;
				}
			}
		}
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		updateFluid();
	}

	/*
	 * BuildCraft Fluid Sync Code
	 */
	private final SafeTimeTracker tracker = new SafeTimeTracker(10);
	private long clientSyncCounter = 30;
	public byte initClient = 0;

	private static final EnumFacing[] orientations = EnumFacing.values();

	private void updateFluid() {
		if (MainProxy.isClient(getWorld())) {
			return;
		}
		if (tracker.markTimeIfDelay(getWorld())) {

			boolean init = false;
			if (++clientSyncCounter > 40) {
				clientSyncCounter = 0;
				init = true;
			}
			if (clientSyncCounter < 0) {
				clientSyncCounter = 0;
			}
			ModernPacket packet = computeFluidUpdate(init, true);
			if (packet != null) {
				MainProxy.sendPacketToAllWatchingChunk(container.xCoord, container.zCoord, MainProxy.getDimensionForWorld(getWorld()), packet);
			}
		}
	}

	/**
	 * Computes the PacketFluidUpdate packet for transmission to a client
	 * 
	 * @param initPacket
	 *            everything is sent, no delta stuff ( first packet )
	 * @param persistChange
	 *            The render cache change is persisted
	 * @return PacketFluidUpdate liquid update packet
	 */
	private ModernPacket computeFluidUpdate(boolean initPacket, boolean persistChange) {

		boolean changed = false;

		if (initClient > 0) {
			initClient--;
			if (initClient == 1) {
				changed = true;
			}
		}

		FluidStack[] renderCache = this.renderCache.clone();

		for (EnumFacing dir : PipeFluidTransportLogistics.orientations) {
			FluidStack current;
			if (dir != EnumFacing.UNKNOWN) {
				current = sideTanks[dir.ordinal()].getFluid();
			} else {
				current = internalTank.getFluid();
			}
			FluidStack prev = renderCache[dir.ordinal()];

			if (prev == null && current == null) {
				continue;
			}

			if (prev == null && current != null) {
				changed = true;
				renderCache[dir.ordinal()] = current.copy();
				continue;
			}

			if (prev != null && current == null) {
				changed = true;
				renderCache[dir.ordinal()] = null;
				continue;
			}

			if (prev.getFluidID() != current.getFluidID() || initPacket) {
				changed = true;
				renderCache[dir.ordinal()] = new FluidStack(current.getFluid(), renderCache[dir.ordinal()].amount);
			}

			if (prev.amount != current.amount || initPacket) {
				changed = true;
				renderCache[dir.ordinal()].amount = current.amount;
			}
		}

		if (persistChange) {
			this.renderCache = renderCache;
		}

		if (changed || initPacket) {
			return PacketHandler.getPacket(PipeFluidUpdate.class).setRenderCache(renderCache).setPosX(container.xCoord).setPosY(container.yCoord).setPosZ(container.zCoord).setChunkDataPacket(initPacket);
		}

		return null;
	}

	@Override
	protected boolean isItemExitable(ItemIdentifierStack stack) {
		return true;
	}

	@Override
	protected boolean isPipeCheck(TileEntity tile) {
		return SimpleServiceLocator.pipeInformationManager.isPipe(tile);
	}
}
