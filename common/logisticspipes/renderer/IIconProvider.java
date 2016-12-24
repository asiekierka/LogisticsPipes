package logisticspipes.renderer;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IIconProvider {

	@SideOnly(Side.CLIENT)
	IIcon getIcon(int iconIndex);

	@SideOnly(Side.CLIENT)
	void registerIcons(IIconRegister iconRegister);

}
