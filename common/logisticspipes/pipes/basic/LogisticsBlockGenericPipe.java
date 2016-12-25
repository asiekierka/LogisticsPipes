package logisticspipes.pipes.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraft.util.EnumFacing;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import logisticspipes.LPConstants;
import logisticspipes.LogisticsPipes;
import logisticspipes.config.Configs;
import logisticspipes.config.PlayerConfig;
import logisticspipes.interfaces.IRotationProvider;
import logisticspipes.interfaces.ITubeOrientation;
import logisticspipes.items.ItemLogisticsPipe;
import logisticspipes.pipes.PipeBlockRequestTable;
import logisticspipes.proxy.MainProxy;
import logisticspipes.proxy.SimpleServiceLocator;
import logisticspipes.proxy.buildcraft.subproxies.IBCClickResult;
import logisticspipes.proxy.buildcraft.subproxies.IBCPipePluggable;
import logisticspipes.renderer.LogisticsPipeWorldRenderer;
import logisticspipes.renderer.newpipe.LogisticsNewRenderPipe;
import logisticspipes.ticks.QueuedTasks;
import logisticspipes.utils.LPPositionSet;
import logisticspipes.utils.math.MatrixTranformations;
import network.rs485.logisticspipes.world.DoubleCoordinates;
import network.rs485.logisticspipes.world.DoubleCoordinatesType;

public class LogisticsBlockGenericPipe extends BlockContainer {
	private static final EnumFacing[] DIR_VALUES = EnumFacing.values();
	public static RaytraceResult bypassPlayerTrace = null;
	public static boolean ignoreSideRayTrace = false;
	public static Map<Item, Class<? extends CoreUnroutedPipe>> pipes = new HashMap<>();
	public static Map<DoubleCoordinates, CoreUnroutedPipe> pipeRemoved = new HashMap<>();
	private static long lastRemovedDate = -1;
	protected final Random rand = new Random();
	private boolean skippedFirstIconRegister;
	private int renderMask = 0;
	private AxisAlignedBB bounds;

	public LogisticsBlockGenericPipe() {
		super(Material.GLASS);
		setRenderAllSides();
		setCreativeTab(null);
	}

	// TODO: Adapt to new rendering
	/* public static IIcon getRequestTableTextureFromSide(int l) {
		EnumFacing dir = EnumFacing.getOrientation(l);
		switch (dir) {
			case UP:
				return Textures.LOGISTICS_REQUEST_TABLE[0];
			case DOWN:
				return Textures.LOGISTICS_REQUEST_TABLE[1];
			default:
				return Textures.LOGISTICS_REQUEST_TABLE[4];
		}
	} */

	public static void removePipe(CoreUnroutedPipe pipe) {
		if (!LogisticsBlockGenericPipe.isValid(pipe)) {
			return;
		}

		if (pipe.canBeDestroyed() || pipe.destroyByPlayer()) {
			pipe.onBlockRemoval();
		} else if (pipe.preventRemove()) {
			LogisticsBlockGenericPipe.cacheTileToPreventRemoval(pipe);
		}

		World world = pipe.container.getWorld();

		if (pipe.isMultiBlock()) {
			if (pipe.preventRemove()) {
				throw new UnsupportedOperationException("A multi block can't be protected against removal.");
			}
			LPPositionSet<DoubleCoordinatesType<CoreMultiBlockPipe.SubBlockTypeForShare>> list = ((CoreMultiBlockPipe) pipe).getRotatedSubBlocks();
			list.stream().forEach(pos -> pos.add(new DoubleCoordinates(pipe)));
			for (DoubleCoordinates pos : pipe.container.subMultiBlock) {
				TileEntity tile = pos.getTileEntity(world);
				if(tile instanceof LogisticsTileGenericSubMultiBlock) {
					DoubleCoordinatesType<CoreMultiBlockPipe.SubBlockTypeForShare> equ = list.findClosest(pos);
					if(equ != null) {
						((LogisticsTileGenericSubMultiBlock) tile).removeSubType(equ.getType());
					}
					if(((LogisticsTileGenericSubMultiBlock) tile).removeMainPipe(new DoubleCoordinates(pipe))) {
						pos.setBlockToAir(world);
					} else {
						MainProxy.sendPacketToAllWatchingChunk(tile, ((LogisticsTileGenericSubMultiBlock) tile).getLPDescriptionPacket());
					}
				}
			}
		}

		if (world == null) {
			return;
		}

		BlockPos pos = pipe.container.getPos();

		if (LogisticsBlockGenericPipe.lastRemovedDate != world.getTotalWorldTime()) {
			LogisticsBlockGenericPipe.lastRemovedDate = world.getTotalWorldTime();
			LogisticsBlockGenericPipe.pipeRemoved.clear();
		}

		LogisticsBlockGenericPipe.pipeRemoved.put(new DoubleCoordinates(pos), pipe);
		world.removeTileEntity(pos);
	}

	/* Registration ******************************************************** */
	public static ItemLogisticsPipe registerPipe(Class<? extends CoreUnroutedPipe> clas) {
		ItemLogisticsPipe item = new ItemLogisticsPipe();
		item.setUnlocalizedName(clas.getSimpleName());
		GameRegistry.registerItem(item, item.getUnlocalizedName());

		LogisticsBlockGenericPipe.pipes.put(item, clas);

		CoreUnroutedPipe dummyPipe = LogisticsBlockGenericPipe.createPipe(item);
		if (dummyPipe != null) {
			item.setPipeIconIndex(dummyPipe.getIconIndexForItem(), dummyPipe.getTextureIndex());
			MainProxy.proxy.setIconProviderFromPipe(item, dummyPipe);
			item.setDummyPipe(dummyPipe);
		}

		return item;
	}

