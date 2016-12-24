package logisticspipes.proxy.ic2;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.common.MinecraftForge;
import net.minecraft.util.EnumFacing;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.item.IElectricItem;

import logisticspipes.proxy.MainProxy;
import logisticspipes.proxy.interfaces.IIC2Proxy;
import logisticspipes.recipes.CraftingParts;

public class IC2Proxy implements IIC2Proxy {

	/**
	 * @return Boolean, true if itemstack is a ic2 electric item.
	 * @param stack
	 *            The stack to check.
	 */
	@Override
	public boolean isElectricItem(ItemStack stack) {
		return stack != null && (stack.getItem() instanceof IElectricItem);
	}

	/**
	 * @return Boolean, true if stack is the same type of ic2 electric item as
	 *         template.
	 * @param stack
	 *            The stack to check
	 * @param template
	 *            The stack to compare to
	 */
	@Override
	public boolean isSimilarElectricItem(ItemStack stack, ItemStack template) {
		if (stack == null || template == null || !isElectricItem(template)) {
			return false;
		}
		if (((IElectricItem) template.getItem()).getEmptyItem(stack) == stack.getItem()) {
			return true;
		}
		return ((IElectricItem) template.getItem()).getChargedItem(stack) == stack.getItem();
	}

	/**
	 * @return Int value of current charge on electric item.
	 * @param stack
	 *            The stack to get charge for.
	 */
	private double getCharge(ItemStack stack) {
		if ((stack.getItem() instanceof IElectricItem) && stack.hasTagCompound()) {
			return stack.getTagCompound().getDouble("charge");
		} else {
			return 0;
		}
	}

	/**
	 * @return Int value of maximum charge on electric item.
	 * @param stack
	 *            The stack to get max charge for.
	 */
	private double getMaxCharge(ItemStack stack) {
		if (!(stack.getItem() instanceof IElectricItem)) {
			return 0;
		}
		return ((IElectricItem) stack.getItem()).getMaxCharge(stack);
	}

	/**
	 * @return Boolean, true if electric item is fully charged.
	 * @param stack
	 *            The stack to check if its fully charged.
	 */
	@Override
	public boolean isFullyCharged(ItemStack stack) {
		if (!isElectricItem(stack)) {
			return false;
		}
		if (((IElectricItem) stack.getItem()).getChargedItem(stack) != stack.getItem()) {
			return false;
		}
		double charge = getCharge(stack);
		double maxCharge = getMaxCharge(stack);
		return charge == maxCharge;
	}

	/**
	 * @return Boolean, true if electric item is fully discharged.
	 * @param stack
	 *            The stack to check if its fully discharged.
	 */
	@Override
	public boolean isFullyDischarged(ItemStack stack) {
		if (!isElectricItem(stack)) {
			return false;
		}
		if (((IElectricItem) stack.getItem()).getEmptyItem(stack) != stack.getItem()) {
			return false;
		}
		double charge = getCharge(stack);
		return charge == 0;
	}

	/**
	 * @return Boolean, true if electric item contains charge but is not full.
	 * @param stack
	 *            The stack to check if its partially charged.
	 */
	@Override
	public boolean isPartiallyCharged(ItemStack stack) {
		if (!isElectricItem(stack)) {
			return false;
		}
		if (((IElectricItem) stack.getItem()).getChargedItem(stack) != stack.getItem()) {
			return false;
		}
		double charge = getCharge(stack);
		double maxCharge = getMaxCharge(stack);
		return charge != maxCharge;
	}

