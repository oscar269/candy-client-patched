package as.pw.candee.command;

import java.util.ArrayList;
import as.pw.candee.command.impl.FriendCommand;
import as.pw.candee.command.impl.PrefixCommand;
import as.pw.candee.utils.ChatUtil;

public class CommandManager {
    private static String commandPrefix = "-";
    public static final ArrayList<Command> commands = new ArrayList<>();
    public static boolean isValidCommand = false;

    public static void init() {
        registerCommand(new PrefixCommand());
        registerCommand(new FriendCommand());
    }

    public static void registerCommand(Command command) {
        commands.add(command);
    }

    public static ArrayList<Command> getCommands() {
        return commands;
    }

    public static String getCommandPrefix() {
        return commandPrefix;
    }

    public static void setCommandPrefix(String prefix) {
        commandPrefix = prefix;
    }

    public static void callCommand(String input, boolean clientSide) {
        String[] split = input.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String commandLabel = split[0];
        String argsString = input.substring(commandLabel.length()).trim();
        isValidCommand = false;
        for (Command command : commands) {
            for (String alias : command.getAlias()) {
                if (alias.equalsIgnoreCase(commandLabel)) {
                    isValidCommand = true;
                    try {
                        command.onCommand(argsString, argsString.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"), clientSide);
                    } catch (Exception e) {
                        ChatUtil.sendMessage(command.getSyntax());
                    }
                    break;
                }
            }
            if (isValidCommand)
                break;
        }    if (!isValidCommand)
            ChatUtil.sendMessage("Error! Invalid command!");
    }
}