package logisticspipes.renderer.newpipe;

import net.minecraft.util.ResourceLocation;

import lombok.Data;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;

@Data
public class RenderEntry {

    private static final ResourceLocation BLOCKS = new ResourceLocation("textures/atlas/blocks.png");

    public RenderEntry(IModel model, IModelState[] operations, ResourceLocation texture) {
        this.model = model;
        this.operations = operations;
        this.texture = texture;
    }

    public RenderEntry(IModel model, IModelState[] operations) {
        this(model, operations, RenderEntry.BLOCKS);
    }

    public RenderEntry(IModel model) {
        this(model, new IModelState[] {});
    }

    public RenderEntry(IModel model, ResourceLocation texture) {
        this(model, new IModelState[] {}, texture);
    }

    private final IModel model;
    private final IModelState[] operations;
    private final ResourceLocation texture;
}