	/**
	 * Adds crafting recipes to "IC2 Crafting"
	 */
	@Override
	public void addCraftingRecipes(CraftingParts parts) {
		/*
		Recipes.advRecipes.addRecipe(new ItemStack(LogisticsPipes.ModuleItem, 1, ItemModule.ELECTRICBUFFER), new Object[] { "CGC", "rBr", "CrC", Character.valueOf('C'), IC2Items.getItem("advancedCircuit"), Character.valueOf('G'), parts.getGearTear2(), Character.valueOf('r'), Items.redstone, Character.valueOf('B'),
				new ItemStack(LogisticsPipes.ModuleItem, 1, ItemModule.BLANK) });

		Recipes.advRecipes.addRecipe(new ItemStack(LogisticsPipes.ModuleItem, 1, ItemModule.ELECTRICBUFFER), new Object[] { " G ", "rBr", "CrC", Character.valueOf('C'), IC2Items.getItem("advancedCircuit"), Character.valueOf('G'), parts.getChipTear2(), Character.valueOf('r'), Items.redstone, Character.valueOf('B'),
				new ItemStack(LogisticsPipes.ModuleItem, 1, ItemModule.BLANK) });

		Recipes.advRecipes.addRecipe(new ItemStack(LogisticsPipes.ModuleItem, 1, ItemModule.ELECTRICMANAGER), new Object[] { "CGD", "rBr", "DrC", Character.valueOf('C'), IC2Items.getItem("electronicCircuit"), Character.valueOf('D'), IC2Items.getItem("reBattery"), Character.valueOf('G'), parts.getGearTear2(),
				Character.valueOf('r'), Items.redstone, Character.valueOf('B'), new ItemStack(LogisticsPipes.ModuleItem, 1, ItemModule.BLANK) });

		Recipes.advRecipes.addRecipe(new ItemStack(LogisticsPipes.ModuleItem, 1, ItemModule.ELECTRICMANAGER), new Object[] { "CGD", "rBr", "DrC", Character.valueOf('C'), IC2Items.getItem("electronicCircuit"), Character.valueOf('D'), IC2Items.getItem("chargedReBattery"), Character.valueOf('G'),
				parts.getGearTear2(), Character.valueOf('r'), Items.redstone, Character.valueOf('B'), new ItemStack(LogisticsPipes.ModuleItem, 1, ItemModule.BLANK) });

		Recipes.advRecipes.addRecipe(new ItemStack(LogisticsPipes.ModuleItem, 1, ItemModule.ELECTRICMANAGER),
				new Object[] { "CGc", "rBr", "DrC", Character.valueOf('C'), IC2Items.getItem("electronicCircuit"), Character.valueOf('c'), IC2Items.getItem("reBattery"), Character.valueOf('D'), IC2Items.getItem("chargedReBattery"), Character.valueOf('G'), parts.getGearTear2(), Character.valueOf('r'),
						Items.redstone, Character.valueOf('B'), new ItemStack(LogisticsPipes.ModuleItem, 1, ItemModule.BLANK) });

		Recipes.advRecipes.addRecipe(new ItemStack(LogisticsPipes.ModuleItem, 1, ItemModule.ELECTRICMANAGER),
				new Object[] { "CGc", "rBr", "DrC", Character.valueOf('C'), IC2Items.getItem("electronicCircuit"), Character.valueOf('c'), IC2Items.getItem("chargedReBattery"), Character.valueOf('D'), IC2Items.getItem("reBattery"), Character.valueOf('G'), parts.getGearTear2(), Character.valueOf('r'),
						Items.redstone, Character.valueOf('B'), new ItemStack(LogisticsPipes.ModuleItem, 1, ItemModule.BLANK) });

		Recipes.advRecipes.addRecipe(new ItemStack(LogisticsPipes.ModuleItem, 1, ItemModule.ELECTRICMANAGER), new Object[] { " G ", "rBr", "DrC", Character.valueOf('C'), IC2Items.getItem("electronicCircuit"), Character.valueOf('D'), IC2Items.getItem("reBattery"), Character.valueOf('G'), parts.getChipTear2(),
				Character.valueOf('r'), Items.redstone, Character.valueOf('B'), new ItemStack(LogisticsPipes.ModuleItem, 1, ItemModule.BLANK) });

		Recipes.advRecipes.addRecipe(new ItemStack(LogisticsPipes.ModuleItem, 1, ItemModule.ELECTRICMANAGER), new Object[] { " G ", "rBr", "DrC", Character.valueOf('C'), IC2Items.getItem("electronicCircuit"), Character.valueOf('D'), IC2Items.getItem("chargedReBattery"), Character.valueOf('G'),
				parts.getChipTear2(), Character.valueOf('r'), Items.redstone, Character.valueOf('B'), new ItemStack(LogisticsPipes.ModuleItem, 1, ItemModule.BLANK) });

		Recipes.advRecipes.addRecipe(new ItemStack(LogisticsPipes.UpgradeItem, 1, ItemUpgrade.POWER_IC2_LV_SUPPLIER),
				new Object[] { "PSP", "OBO", "PTP", Character.valueOf('B'), new ItemStack(LogisticsPipes.UpgradeItem, 1, ItemUpgrade.POWER_TRANSPORTATION), Character.valueOf('S'), IC2Items.getItem("energyStorageUpgrade"), Character.valueOf('O'), IC2Items.getItem("overclockerUpgrade"), Character.valueOf('T'),
						IC2Items.getItem("transformerUpgrade"), Character.valueOf('P'), Items.paper });

		Recipes.advRecipes.addRecipe(new ItemStack(LogisticsPipes.UpgradeItem, 1, ItemUpgrade.POWER_IC2_MV_SUPPLIER),
				new Object[] { "PSP", "OBO", "PTP", Character.valueOf('B'), new ItemStack(LogisticsPipes.UpgradeItem, 1, ItemUpgrade.POWER_IC2_LV_SUPPLIER), Character.valueOf('S'), IC2Items.getItem("energyStorageUpgrade"), Character.valueOf('O'), IC2Items.getItem("overclockerUpgrade"), Character.valueOf('T'),
						IC2Items.getItem("transformerUpgrade"), Character.valueOf('P'), Items.paper });

		Recipes.advRecipes.addRecipe(new ItemStack(LogisticsPipes.UpgradeItem, 1, ItemUpgrade.POWER_IC2_HV_SUPPLIER),
				new Object[] { "PSP", "OBO", "PTP", Character.valueOf('B'), new ItemStack(LogisticsPipes.UpgradeItem, 1, ItemUpgrade.POWER_IC2_MV_SUPPLIER), Character.valueOf('S'), IC2Items.getItem("energyStorageUpgrade"), Character.valueOf('O'), IC2Items.getItem("overclockerUpgrade"), Character.valueOf('T'),
						IC2Items.getItem("transformerUpgrade"), Character.valueOf('P'), Items.paper });

		Recipes.advRecipes.addRecipe(new ItemStack(LogisticsPipes.UpgradeItem, 1, ItemUpgrade.POWER_IC2_EV_SUPPLIER),
				new Object[] { "PSP", "OBO", "PTP", Character.valueOf('B'), new ItemStack(LogisticsPipes.UpgradeItem, 1, ItemUpgrade.POWER_IC2_HV_SUPPLIER), Character.valueOf('S'), IC2Items.getItem("energyStorageUpgrade"), Character.valueOf('O'), IC2Items.getItem("overclockerUpgrade"), Character.valueOf('T'),
						IC2Items.getItem("transformerUpgrade"), Character.valueOf('P'), Items.paper });

		Recipes.advRecipes.addRecipe(new ItemStack(LogisticsPipes.LogisticsSolidBlock, 1, LogisticsSolidBlock.LOGISTICS_IC2_POWERPROVIDER), new Object[] { "PSP", "OBO", "PTP", Character.valueOf('B'), Blocks.redstone_block, Character.valueOf('S'), IC2Items.getItem("energyStorageUpgrade"), Character.valueOf('O'),
				IC2Items.getItem("overclockerUpgrade"), Character.valueOf('T'), IC2Items.getItem("transformerUpgrade"), Character.valueOf('P'), Items.paper });
		*/
	}