	public static boolean isPipeRegistered(int key) {
		return LogisticsBlockGenericPipe.pipes.containsKey(key);
	}

	public static CoreUnroutedPipe createPipe(Item key) {
		Class<? extends CoreUnroutedPipe> pipe = LogisticsBlockGenericPipe.pipes.get(key);
		if (pipe != null) {
			try {
				return pipe.getConstructor(Item.class).newInstance(key);
			} catch (ReflectiveOperationException e) {
				LogisticsPipes.log.error("Could not construct class " + pipe.getSimpleName() + " for key " + key, e);
			}
		} else {
			LogisticsPipes.log.warn("Detected pipe with unknown key (" + key + "). Did you remove a buildcraft addon?");
		}

		return null;
	}

	public static boolean placePipe(CoreUnroutedPipe pipe, World world, BlockPos pos, IBlockState state) {
		return LogisticsBlockGenericPipe.placePipe(pipe, world, pos, state, null);
	}

	public static boolean placePipe(CoreUnroutedPipe pipe, World world, BlockPos posIn, IBlockState state, ITubeOrientation orientation) {
		if (world.isRemote) {
			return true;
		}

		boolean placed = world.setBlockState(posIn, state, 2);

		if (placed) {
			TileEntity tile = world.getTileEntity(posIn);
			if (tile instanceof LogisticsTileGenericPipe) {
				LogisticsTileGenericPipe tilePipe = (LogisticsTileGenericPipe) tile;
				if (pipe instanceof CoreMultiBlockPipe) {
					if (orientation == null) {
						throw new NullPointerException();
					}
					CoreMultiBlockPipe mPipe = (CoreMultiBlockPipe) pipe;
					orientation.setOnPipe(mPipe);
					DoubleCoordinates placeAt = new DoubleCoordinates(posIn);
					LogisticsBlockGenericSubMultiBlock.currentCreatedMultiBlock = placeAt;
					LPPositionSet<DoubleCoordinatesType<CoreMultiBlockPipe.SubBlockTypeForShare>> positions = ((CoreMultiBlockPipe) pipe).getSubBlocks();
					orientation.rotatePositions(positions);
					for (DoubleCoordinatesType<CoreMultiBlockPipe.SubBlockTypeForShare> pos : positions) {
						pos.add(placeAt);
						TileEntity subTile = world.getTileEntity(pos.getBlockPos());
						if(subTile instanceof LogisticsTileGenericSubMultiBlock) {
							((LogisticsTileGenericSubMultiBlock) subTile).addMultiBlockMainPos(placeAt);
							((LogisticsTileGenericSubMultiBlock) subTile).addSubTypeTo(pos.getType());
							MainProxy.sendPacketToAllWatchingChunk(subTile, ((LogisticsTileGenericSubMultiBlock) subTile).getLPDescriptionPacket());
						} else {
							world.setBlockState(pos.getBlockPos(), LogisticsPipes.LogisticsSubMultiBlock.getDefaultState(), 2);
							subTile = world.getTileEntity(pos.getBlockPos());
							if (subTile instanceof LogisticsTileGenericSubMultiBlock) {
								((LogisticsTileGenericSubMultiBlock) subTile).addSubTypeTo(pos.getType());
							}
						}
						world.notifyBlockOfStateChange(pos.getBlockPos(), LogisticsPipes.LogisticsSubMultiBlock);
					}
					LogisticsBlockGenericSubMultiBlock.currentCreatedMultiBlock = null;
				}
				tilePipe.initialize(pipe);
				tilePipe.sendUpdateToClient();
			}
			world.notifyBlockOfStateChange(posIn, state.getBlock());
		}

		return placed;
	}

	public static CoreUnroutedPipe getPipe(IBlockAccess blockAccess, BlockPos pos) {
		TileEntity tile = blockAccess.getTileEntity(pos);

		if (!(tile instanceof LogisticsTileGenericPipe) || tile.isInvalid()) {
			return null;
		} else {
			return ((LogisticsTileGenericPipe) tile).pipe;
		}
	}

	public static boolean isFullyDefined(CoreUnroutedPipe pipe) {
		return pipe != null && pipe.transport != null && pipe.container != null;
	}

	public static boolean isValid(CoreUnroutedPipe pipe) {
		return LogisticsBlockGenericPipe.isFullyDefined(pipe);
	}

	private static void cacheTileToPreventRemoval(CoreUnroutedPipe pipe) {
		final World worldCache = pipe.getWorld();
		final BlockPos posCache = pipe.getPos();
		final TileEntity tileCache = pipe.container;
		final CoreUnroutedPipe fPipe = pipe;
		fPipe.setPreventRemove(true);
		QueuedTasks.queueTask(() -> {
			if (!fPipe.preventRemove()) {
				return null;
			}
			boolean changed = false;
			if (worldCache.getBlockState(posCache).getBlock() != LogisticsPipes.LogisticsPipeBlock) {
				worldCache.setBlockState(posCache, LogisticsPipes.LogisticsPipeBlock.getDefaultState());
				changed = true;
			}
			if (worldCache.getTileEntity(posCache) != tileCache) {
				worldCache.setTileEntity(posCache, tileCache);
				changed = true;
			}
			if (changed) {
				worldCache.notifyBlockOfStateChange(posCache, LogisticsPipes.LogisticsPipeBlock);
			}
			fPipe.setPreventRemove(false);
			return null;
		});
	}

