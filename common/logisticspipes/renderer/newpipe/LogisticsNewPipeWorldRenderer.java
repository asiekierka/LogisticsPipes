package logisticspipes.renderer.newpipe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import net.minecraft.util.EnumFacing;

import logisticspipes.LPConstants;
import logisticspipes.pipes.PipeBlockRequestTable;
import logisticspipes.pipes.basic.LogisticsTileGenericPipe;
import logisticspipes.proxy.SimpleServiceLocator;
import logisticspipes.proxy.buildcraft.subproxies.IBCPipePluggable;
import logisticspipes.renderer.IIconProvider;
import logisticspipes.renderer.LogisticsPipeWorldRenderer;
import logisticspipes.renderer.state.PipeRenderState;
import logisticspipes.textures.Textures;
import network.rs485.logisticspipes.world.CoordinateUtils;
import network.rs485.logisticspipes.world.DoubleCoordinates;

public class LogisticsNewPipeWorldRenderer {
/*
	private Map<BlockRotation, IModel3D> requestBlock = null;

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		Tessellator tess = Tessellator.instance;
		TileEntity tile = world.getTileEntity(x, y, z);
		LogisticsTileGenericPipe pipeTile = (LogisticsTileGenericPipe) tile;
		PipeRenderState renderState = pipeTile.renderState;

		if (pipeTile.pipe instanceof PipeBlockRequestTable) {
			if (LogisticsPipeWorldRenderer.renderPass != 0) {
				return false;
			}
			IIconProvider icons = pipeTile.getPipeIcons();
			if (icons == null) {
				return false;
			}
			if (requestBlock == null || true) {
				requestBlock = new HashMap<>();
				for (BlockRotation rot : BlockRotation.values()) {
					requestBlock.put(rot, LogisticsNewSolidBlockWorldRenderer.block.get(rot).copy().apply(new LPScale(0.999)).apply(new LPTranslation(0.0005, 0.0005, 0.0005)));
				}
			}

			SimpleServiceLocator.cclProxy.getRenderState().reset();
			SimpleServiceLocator.cclProxy.getRenderState().setUseNormals(true);
			SimpleServiceLocator.cclProxy.getRenderState().setAlphaOverride(0xff);

			BlockRotation rotation = BlockRotation.getRotation(((PipeBlockRequestTable) pipeTile.pipe).getRotation());

			int brightness = new DoubleCoordinates(x, y, z).getBlock(world).getMixedBrightnessForBlock(world, x, y, z);

			tess.setColorOpaque_F(1F, 1F, 1F);
			tess.setBrightness(brightness);

			IIconTransformation icon = SimpleServiceLocator.cclProxy.createIconTransformer(Textures.LOGISTICS_REQUEST_TABLE_NEW);

			requestBlock.get(rotation).render(new LPTranslation(x, y, z), icon);

			for (CoverSides side : CoverSides.values()) {
				if (!pipeTile.renderState.pipeConnectionMatrix.isConnected(side.getDir(rotation))) {
					LogisticsNewSolidBlockWorldRenderer.texturePlate_Outer.get(side).get(rotation).render(new LPTranslation(x, y, z), icon);
					LogisticsNewSolidBlockWorldRenderer.texturePlate_Inner.get(side).get(rotation).render(new LPTranslation(x, y, z), icon);
				}
			}

			return true;
		}

		boolean hasRendered = false;

		tess.addTranslation(0.00002F, 0.00002F, 0.00002F);
		renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		for (EnumFacing dir : EnumFacing.VALUES) {
			if (pipeTile.tilePart.hasPipePluggable(dir)) {
				IBCPipePluggable p = pipeTile.tilePart.getBCPipePluggable(dir);
				p.renderPluggable(renderer, dir, LogisticsPipeWorldRenderer.renderPass, x, y, z);
				hasRendered = true;
			}
		}
		tess.addTranslation(-0.00002F, -0.00002F, -0.00002F);

		boolean[] solidSides = new boolean[6];
		for (EnumFacing dir : EnumFacing.VALUES) {
			DoubleCoordinates pos = CoordinateUtils.add(new DoubleCoordinates((TileEntity) pipeTile), dir);
			Block blockSide = pos.getBlock(pipeTile.getWorld());
			if (blockSide != null && blockSide.isSideSolid(pipeTile.getWorld(), pos.getXInt(), pos.getYInt(), pos.getZInt(), dir.getOpposite())
					&& !renderState.pipeConnectionMatrix.isConnected(dir)) {
				solidSides[dir.ordinal()] = true;
			}
		}
		if (!Arrays.equals(solidSides, renderState.solidSidesCache)) {
			renderState.solidSidesCache = solidSides.clone();
			renderState.cachedRenderer = null;
		}

		if(hasRendered) {
			block.setBlockBounds(0, 0, 0, 0, 0, 0);
			renderer.setRenderBoundsFromBlock(block);
			renderer.renderStandardBlock(block, x, y, z);

			block.setBlockBounds(0, 0, 0, 1, 1, 1);
		}
		return hasRendered;
	}

	@Override
	public int getRenderId() {
		return LPConstants.pipeModel;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}
*/
}
