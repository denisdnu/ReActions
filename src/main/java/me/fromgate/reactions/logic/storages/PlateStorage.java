/*
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
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

package me.fromgate.reactions.logic.storages;

import lombok.Getter;
import me.fromgate.reactions.logic.activators.ActivatorType;
import me.fromgate.reactions.util.collections.MapBuilder;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;

@Getter
public class PlateStorage extends Storage {

    private final Location location;

    public PlateStorage(Player p, Location loc) {
        super(p, ActivatorType.PLATE);
        this.location = loc;
    }

    @Override
    protected Map<String, DataValue> prepareChangeables() {
        return MapBuilder.single(Storage.CANCEL_EVENT, new BooleanValue(false));
    }
}
