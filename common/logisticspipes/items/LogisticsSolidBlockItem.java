package logisticspipes.items;

import java.util.List;

import net.minecraft.block.Block;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import logisticspipes.LogisticsPipes;
import logisticspipes.blocks.LogisticsSolidBlock;
import logisticspipes.proxy.SimpleServiceLocator;
import logisticspipes.utils.string.StringUtils;

public class LogisticsSolidBlockItem extends ItemBlock {

	public LogisticsSolidBlockItem(Block par1) {
		super(par1);
		setHasSubtypes(true);
		setUnlocalizedName("logisticsSolidBlock");
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		switch (LogisticsSolidBlock.Type.byMeta(stack.getItemDamage())) {
			case SOLDERING_STATION:
				return "tile.solderingstation";
			case POWER_JUNCTION:
				return "tile.logisticspowerjunction";
			case SECURITY_STATION:
				return "tile.logisticssecuritystation";
			case AUTOCRAFTING_TABLE:
				return "tile.logisticscraftingtable";
			case FUZZYCRAFTING_TABLE:
				return "tile.logisticsfuzzycraftingtable";
			case STATISTICS_TABLE:
				return "tile.logisticsstatisticstable";
			case FU_POWERPROVIDER:
				return "tile.logisticsforgepowerprovider";
			case IC2_POWERPROVIDER:
				return "tile.logisticsic2powerprovider";
			case BLOCK_FRAME:
				return "tile.logisticsblankblock";
		}
		return super.getUnlocalizedName(stack);
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemstack) {
		return StringUtils.translate(getUnlocalizedName(itemstack));
	}

	@Override
	public CreativeTabs getCreativeTab() {
		return LogisticsPipes.LPCreativeTab;
	}

	@Override
	public int getMetadata(int par1) {
		return par1;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(this, 1, LogisticsSolidBlock.Type.BLOCK_FRAME.meta()));
		par3List.add(new ItemStack(this, 1, LogisticsSolidBlock.Type.SOLDERING_STATION.meta()));
		par3List.add(new ItemStack(this, 1, LogisticsSolidBlock.Type.POWER_JUNCTION.meta()));
		par3List.add(new ItemStack(this, 1, LogisticsSolidBlock.Type.SECURITY_STATION.meta()));
		par3List.add(new ItemStack(this, 1, LogisticsSolidBlock.Type.AUTOCRAFTING_TABLE.meta()));
		par3List.add(new ItemStack(this, 1, LogisticsSolidBlock.Type.FUZZYCRAFTING_TABLE.meta()));
		par3List.add(new ItemStack(this, 1, LogisticsSolidBlock.Type.STATISTICS_TABLE.meta()));
		par3List.add(new ItemStack(this, 1, LogisticsSolidBlock.Type.FU_POWERPROVIDER.meta()));
		if (SimpleServiceLocator.IC2Proxy.hasIC2()) {
			par3List.add(new ItemStack(this, 1, LogisticsSolidBlock.Type.IC2_POWERPROVIDER.meta()));
		}
	}
}
