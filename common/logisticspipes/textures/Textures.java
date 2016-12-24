package logisticspipes.textures;




import logisticspipes.renderer.IIconProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;

import logisticspipes.LPConstants;
import logisticspipes.proxy.MainProxy;
import logisticspipes.textures.provider.LPActionTriggerIconProvider;
import logisticspipes.textures.provider.LPPipeIconProvider;
import logisticspipes.textures.provider.LPPipeIconTransformerProvider;

public class Textures {

	public static TextureType empty = new TextureType();
	public static TextureType empty_1 = new TextureType();
	public static TextureType empty_2 = new TextureType();
	public static SmallTextureType smallEmpty = new SmallTextureType();
	public static TextureType LOGISTICSPIPE_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_PROVIDER_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_REQUESTER_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_CRAFTER_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_SATELLITE_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_SUPPLIER_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_LIQUIDSUPPLIER_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_LIQUIDSUPPLIER_MK2_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_ROUTED_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_NOTROUTED_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_LIQUID_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_ROUTED_POWERED_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_NOTROUTED_POWERED_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_LIQUID_POWERED_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_DIRECTION_POWERED_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_SUBPOWER_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_POWERED_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_POWERED_POWERED_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_SECURITY_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_CHASSI_ROUTED_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_CHASSI_NOTROUTED_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_CHASSI_DIRECTION_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_CHASSI1_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_CHASSI2_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_CHASSI3_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_CHASSI4_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_CHASSI5_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_CRAFTERMK2_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_REQUESTERMK2_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_PROVIDERMK2_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_REMOTE_ORDERER_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_APIARIST_ANALYSER_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_APIARIST_SINK_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_INVSYSCON_CON_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_INVSYSCON_DIS_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_INVSYSCON_MIS_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_ENTRANCE_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_DESTINATION_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_CRAFTERMK3_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_FIREWALL_TEXTURE = Textures.empty;
	public static TextureType LOGISTICSPIPE_LIQUID_BASIC = Textures.empty;
	public static TextureType LOGISTICSPIPE_LIQUID_INSERTION = Textures.empty;
	public static TextureType LOGISTICSPIPE_LIQUID_PROVIDER = Textures.empty;
	public static TextureType LOGISTICSPIPE_LIQUID_REQUEST = Textures.empty;
	public static TextureType LOGISTICSPIPE_LIQUID_EXTRACTOR = Textures.empty;
	public static TextureType LOGISTICSPIPE_LIQUID_SATELLITE = Textures.empty;
	public static TextureType LOGISTICSPIPE_OPAQUE_TEXTURE = Textures.empty;
	public static SmallTextureType LOGISTICSPIPE_BASIC_TRANSPORT_TEXTURE = Textures.smallEmpty;
	public static TextureAtlasSprite LOGISTICS_REQUEST_TABLE[] = new TextureAtlasSprite[0];
	public static TextureAtlasSprite LOGISTICS_REQUEST_TABLE_NEW = null;
	public static TextureAtlasSprite LOGISTICS_REQUEST_TABLE_NEW_ROUTED = null;
	public static TextureAtlasSprite LOGISTICS_REQUEST_TABLE_NEW_UNROUTED = null;
	public static TextureAtlasSprite LOGISTICS_REQUEST_TABLE_NEW_EMPTY = null;
	public static int LOGISTICSPIPE_LIQUID_CONNECTOR = 0;
	public static TextureAtlasSprite LOGISTICSACTIONTRIGGERS_DISABLED;
	public static TextureAtlasSprite LOGISTICSACTIONTRIGGERS_CRAFTING_ICON;
	public static TextureAtlasSprite LOGISTICSACTIONTRIGGERS_TEXTURE_FILE;
	public static TextureAtlasSprite LOGISTICSACTIONTRIGGERS_NEEDS_POWER_ICON;
	public static TextureAtlasSprite LOGISTICSACTIONTRIGGERS_SUPPLIER_FAILED_ICON;
	public static TextureAtlasSprite[] LOGISTICS_UPGRADES_DISCONECT_ICONINDEX;
	public static TextureAtlasSprite[] LOGISTICS_UPGRADES_SNEAKY_ICONINDEX;
	public static TextureAtlasSprite[] LOGISTICS_UPGRADES_ICONINDEX;
	public static TextureAtlasSprite LOGISTICSITEMS_ITEMHUD_ICON;
	public static TextureAtlasSprite LOGISTICSITEMTEXTURE_FOR_DISK;
	public static TextureAtlasSprite LOGISTICS_SIDE_SELECTION;
	// Standalone pipes
	public static String LOGISTICSPIPE_TEXTURE_FILE = "pipes/basic";
	public static String LOGISTICSPIPE_PROVIDER_TEXTURE_FILE = "pipes/provider";
	public static String LOGISTICSPIPE_PROVIDERMK2_TEXTURE_FILE = "pipes/provider_mk2";
	public static String LOGISTICSPIPE_REQUESTER_TEXTURE_FILE = "pipes/request";
	public static String LOGISTICSPIPE_REQUESTERMK2_TEXTURE_FILE = "pipes/request_mk2";
	public static String LOGISTICSPIPE_CRAFTER_TEXTURE_FILE = "pipes/crafting";
	public static String LOGISTICSPIPE_CRAFTERMK2_TEXTURE_FILE = "pipes/crafting_mk2";
	public static String LOGISTICSPIPE_SATELLITE_TEXTURE_FILE = "pipes/satellite";
	public static String LOGISTICSPIPE_SUPPLIER_TEXTURE_FILE = "pipes/supplier";
	public static String LOGISTICSPIPE_LIQUIDSUPPLIER_TEXTURE_FILE = "pipes/liquid_supplier";
	public static String LOGISTICSPIPE_LIQUIDSUPPLIER_MK2_TEXTURE_FILE = "pipes/liquid_supplier_mk2";
	public static String LOGISTICSPIPE_REMOTE_ORDERER_TEXTURE_FILE = "pipes/remote_orderer";
	public static String LOGISTICSPIPE_APIARIST_ANALYSER_TEXTURE_FILE = "pipes/analyzer";
	public static String LOGISTICSPIPE_APIARIST_SINK_TEXTURE_FILE = "pipes/beesink";
	public static String LOGISTICSPIPE_INVSYSCON_CON_TEXTURE_FILE = "pipes/invsyscon_con";
	public static String LOGISTICSPIPE_INVSYSCON_DIS_TEXTURE_FILE = "pipes/invsyscon_dis";
	public static String LOGISTICSPIPE_INVSYSCON_MIS_TEXTURE_FILE = "pipes/invsyscon_mis";
	public static String LOGISTICSPIPE_ENTRANCE_TEXTURE_FILE = "pipes/entrance";
	public static String LOGISTICSPIPE_DESTINATION_TEXTURE_FILE = "pipes/destination";
	public static String LOGISTICSPIPE_CRAFTERMK3_TEXTURE_FILE = "pipes/crafting_mk3";
	public static String LOGISTICSPIPE_FIREWALL_TEXTURE_FILE = "pipes/firewall";
	// Fluid Pipes
	public static String LOGISTICSPIPE_LIQUID_CONNECTOR_TEXTURE_FILE = "pipes/liquid_connector";
	public static String LOGISTICSPIPE_LIQUID_BASIC_FILE = "pipes/liquid_basic";
	public static String LOGISTICSPIPE_LIQUID_INSERTION_FILE = "pipes/liquid_insertion";
	public static String LOGISTICSPIPE_LIQUID_PROVIDER_FILE = "pipes/liquid_provider";
	public static String LOGISTICSPIPE_LIQUID_REQUEST_FILE = "pipes/liquid_request";
	public static String LOGISTICSPIPE_LIQUID_EXTRACTOR_FILE = "pipes/liquid_extractor";
	public static String LOGISTICSPIPE_LIQUID_SATELLITE_FILE = "pipes/liquid_satellite";
	// Status overlay
	public static String LOGISTICSPIPE_ROUTED_TEXTURE_FILE = "pipes/status_overlay/routed";
	public static String LOGISTICSPIPE_NOTROUTED_TEXTURE_FILE = "pipes/status_overlay/not_routed";
	public static String LOGISTICSPIPE_LIQUID_TEXTURE_FILE = "pipes/status_overlay/liquid_connection";
	public static String LOGISTICSPIPE_ROUTED_POWERED_TEXTURE_FILE = "pipes/status_overlay/routed_powered";
	public static String LOGISTICSPIPE_NOTROUTED_POWERED_TEXTURE_FILE = "pipes/status_overlay/not_routed_powered";
	public static String LOGISTICSPIPE_LIQUID_POWERED_TEXTURE_FILE = "pipes/status_overlay/liquid_connection_powered";
	public static String LOGISTICSPIPE_POWERED_TEXTURE_FILE = "pipes/status_overlay/powered";
	public static String LOGISTICSPIPE_POWERED_POWERED_TEXTURE_FILE = "pipes/status_overlay/powered_powered";
	public static String LOGISTICSPIPE_DIRECTION_POWERED_TEXTURE_FILE = "pipes/status_overlay/direction_powered";
	public static String LOGISTICSPIPE_SECURITY_TEXTURE_FILE = "pipes/status_overlay/security";
	public static String LOGISTICSPIPE_SUBPOWER_TEXTURE_FILE = "pipes/status_overlay/subpower";
	public static String LOGISTICSPIPE_OPAQUE_TEXTURE_FILE = "pipes/status_overlay/opaque";
	// Chassi pipes
	public static String LOGISTICSPIPE_CHASSI1_TEXTURE_FILE = "pipes/chassi/chassi_mk1";
	public static String LOGISTICSPIPE_CHASSI2_TEXTURE_FILE = "pipes/chassi/chassi_mk2";
	public static String LOGISTICSPIPE_CHASSI3_TEXTURE_FILE = "pipes/chassi/chassi_mk3";
	public static String LOGISTICSPIPE_CHASSI4_TEXTURE_FILE = "pipes/chassi/chassi_mk4";
	public static String LOGISTICSPIPE_CHASSI5_TEXTURE_FILE = "pipes/chassi/chassi_mk5";
	// Chassi status overlay
	public static String LOGISTICSPIPE_CHASSI_ROUTED_TEXTURE_FILE = "pipes/chassi/status_overlay/routed";
	public static String LOGISTICSPIPE_CHASSI_NOTROUTED_TEXTURE_FILE = "pipes/chassi/status_overlay/not_routed";
	public static String LOGISTICSPIPE_CHASSI_DIRECTION_TEXTURE_FILE = "pipes/chassi/status_overlay/direction";
	// Pipe Power Overlays
	public static String LOGISTICSPIPE_OVERLAY_POWERED_TEXTURE_FILE = "pipes/status_overlay/powered-pipe";
	public static String LOGISTICSPIPE_OVERLAY_UNPOWERED_TEXTURE_FILE = "pipes/status_overlay/un-powered-pipe";
	public static String LOGISTICSPIPE_UN_OVERLAY_TEXTURE_FILE = "pipes/status_overlay/un-overlayed";
	public static String LOGISTICSPIPE_BASIC_TRANSPORT_TEXTURE_FILE = "pipes/transport/basic";
	public static String LOGISTICS_SOLID_BLOCK = Textures.LOGISTICSPIPE_TEXTURE_FILE;
	public static IIconProvider LPactionIconProvider;
	public static LPPipeIconProvider LPpipeIconProvider;
	public static LPPipeIconTransformerProvider LPnewPipeIconProvider;

