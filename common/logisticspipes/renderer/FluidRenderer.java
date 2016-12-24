package logisticspipes.renderer;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

public final class FluidRenderer {

	public static final int DISPLAY_STAGES = 100;
	private static final ResourceLocation BLOCK_TEXTURE = TextureMap.LOCATION_BLOCKS_TEXTURE;
	private static Map<Fluid, int[]> flowingRenderCache = new HashMap<>();
	private static Map<Fluid, int[]> stillRenderCache = new HashMap<>();

	/**
	 * Deactivate default constructor
	 */
	private FluidRenderer() {

	}

	public static TextureAtlasSprite getFluidTexture(FluidStack fluidStack, boolean flowing) {
		if (fluidStack == null) {
			return null;
		}
		return FluidRenderer.getFluidTexture(fluidStack.getFluid(), flowing);
	}

	public static TextureAtlasSprite getFluidTexture(Fluid fluid, boolean flowing) {
		if (fluid == null) {
			return null;
		}
		ResourceLocation icon = flowing ? fluid.getFlowing() : fluid.getStill();
		if (icon == null) {
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("missingno");
		} else {
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(icon.toString());
		}
	}

	public static ResourceLocation getFluidSheet(FluidStack liquid) {
		if (liquid == null) {
			return FluidRenderer.BLOCK_TEXTURE;
		}
		return FluidRenderer.getFluidSheet(liquid.getFluid());
	}

	public static ResourceLocation getFluidSheet(Fluid liquid) {
		return FluidRenderer.BLOCK_TEXTURE;
	}

	public static void setColorForFluidStack(FluidStack fluidstack) {
		if (fluidstack == null) {
			return;
		}

		int color = fluidstack.getFluid().getColor(fluidstack);
		FluidRenderer.setGLColorFromInt(color);
	}

	private static void setGLColorFromInt(int color) {
		float red = (color >> 16 & 255) / 255.0F;
		float green = (color >> 8 & 255) / 255.0F;
		float blue = (color & 255) / 255.0F;
		GL11.glColor4f(red, green, blue, 1.0F);
	}

	public static int[] getFluidDisplayLists(FluidStack fluidStack, World world, boolean flowing) {
		if (fluidStack == null) {
			return null;
		}
		Fluid fluid = fluidStack.getFluid();
		if (fluid == null) {
			return null;
		}
		Map<Fluid, int[]> cache = flowing ? FluidRenderer.flowingRenderCache : FluidRenderer.stillRenderCache;
		int[] diplayLists = cache.get(fluid);
		if (diplayLists != null) {
			return diplayLists;
		}

		diplayLists = new int[FluidRenderer.DISPLAY_STAGES];

		// TODO: Rewrite for 1.8+
		/* if (fluid.getBlock() != null) {
			FluidRenderer.liquidBlock.baseBlock = fluid.getBlock();
			FluidRenderer.liquidBlock.texture = FluidRenderer.getFluidTexture(fluidStack, flowing);
		} else {
			FluidRenderer.liquidBlock.baseBlock = Blocks.WATER;
			FluidRenderer.liquidBlock.texture = FluidRenderer.getFluidTexture(fluidStack, flowing);
		}

		cache.put(fluid, diplayLists);

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_CULL_FACE);

		for (int s = 0; s < FluidRenderer.DISPLAY_STAGES; ++s) {
			diplayLists[s] = GLAllocation.generateDisplayLists(1);
			GL11.glNewList(diplayLists[s], GL11.GL_COMPILE);

			FluidRenderer.liquidBlock.minX = 0.01f;
			FluidRenderer.liquidBlock.minY = 0;
			FluidRenderer.liquidBlock.minZ = 0.01f;

			FluidRenderer.liquidBlock.maxX = 0.99f;
			FluidRenderer.liquidBlock.maxY = (float) s / (float) FluidRenderer.DISPLAY_STAGES;
			FluidRenderer.liquidBlock.maxZ = 0.99f;

			CustomBlockRenderer.INSTANCE.renderBlock(FluidRenderer.liquidBlock, world, 0, 0, 0, false, true);

			GL11.glEndList();
		}

		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		*/

		return diplayLists;
	}
}
