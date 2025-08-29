package as.pw.candee.command.impl;

import as.pw.candee.command.Command;
import as.pw.candee.command.Command.Declaration;
import as.pw.candee.managers.ModuleManager;
import as.pw.candee.module.Module;
import as.pw.candee.utils.ChatUtil;
import org.lwjgl.input.Keyboard;

@Declaration(name = "Bind", syntax = "bind [module] key", alias = {"bind", "b", "setbind", "key"})
public class BindCommand extends Command {
    public void onCommand(String label, String[] args, boolean clientSide) {
        if (args.length < 2) {
            ChatUtil.sendMessage(getSyntax());
                return;
            }
            String moduleName = args[0];
            Module m = ModuleManager.getModule(moduleName);
            if (m == null) {
                for (Module mod : ModuleManager.getModules()) {
                    if (mod.getName().equalsIgnoreCase(moduleName)) {
                        m = mod;
                        break;
                    }
                }
            }
            if (m == null) {
                ChatUtil.sendMessage("Module " + moduleName + " is Not Found");
                return;
            }
            String value = args[1].toUpperCase();
            if ("NONE".equalsIgnoreCase(value)) {
                m.setKey(-1);
                ChatUtil.sendMessage("Module " + m.getName() + " bind set to: NONE!");
            } else if (value.length() == 1) {
                int key = Keyboard.getKeyIndex(value);
                if (key == 0) {
                    ChatUtil.sendMessage(getSyntax());
                } else {
                    m.setKey(key);
                    ChatUtil.sendMessage("Module " + m.getName() + " bind set to: " + value + "!");
                }
            } else {
                ChatUtil.sendMessage(getSyntax());
            }
    }
}