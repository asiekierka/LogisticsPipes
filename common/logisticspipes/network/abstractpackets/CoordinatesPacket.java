package logisticspipes.network.abstractpackets;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import logisticspipes.network.exception.TargetNotFoundException;
import logisticspipes.pipes.basic.LogisticsTileGenericPipe;
import network.rs485.logisticspipes.util.LPDataInput;
import network.rs485.logisticspipes.util.LPDataOutput;
import network.rs485.logisticspipes.world.DoubleCoordinates;

@ToString
public abstract class CoordinatesPacket extends ModernPacket {
	// TODO: Refactor into BlockPos
	@Getter
	@Setter
	private int posX;
	@Getter
	@Setter
	private int posY;
	@Getter
	@Setter
	private int posZ;

	public CoordinatesPacket(int id) {
		super(id);
	}

	@Override
	public void writeData(LPDataOutput output) {

		output.writeInt(posX);
		output.writeInt(posY);
		output.writeInt(posZ);
	}

	@Override
	public void readData(LPDataInput input) {

		posX = input.readInt();
		posY = input.readInt();
		posZ = input.readInt();

	}

	public BlockPos getPos() {
		return new BlockPos(posX, posY, posZ);
	}

	public CoordinatesPacket setPos(BlockPos pos) {
		setPosX(pos.getX());
		setPosY(pos.getY());
		setPosZ(pos.getZ());
		return this;
	}

	@Deprecated
	public CoordinatesPacket setTilePos(TileEntity tile) {
		setPos(tile.getPos());
		return this;
	}

	public CoordinatesPacket setLPPos(DoubleCoordinates pos) {
		setPosX(pos.getXInt());
		setPosY(pos.getYInt());
		setPosZ(pos.getZInt());
		return this;
	}

	public CoordinatesPacket setPacketPos(CoordinatesPacket packet) {
		posX = packet.posX;
		posY = packet.posY;
		posZ = packet.posZ;
		return this;
	}

	@SuppressWarnings("unchecked")
	/**
	 * Retrieves tileEntity at packet coordinates if any.
	 *
	 * @param world
	 * @param clazz
	 * @return TileEntity
	 */
	public <T> T getTile(World world, Class<T> clazz) {
		if (world == null) {
			targetNotFound("World was null");
			return null;
		}
		if (!world.isBlockLoaded(getPos())) {
			targetNotFound("Couldn't find " + clazz.getName());
			return null;
		}

		final TileEntity tile = world.getTileEntity(getPos());
		if (tile != null) {
			if (!(clazz.isAssignableFrom(tile.getClass()))) {
				targetNotFound("Couldn't find " + clazz.getName() + ", found " + tile.getClass());
				return null;
			}
		} else {
			targetNotFound("Couldn't find " + clazz.getName());
		}
		return (T) tile;
	}

	@SuppressWarnings("unchecked")
	/**
	 * Retrieves tileEntity or CoreUnroutedPipe at packet coordinates if any.
	 *
	 * @param world
	 * @param clazz
	 * @return TileEntity
	 */
	public <T> T getTileOrPipe(World world, Class<T> clazz) {
		if (world == null) {
			targetNotFound("World was null");
			return null;
		}
		if (!world.isBlockLoaded(getPos())) {
			targetNotFound("Couldn't find " + clazz.getName());
			return null;
		}

		final TileEntity tile = world.getTileEntity(getPos());
		if (tile != null) {
			if (clazz.isAssignableFrom(tile.getClass())) {
				return (T) tile;
			}
			if (tile instanceof LogisticsTileGenericPipe) {
				if (((LogisticsTileGenericPipe) tile).pipe != null && clazz.isAssignableFrom(((LogisticsTileGenericPipe) tile).pipe.getClass())) {
					return (T) ((LogisticsTileGenericPipe) tile).pipe;
				}
				targetNotFound("Couldn't find " + clazz.getName() + ", found pipe with " + tile.getClass());
				return null;
			}
		} else {
			targetNotFound("Couldn't find " + clazz.getName());
			return null;
		}
		targetNotFound("Couldn't find " + clazz.getName() + ", found " + tile.getClass());
		return null;
	}

	/**
	 * Retrieves pipe at packet coordinates if any.
	 *
	 * @param world
	 * @return
	 */
	@Deprecated
	public LogisticsTileGenericPipe getPipe(World world) {
		return getPipe(world, LTGPCompletionCheck.NONE);
	}

	public LogisticsTileGenericPipe getPipe(World world, LTGPCompletionCheck check) {
		LogisticsTileGenericPipe pipe = getTile(world, LogisticsTileGenericPipe.class);
		if (check == LTGPCompletionCheck.PIPE || check == LTGPCompletionCheck.TRANSPORT) {
			if (pipe.pipe == null) {
				targetNotFound("The found pipe didn't have a loaded pipe field");
			}
		}
		if (check == LTGPCompletionCheck.TRANSPORT) {
			if (pipe.pipe.transport == null) {
				targetNotFound("The found pipe didn't have a loaded transport field");
			}
		}
		return pipe;
	}

	protected void targetNotFound(String message) {
		throw new TargetNotFoundException(message, this);
	}

	public enum LTGPCompletionCheck {
		NONE,
		PIPE,
		TRANSPORT
	}
}
