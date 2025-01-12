package me.fromgate.reactions.commands;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.logic.actions.Actions;
import me.fromgate.reactions.logic.activators.Activator;
import me.fromgate.reactions.logic.flags.Flags;
import me.fromgate.reactions.util.location.LocationUtils;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CmdDefine(command = "react", description = Msg.CMD_ADD, permission = "reactions.config",
        subCommands = {"add"}, allowConsole = true,
        shortDescription = "&3/react add <Id> [f <flag> <param> | <a|r> <action> <param>]")
public class CmdAdd extends Cmd {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 1) return false;
        Player player = (sender instanceof Player) ? (Player) sender : null;
        String arg1 = args[1];
        String arg2 = args.length >= 3 ? args[2] : "";
        String arg3 = args.length >= 4 ? args[3] : "";
        StringBuilder arg4 = new StringBuilder(args.length >= 5 ? args[4] : "");
        if (args.length > 5) {
            for (int i = 5; i < args.length; i++)
                arg4.append(" ").append(args[i]);
            arg4 = new StringBuilder(arg4.toString().trim());
        }
        if (ReActions.getActivators().containsActivator(arg1)) {
            String param = LocationUtils.parsePlaceholders(player, arg4.toString()); // используется в addActions
            Activator act = ReActions.getActivators().getActivator(arg1);
            switch (arg2) {
                case "a":
                case "action":
                    if (addAction(arg1, arg3, param)) {
                        Msg.CMD_ACTADDED.print(sender, arg3 + " (" + param + ")");
                        break;
                    } else {
                        Msg.CMD_ACTNOTADDED.print(sender, arg3 + " (" + param + ")");
                        return true;
                    }
                case "r":
                case "reaction":
                    if (addReaction(arg1, arg3, param)) {
                        Msg.CMD_REACTADDED.print(sender, arg3 + " (" + param + ")");
                        break;
                    } else {
                        Msg.CMD_REACTNOTADDED.print(sender, arg3 + " (" + param + ")");
                        return true;
                    }
                case "f":
                case "flag":
                    if (addFlag(arg1, arg3, param)) {
                        Msg.CMD_FLAGADDED.print(sender, arg3 + " (" + param + ")");
                        break;
                    } else {
                        Msg.CMD_FLAGNOTADDED.print(sender, arg3 + " (" + param + ")");
                        return true;
                    }
                default:
                    Msg.CMD_UNKNOWNBUTTON.print(sender, arg2);
                    return true;
            }
            ReActions.getActivators().saveActivators(act.getLogic().getGroup());
        } else {
            Msg.CMD_UNKNOWNADD.print(sender, 'c');
        }
        return true;
    }

    private boolean addAction(String activator, String act, String param) {
        if (Actions.isValid(act)) {
            ReActions.getActivators().addAction(activator, act, param);
            return true;
        }
        return false;
    }

    private boolean addReaction(String activator, String act, String param) {
        if (Actions.isValid(act)) {
            ReActions.getActivators().addReaction(activator, act, param);
            return true;
        }
        return false;
    }

    private boolean addFlag(String activator, String fl, String param) {
        String flag = fl.replaceFirst("!", "");
        boolean not = fl.startsWith("!");
        if (Flags.isValid(flag)) {
            // TODO: все эти проверки вынести в соответствующие классы
            ReActions.getActivators().addFlag(activator, flag, param, not);
            return true;
        }
        return false;
    }

}
