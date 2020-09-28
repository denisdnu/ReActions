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

package me.fromgate.reactions.logic.activators;

import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;

public class QuitActivator extends Activator {

    private QuitActivator(ActivatorBase base) {
        super(base);
    }

    public static QuitActivator create(ActivatorBase base, Parameters ignore) {
        return new QuitActivator(base);
    }

    public static QuitActivator load(ActivatorBase base, ConfigurationSection ignore) {
        return new QuitActivator(base);
    }

    @Override
    public boolean proceed(Storage event) {
        return true;
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.QUIT;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        return sb.toString();
    }
}