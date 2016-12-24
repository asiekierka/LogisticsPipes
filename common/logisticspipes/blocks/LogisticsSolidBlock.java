package logisticspipes.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraft.util.EnumFacing;

import logisticspipes.LPConstants;
import logisticspipes.LogisticsPipes;
import logisticspipes.blocks.crafting.LogisticsCraftingTableTileEntity;
import logisticspipes.blocks.powertile.LogisticsIC2PowerProviderTileEntity;
import logisticspipes.blocks.powertile.LogisticsPowerJunctionTileEntity;
import logisticspipes.blocks.powertile.LogisticsForgePowerProviderTileEntity;
import logisticspipes.blocks.stats.LogisticsStatisticsTileEntity;
import logisticspipes.interfaces.IGuiTileEntity;
import logisticspipes.interfaces.IRotationProvider;
import logisticspipes.proxy.MainProxy;

import javax.annotation.Nullable;

public class LogisticsSolidBlock extends BlockContainer {
	public enum Type implements IStringSerializable {
		SOLDERING_STATION(0),
		POWER_JUNCTION(1),
		SECURITY_STATION(2),
		AUTOCRAFTING_TABLE(3),
		FUZZYCRAFTING_TABLE(4),
		STATISTICS_TABLE(5),
		FU_POWERPROVIDER(11),
		IC2_POWERPROVIDER(12),
		BLOCK_FRAME(15);

		private static final Type[] byMeta = new Type[16];
		private final int meta;

		Type(int meta) {
			this.meta = meta;
			setMeta(meta);
		}

		private void setMeta(int meta) {
			byMeta[meta] = this;
		}

		public int meta() {
			return meta;
		}

		public static Type byMeta(int meta) {
			return byMeta[meta];
		}

		@Override
		public String getName() {
			return name().toLowerCase();
		}
	}

