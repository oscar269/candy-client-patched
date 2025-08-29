package as.pw.candee.command.impl;

import as.pw.candee.command.Command;
import as.pw.candee.command.Command.Declaration;
import as.pw.candee.managers.FriendManager;
import as.pw.candee.utils.ChatUtil;

@Declaration(name = "Friend", syntax = "friend list/add/del [player]", alias = {"friend", "friends", "f"})
public class FriendCommand extends Command {
    public void onCommand(String label, String[] args, boolean clientSide) {
        String msg, name;
        if (args.length < 1) {
            ChatUtil.sendMessage(getSyntax());
            return;
        }
        String action = args[0].toLowerCase();
        switch (action) {
            case "list":
                msg = "Friends: " + FriendManager.getFriendsByName() + ".";
                break;
            case "add":
                if (args.length < 2) {
                    msg = getSyntax(); break;
                }
                name = args[1];
                if (FriendManager.isFriend(name)) {
                    msg = name + " is already your friend."; break;
                }
                FriendManager.addFriend(name);
                msg = "Added friend: " + name + ".";
                break;
            case "del":
                if (args.length < 2) {
                    msg = getSyntax(); break;
                }
                name = args[1];
                if (FriendManager.isFriend(name)) {
                    FriendManager.delFriend(name);
                    msg = "Deleted friend: " + name + "."; break;
                }
                msg = name + " isn't your friend.";
                break;
            default:
                msg = getSyntax(); break;
        }
        ChatUtil.sendMessage(msg);
    }
}