	static {
		Textures.empty.normal = 0;
		Textures.empty.powered = 0;
		Textures.empty.unpowered = 0;
	}

	static {
		Textures.empty_1.normal = 1;
		Textures.empty_1.powered = 1;
		Textures.empty_1.unpowered = 1;
	}

	static {
		Textures.empty_2.normal = 2;
		Textures.empty_2.powered = 2;
		Textures.empty_2.unpowered = 2;
	}

	static {
		Textures.smallEmpty.normal = 0;
		Textures.smallEmpty.newTexture = 0;
	}

	private int index = 0;
	private int newTextureIndex = 0;
	public Textures() {
		Textures.LPactionIconProvider = new LPActionTriggerIconProvider();
		Textures.LPpipeIconProvider = new LPPipeIconProvider();
		Textures.LPnewPipeIconProvider = new LPPipeIconTransformerProvider();
	}

	//this gets called with null par1TextureAtlasSpriteRegister from preinit, and later with non-null from textureprestitch
	public void registerBlockIcons(TextureMap map) {
		//Register Empty Texture for slot 0
		MainProxy.proxy.addLogisticsPipesOverride(map, 0, "empty", "", true);
		MainProxy.proxy.addLogisticsPipesOverride(map, 1, "empty", "", true);
		MainProxy.proxy.addLogisticsPipesOverride(map, 2, "empty", "", true);

		index = 3;
		newTextureIndex = 0;

		// Standalone pipes
		Textures.LOGISTICSPIPE_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_PROVIDER_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_PROVIDER_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_POWERED_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_POWERED_TEXTURE_FILE, 2);
		Textures.LOGISTICSPIPE_POWERED_POWERED_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_POWERED_POWERED_TEXTURE_FILE, 2);
		Textures.LOGISTICSPIPE_DIRECTION_POWERED_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_DIRECTION_POWERED_TEXTURE_FILE, 2);
		Textures.LOGISTICSPIPE_SECURITY_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_SECURITY_TEXTURE_FILE, 2);
		Textures.LOGISTICSPIPE_ROUTED_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_ROUTED_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_NOTROUTED_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_NOTROUTED_TEXTURE_FILE, 2);
		Textures.LOGISTICSPIPE_ROUTED_POWERED_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_ROUTED_POWERED_TEXTURE_FILE, 2);
		Textures.LOGISTICSPIPE_NOTROUTED_POWERED_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_NOTROUTED_POWERED_TEXTURE_FILE, 2);
		Textures.LOGISTICSPIPE_SUBPOWER_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_SUBPOWER_TEXTURE_FILE, 2);
		Textures.LOGISTICSPIPE_OPAQUE_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_OPAQUE_TEXTURE_FILE, 2);
		Textures.LOGISTICSPIPE_REQUESTER_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_REQUESTER_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_CRAFTER_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_CRAFTER_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_SATELLITE_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_SATELLITE_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_SUPPLIER_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_SUPPLIER_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_LIQUIDSUPPLIER_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_LIQUIDSUPPLIER_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_LIQUIDSUPPLIER_MK2_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_LIQUIDSUPPLIER_MK2_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_CRAFTERMK2_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_CRAFTERMK2_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_REQUESTERMK2_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_REQUESTERMK2_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_PROVIDERMK2_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_PROVIDERMK2_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_REMOTE_ORDERER_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_REMOTE_ORDERER_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_APIARIST_ANALYSER_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_APIARIST_ANALYSER_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_APIARIST_SINK_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_APIARIST_SINK_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_INVSYSCON_CON_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_INVSYSCON_CON_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_INVSYSCON_DIS_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_INVSYSCON_DIS_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_INVSYSCON_MIS_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_INVSYSCON_MIS_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_ENTRANCE_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_ENTRANCE_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_DESTINATION_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_DESTINATION_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_CRAFTERMK3_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_CRAFTERMK3_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_FIREWALL_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_FIREWALL_TEXTURE_FILE);
		//Fluid
		Textures.LOGISTICSPIPE_LIQUID_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_LIQUID_TEXTURE_FILE, 2);
		Textures.LOGISTICSPIPE_LIQUID_POWERED_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_LIQUID_POWERED_TEXTURE_FILE, 2);
		Textures.LOGISTICSPIPE_LIQUID_CONNECTOR = registerSingleTexture(map, Textures.LOGISTICSPIPE_LIQUID_CONNECTOR_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_LIQUID_BASIC = registerTexture(map, Textures.LOGISTICSPIPE_LIQUID_BASIC_FILE);
		Textures.LOGISTICSPIPE_LIQUID_INSERTION = registerTexture(map, Textures.LOGISTICSPIPE_LIQUID_INSERTION_FILE);
		Textures.LOGISTICSPIPE_LIQUID_PROVIDER = registerTexture(map, Textures.LOGISTICSPIPE_LIQUID_PROVIDER_FILE);
		Textures.LOGISTICSPIPE_LIQUID_REQUEST = registerTexture(map, Textures.LOGISTICSPIPE_LIQUID_REQUEST_FILE);
		Textures.LOGISTICSPIPE_LIQUID_EXTRACTOR = registerTexture(map, Textures.LOGISTICSPIPE_LIQUID_EXTRACTOR_FILE);
		Textures.LOGISTICSPIPE_LIQUID_SATELLITE = registerTexture(map, Textures.LOGISTICSPIPE_LIQUID_SATELLITE_FILE);

		//Chassi
		Textures.LOGISTICSPIPE_CHASSI_ROUTED_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_CHASSI_ROUTED_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_CHASSI_NOTROUTED_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_CHASSI_NOTROUTED_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_CHASSI_DIRECTION_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_CHASSI_DIRECTION_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_CHASSI1_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_CHASSI1_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_CHASSI2_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_CHASSI2_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_CHASSI3_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_CHASSI3_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_CHASSI4_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_CHASSI4_TEXTURE_FILE);
		Textures.LOGISTICSPIPE_CHASSI5_TEXTURE = registerTexture(map, Textures.LOGISTICSPIPE_CHASSI5_TEXTURE_FILE);

		//Transport
		Textures.LOGISTICSPIPE_BASIC_TRANSPORT_TEXTURE = registerSmallTexture(map, Textures.LOGISTICSPIPE_BASIC_TRANSPORT_TEXTURE_FILE);

		if (MainProxy.isClient() && map != null) {
			Textures.LOGISTICS_REQUEST_TABLE = new TextureAtlasSprite[5];
			for (int i = 0; i < 5; i++) {
				Textures.LOGISTICS_REQUEST_TABLE[i] = map.registerSprite(new ResourceLocation("logisticspipes:requesttable/" + i));
			}
			Textures.LOGISTICS_REQUEST_TABLE_NEW = map.registerSprite(new ResourceLocation("logisticspipes:requesttable/requestTexture"));
			Textures.LOGISTICS_REQUEST_TABLE_NEW_ROUTED = map.registerSprite(new ResourceLocation("logisticspipes:requesttable/routed"));
			Textures.LOGISTICS_REQUEST_TABLE_NEW_UNROUTED = map.registerSprite(new ResourceLocation("logisticspipes:requesttable/unrouted"));
			Textures.LOGISTICS_REQUEST_TABLE_NEW_EMPTY = map.registerSprite(new ResourceLocation("logisticspipes:empty"));
			Textures.LOGISTICS_SIDE_SELECTION = map.registerSprite(new ResourceLocation("logisticspipes:sideSelection"));
		}

		if (LPConstants.DEBUG) {
			System.out.println("LP: pipetextures " + index);
		}
	}

	public void registerItemIcons(TextureMap par1TextureAtlasSpriteRegister) {
		Textures.LPactionIconProvider.registerIcons(par1TextureAtlasSpriteRegister);
	}

	private TextureType registerTexture(TextureMap par1TextureAtlasSpriteRegister, String fileName) {
		return registerTexture(par1TextureAtlasSpriteRegister, fileName, 1);
	}

	/**
	 * @param map
	 *            - IconRegister
	 * @param fileName
	 *            - name of texture
	 * @param flag
	 *            - 2 - register single texture without overlay, 1/0 register
	 *            with overlay
	 */

	private TextureType registerTexture(TextureMap map, String fileName, int flag) {
		TextureType texture = new TextureType();
		texture.normal = index++;
		texture.powered = texture.normal;
		texture.unpowered = texture.normal;
		texture.fileName = fileName;
		boolean isClient = MainProxy.isClient();
		if (isClient) {
			MainProxy.proxy.addLogisticsPipesOverride(map, texture.normal, fileName, Textures.LOGISTICSPIPE_UN_OVERLAY_TEXTURE_FILE, (flag == 2));
		}
		if (flag == 1) {
			texture.powered = index++;
			texture.unpowered = index++;
		}
		return texture;
	}

	private SmallTextureType registerSmallTexture(TextureMap map, String fileName) {
		SmallTextureType texture = new SmallTextureType();
		texture.normal = index++;
		texture.fileName = fileName;
		boolean isClient = MainProxy.isClient();
		if (isClient) {
			MainProxy.proxy.addLogisticsPipesOverride(map, texture.normal, fileName, "", true);
		}
		texture.newTexture = newTextureIndex++;
		MainProxy.proxy.addLogisticsPipesOverride(map, texture.newTexture, fileName, "NewPipeTexture", true);
		return texture;
	}

	private int registerSingleTexture(TextureMap map, String fileName) {
		int texture = index++;
		if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			MainProxy.proxy.addLogisticsPipesOverride(map, texture, fileName, Textures.LOGISTICSPIPE_UN_OVERLAY_TEXTURE_FILE, true);
		}
		return texture;
	}

	public static class TextureType {

		public int normal;
		public int powered;
		public int unpowered;
		public int newTexture;
		public String fileName = "";
	}

	public static class SmallTextureType {

		public int normal;
		public int newTexture;
		public String fileName = "";
	}
}
