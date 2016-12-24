package logisticspipes.utils;

import net.minecraft.util.EnumFacing;

public final class DirectionUtils {
    private DirectionUtils() {

    }

    public static int ordinalNullable(EnumFacing facing) {
        return facing != null ? facing.ordinal() : 6;
    }

    public static EnumFacing getFacingNullable(int i) {
        return i == 6 ? null : EnumFacing.getFront(i);
    }
}
