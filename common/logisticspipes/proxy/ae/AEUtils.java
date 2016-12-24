package logisticspipes.proxy.ae;

import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IStorageMonitorable;
import appeng.api.storage.IStorageMonitorableAccessor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class AEUtils {
	@CapabilityInject(IStorageMonitorableAccessor.class)
	public static Capability<IStorageMonitorableAccessor> STORAGE_MONITORABLE_ACCESSOR_CAPABILITY;

	public static boolean hasStorageMonitorableAccessor(TileEntity tile, EnumFacing facing) {
		return tile.hasCapability(STORAGE_MONITORABLE_ACCESSOR_CAPABILITY, facing);
	}

	public static IStorageMonitorableAccessor getStorageMonitorableAccessor(TileEntity tile, EnumFacing facing) {
		if (tile.hasCapability(STORAGE_MONITORABLE_ACCESSOR_CAPABILITY, facing)) {
			IStorageMonitorableAccessor accessor = tile.getCapability(STORAGE_MONITORABLE_ACCESSOR_CAPABILITY, facing);
			return accessor;
		}

		return null;
	}
}
