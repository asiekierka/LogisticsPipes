package logisticspipes.utils;

import logisticspipes.pipes.basic.LogisticsTileGenericPipe;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraft.util.EnumFacing;

public final class TileBuffer {
	private IBlockState state = null;
	private TileEntity tile;

	private final SafeTimeTracker tracker = new SafeTimeTracker(20, 5);
	private final World world;
	private final BlockPos pos;
	private final boolean loadUnloaded;

	public TileBuffer(World world, BlockPos pos, boolean loadUnloaded) {
		this.world = world;
		this.pos = pos;
		this.loadUnloaded = loadUnloaded;

		refresh();
	}

	public void refresh() {
		if (tile instanceof LogisticsTileGenericPipe && ((LogisticsTileGenericPipe) tile).pipe != null && ((LogisticsTileGenericPipe) tile).pipe.preventRemove()) {
			if (world.isAirBlock(pos)) {
				return;
			}
		}
		tile = null;
		state = null;

		if (!loadUnloaded && !world.isBlockLoaded(pos)) {
			return;
		}

		state = world.getBlockState(pos);

		if (state != null && state.getBlock().hasTileEntity(state)) {
			tile = world.getTileEntity(pos);
		}
	}

	public void set(IBlockState state, TileEntity tile) {
		this.state = state;
		this.tile = tile;
		tracker.markTime(world);
	}

	public IBlockState getState() {
		if (tile != null && !tile.isInvalid()) {
			return state;
		}

		if (tracker.markTimeIfDelay(world)) {
			refresh();

			if (tile != null && !tile.isInvalid()) {
				return state;
			}
		}

		return null;
	}

	public Block getBlock() {
		IBlockState state = getState();
		return state != null ? state.getBlock() : null;
	}

	public TileEntity getTile() {
		if (tile != null && !tile.isInvalid()) {
			return tile;
		}

		if (tracker.markTimeIfDelay(world)) {
			refresh();

			if (tile != null && !tile.isInvalid()) {
				return tile;
			}
		}

		return null;
	}

	public boolean exists() {
		if (tile != null && !tile.isInvalid()) {
			return true;
		}

		return world.isBlockLoaded(pos);
	}

	public static TileBuffer[] makeBuffer(World world, BlockPos pos, boolean loadUnloaded) {
		TileBuffer[] buffer = new TileBuffer[6];

		for (int i = 0; i < 6; i++) {
			EnumFacing d = EnumFacing.getFront(i);
			buffer[i] = new TileBuffer(world, pos.offset(d), loadUnloaded);
		}

		return buffer;
	}
}
