package as.pw.candee.command.impl;

import java.util.Optional;
import as.pw.candee.command.Command;
import as.pw.candee.command.Command.Declaration;
import as.pw.candee.managers.ModuleManager;
import as.pw.candee.module.Module;
import as.pw.candee.utils.ChatUtil;

@Declaration(name = "Toggle", syntax = "toggle [module]", alias = {"toggle", "t", "enable", "disable"})
public class ToggleCommand extends Command {
    public void onCommand(String label, String[] args, boolean clientSide) {
        if (args.length < 1) {
            ChatUtil.sendMessage(getSyntax());
            return;
        }
        String moduleName = args[0];
        Module module = ModuleManager.getModule(moduleName);
        if (module == null) {
            Optional<Module> match = ModuleManager.getModules().stream().filter(m -> m.getName().equalsIgnoreCase(moduleName)).findFirst();
            if (match.isPresent()) module = match.get();
        }
        if (module == null) {
            ChatUtil.sendMessage(getSyntax());
            return;
        }
        module.toggle();
    }
}