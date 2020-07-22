package me.fromgate.reactions.logic.actions;

import me.fromgate.reactions.externals.worldedit.RaWorldEdit;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 18/10/2017.
 */
public class ActionWeSuperPickaxe extends Action {
    @Override
    public boolean execute(RaContext context, Parameters params) {
        Player player = context.getPlayer();
        boolean isSP = params.getParam("value", params.getParam("param-line", false));
        if (params.hasAnyParam("player"))
            player = Utils.getPlayerExact(params.getParam("player"));
        if (isSP) RaWorldEdit.getSession(player).enableSuperPickAxe();
        else RaWorldEdit.getSession(player).disableSuperPickAxe();
        return true;

    }
}