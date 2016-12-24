package logisticspipes.renderer;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IIconProvider {

	@SideOnly(Side.CLIENT)
	TextureAtlasSprite getIcon(int iconIndex);

	@SideOnly(Side.CLIENT)
	void registerIcons(TextureMap iconRegister);

}
