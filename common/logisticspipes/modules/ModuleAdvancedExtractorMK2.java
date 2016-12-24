package logisticspipes.modules;




import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

public class ModuleAdvancedExtractorMK2 extends ModuleAdvancedExtractor {

	public ModuleAdvancedExtractorMK2() {
		super();
	}

	@Override
	protected int ticksToAction() {
		return 20;
	}

	@Override
	protected int neededEnergy() {
		return 8;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getIcon() {
		return new ResourceLocation("logisticspipes:itemModule/ModuleAdvancedExtractorMK2");
	}
}
