package logisticspipes.commands.commands.wrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import logisticspipes.asm.wrapper.AbstractWrapper;
import logisticspipes.asm.wrapper.LogisticsWrapperHandler;
import logisticspipes.asm.wrapper.WrapperState;
import logisticspipes.commands.LogisticsPipesCommand;
import logisticspipes.commands.abstracts.ICommandHandler;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;

public class EnableCommand implements ICommandHandler {

	@Override
	public String[] getNames() {
		return new String[] { "enable" };
	}

	@Override
	public boolean isCommandUsableBy(ICommandSender sender) {
		return LogisticsPipesCommand.isOP(sender);
	}

	@Override
	public String[] getDescription() {
		return new String[] { "Reenables the givven proxy", "if it was disabled by an exception." };
	}

	@Override
	public void executeCommand(ICommandSender sender, String[] args) {
		if (args.length != 1) {
			sender.addChatMessage(new TextComponentString("Wrong amount of arguments"));
			return;
		}
		String name = args[0];
		List<AbstractWrapper> list = new ArrayList<>(LogisticsWrapperHandler.wrapperController);
		Iterator<AbstractWrapper> iter = list.iterator();
		while (iter.hasNext()) {
			AbstractWrapper item = iter.next();
			if (item.getState() != WrapperState.Exception) {
				iter.remove();
			}
		}
		iter = list.iterator();
		while (iter.hasNext()) {
			AbstractWrapper item = iter.next();
			if (!(item.getName() + item.getTypeName()).startsWith(name)) {
				iter.remove();
			}
		}
		if (list.size() > 1) {
			sender.addChatMessage(new TextComponentString("Possible: "));
			for (AbstractWrapper can : list) {
				sender.addChatMessage(new TextComponentString(can.getName() + can.getTypeName()));
			}
		} else if (list.isEmpty()) {
			sender.addChatMessage(new TextComponentString("No match found"));
		} else {
			AbstractWrapper wrapper = list.get(0);
			wrapper.reEnable();
		}
	}
}