	public static final PropertyEnum<Type> TYPE = PropertyEnum.create("type", Type.class);

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
	}

	public LogisticsSolidBlock() {
		super(Material.IRON);
		setCreativeTab(LogisticsPipes.LPCreativeTab);
		setHardness(6.0F);
	}

	@Override
	public void onBlockClicked(World par1World, BlockPos pos, EntityPlayer par5EntityPlayer) {
		super.onBlockClicked(par1World, pos, par5EntityPlayer);
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighborPos) {
		super.onNeighborChange(world, pos, neighborPos);
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof LogisticsSolidTileEntity) {
			((LogisticsSolidTileEntity) tile).notifyOfBlockChange();
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!playerIn.isSneaking()) {
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof IGuiTileEntity) {
				if (MainProxy.isServer(playerIn.worldObj)) {
					((IGuiTileEntity) tile).getGuiProvider().setTilePos(tile).open(playerIn);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack itemStack) {
		super.onBlockPlacedBy(world, pos, state, entity, itemStack);
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof LogisticsCraftingTableTileEntity) {
			((LogisticsCraftingTableTileEntity) tile).placedBy(entity);
		}
		if (tile instanceof IRotationProvider) {
			double x = tile.getPos().getX() + 0.5 - entity.posX;
			double z = tile.getPos().getZ() + 0.5 - entity.posZ;
			double w = Math.atan2(x, z);
			double halfPI = Math.PI / 2;
			double halfhalfPI = halfPI / 2;
			w -= halfhalfPI;
			if (w < 0) {
				w += 2 * Math.PI;
			}
			if (0 < w && w <= halfPI) {
				((IRotationProvider) tile).setRotation(1);
			} else if (halfPI < w && w <= 2 * halfPI) {
				((IRotationProvider) tile).setRotation(2);
			} else if (2 * halfPI < w && w <= 3 * halfPI) {
				((IRotationProvider) tile).setRotation(0);
			} else if (3 * halfPI < w && w <= 4 * halfPI) {
				((IRotationProvider) tile).setRotation(3);
			}
		}
	}

	@Override
	public void breakBlock(World par1World, BlockPos pos, IBlockState state) {
		TileEntity tile = par1World.getTileEntity(pos);
		if (tile instanceof LogisticsSolderingTileEntity) {
			((LogisticsSolderingTileEntity) tile).onBlockBreak();
		}
		if (tile instanceof LogisticsCraftingTableTileEntity) {
			((LogisticsCraftingTableTileEntity) tile).onBlockBreak();
		}
		super.breakBlock(par1World, pos, state);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		switch (getStateFromMeta(metadata).getValue(TYPE)) {
			case SOLDERING_STATION:
				return new LogisticsSolderingTileEntity();
			case POWER_JUNCTION:
				return new LogisticsPowerJunctionTileEntity();
			case SECURITY_STATION:
				return new LogisticsSecurityTileEntity();
			case AUTOCRAFTING_TABLE:
			case FUZZYCRAFTING_TABLE:
				return new LogisticsCraftingTableTileEntity();
			case STATISTICS_TABLE:
				return new LogisticsStatisticsTileEntity();
			case FU_POWERPROVIDER:
				return new LogisticsForgePowerProviderTileEntity();
			case IC2_POWERPROVIDER:
				return new LogisticsIC2PowerProviderTileEntity();
			default:
				return null;
		}
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).meta();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, Type.byMeta(meta));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE);
	}

	/* @Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side) {
		int meta = access.getBlockMetadata(x, y, z);
		TileEntity tile = access.getTileEntity(x, y, z);
		if (tile instanceof IRotationProvider) {
			return getRotatedTexture(meta, side, ((IRotationProvider) tile).getRotation(), ((IRotationProvider) tile).getFrontTexture());
		} else {
			return getRotatedTexture(meta, side, 3, 0);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IIconRegister) {
		for (int i = 0; i < LogisticsSolidBlock.icons.length; i++) {
			LogisticsSolidBlock.icons[i] = par1IIconRegister.registerIcon("logisticspipes:lpsolidblock/" + i);
		}
		LogisticsSolidBlock.newTextures[0] = par1IIconRegister.registerIcon("logisticspipes:lpsolidblock/baseTexture"); // Base
		LogisticsSolidBlock.newTextures[1] = par1IIconRegister.registerIcon("logisticspipes:lpsolidblock/solderTexture"); // SOLDERING_STATION
		LogisticsSolidBlock.newTextures[9] = par1IIconRegister.registerIcon("logisticspipes:lpsolidblock/solderTexture_active"); // SOLDERING_STATION Active
		LogisticsSolidBlock.newTextures[2] = par1IIconRegister.registerIcon("logisticspipes:lpsolidblock/powerTexture"); // LOGISTICS_POWER_JUNCTION
		LogisticsSolidBlock.newTextures[3] = par1IIconRegister.registerIcon("logisticspipes:lpsolidblock/securityTexture"); // LOGISTICS_SECURITY_STATION
		LogisticsSolidBlock.newTextures[4] = par1IIconRegister.registerIcon("logisticspipes:lpsolidblock/craftingTexture"); // LOGISTICS_AUTOCRAFTING_TABLE
		LogisticsSolidBlock.newTextures[5] = par1IIconRegister.registerIcon("logisticspipes:lpsolidblock/fuzzycraftingTexture"); // LOGISTICS_FUZZYCRAFTING_TABLE
		LogisticsSolidBlock.newTextures[6] = par1IIconRegister.registerIcon("logisticspipes:lpsolidblock/statisticsTexture"); // LOGISTICS_STATISTICS_TABLE
		LogisticsSolidBlock.newTextures[7] = par1IIconRegister.registerIcon("logisticspipes:lpsolidblock/powerRFTexture"); // LOGISTICS_RF_POWERPROVIDER
		LogisticsSolidBlock.newTextures[8] = par1IIconRegister.registerIcon("logisticspipes:lpsolidblock/powerIC2Texture"); // LOGISTICS_IC2_POWERPROVIDER
	}

	private IIcon getRotatedTexture(int meta, int side, int rotation, int front) {
		switch (meta) {
			case SOLDERING_STATION:
				if (front == 0) {
					front = 8;
				}
				switch (side) {
					case 1: //TOP
						return LogisticsSolidBlock.icons[1];
					case 0: //Bottom
						return LogisticsSolidBlock.icons[2];
					case 2: //East
						switch (rotation) {
							case 0:
							case 1:
							case 2:
							default:
								return LogisticsSolidBlock.icons[7];
							case 3:
								return LogisticsSolidBlock.icons[front];
						}
					case 3: //West
						switch (rotation) {
							case 0:
							case 1:
							case 3:
							default:
								return LogisticsSolidBlock.icons[7];
							case 2:
								return LogisticsSolidBlock.icons[front];
						}
					case 4: //South
						switch (rotation) {
							case 0:
							case 2:
							case 3:
							default:
								return LogisticsSolidBlock.icons[7];
							case 1:
								return LogisticsSolidBlock.icons[front];
						}
					case 5: //North
						switch (rotation) {
							case 0:
								return LogisticsSolidBlock.icons[front];
							case 1:
							case 2:
							case 3:
							default:
								return LogisticsSolidBlock.icons[7];
						}

					default:
						return LogisticsSolidBlock.icons[0];
				}
			case LOGISTICS_POWER_JUNCTION:
				switch (side) {
					case 1: //TOP
						return LogisticsSolidBlock.icons[4];
					case 0: //Bottom
						return LogisticsSolidBlock.icons[5];
					default: //Front
						return LogisticsSolidBlock.icons[6];
				}
			case LOGISTICS_SECURITY_STATION:
				switch (side) {
					case 1: //TOP
						return LogisticsSolidBlock.icons[9];
					case 0: //Bottom
						return LogisticsSolidBlock.icons[5];
					default: //Front
						return LogisticsSolidBlock.icons[6];
				}
			case LOGISTICS_AUTOCRAFTING_TABLE:
				switch (side) {
					case 1: //TOP
						return LogisticsSolidBlock.icons[11];
					case 0: //Bottom
						return LogisticsSolidBlock.icons[12];
					default: //Front
						return LogisticsSolidBlock.icons[10];
				}
			case LOGISTICS_FUZZYCRAFTING_TABLE:
				switch (side) {
					case 1: //TOP
						return LogisticsSolidBlock.icons[16];
					case 0: //Bottom
						return LogisticsSolidBlock.icons[12];
					default: //Front
						return LogisticsSolidBlock.icons[10];
				}
			case LOGISTICS_STATISTICS_TABLE:
				switch (side) {
					case 1: //TOP
						return LogisticsSolidBlock.icons[17];
					case 0: //Bottom
						return LogisticsSolidBlock.icons[5];
					default: //Front
						return LogisticsSolidBlock.icons[6];
				}
			case LOGISTICS_RF_POWERPROVIDER:
				switch (side) {
					case 1: //TOP
						return LogisticsSolidBlock.icons[14];
					case 0: //Bottom
						return LogisticsSolidBlock.icons[5];
					default: //Front
						return LogisticsSolidBlock.icons[6];
				}
			case LOGISTICS_IC2_POWERPROVIDER:
				switch (side) {
					case 1: //TOP
						return LogisticsSolidBlock.icons[15];
					case 0: //Bottom
						return LogisticsSolidBlock.icons[5];
					default: //Front
						return LogisticsSolidBlock.icons[6];
				}
			case LOGISTICS_BLOCK_FRAME:
				switch (side) {
					case 1: //TOP
						return LogisticsSolidBlock.icons[10];
					default:
						return LogisticsSolidBlock.icons[2];
				}
			default:
				return LogisticsSolidBlock.icons[0];
		}
	}

	public static IIcon getNewIcon(IBlockAccess access, int x, int y, int z) {
		int meta = access.getBlockMetadata(x, y, z);
		if (meta == LogisticsSolidBlock.SOLDERING_STATION) {
			TileEntity tile = access.getTileEntity(x, y, z);
			if (tile instanceof IRotationProvider) {
				if (((IRotationProvider) tile).getFrontTexture() == 3) {
					return LogisticsSolidBlock.newTextures[9];
				}
			}
		}
		return LogisticsSolidBlock.getNewIcon(meta);
	}

	public static IIcon getNewIcon(int meta) {
		switch (meta) {
			case SOLDERING_STATION:
				return LogisticsSolidBlock.newTextures[1];
			case LOGISTICS_POWER_JUNCTION:
				return LogisticsSolidBlock.newTextures[2];
			case LOGISTICS_SECURITY_STATION:
				return LogisticsSolidBlock.newTextures[3];
			case LOGISTICS_AUTOCRAFTING_TABLE:
				return LogisticsSolidBlock.newTextures[4];
			case LOGISTICS_FUZZYCRAFTING_TABLE:
				return LogisticsSolidBlock.newTextures[5];
			case LOGISTICS_STATISTICS_TABLE:
				return LogisticsSolidBlock.newTextures[6];
			case LOGISTICS_RF_POWERPROVIDER:
				return LogisticsSolidBlock.newTextures[7];
			case LOGISTICS_IC2_POWERPROVIDER:
				return LogisticsSolidBlock.newTextures[8];
			default:
				return LogisticsSolidBlock.newTextures[0];
		}
	} */
}
