package logisticspipes.proxy.cofhccl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import logisticspipes.proxy.DontLoadProxy;
import logisticspipes.proxy.SimpleServiceLocator;
import logisticspipes.proxy.interfaces.ICCLProxy;
import logisticspipes.proxy.object3d.interfaces.I3DOperation;
import logisticspipes.proxy.object3d.interfaces.IIconTransformation;
import logisticspipes.proxy.object3d.interfaces.IModel3D;
import logisticspipes.proxy.object3d.interfaces.IRenderState;
import logisticspipes.proxy.object3d.interfaces.ITranslation;
import logisticspipes.proxy.object3d.interfaces.IVec3;
import logisticspipes.proxy.object3d.operation.LPScale;



import cofh.repack.codechicken.lib.render.CCModel;
import cofh.repack.codechicken.lib.render.CCRenderState;
import cofh.repack.codechicken.lib.render.ColourMultiplier;
import cofh.repack.codechicken.lib.render.uv.IconTransformation;
import cofh.repack.codechicken.lib.render.uv.UVScale;
import cofh.repack.codechicken.lib.render.uv.UVTransformation;
import cofh.repack.codechicken.lib.render.uv.UVTransformationList;
import cofh.repack.codechicken.lib.render.uv.UVTranslation;
import cofh.repack.codechicken.lib.vec.Rotation;
import cofh.repack.codechicken.lib.vec.Scale;
import cofh.repack.codechicken.lib.vec.Transformation;
import cofh.repack.codechicken.lib.vec.Translation;
import cofh.repack.codechicken.lib.vec.Vector3;

public class CoFHCCLProxy implements ICCLProxy {

	public CoFHCCLProxy() {
		try {
			CCModel.class.getName();
		} catch (Throwable e) {
			throw new DontLoadProxy();
		}
	}

	@Override
	public IIconTransformation createIconTransformer(IIcon registerIcon) {
		final IconTransformation icon = new IconTransformation(registerIcon);
		return new IIconTransformation() {

			@Override
			public Object getOriginal() {
				return icon;
			}

			@Override
			public void update(IIcon registerIcon) {
				icon.icon = registerIcon;
			}

		};
	}

	@Override
	public IRenderState getRenderState() {
		return new IRenderState() {

			@Override
			public void reset() {
				CCRenderState.reset();
			}

			@Override
			public void setUseNormals(boolean b) {
				CCRenderState.useNormals = b;
			}

			@Override
			public void setAlphaOverride(int i) {
				CCRenderState.alphaOverride = i;
			}

			@Override
			public void draw() {
				CCRenderState.draw();
			}

			@Override
			public void setBrightness(int brightness) {
				CCRenderState.setBrightness(brightness);
			}

			@Override
			public void startDrawing() {
				CCRenderState.startDrawing();
			}

		};
	}

	@Override
	public Map<String, IModel3D> parseObjModels(InputStream resourceAsStream, int i, LPScale scale) throws IOException {
		Map<String, IModel3D> target = new HashMap<>();
		Map<String, CCModel> source = CCModel.parseObjModels(resourceAsStream, i, (Transformation) scale.getOriginal());
		for (Entry<String, CCModel> entry : source.entrySet()) {
			target.put(entry.getKey(), SimpleServiceLocator.cclProxy.wrapModel(entry.getValue()));
		}
		return target;
	}

	@Override
	public Object getRotation(int i, int j) {
		return Rotation.sideOrientation(i, j);
	}

	@Override
	public Object getScale(double d, double e, double f) {
		return new Scale(d, e, f);
	}

	@Override
	public Object getScale(double d) {
		return new Scale(d);
	}

	@Override
	public ITranslation getTranslation(double d, double e, double f) {
		return new CoFHTransformationProxy(new Translation(d, e, f));
	}

	@Override
	public ITranslation getTranslation(IVec3 vec) {
		final Translation translation;
		if (vec.getOriginal() instanceof Vector3) {
			translation = new Translation((Vector3) vec.getOriginal());
		} else {
			translation = new Translation(vec.x(), vec.y(), vec.z());
		}
		return new CoFHTransformationProxy(translation);
	}

	@Override
	public Object getUVScale(double i, double d) {
		return new UVScale(i, d);
	}

	@Override
	public Object getUVTranslation(float i, float f) {
		return new UVTranslation(i, f);
	}

	@Override
	public Object getUVTransformationList(I3DOperation[] uvTranslation) {
		List<UVTransformation> transforms = new ArrayList<>();
		for (I3DOperation op : uvTranslation) {
			transforms.add((UVTransformation) op.getOriginal());
		}
		return new UVTransformationList(transforms.toArray(new UVTransformation[transforms.size()]));
	}

	@Override
	public IModel3D wrapModel(Object oModel) {
		final CCModel model = (CCModel) oModel;
		return new CoFHModel3D(model);
	}

	@Override
	public boolean isActivated() {
		return true;
	}

	@Override
	public Object getRotation(double d, int i, int j, int k) {
		return new Rotation(d, i, j, k);
	}

	@Override
	public IModel3D combine(Collection<IModel3D> list) {
		List<CCModel> collection = new ArrayList<>(list.size());
		collection.addAll(list.stream().map(model -> (CCModel) model.getOriginal()).collect(Collectors.toList()));
		return SimpleServiceLocator.cclProxy.wrapModel(CCModel.combine(collection));
	}

	@Override
	public Object getColourMultiplier(int i) {
		return ColourMultiplier.instance(i);
	}
}