	@Override
	public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		if (!(world instanceof World) || ((World) world).isRemote) {
			return null;
		}
		ArrayList<ItemStack> list = new ArrayList<>();
		int count = quantityDropped(getDefaultState(), fortune, ((World) world).rand);
		for (int i = 0; i < count; i++) {
			CoreUnroutedPipe pipe = LogisticsBlockGenericPipe.getPipe(world, pos);

			if (pipe == null) {
				pipe = LogisticsBlockGenericPipe.pipeRemoved.get(new DoubleCoordinates(pos));
			}

			if (pipe != null) {
				if (pipe.item != null && (pipe.canBeDestroyed() || pipe.destroyByPlayer())) {
					list.addAll(pipe.dropContents());
					list.add(new ItemStack(pipe.item, 1, damageDropped(state)));
				} else if (pipe.item != null) {
					LogisticsBlockGenericPipe.cacheTileToPreventRemoval(pipe);
				}
			}
		}
		return list;
	}

	// TODO: Rendering
	/* @Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "all" })
	public IIcon getIcon(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		TileEntity tile = iblockaccess.getTileEntity(i, j, k);
		if (!(tile instanceof LogisticsTileGenericPipe)) {
			return null;
		}
		if (((LogisticsTileGenericPipe) tile).pipe instanceof PipeBlockRequestTable) {
			PipeBlockRequestTable table = (PipeBlockRequestTable) ((LogisticsTileGenericPipe) tile).pipe;
			return table.getTextureFor(l);
		}
		if (((LogisticsTileGenericPipe) tile).renderState.textureArray != null) {
			return ((LogisticsTileGenericPipe) tile).renderState.textureArray[l];
		}
		return ((LogisticsTileGenericPipe) tile).renderState.currentTexture;
	} */

	@Override
	@SuppressWarnings({ "rawtypes" })
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> arraylist, Entity par7Entity) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof LogisticsTileGenericPipe && ((LogisticsTileGenericPipe) tile).pipe instanceof PipeBlockRequestTable) {
			setBlockBounds(0, 0, 0, 1, 1, 1);
			super.addCollisionBoxToList(state, world, pos, axisalignedbb, arraylist, par7Entity);
			return;
		}
		if (tile instanceof LogisticsTileGenericPipe && ((LogisticsTileGenericPipe) tile).pipe != null && ((LogisticsTileGenericPipe) tile).pipe.isMultiBlock()) {
			((CoreMultiBlockPipe) ((LogisticsTileGenericPipe) tile).pipe).addCollisionBoxesToList(arraylist, axisalignedbb);
			if (!((LogisticsTileGenericPipe) tile).pipe.actAsNormalPipe()) {
				return;
			}
		}
		setBlockBounds(LPConstants.PIPE_MIN_POS, LPConstants.PIPE_MIN_POS, LPConstants.PIPE_MIN_POS, LPConstants.PIPE_MAX_POS, LPConstants.PIPE_MAX_POS, LPConstants.PIPE_MAX_POS);
		super.addCollisionBoxToList(state, world, pos, axisalignedbb, arraylist, par7Entity);
		if (tile instanceof LogisticsTileGenericPipe) {
			LogisticsTileGenericPipe tileG = (LogisticsTileGenericPipe) tile;

			if (tileG.isPipeConnected(EnumFacing.WEST)) {
				setBlockBounds(0.0F, LPConstants.PIPE_MIN_POS, LPConstants.PIPE_MIN_POS, LPConstants.PIPE_MAX_POS, LPConstants.PIPE_MAX_POS, LPConstants.PIPE_MAX_POS);
				super.addCollisionBoxToList(state, world, pos, axisalignedbb, arraylist, par7Entity);
			}

			if (tileG.isPipeConnected(EnumFacing.EAST)) {
				setBlockBounds(LPConstants.PIPE_MIN_POS, LPConstants.PIPE_MIN_POS, LPConstants.PIPE_MIN_POS, 1.0F, LPConstants.PIPE_MAX_POS, LPConstants.PIPE_MAX_POS);
				super.addCollisionBoxToList(state, world, pos, axisalignedbb, arraylist, par7Entity);
			}

			if (tileG.isPipeConnected(EnumFacing.DOWN)) {
				setBlockBounds(LPConstants.PIPE_MIN_POS, 0.0F, LPConstants.PIPE_MIN_POS, LPConstants.PIPE_MAX_POS, LPConstants.PIPE_MAX_POS, LPConstants.PIPE_MAX_POS);
				super.addCollisionBoxToList(state, world, pos, axisalignedbb, arraylist, par7Entity);
			}

			if (tileG.isPipeConnected(EnumFacing.UP)) {
				setBlockBounds(LPConstants.PIPE_MIN_POS, LPConstants.PIPE_MIN_POS, LPConstants.PIPE_MIN_POS, LPConstants.PIPE_MAX_POS, 1.0F, LPConstants.PIPE_MAX_POS);
				super.addCollisionBoxToList(state, world, pos, axisalignedbb, arraylist, par7Entity);
			}

			if (tileG.isPipeConnected(EnumFacing.NORTH)) {
				setBlockBounds(LPConstants.PIPE_MIN_POS, LPConstants.PIPE_MIN_POS, 0.0F, LPConstants.PIPE_MAX_POS, LPConstants.PIPE_MAX_POS, LPConstants.PIPE_MAX_POS);
				super.addCollisionBoxToList(state, world, pos, axisalignedbb, arraylist, par7Entity);
			}

			if (tileG.isPipeConnected(EnumFacing.SOUTH)) {
				setBlockBounds(LPConstants.PIPE_MIN_POS, LPConstants.PIPE_MIN_POS, LPConstants.PIPE_MIN_POS, LPConstants.PIPE_MAX_POS, LPConstants.PIPE_MAX_POS, 1.0F);
				super.addCollisionBoxToList(state, world, pos, axisalignedbb, arraylist, par7Entity);
			}

			float facadeThickness = LPConstants.FACADE_THICKNESS;

			if (tileG.tilePart.hasEnabledFacade(EnumFacing.EAST)) {
				setBlockBounds(1 - facadeThickness, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				super.addCollisionBoxToList(state, world, pos, axisalignedbb, arraylist, par7Entity);
			}

			if (tileG.tilePart.hasEnabledFacade(EnumFacing.WEST)) {
				setBlockBounds(0.0F, 0.0F, 0.0F, facadeThickness, 1.0F, 1.0F);
				super.addCollisionBoxToList(state, world, pos, axisalignedbb, arraylist, par7Entity);
			}

			if (tileG.tilePart.hasEnabledFacade(EnumFacing.UP)) {
				setBlockBounds(0.0F, 1 - facadeThickness, 0.0F, 1.0F, 1.0F, 1.0F);
				super.addCollisionBoxToList(state, world, pos, axisalignedbb, arraylist, par7Entity);
			}

			if (tileG.tilePart.hasEnabledFacade(EnumFacing.DOWN)) {
				setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, facadeThickness, 1.0F);
				super.addCollisionBoxToList(state, world, pos, axisalignedbb, arraylist, par7Entity);
			}

			if (tileG.tilePart.hasEnabledFacade(EnumFacing.SOUTH)) {
				setBlockBounds(0.0F, 0.0F, 1 - facadeThickness, 1.0F, 1.0F, 1.0F);
				super.addCollisionBoxToList(state, world, pos, axisalignedbb, arraylist, par7Entity);
			}

			if (tileG.tilePart.hasEnabledFacade(EnumFacing.NORTH)) {
				setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, facadeThickness);
				super.addCollisionBoxToList(state, world, pos, axisalignedbb, arraylist, par7Entity);
			}
		}
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Deprecated
	private void setBlockBounds(float a, float b, float c, float d, float e, float f) {
		setBlockBounds(new AxisAlignedBB(a, b, c, d, e, f));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof LogisticsTileGenericPipe && ((LogisticsTileGenericPipe) tile).pipe instanceof PipeBlockRequestTable) {
			return new AxisAlignedBB(pos);
		}
		RaytraceResult rayTraceResult = null;
		if(bypassPlayerTrace == null) {
			rayTraceResult = doRayTrace(world, pos, Minecraft.getMinecraft().thePlayer);
		} else {
			rayTraceResult = bypassPlayerTrace;
		}

		if (rayTraceResult != null && rayTraceResult.boundingBox != null) {
			AxisAlignedBB box = rayTraceResult.boundingBox;
			switch (rayTraceResult.hitPart) {
				case Pluggable: {
					float scale = 0.001F;
					box = box.expand(scale, scale, scale);
					break;
				}
				case Pipe: {
					float scale = 0.001F;
					box = box.expand(scale, scale, scale);
					break;
				}
			}
			return box.offset(pos);
		}
		return super.getSelectedBoundingBox(state, world, pos).expand(-0.85F, -0.85F, -0.85F);
	}

	@Override
	public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d origin, Vec3d direction) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof LogisticsTileGenericPipe && ((LogisticsTileGenericPipe) tile).pipe instanceof PipeBlockRequestTable) {
			// setBlockBoundsBasedOnState(world, pos);
			origin = origin.subtract(new Vec3d(pos));
			direction = direction.subtract(new Vec3d(pos));
			this.setBlockBounds(0, 0, 0, 1, 1, 1);
			Vec3d vec32 = origin.getIntermediateWithXValue(direction, bounds.minX);
			Vec3d vec33 = origin.getIntermediateWithXValue(direction, bounds.maxX);
			Vec3d vec34 = origin.getIntermediateWithYValue(direction, bounds.minY);
			Vec3d vec35 = origin.getIntermediateWithYValue(direction, bounds.maxY);
			Vec3d vec36 = origin.getIntermediateWithZValue(direction, bounds.minZ);
			Vec3d vec37 = origin.getIntermediateWithZValue(direction, bounds.maxZ);
			if (!isVecInsideYZBounds(vec32)) {
				vec32 = null;
			}
			if (!isVecInsideYZBounds(vec33)) {
				vec33 = null;
			}
			if (!isVecInsideXZBounds(vec34)) {
				vec34 = null;
			}
			if (!isVecInsideXZBounds(vec35)) {
				vec35 = null;
			}
			if (!isVecInsideXYBounds(vec36)) {
				vec36 = null;
			}
			if (!isVecInsideXYBounds(vec37)) {
				vec37 = null;
			}
			Vec3d vec38 = null;
			if (vec32 != null && (vec38 == null || origin.squareDistanceTo(vec32) < origin.squareDistanceTo(vec38))) {
				vec38 = vec32;
			}
			if (vec33 != null && (vec38 == null || origin.squareDistanceTo(vec33) < origin.squareDistanceTo(vec38))) {
				vec38 = vec33;
			}
			if (vec34 != null && (vec38 == null || origin.squareDistanceTo(vec34) < origin.squareDistanceTo(vec38))) {
				vec38 = vec34;
			}
			if (vec35 != null && (vec38 == null || origin.squareDistanceTo(vec35) < origin.squareDistanceTo(vec38))) {
				vec38 = vec35;
			}
			if (vec36 != null && (vec38 == null || origin.squareDistanceTo(vec36) < origin.squareDistanceTo(vec38))) {
				vec38 = vec36;
			}
			if (vec37 != null && (vec38 == null || origin.squareDistanceTo(vec37) < origin.squareDistanceTo(vec38))) {
				vec38 = vec37;
			}
			if (vec38 == null) {
				return null;
			} else {
				byte b0 = -1;
				if (vec38 == vec32) {
					b0 = 4;
				}
				if (vec38 == vec33) {
					b0 = 5;
				}
				if (vec38 == vec34) {
					b0 = 0;
				}
				if (vec38 == vec35) {
					b0 = 1;
				}
				if (vec38 == vec36) {
					b0 = 2;
				}
				if (vec38 == vec37) {
					b0 = 3;
				}
				return new RayTraceResult(vec38.add(new Vec3d(pos)), b0 >= 0 ? EnumFacing.getFront(b0) : null, pos);
			}
		}
		RaytraceResult raytraceResult = doRayTrace(world, pos, origin, direction);

		if (raytraceResult == null) {
			return null;
		} else {
			return raytraceResult.RayTraceResult;
		}
	}

	public RaytraceResult doRayTrace(World world, BlockPos pos, EntityPlayer player) {
		double reachDistance = 5;

		if (player instanceof EntityPlayerMP) {
			reachDistance = ((EntityPlayerMP) player).interactionManager.getBlockReachDistance();
		}

		double eyeHeight = world.isRemote ? player.getEyeHeight() - player.getDefaultEyeHeight() : player.getEyeHeight();
		Vec3d lookVec = player.getLookVec();
		Vec3d origin = new Vec3d(player.posX, player.posY + eyeHeight, player.posZ);
		Vec3d direction = origin.addVector(lookVec.xCoord * reachDistance, lookVec.yCoord * reachDistance, lookVec.zCoord * reachDistance);

		return doRayTrace(world, pos, origin, direction);
	}

	public RaytraceResult doRayTrace(World world, BlockPos pos, Vec3d origin, Vec3d direction) {
		TileEntity pipeTileEntity = world.getTileEntity(pos);

		LogisticsTileGenericPipe tileG = null;
		if (pipeTileEntity instanceof LogisticsTileGenericPipe) {
			tileG = (LogisticsTileGenericPipe) pipeTileEntity;
			CoreUnroutedPipe pipe = tileG.pipe;
			if (!LogisticsBlockGenericPipe.isValid(pipe)) {
				return null;
			}
			if (pipe.isMultiBlock()) {
				RaytraceResult result1 = doRayTrace(tileG, (CoreMultiBlockPipe) pipe, origin, direction);
				if (!pipe.actAsNormalPipe()) {
					return result1;
				}
				RaytraceResult result2 = doRayTrace(tileG, pipe, origin, direction);
				if (result1 == null) {
					return result2;
				} else if (result2 == null) {
					return result1;
				}

				double length1 = result1.RayTraceResult.hitVec.squareDistanceTo(origin);
				double length2 = result2.RayTraceResult.hitVec.squareDistanceTo(origin);

				if (length1 < length2) {
					return result1;
				} else {
					return result2;
				}
			} else {
				return doRayTrace(tileG, pipe, origin, direction);
			}
		}
		return null;
	}

	private RaytraceResult doRayTrace(LogisticsTileGenericPipe tileG, CoreUnroutedPipe pipe, Vec3d origin, Vec3d direction) {
		if (tileG == null) {
			return null;
		}
		if (!LogisticsBlockGenericPipe.isValid(pipe)) {
			return null;
		}

		/**
		 * pipe hits along x, y, and z axis, gate (all 6 sides) [and
		 * wires+facades]
		 */
		RayTraceResult[] hits = new RayTraceResult[31];
		AxisAlignedBB[] boxes = new AxisAlignedBB[31];
		EnumFacing[] sideHit = new EnumFacing[31];

		// pipe
		for (EnumFacing side : LogisticsBlockGenericPipe.DIR_VALUES) {
			if (side == null || tileG.isPipeConnected(side)) {
				if(side != null && ignoreSideRayTrace) continue;
				AxisAlignedBB bb = getPipeBoundingBox(side);
				setBlockBounds(bb);
				boxes[side.ordinal()] = bb;
				hits[side.ordinal()] = super.collisionRayTrace(getDefaultState(), tileG
						.getWorld(), tileG.getPos(), origin, direction);
				sideHit[side.ordinal()] = side;
			}
		}

		// pluggables

		for (EnumFacing side : EnumFacing.VALUES) {
			if(side != null && ignoreSideRayTrace) continue;

			IBCPipePluggable pluggable = tileG.getBCPipePluggable(side);
			if (pluggable != null) {
				AxisAlignedBB bb = pluggable.getBoundingBox(side);
				setBlockBounds(bb);
				boxes[7 + side.ordinal()] = bb;
				hits[7 + side.ordinal()] = super.collisionRayTrace(getDefaultState(), tileG
						.getWorld(), tileG.getPos(), origin, direction);
				sideHit[7 + side.ordinal()] = side;
			}
		}

		// TODO: check wires

		// get closest hit

		double minLengthSquared = Double.POSITIVE_INFINITY;
		int minIndex = -1;

		for (int i = 0; i < hits.length; i++) {
			RayTraceResult hit = hits[i];
			if (hit == null) {
				continue;
			}

			double lengthSquared = hit.hitVec.squareDistanceTo(origin);

			if (lengthSquared < minLengthSquared) {
				minLengthSquared = lengthSquared;
				minIndex = i;
			}
		}

		// reset bounds

		setBlockBounds(0, 0, 0, 1, 1, 1);

		if (minIndex == -1) {
			return null;
		} else {
			Part hitPart;

			if (minIndex < 7) {
				hitPart = Part.Pipe;
			} else {
				hitPart = Part.Pluggable;
			}

			return new RaytraceResult(hitPart, hits[minIndex], boxes[minIndex], sideHit[minIndex]);
		}
	}

	private RaytraceResult doRayTrace(LogisticsTileGenericPipe tileG, CoreMultiBlockPipe pipe, Vec3d origin, Vec3d direction) {
		if (tileG == null) {
			return null;
		}
		if (!LogisticsBlockGenericPipe.isValid(pipe)) {
			return null;
		}

		List<RayTraceResult> hits = new ArrayList<>();
		List<AxisAlignedBB> boxes = new ArrayList<>();

		pipe.addCollisionBoxesToList(boxes, null);

		while (hits.size() < boxes.size()) {
			hits.add(null);
		}

		for (int i = 0; i < boxes.size(); i++) {
			AxisAlignedBB bb = boxes.get(i);
			setBlockBoundsFromAbsolut(bb, tileG);
			hits.set(i, super.collisionRayTrace(this.getDefaultState(), tileG.getWorld(), tileG.getPos(), origin, direction));
		}

		double minLengthSquared = Double.POSITIVE_INFINITY;
		int minIndex = -1;

		for (int i = 0; i < hits.size(); i++) {
			RayTraceResult hit = hits.get(i);
			if (hit == null) {
				continue;
			}

			double lengthSquared = hit.hitVec.squareDistanceTo(origin);

			if (lengthSquared < minLengthSquared) {
				minLengthSquared = lengthSquared;
				minIndex = i;
			}
		}

		// reset bounds

		setBlockBounds(0, 0, 0, 1, 1, 1);

		if (minIndex == -1) {
			return null;
		} else {
			return new RaytraceResult(Part.Pipe, hits.get(minIndex),
			//*
					pipe.getCompleteBox()
					/*/
					boxes.get(minIndex).getOffsetBoundingBox(-tileG.xCoord, -tileG.yCoord, -tileG.zCoord)
					//*/
					, null);
		}
	}

	private void setBlockBounds(AxisAlignedBB bb) {
		this.bounds = bb;
	}

	private void setBlockBoundsFromAbsolut(AxisAlignedBB bb, TileEntity tile) {
		setBlockBounds((float) bb.minX - tile.getPos().getX(),
				(float) bb.minY - tile.getPos().getY(),
				(float) bb.minZ - tile.getPos().getZ(),
				(float) bb.maxX - tile.getPos().getX(),
				(float) bb.maxY - tile.getPos().getY(),
				(float) bb.maxZ - tile.getPos().getZ());
	}

	private AxisAlignedBB getPipeBoundingBox(EnumFacing side) {
		float min = LPConstants.PIPE_MIN_POS;
		float max = LPConstants.PIPE_MAX_POS;

		if (side == null) {
			return new AxisAlignedBB(min, min, min, max, max, max);
		}

		float[][] bounds = new float[3][2];
		// X START - END
		bounds[0][0] = min;
		bounds[0][1] = max;
		// Y START - END
		bounds[1][0] = 0;
		bounds[1][1] = min;
		// Z START - END
		bounds[2][0] = min;
		bounds[2][1] = max;

		MatrixTranformations.transform(bounds, side);
		return new AxisAlignedBB(bounds[0][0], bounds[1][0], bounds[2][0], bounds[0][1], bounds[1][1], bounds[2][1]);
	}

	private boolean isVecInsideYZBounds(Vec3d par1Vec3d) {
		return par1Vec3d != null && (par1Vec3d.yCoord >= bounds.minY && par1Vec3d.yCoord <= bounds.maxY && par1Vec3d.zCoord >= bounds.minZ && par1Vec3d.zCoord <= bounds.maxZ);
	}

	private boolean isVecInsideXZBounds(Vec3d par1Vec3d) {
		return par1Vec3d != null && (par1Vec3d.xCoord >= bounds.minX && par1Vec3d.xCoord <= bounds.maxX && par1Vec3d.zCoord >= bounds.minZ && par1Vec3d.zCoord <= bounds.maxZ);
	}

	private boolean isVecInsideXYBounds(Vec3d par1Vec3d) {
		return par1Vec3d != null && (par1Vec3d.xCoord >= bounds.minX && par1Vec3d.xCoord <= bounds.maxX && par1Vec3d.yCoord >= bounds.minY && par1Vec3d.yCoord <= bounds.maxY);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new LogisticsTileGenericPipe();
	}

	@Override
	public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
		return Configs.pipeDurability;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}

	public void setRenderMask(int mask) {
		renderMask = mask;
	}

	public final void setRenderAllSides() {
		renderMask = 0x3f;
	}

	public void setRenderSide(EnumFacing side, boolean render) {
		if (render) {
			renderMask |= 1 << side.ordinal();
		} else {
			renderMask &= ~(1 << side.ordinal());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return (renderMask & (1 << side.ordinal())) != 0;
	}

	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof LogisticsTileGenericPipe) {
			if (((LogisticsTileGenericPipe) tile).pipe instanceof PipeBlockRequestTable) {
				return true;
			}
			return ((LogisticsTileGenericPipe) tile).isSolidOnSide(side);
		}

		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state) {
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		LogisticsBlockGenericPipe.removePipe(LogisticsBlockGenericPipe.getPipe(world, pos));
		super.breakBlock(world, pos, state);
		SimpleServiceLocator.buildCraftProxy.callBCRemovePipe(world, pos);
	}

	@Override
	public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, float f, int dmg) {

		if (world.isRemote) {
			return;
		}

		int i1 = quantityDropped(world.rand);
		for (int j1 = 0; j1 < i1; j1++) {
			if (world.rand.nextFloat() > f) {
				continue;
			}

			CoreUnroutedPipe pipe = LogisticsBlockGenericPipe.getPipe(world, pos);

			if (pipe == null) {
				pipe = LogisticsBlockGenericPipe.pipeRemoved.get(new DoubleCoordinates(pos));
			}

			if (pipe.item != null && (pipe.canBeDestroyed() || pipe.destroyByPlayer())) {
				for (ItemStack stack : pipe.dropContents()) {
					spawnAsEntity(world, pos, stack);
				}
				spawnAsEntity(world, pos, new ItemStack(pipe.item, 1, damageDropped(state)));
			} else if (pipe.item != null) {
				LogisticsBlockGenericPipe.cacheTileToPreventRemoval(pipe);
			}
		}
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		// Returns null to be safe - the id does not depend on the meta
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		RaytraceResult rayTraceResult = doRayTrace(world, pos, player);

		if (rayTraceResult != null && rayTraceResult.boundingBox != null) {
			switch (rayTraceResult.hitPart) {
				case Pluggable: {
					CoreUnroutedPipe pipe = LogisticsBlockGenericPipe.getPipe(world, pos);
					IBCPipePluggable pluggable = pipe.container.tilePart.getBCPipePluggable(rayTraceResult.sideHit);
					if (pluggable != null) {
						ItemStack[] drops = pluggable.getDropItems(pipe.container);
						if (drops != null && drops.length > 0) {
							return drops[0];
						}
					}
				}
				case Pipe:
					return new ItemStack(LogisticsBlockGenericPipe.getPipe(world, pos).item);
			}
		}
		return null;
	}

	/* Wrappers ************************************************************ */
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block) {
		super.neighborChanged(state, world, pos, block);

		CoreUnroutedPipe pipe = LogisticsBlockGenericPipe.getPipe(world, pos);

		if (LogisticsBlockGenericPipe.isValid(pipe)) {
			pipe.container.scheduleNeighborChange();
		}
		SimpleServiceLocator.buildCraftProxy.callBCNeighborBlockChange(world, pos, block);
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float par6, float par7, float par8, int meta, EntityLivingBase placer) {
		super.onBlockPlaced(world, pos, facing, par6, par7, par8, meta, placer);
		CoreUnroutedPipe pipe = LogisticsBlockGenericPipe.getPipe(world, pos);

		if (LogisticsBlockGenericPipe.isValid(pipe)) {
			pipe.onBlockPlaced();
		}

		return getStateFromMeta(meta);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		CoreUnroutedPipe pipe = LogisticsBlockGenericPipe.getPipe(world, pos);

		if (LogisticsBlockGenericPipe.isValid(pipe)) {
			pipe.onBlockPlacedBy(placer);
			if (pipe instanceof IRotationProvider) {
				double xPos = pipe.getX() + 0.5 - placer.posX;
				double zPos = pipe.getZ() + 0.5 - placer.posZ;
				double w = Math.atan2(xPos, zPos);
				double halfPI = Math.PI / 2;
				double halfhalfPI = halfPI / 2;
				w -= halfhalfPI;
				if (w < 0) {
					w += 2 * Math.PI;
				}
				if (0 < w && w <= halfPI) {
					((IRotationProvider) pipe).setRotation(1);
				} else if (halfPI < w && w <= 2 * halfPI) {
					((IRotationProvider) pipe).setRotation(2);
				} else if (2 * halfPI < w && w <= 3 * halfPI) {
					((IRotationProvider) pipe).setRotation(0);
				} else if (3 * halfPI < w && w <= 4 * halfPI) {
					((IRotationProvider) pipe).setRotation(3);
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float xOffset, float yOffset, float zOffset) {
		super.onBlockActivated(world, pos, state, player, hand, heldItem, side, xOffset, yOffset, zOffset);

		world.notifyNeighborsRespectDebug(pos, LogisticsPipes.LogisticsPipeBlock);

		CoreUnroutedPipe pipe = LogisticsBlockGenericPipe.getPipe(world, pos);

		if (LogisticsBlockGenericPipe.isValid(pipe)) {
			ItemStack currentItem = heldItem;

			if (currentItem == null) {
				// Fall through the end of the test
			} else if (currentItem.getItem() == Items.SIGN) {
				// Sign will be placed anyway, so lets show the sign gui
				return false;
			} else if (currentItem.getItem() instanceof ItemLogisticsPipe) {
				return false;
			} else if (SimpleServiceLocator.toolWrenchHandler.isWrench(currentItem.getItem())) {
				// Only check the instance at this point. Call the IToolWrench
				// interface callbacks for the individual pipe/logic calls
				return pipe.blockActivated(player);
			}
			if (pipe.canHoldBCParts()) {
				IBCClickResult result = SimpleServiceLocator.buildCraftProxy.handleBCClickOnPipe(world, pos, player, side, xOffset, yOffset, zOffset, pipe);
				if (result.handled()) {
					return true;
				}
				if (result.blocked()) {
					return false;
				}
			}
			return pipe.blockActivated(player);
		}

		return false;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		super.onEntityCollidedWithBlock(world, pos, state, entity);

		CoreUnroutedPipe pipe = LogisticsBlockGenericPipe.getPipe(world, pos);

		if (LogisticsBlockGenericPipe.isValid(pipe)) {
			pipe.onEntityCollidedWithBlock(entity);
		}
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		CoreUnroutedPipe pipe = LogisticsBlockGenericPipe.getPipe(world, pos);

		if (LogisticsBlockGenericPipe.isValid(pipe)) {
			return pipe.bcPipePart.canConnectRedstone();
		} else {
			return false;
		}
	}

	@Override
	public int getStrongPower(IBlockState state, IBlockAccess iblockaccess, BlockPos pos, EnumFacing dir) {
		CoreUnroutedPipe pipe = LogisticsBlockGenericPipe.getPipe(iblockaccess, pos);

		if (LogisticsBlockGenericPipe.isValid(pipe)) {
			return pipe.bcPipePart.isPoweringTo(dir);
		} else {
			return 0;
		}
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing dir) {
		CoreUnroutedPipe pipe = LogisticsBlockGenericPipe.getPipe(world, pos);

		if (LogisticsBlockGenericPipe.isValid(pipe)) {
			return pipe.bcPipePart.isIndirectlyPoweringTo(dir);
		} else {
			return 0;
		}
	}

	@Override
	public String getUnlocalizedName() {
		return "LogisticsPipes Pipe Block";
	}

	@Override
	public String getLocalizedName() {
		return getUnlocalizedName();
	}

	/* @Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		LogisticsNewRenderPipe.registerTextures(iconRegister);
		SimpleServiceLocator.thermalDynamicsProxy.registerTextures(iconRegister);
		if (!skippedFirstIconRegister) {
			skippedFirstIconRegister = true;
			return;
		}
		for (Item i : LogisticsBlockGenericPipe.pipes.keySet()) {
			CoreUnroutedPipe dummyPipe = LogisticsBlockGenericPipe.createPipe(i);
			if (dummyPipe != null) {
				dummyPipe.getIconProvider().registerIcons(iconRegister);
			}
		}
	} */
	// TODO: Rendering

	/* @SideOnly(Side.CLIENT)
	@Override
	public boolean addHitEffects(World worldObj, RayTraceResult target, EffectRenderer effectRenderer) {
		int x = target.blockX;
		int y = target.blockY;
		int z = target.blockZ;

		CoreUnroutedPipe pipe = LogisticsBlockGenericPipe.getPipe(worldObj, x, y, z);
		if (pipe == null) {
			return false;
		}

		IIcon icon = pipe.getIconProvider().getIcon(pipe.getIconIndexForItem());

		int sideHit = target.sideHit;

		Block block = LogisticsPipes.LogisticsPipeBlock;
		float b = 0.1F;
		double px = x + rand.nextDouble() * (bounds.maxX - bounds.minX - (b * 2.0F)) + b + bounds.minX;
		double py = y + rand.nextDouble() * (bounds.maxY - bounds.minY - (b * 2.0F)) + b + bounds.minY;
		double pz = z + rand.nextDouble() * (bounds.maxZ - bounds.minZ - (b * 2.0F)) + b + bounds.minZ;

		if (sideHit == 0) {
			py = y + bounds.minY - b;
		}

		if (sideHit == 1) {
			py = y + bounds.maxY + b;
		}

		if (sideHit == 2) {
			pz = z + bounds.minZ - b;
		}

		if (sideHit == 3) {
			pz = z + bounds.maxZ + b;
		}

		if (sideHit == 4) {
			px = x + bounds.minX - b;
		}

		if (sideHit == 5) {
			px = x + bounds.maxX + b;
		}

		EntityDiggingFX fx = new EntityDiggingFX(worldObj, px, py, pz, 0.0D, 0.0D, 0.0D, block, sideHit, worldObj.getBlockMetadata(x, y, z));
		fx.setParticleIcon(icon);
		effectRenderer.addEffect(fx.applyColourMultiplier(x, y, z).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
		return true;
	} */

	/* @SideOnly(Side.CLIENT)
	@Override
	public boolean addDestroyEffects(World worldObj, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
		CoreUnroutedPipe pipe = LogisticsBlockGenericPipe.getPipe(worldObj, x, y, z);
		if (pipe == null) {
			return false;
		}

		PlayerConfig config = LogisticsPipes.getClientPlayerConfig();
		if (config.isUseNewRenderer()) {
			LogisticsNewRenderPipe.renderDestruction(pipe, worldObj, x, y, z, effectRenderer);
		} else {
			IIcon icon = pipe.getIconProvider().getIcon(pipe.getIconIndexForItem());

			byte its = 4;
			for (int i = 0; i < its; ++i) {
				for (int j = 0; j < its; ++j) {
					for (int k = 0; k < its; ++k) {
						if (pipe.isMultiBlock()) {
							LPPositionSet<DoubleCoordinatesType<CoreMultiBlockPipe.SubBlockTypeForShare>> set = ((CoreMultiBlockPipe) pipe).getRotatedSubBlocks();
							set.add(new DoubleCoordinatesType<>(0, 0, 0, CoreMultiBlockPipe.SubBlockTypeForShare.NON_SHARE));
							for (DoubleCoordinates pos : set) {
								int localx = x + pos.getXInt();
								int localy = y + pos.getYInt();
								int localz = z + pos.getZInt();
								double px = localx + (i + 0.5D) / its;
								double py = localy + (j + 0.5D) / its;
								double pz = localz + (k + 0.5D) / its;
								int random = rand.nextInt(6);
								EntityDiggingFX fx = new ParticleDigging(worldObj, px, py, pz, px - localx - 0.5D, py - localy - 0.5D, pz - localz - 0.5D, LogisticsPipes.LogisticsPipeBlock, random, meta);
								fx.setParticleIcon(icon);
								effectRenderer.addEffect(fx.applyColourMultiplier(x, y, z));
							}
						} else {
							double px = x + (i + 0.5D) / its;
							double py = y + (j + 0.5D) / its;
							double pz = z + (k + 0.5D) / its;
							int random = rand.nextInt(6);
							EntityDiggingFX fx = new EntityDiggingFX(worldObj, px, py, pz, px - x - 0.5D, py - y - 0.5D, pz - z - 0.5D, LogisticsPipes.LogisticsPipeBlock, random, meta);
							fx.setParticleIcon(icon);
							effectRenderer.addEffect(fx.applyColourMultiplier(x, y, z));
						}
					}
				}
			}
		}
		return true;
	} */

	public enum Part {
		Pipe,
		Pluggable
	}

	public static class RaytraceResult {

		public final Part hitPart;
		public final RayTraceResult RayTraceResult;
		public final AxisAlignedBB boundingBox;
		public final EnumFacing sideHit;

		RaytraceResult(Part hitPart, RayTraceResult RayTraceResult, AxisAlignedBB boundingBox, EnumFacing side) {
			this.hitPart = hitPart;
			this.RayTraceResult = RayTraceResult;
			this.boundingBox = boundingBox;
			sideHit = side;
		}

		@Override
		public String toString() {
			return String.format("RayTraceResult: %s, %s", hitPart == null ? "null" : hitPart.name(), boundingBox == null ? "null" : boundingBox.toString());
		}
	}
}
