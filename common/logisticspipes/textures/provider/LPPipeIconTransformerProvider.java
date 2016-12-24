package logisticspipes.textures.provider;

import java.util.ArrayList;

import logisticspipes.proxy.SimpleServiceLocator;
import logisticspipes.proxy.object3d.interfaces.IIconTransformation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;


public class LPPipeIconTransformerProvider {

	public ArrayList<IIconTransformation> icons = new ArrayList<>();

	public IIconTransformation getIcon(int iconIndex) {
		return icons.get(iconIndex);
	}

	public void setIcon(int index, TextureAtlasSprite icon) {
		while (icons.size() < index + 1) {
			icons.add(null);
		}
		if (icons.get(index) != null) {
			icons.get(index).update(icon);
		} else {
			icons.set(index, SimpleServiceLocator.cclProxy.createIconTransformer(icon));
		}
	}
}
