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

package me.fromgate.reactions.activators.triggers;

import me.fromgate.reactions.activators.storages.ItemConsumeStorage;
import me.fromgate.reactions.activators.storages.Storage;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.item.ItemUtils;
import me.fromgate.reactions.util.item.VirtualItem;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;

public class ItemConsumeTrigger extends Trigger {
    // TODO: Store VirtualItem
    private final String item;
    // TODO: Hand option

    private ItemConsumeTrigger(ActivatorBase base, String item) {
        super(base);
        this.item = item;
    }

    public static ItemConsumeTrigger create(ActivatorBase base, Parameters param) {
        String item = param.getString("item", param.toString());
        return new ItemConsumeTrigger(base, item);
    }

    public static ItemConsumeTrigger load(ActivatorBase base, ConfigurationSection cfg) {
        String item = cfg.getString("item", "");
        return new ItemConsumeTrigger(base, item);
    }

    public boolean proceed(Storage event) {
        if (this.item.isEmpty() || VirtualItem.fromString(this.item) == null) {
            Msg.logOnce(base.getName() + "activatoritemempty", "Failed to parse item of activator " + base.getName());
            return false;
        }
        ItemConsumeStorage ie = (ItemConsumeStorage) event;
        return ItemUtils.compareItemStr(ie.getItem(), this.item);
    }

    public void saveTrigger(ConfigurationSection cfg) {
        cfg.set("item", this.item);
    }

    public ActivatorType getType() {
        return ActivatorType.ITEM_CONSUME;
    }

    @Override
    public boolean isValid() {
        return !Utils.isStringEmpty(item);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (");
        sb.append(this.item);
        sb.append(")");
        return sb.toString();
    }
}