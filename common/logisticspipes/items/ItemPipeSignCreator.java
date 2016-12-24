package logisticspipes.items;

import java.util.ArrayList;
import java.util.List;

import logisticspipes.pipes.basic.CoreRoutedPipe;
import logisticspipes.pipes.basic.LogisticsTileGenericPipe;
import logisticspipes.pipes.signs.CraftingPipeSign;
import logisticspipes.pipes.signs.IPipeSign;
import logisticspipes.pipes.signs.ItemAmountPipeSign;
import logisticspipes.proxy.MainProxy;
import logisticspipes.utils.string.StringUtils;


import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraft.util.EnumFacing;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPipeSignCreator extends LogisticsItem {

	public static final List<Class<? extends IPipeSign>> signTypes = new ArrayList<>();

	public ItemPipeSignCreator() {
		super();
		setMaxStackSize(1);
		setMaxDamage(250);
	}

	@Override
	public EnumActionResult onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (MainProxy.isClient(world)) {
			return EnumActionResult.PASS;
		}
		if (itemStack.getItemDamage() > this.getMaxDamage() || itemStack.stackSize == 0) {
			return EnumActionResult.PASS;
		}
		TileEntity tile = world.getTileEntity(pos);
		if (!(tile instanceof LogisticsTileGenericPipe)) {
			return EnumActionResult.PASS;
		}

		if (!itemStack.hasTagCompound()) {
			itemStack.setTagCompound(new NBTTagCompound());
		}
		itemStack.getTagCompound().setInteger("PipeClicked", 0);

		int mode = itemStack.getTagCompound().getInteger("CreatorMode");

		EnumFacing dir = side;
		if (dir == null) {
			return EnumActionResult.PASS;
		}

		if(!(((LogisticsTileGenericPipe) tile).pipe instanceof CoreRoutedPipe)) {
			return EnumActionResult.PASS;
		}

		CoreRoutedPipe pipe = (CoreRoutedPipe) ((LogisticsTileGenericPipe) tile).pipe;
		if (pipe == null) {
			return EnumActionResult.PASS;
		}
		if (!player.isSneaking()) {
			if (pipe.hasPipeSign(dir)) {
				pipe.activatePipeSign(dir, player);
				return EnumActionResult.SUCCESS;
			} else if (mode >= 0 && mode < ItemPipeSignCreator.signTypes.size()) {
				Class<? extends IPipeSign> signClass = ItemPipeSignCreator.signTypes.get(mode);
				try {
					IPipeSign sign = signClass.newInstance();
					if (sign.isAllowedFor(pipe)) {
						itemStack.damageItem(1, player);
						sign.addSignTo(pipe, dir, player);
						return EnumActionResult.SUCCESS;
					} else {
						return EnumActionResult.PASS;
					}
				} catch (InstantiationException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			} else {
				return EnumActionResult.PASS;
			}
		} else {
			if (pipe.hasPipeSign(dir)) {
				pipe.removePipeSign(dir, player);
				itemStack.damageItem(-1, player);
			}
			return EnumActionResult.SUCCESS;
		}
	}

	// TODO: Rendering
	/* @Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		super.registerIcons(par1IconRegister); // Fallback
		for (int i = 0; i < ItemPipeSignCreator.signTypes.size(); i++) {
			itemIcon[i] = par1IconRegister.registerIcon("logisticspipes:" + getUnlocalizedName().replace("item.", "") + "." + i);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconIndex(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		int mode = stack.getTagCompound().getInteger("CreatorMode");
		if (mode < ItemPipeSignCreator.signTypes.size()) {
			return itemIcon[mode];
		} else {
			return super.getIconIndex(stack); // Fallback
		}
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		int mode = stack.getTagCompound().getInteger("CreatorMode");
		if (mode < ItemPipeSignCreator.signTypes.size()) {
			return itemIcon[mode];
		} else {
			return super.getIcon(stack, pass); // Fallback
		}
	} */

	@Override
	public String getItemStackDisplayName(ItemStack itemstack) {
		if (!itemstack.hasTagCompound()) {
			itemstack.setTagCompound(new NBTTagCompound());
		}
		int mode = itemstack.getTagCompound().getInteger("CreatorMode");
		return StringUtils.translate(getUnlocalizedName(itemstack) + "." + mode);
	}

	@Override
	public CreativeTabs getCreativeTab() {
		return CreativeTabs.TOOLS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if (MainProxy.isClient(world)) {
			return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
		}
		if (player.isSneaking()) {
			if (!stack.hasTagCompound()) {
				stack.setTagCompound(new NBTTagCompound());
			}
			if (!stack.getTagCompound().hasKey("PipeClicked")) {
				int mode = stack.getTagCompound().getInteger("CreatorMode");
				mode++;
				if (mode >= ItemPipeSignCreator.signTypes.size()) {
					mode = 0;
				}
				stack.getTagCompound().setInteger("CreatorMode", mode);
			}
		}
		if (stack.hasTagCompound()) {
			stack.getTagCompound().removeTag("PipeClicked");
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	public static void registerPipeSignTypes() {
		// Never change this order. It defines the id each signType has.
		ItemPipeSignCreator.signTypes.add(CraftingPipeSign.class);
		ItemPipeSignCreator.signTypes.add(ItemAmountPipeSign.class);
	}
}
