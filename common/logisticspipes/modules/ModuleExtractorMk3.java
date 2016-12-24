package logisticspipes.modules;

import logisticspipes.pipes.basic.CoreRoutedPipe.ItemSendMode;




import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

public class ModuleExtractorMk3 extends ModuleExtractorMk2 {

	public ModuleExtractorMk3() {
		super();
	}

	@Override
	protected int ticksToAction() {
		return 1;
	}

	@Override
	protected int itemsToExtract() {
		return 64;
	}

	@Override
	protected int neededEnergy() {
		return 10;
	}

	@Override
	protected ItemSendMode itemSendMode() {
		return ItemSendMode.Fast;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getIcon() {
		return new ResourceLocation("logisticspipes:itemModule/ModuleExtractorMk3");
	}
}
