package logisticspipes.pipes.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import logisticspipes.LPConstants;
import logisticspipes.LogisticsPipes;
import logisticspipes.proxy.MainProxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import network.rs485.logisticspipes.world.DoubleCoordinates;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LogisticsBlockGenericSubMultiBlock extends BlockContainer {

	protected final Random rand = new Random();

	public LogisticsBlockGenericSubMultiBlock() {
		super(Material.GLASS);
		setCreativeTab(null);
	}

	@Override
	public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ArrayList<ItemStack> list = new ArrayList<>();
		return list;
	}

	/* @Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return LogisticsPipes.LogisticsPipeBlock.getIcon(p_149691_1_, p_149691_2_);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "all" })
	public IIcon getIcon(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		DoubleCoordinates pos = new DoubleCoordinates(i, j, k);
		TileEntity tile = pos.getTileEntity(iblockaccess);
		if (tile instanceof LogisticsTileGenericSubMultiBlock) {
			List<LogisticsTileGenericPipe> mainPipe = ((LogisticsTileGenericSubMultiBlock) tile).getMainPipe();
			if (!mainPipe.isEmpty() && mainPipe.get(0).pipe != null && mainPipe.get(0).pipe.isMultiBlock()) {
				return LogisticsPipes.LogisticsPipeBlock.getIcon(iblockaccess, mainPipe.get(0).xCoord, mainPipe.get(0).yCoord, mainPipe.get(0).zCoord, l);
			}
		}
		return null;
	} */

	public static DoubleCoordinates currentCreatedMultiBlock;

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		if (LogisticsBlockGenericSubMultiBlock.currentCreatedMultiBlock == null && MainProxy.isServer(p_149915_1_)) {
			new RuntimeException("Unknown MultiBlock controller").printStackTrace();
		}
		return new LogisticsTileGenericSubMultiBlock(LogisticsBlockGenericSubMultiBlock.currentCreatedMultiBlock);
	}

	@Override
	public void breakBlock(World world, BlockPos posIn, IBlockState state) {
		DoubleCoordinates pos = new DoubleCoordinates(posIn);
		TileEntity tile = pos.getTileEntity(world);
		if (tile instanceof LogisticsTileGenericSubMultiBlock) {
			boolean handled = false;
			List<LogisticsTileGenericPipe> mainPipeList = ((LogisticsTileGenericSubMultiBlock) tile).getMainPipe();
			for(LogisticsTileGenericPipe mainPipe:mainPipeList) {
				if (mainPipe != null && mainPipe.pipe != null && mainPipe.pipe.isMultiBlock()) {
					if (LogisticsPipes.LogisticsPipeBlock.doRayTrace(world, mainPipe.getPos(), Minecraft.getMinecraft().thePlayer) != null) {
						DoubleCoordinates mainPipePos = mainPipe.pipe.getLPPosition();
						mainPipePos.setBlockToAir(world);
						handled = true;
					}
				}
			}
			if(!handled) {
				mainPipeList.stream()
						.filter(mainPipe -> mainPipe != null && mainPipe.pipe != null && mainPipe.pipe.isMultiBlock())
						.forEach(mainPipe -> {
							DoubleCoordinates mainPipePos = mainPipe.pipe.getLPPosition();
							mainPipePos.setBlockToAir(world);
						});
			}
		}
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos posIn, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> arraylist, Entity par7Entity) {
		DoubleCoordinates pos = new DoubleCoordinates(posIn);
		TileEntity tile = pos.getTileEntity(world);
		if (tile instanceof LogisticsTileGenericSubMultiBlock) {
			List<LogisticsTileGenericPipe> mainPipeList = ((LogisticsTileGenericSubMultiBlock) tile).getMainPipe();
			mainPipeList.stream()
					.filter(mainPipe -> mainPipe != null && mainPipe.pipe != null && mainPipe.pipe.isMultiBlock())
					.forEach(mainPipe -> LogisticsPipes.LogisticsPipeBlock.addCollisionBoxToList(state, world, mainPipe.getPos(), axisalignedbb, arraylist, par7Entity));
		}
	}

	@Override
	public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos posIn, Vec3d origin, Vec3d direction) {
		DoubleCoordinates pos = new DoubleCoordinates(posIn);
		TileEntity tile = pos.getTileEntity(world);
		if (tile instanceof LogisticsTileGenericSubMultiBlock) {
			List<LogisticsTileGenericPipe> mainPipeList = ((LogisticsTileGenericSubMultiBlock) tile).getMainPipe();
			for(LogisticsTileGenericPipe mainPipe:mainPipeList) {
				if (mainPipe != null && mainPipe.pipe != null && mainPipe.pipe.isMultiBlock()) {
					RayTraceResult result = LogisticsPipes.LogisticsPipeBlock.collisionRayTrace(state, world, mainPipe.getPos(), origin, direction);
					if (result != null) {
						return new RayTraceResult(result.typeOfHit, result.hitVec, result.sideHit, posIn);
					}
				}
			}
		}
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos posIn) {
		DoubleCoordinates pos = new DoubleCoordinates(posIn);
		TileEntity tile = pos.getTileEntity(world);
		if (tile instanceof LogisticsTileGenericSubMultiBlock) {
			List<LogisticsTileGenericPipe> mainPipeList = ((LogisticsTileGenericSubMultiBlock) tile).getMainPipe();
			for(LogisticsTileGenericPipe mainPipe:mainPipeList) {
				if (mainPipe != null && mainPipe.pipe != null && mainPipe.pipe.isMultiBlock()) {
					if (LogisticsPipes.LogisticsPipeBlock.doRayTrace(world, mainPipe.getPos(), Minecraft.getMinecraft().thePlayer) != null) {
						return LogisticsPipes.LogisticsPipeBlock.getSelectedBoundingBox(state, world, mainPipe.getPos());
					}
				}
			}
		}
		return super.getSelectedBoundingBox(state, world, posIn).expand(-0.85F, -0.85F, -0.85F);
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos posIn, Block block) {
		super.neighborChanged(state, world, posIn, block);
		DoubleCoordinates pos = new DoubleCoordinates(posIn);
		TileEntity tile = pos.getTileEntity(world);
		if (tile instanceof LogisticsTileGenericSubMultiBlock) {
			((LogisticsTileGenericSubMultiBlock) tile).scheduleNeighborChange();
		}
	}

	/* @Override
	public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
		DoubleCoordinates pos = new DoubleCoordinates(x, y, z);
		TileEntity tile = pos.getTileEntity(world);
		if (tile instanceof LogisticsTileGenericSubMultiBlock) {
			List<LogisticsTileGenericPipe> mainPipeList = ((LogisticsTileGenericSubMultiBlock) tile).getMainPipe();
			for(LogisticsTileGenericPipe mainPipe:mainPipeList) {
				if (mainPipe != null && mainPipe.pipe != null && mainPipe.pipe.isMultiBlock()) {
					if (LogisticsPipes.LogisticsPipeBlock.doRayTrace(world, mainPipe.xCoord, mainPipe.yCoord, mainPipe.zCoord, Minecraft.getMinecraft().thePlayer) != null) {
						return LogisticsPipes.LogisticsPipeBlock.addDestroyEffects(world, mainPipe.xCoord, mainPipe.yCoord, mainPipe.zCoord, meta, effectRenderer);
					}
				}
			}
		}
		return super.addDestroyEffects(world, x, y, z, meta, effectRenderer);
	} */

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos posIn, EntityPlayer player) {
		DoubleCoordinates pos = new DoubleCoordinates(posIn);
		TileEntity tile = pos.getTileEntity(world);
		if (tile instanceof LogisticsTileGenericSubMultiBlock) {
			List<LogisticsTileGenericPipe> mainPipeList = ((LogisticsTileGenericSubMultiBlock) tile).getMainPipe();
			for(LogisticsTileGenericPipe mainPipe:mainPipeList) {
				if (mainPipe != null && mainPipe.pipe != null && mainPipe.pipe.isMultiBlock()) {
					if (LogisticsPipes.LogisticsPipeBlock.doRayTrace(world, mainPipe.getPos(), Minecraft.getMinecraft().thePlayer) != null) {
						return LogisticsPipes.LogisticsPipeBlock.getPickBlock(state, target, world, mainPipe.getPos(), player);
					}
				}
			}
		}
		return super.getPickBlock(state, target, world, posIn, player);
	}

	/*
	@Override
	public boolean addHitEffects(World worldObj, RayTraceResult target, EffectRenderer effectRenderer) {
		int x = target.blockX;
		int y = target.blockY;
		int z = target.blockZ;
		DoubleCoordinates pos = new DoubleCoordinates(x, y, z);
		TileEntity tile = pos.getTileEntity(worldObj);
		if (tile instanceof LogisticsTileGenericSubMultiBlock) {
			List<LogisticsTileGenericPipe> mainPipeList = ((LogisticsTileGenericSubMultiBlock) tile).getMainPipe();
			for(LogisticsTileGenericPipe mainPipe:mainPipeList) {
				if (mainPipe != null && mainPipe.pipe != null && mainPipe.pipe.isMultiBlock()) {
					if (LogisticsPipes.LogisticsPipeBlock.doRayTrace(worldObj, mainPipe.xCoord, mainPipe.yCoord, mainPipe.zCoord, Minecraft.getMinecraft().thePlayer) != null) {
						CoreUnroutedPipe pipe = mainPipe.pipe;
						if (pipe == null) {
							return false;
						}

						IIcon icon = pipe.getIconProvider().getIcon(pipe.getIconIndexForItem());

						int sideHit = target.sideHit;

						Block block = LogisticsPipes.LogisticsPipeBlock;
						float b = 0.1F;
						double px = x + rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - (b * 2.0F)) + b + block.getBlockBoundsMinX();
						double py = y + rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - (b * 2.0F)) + b + block.getBlockBoundsMinY();
						double pz = z + rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - (b * 2.0F)) + b + block.getBlockBoundsMinZ();

						if (sideHit == 0) {
							py = y + block.getBlockBoundsMinY() - b;
						}

						if (sideHit == 1) {
							py = y + block.getBlockBoundsMaxY() + b;
						}

						if (sideHit == 2) {
							pz = z + block.getBlockBoundsMinZ() - b;
						}

						if (sideHit == 3) {
							pz = z + block.getBlockBoundsMaxZ() + b;
						}

						if (sideHit == 4) {
							px = x + block.getBlockBoundsMinX() - b;
						}

						if (sideHit == 5) {
							px = x + block.getBlockBoundsMaxX() + b;
						}

						EntityDiggingFX fx = new EntityDiggingFX(worldObj, px, py, pz, 0.0D, 0.0D, 0.0D, block, sideHit, worldObj.getBlockMetadata(x, y, z));
						fx.setParticleIcon(icon);
						effectRenderer.addEffect(fx.applyColourMultiplier(x, y, z).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
						return true;
					}
				}
			}
		}
		return super.addHitEffects(worldObj, target, effectRenderer);
	}
	*/
}
