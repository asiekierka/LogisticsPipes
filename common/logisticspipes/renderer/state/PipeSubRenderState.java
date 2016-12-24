package logisticspipes.renderer.state;

import logisticspipes.renderer.newpipe.GLRenderList;

import java.util.List;

public class PipeSubRenderState {
	public List<RenderEntry> cachedRenderer = null;
	public GLRenderList renderList;
	public boolean forceRenderOldPipe = false;
}
