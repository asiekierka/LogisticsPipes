package logisticspipes.network;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import lombok.SneakyThrows;

import logisticspipes.LogisticsPipes;
import logisticspipes.network.abstractguis.GuiProvider;
import logisticspipes.network.abstractguis.PopupGuiProvider;
import logisticspipes.network.exception.TargetNotFoundException;
import logisticspipes.network.packets.gui.GUIPacket;
import logisticspipes.proxy.MainProxy;
import logisticspipes.utils.gui.LogisticsBaseGuiScreen;
import logisticspipes.utils.gui.SubGuiScreen;
import network.rs485.logisticspipes.util.LPDataIOWrapper;

public class NewGuiHandler {

	public static List<GuiProvider> guilist;
	public static Map<Class<? extends GuiProvider>, GuiProvider> guimap;

	private NewGuiHandler() { }

	@SuppressWarnings("unchecked")
	// Suppressed because this cast should never fail.
	public static <T extends GuiProvider> T getGui(Class<T> clazz) {
		return (T) NewGuiHandler.guimap.get(clazz).template();
	}

	@SuppressWarnings("unchecked")
	@SneakyThrows({ IOException.class, InvocationTargetException.class, IllegalAccessException.class, InstantiationException.class })
	// Suppression+sneakiness because these shouldn't ever fail, and if they do, it needs to fail.
	public static final void initialize() {
		final List<ClassInfo> classes = new ArrayList<>(ClassPath.from(NewGuiHandler.class.getClassLoader())
				.getTopLevelClassesRecursive("logisticspipes.network.guis"));
		Collections.sort(classes, (o1, o2) -> o1.getSimpleName().compareTo(o2.getSimpleName()));

		NewGuiHandler.guilist = new ArrayList<>(classes.size());
		NewGuiHandler.guimap = new HashMap<>(classes.size());

		int currentid = 0;

		for (ClassInfo c : classes) {
			final Class<?> cls = c.load();
			final GuiProvider instance = (GuiProvider) cls.getConstructors()[0].newInstance(currentid);
			NewGuiHandler.guilist.add(instance);
			NewGuiHandler.guimap.put((Class<? extends GuiProvider>) cls, instance);
			currentid++;
		}
	}

	public static void openGui(GuiProvider guiProvider, EntityPlayer oPlayer) {
		if (!(oPlayer instanceof EntityPlayerMP)) {
			throw new UnsupportedOperationException("Gui can only be opened on the server side");
		}
		EntityPlayerMP player = (EntityPlayerMP) oPlayer;
		Container container = guiProvider.getContainer(player);
		if (container == null) {
			if (guiProvider instanceof PopupGuiProvider) {
				GUIPacket packet = PacketHandler.getPacket(GUIPacket.class);
				packet.setGuiID(guiProvider.getId());
				packet.setWindowID(-2);
				packet.setGuiData(LPDataIOWrapper.collectData(guiProvider::writeData));
				MainProxy.sendPacketToPlayer(packet, player);
			}
			return;
		}
		player.getNextWindowId();
		player.closeContainer();
		int windowId = player.currentWindowId;

		GUIPacket packet = PacketHandler.getPacket(GUIPacket.class);
		packet.setGuiID(guiProvider.getId());
		packet.setWindowID(windowId);
		packet.setGuiData(LPDataIOWrapper.collectData(guiProvider::writeData));
		MainProxy.sendPacketToPlayer(packet, player);

		player.openContainer = container;
		player.openContainer.windowId = windowId;
		player.openContainer.addCraftingToCrafters(player);
	}

	@SideOnly(Side.CLIENT)
	public static void openGui(GUIPacket packet, EntityPlayer player) {
		int guiID = packet.getGuiID();
		GuiProvider provider = NewGuiHandler.guilist.get(guiID).template();
		LPDataIOWrapper.provideData(packet.getGuiData(), provider::readData);
		
		if (provider instanceof PopupGuiProvider && packet.getWindowID() == -2) {
			if (FMLClientHandler.instance().getClient().currentScreen instanceof LogisticsBaseGuiScreen) {
				LogisticsBaseGuiScreen baseGUI = (LogisticsBaseGuiScreen) FMLClientHandler.instance().getClient().currentScreen;
				SubGuiScreen newSub;
				try {
					newSub = (SubGuiScreen) provider.getClientGui(player);
				} catch (TargetNotFoundException e) {
					throw e;
				} catch (Exception e) {
					LogisticsPipes.log.error(packet.getClass().getName());
					LogisticsPipes.log.error(packet.toString());
					throw new RuntimeException(e);
				}
				if (newSub != null) {
					if (!baseGUI.hasSubGui()) {
						baseGUI.setSubGui(newSub);
					} else {
						SubGuiScreen canidate = baseGUI.getSubGui();
						while (canidate.hasSubGui()) {
							canidate = canidate.getSubGui();
						}
						canidate.setSubGui(newSub);
					}
				}
			}
		} else {
			GuiContainer screen;
			try {
				screen = (GuiContainer) provider.getClientGui(player);
			} catch (TargetNotFoundException e) {
				throw e;
			} catch (Exception e) {
				LogisticsPipes.log.error(packet.getClass().getName());
				LogisticsPipes.log.error(packet.toString());
				throw new RuntimeException(e);
			}
			screen.inventorySlots.windowId = packet.getWindowID();
			FMLCommonHandler.instance().showGuiScreen(screen);
		}
	}
}
