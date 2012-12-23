/** 
 * Copyright (c) Krapht, 2011
 * 
 * "LogisticsPipes" is distributed under the terms of the Minecraft Mod Public 
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package logisticspipes.utils;

import java.util.LinkedList;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.core.Position;
import buildcraft.transport.TileGenericPipe;

public class WorldUtil {
	private int _x;
	private int _y;
	private int _z;
	
	private World _worldObj;
	
	public WorldUtil(World worldObj, int x, int y, int z) {
		this._worldObj = worldObj;
		this._x = x;
		this._y = y;
		this._z = z;
	}

	public LinkedList<AdjacentTile> getAdjacentTileEntities(boolean flag) {
		LinkedList<AdjacentTile> foundTiles = new LinkedList<AdjacentTile>();
		for (ForgeDirection o : ForgeDirection.values()) {
			if (o == ForgeDirection.UNKNOWN) continue;
			Position p = new Position(_x, _y, _z, o);
			p.moveForwards(1);
			TileEntity tile = _worldObj.getBlockTileEntity((int)p.x, (int)p.y, (int)p.z);

			if (tile == null) continue;
			
			if(flag) {
				TileEntity tilePipe = _worldObj.getBlockTileEntity(_x, _y, _z);
				if(tilePipe instanceof TileGenericPipe) {
					if(((TileGenericPipe)tilePipe).pipe != null) {
						if(!((TileGenericPipe)tilePipe).pipe.isPipeConnected(tile)) {
							continue;
						}
					}
				}
			}
			foundTiles.add(new AdjacentTile(tile, o));
		}
		return foundTiles;
	}
}
