package me.fromgate.reactions.commands;

import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.message.RaDebug;
import org.bukkit.entity.Player;

@CmdDefine(command = "react", description = Msg.CMD_DEBUG, permission = "reactions.debug",
        subCommands = {"debug", "off|true|false"}, allowConsole = false, shortDescription = "&3/react debug [true|false|off]")
public class CmdDebug extends Cmd {
    @Override
    public boolean execute(Player player, String[] args) {
        String arg = args.length >= 2 ? args[1] : "off";
        if (arg.equalsIgnoreCase("false")) {
            RaDebug.setPlayerDebug(player, false);
            Msg.printMSG(player, "cmd_debugfalse");
        } else if (arg.equalsIgnoreCase("true")) {
            RaDebug.setPlayerDebug(player, true);
            Msg.printMSG(player, "cmd_debugtrue");
        } else {
            RaDebug.offPlayerDebug(player);
            Msg.printMSG(player, "cmd_debugoff");
        }
        return true;
    }
}