	/**
	 * Registers an TileEntity to the IC2 EnergyNet
	 * 
	 * @param has
	 *            to be an instance of IEnergyTile
	 */
	@Override
	public void registerToEneryNet(TileEntity tile) {
		if (MainProxy.isServer(tile.getWorld())) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent((IEnergyTile) tile));
		}
	}

	/**
	 * Removes an TileEntity from the IC2 EnergyNet
	 * 
	 * @param has
	 *            to be an instance of IEnergyTile
	 */
	@Override
	public void unregisterToEneryNet(TileEntity tile) {
		if (MainProxy.isServer(tile.getWorld())) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent((IEnergyTile) tile));
		}
	}

	/**
	 * @return If IC2 is loaded, returns true.
	 */
	@Override
	public boolean hasIC2() {
		return true;
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity energy, TileEntity tile, EnumFacing opposite) {
		return ((IEnergySink) energy).acceptsEnergyFrom(tile, opposite);
	}

	@Override
	public boolean isEnergySink(TileEntity tile) {
		return tile instanceof IEnergySink;
	}

	@Override
	public double demandedEnergyUnits(TileEntity tile) {
		return ((IEnergySink) tile).getDemandedEnergy();
	}

	@Override
	public double injectEnergyUnits(TileEntity tile, EnumFacing opposite, double d) {
		return ((IEnergySink) tile).injectEnergy(opposite, d, 1); //TODO check the voltage
	}
}
