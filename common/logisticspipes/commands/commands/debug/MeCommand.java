package logisticspipes.commands.commands.debug;

import logisticspipes.commands.abstracts.ICommandHandler;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class MeCommand implements ICommandHandler {

	@Override
	public String[] getNames() {
		return new String[] { "me", "self" };
	}

	@Override
	public boolean isCommandUsableBy(ICommandSender sender) {
		return sender instanceof EntityPlayer;
	}

	@Override
	public String[] getDescription() {
		return new String[] { "Start debugging the CommandSender" };
	}

	@Override
	public void executeCommand(ICommandSender sender, String[] args) {
		DebugGuiController.instance().startWatchingOf(sender, (EntityPlayer) sender);
		sender.addChatMessage(new TextComponentString("Starting SelfDebuging"));
	}
}
