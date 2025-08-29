package as.pw.candee.command.impl;

import as.pw.candee.command.Command;
import as.pw.candee.command.Command.Declaration;
import as.pw.candee.command.CommandManager;
import as.pw.candee.utils.ChatUtil;

@Declaration(name = "Prefix", syntax = "prefix <value> (no letters or numbers)", alias = {"prefix", "setprefix", "cmdprefix", "commandprefix"})
public class PrefixCommand extends Command {
    public void onCommand(String label, String[] args, boolean clientSide) {
        if (args.length < 1) {
            ChatUtil.sendMessage(getSyntax());
            return;
        }
        String val = args[0];
        if (val.length() != 1 || Character.isLetterOrDigit(val.charAt(0))) {
            ChatUtil.sendMessage(getSyntax());
        } else {
            CommandManager.setCommandPrefix(val);
            ChatUtil.sendMessage("Prefix set: \"" + val + "\"!");
        }
    }
}