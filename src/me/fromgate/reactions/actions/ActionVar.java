/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2014, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *    
 *  This file is part of ReActions.
 *  
 *  ReActions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ReActions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ReActions.  If not, see <http://www.gnorg/licenses/>.
 * 
 */

package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Variables;

import org.bukkit.entity.Player;

public class ActionVar extends Action  {
    private int actType = -1;
    private boolean personalVar = false;

    public ActionVar(int actType, boolean personalVar) {
        this.actType = actType;
        this.personalVar = personalVar;
    }

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        Player player = p;
        String var;
        String value;
        
        if (ParamUtil.isParamExists(params, "id")){
            var = ParamUtil.getParam(params, "id", "");
            if (var.isEmpty()) return false;
            value = ParamUtil.getParam(params, "value", "");
        } else {
            String [] ln = ParamUtil.getParam(params, "param-line", "").split("/",2);
            if (ln.length == 0) return false;
            var = ln[0];
            value = (ln.length>1) ? ln[1] : "";
        }

        if (!this.personalVar) player = null;
        else if (player == null) return false;

        switch (this.actType){
        case 0: //VAR_SET, VAR_PLAYER_SET
            Variables.setVar(player, var, value);
            return true;
        case 1: //VAR_CLEAR, VAR_PLAYER_CLEAR
            Variables.clearVar(player, var);
            return true;
        case 2: //VAR_INC, VAR_PLAYER_INC
            int incValue = value.isEmpty()||!(u().isInteger(value)) ? 1 : Integer.parseInt(value);
            return Variables.incVar(player, var,incValue);
        case 3: //VAR_DEC, VAR_PLAYER_DEC
            int decValue = value.isEmpty()||!(u().isInteger(value)) ? 1 : Integer.parseInt(value);
            return Variables.decVar(player, var,decValue);
        }
        return false;
    }

}
