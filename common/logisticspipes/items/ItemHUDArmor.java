package logisticspipes.items;

import logisticspipes.LogisticsPipes;
import logisticspipes.api.IHUDArmor;
import logisticspipes.network.GuiIDs;
import logisticspipes.proxy.MainProxy;
import logisticspipes.utils.string.StringUtils;


import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.common.ISpecialArmor;

public class ItemHUDArmor extends ItemArmor implements ISpecialArmor, IHUDArmor {

	public ItemHUDArmor(int renderIndex) {
		super(ArmorMaterial.CHAIN, renderIndex, EntityEquipmentSlot.HEAD);
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
		return new ArmorProperties(0, 0, 0);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return 0;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
		// Does not get dammaged
	}

	@Override
	public boolean getShareTag() {
		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (!MainProxy.isClient(worldIn)) {
			useItem(playerIn, worldIn);
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn.copy());
	}

	@Override
	public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		useItem(player, world);
		// TODO: Is this right?
		if (MainProxy.isClient(world)) {
			return EnumActionResult.PASS;
		}
		return EnumActionResult.SUCCESS;
	}

	private void useItem(EntityPlayer player, World world) {
		player.openGui(LogisticsPipes.instance, GuiIDs.GUI_HUD_Settings, world, player.inventory.currentItem, -1, 0);
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		return new CreativeTabs[] { getCreativeTab(), LogisticsPipes.LPCreativeTab };
	}

	// TODO: Rendering
	/* @Override
	public void registerIcons(IIconRegister par1IIconRegister) {
		itemIcon = par1IIconRegister.registerIcon("logisticspipes:" + getUnlocalizedName().replace("item.", ""));
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		return "logisticspipes:textures/armor/LogisticsHUD_1.png";
	} */

	@Override
	public boolean isEnabled(ItemStack item) {
		return true;
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemstack) {
		return StringUtils.translate(getUnlocalizedName(itemstack));
	}
}
