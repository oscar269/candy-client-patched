package as.pw.candee.command.impl;

import java.util.ArrayList;
import as.pw.candee.command.Command;
import as.pw.candee.command.Command.Declaration;
import as.pw.candee.command.CommandManager;
import as.pw.candee.utils.ChatUtil;

@Declaration(name = "Commands", syntax = "commands", alias = {"commands", "cmds", "help"})
public class CommandsCommand extends Command {
    public void onCommand(String label, String[] args, boolean clientSide) {
        ArrayList<Command> cmds = CommandManager.getCommands();
        StringBuilder sb = new StringBuilder("Commands: ");
        for (Command cmd : cmds) {
            sb.append(cmd.getName()).append(", ");
        }
        if (sb.length() > 11) {
            sb.setLength(sb.length() - 2);
        }
        ChatUtil.sendMessage(sb.toString());
    }
}