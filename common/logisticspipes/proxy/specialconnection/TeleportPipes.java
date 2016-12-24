package logisticspipes.proxy.specialconnection;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import logisticspipes.LogisticsPipes;
import logisticspipes.interfaces.routing.ISpecialPipedConnection;
import logisticspipes.proxy.SimpleServiceLocator;
import logisticspipes.proxy.specialconnection.SpecialPipeConnection.ConnectionInformation;
import logisticspipes.routing.PipeRoutingConnectionType;
import logisticspipes.routing.pathfinder.IPipeInformationProvider;

import net.minecraft.util.EnumFacing;

import buildcraft.transport.Pipe;
import buildcraft.transport.TileGenericPipe;

/** Support for teleport pipes **/
public class TeleportPipes implements ISpecialPipedConnection {

	private static Class<? extends Pipe> PipeItemTeleport;
	private static Method teleportPipeMethod;
	private static Object teleportManager;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init() {
		try {
			try {
				TeleportPipes.PipeItemTeleport = (Class<? extends Pipe>) Class.forName("buildcraft.additionalpipes.pipes.PipeItemTeleport");
			} catch (Exception e) {
				TeleportPipes.PipeItemTeleport = (Class<? extends Pipe>) Class.forName("net.minecraft.src.buildcraft.additionalpipes.pipes.PipeItemTeleport");
			}
			TeleportPipes.teleportPipeMethod = TeleportPipes.PipeItemTeleport.getMethod("getConnectedPipes", boolean.class);
			LogisticsPipes.log.debug("Additional pipes detected, adding compatibility");
			return true;
		} catch (Exception e1) {
			try {
				TeleportPipes.PipeItemTeleport = (Class<? extends Pipe>) Class.forName("buildcraft.additionalpipes.pipes.PipeItemsTeleport");
				Class<?> tpmanager = Class.forName("buildcraft.additionalpipes.pipes.TeleportManager");
				TeleportPipes.teleportManager = tpmanager.getField("instance").get(null);
				TeleportPipes.teleportPipeMethod = tpmanager.getMethod("getConnectedPipes", Class.forName("buildcraft.additionalpipes.pipes.PipeTeleport"), boolean.class);
				LogisticsPipes.log.debug("Additional pipes detected, adding compatibility");
				return true;
			} catch (Exception e2) {
				LogisticsPipes.log.debug("Additional pipes not detected: " + e2.getMessage());
				return false;
			}
		}
	}

	@Override
	public boolean isType(IPipeInformationProvider tile) {
		if (tile.getTile() instanceof TileGenericPipe && ((TileGenericPipe) tile.getTile()).pipe != null) {
			if (TeleportPipes.PipeItemTeleport.isAssignableFrom(((TileGenericPipe) tile.getTile()).pipe.getClass())) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private LinkedList<? extends Pipe> getConnectedTeleportPipes(Pipe pipe) throws Exception {
		if (TeleportPipes.teleportManager != null) {
			return (LinkedList<? extends Pipe>) TeleportPipes.teleportPipeMethod.invoke(TeleportPipes.teleportManager, pipe, false);
		}
		return (LinkedList<? extends Pipe>) TeleportPipes.teleportPipeMethod.invoke(pipe, false);
	}

	@Override
	public List<ConnectionInformation> getConnections(IPipeInformationProvider tile, EnumSet<PipeRoutingConnectionType> connection, EnumFacing side) {
		List<ConnectionInformation> list = new ArrayList<>();
		if (tile.getTile() instanceof TileGenericPipe && ((TileGenericPipe) tile.getTile()).pipe != null) {
			try {
				LinkedList<? extends Pipe> pipes = getConnectedTeleportPipes(((TileGenericPipe) tile.getTile()).pipe);
				list.addAll(pipes.stream()
						.map(pipe -> new ConnectionInformation(SimpleServiceLocator.pipeInformationManager.getInformationProviderFor(pipe.container), connection, side, EnumFacing.UNKNOWN, 0))
						.collect(Collectors.toList()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
