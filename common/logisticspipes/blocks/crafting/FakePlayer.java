package logisticspipes.blocks.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import com.mojang.authlib.GameProfile;
import net.minecraft.util.text.ITextComponent;

import java.util.UUID;

public class FakePlayer extends EntityPlayer {

	public FakePlayer(TileEntity from) {
		super(from.getWorld(), new GameProfile(UUID.fromString("e7d8e347-3828-4f39-b76f-ea519857c004"), "[LogisticsPipes]"));
		posX = from.getPos().getX();
		posY = from.getPos().getY() + 1;
		posZ = from.getPos().getZ();
	}

	@Override
	public void addChatMessage(ITextComponent c) {}

	@Override
	public boolean canCommandSenderUseCommand(int i, String s) {
		return false;
	}

	// TODO: Check me
	/* @Override
	public ChunkCoordinates getPlayerCoordinates() {
		return null;
	} */

	@Override
	public boolean isSpectator() {
		return false;
	}

	@Override
	public boolean isCreative() {
		return false;
	}
}
