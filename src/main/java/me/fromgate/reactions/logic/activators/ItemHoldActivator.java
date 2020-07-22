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

import lombok.Getter;
import me.fromgate.reactions.logic.storages.ItemHoldStorage;
import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.item.ItemUtils;
import me.fromgate.reactions.util.item.VirtualItem;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;

public class ItemHoldActivator extends Activator /*implements Manageable*/ {
    // TODO: Store VirtualItem
    @Getter
    private String itemStr;
    // TODO: Hand option

    private ItemHoldActivator(ActivatorBase base, String item) {
        super(base);
        this.itemStr = item;
    }

    public static ItemHoldActivator create(ActivatorBase base, Parameters param) {
        String item = param.getParam("item", "");
        return new ItemHoldActivator(base, item);
    }

    public static ItemHoldActivator load(ActivatorBase base, ConfigurationSection cfg) {
        String item = cfg.getString("item", "");
        return new ItemHoldActivator(base, item);
    }

    @Override
    public boolean activate(Storage event) {
        if (itemStr.isEmpty() || (VirtualItem.fromString(itemStr) == null)) {
            Msg.logOnce(getBase().getName() + "activatorholdempty", "Failed to parse itemStr of activator " + getBase().getName());
            return false;
        }
        ItemHoldStorage ie = (ItemHoldStorage) event;
        return ItemUtils.compareItemStr(ie.getItem(), this.itemStr);
    }

    @Override
    public void save(ConfigurationSection cfg) {
        cfg.set("item", this.itemStr);
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.ITEM_HOLD;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (").append(this.itemStr).append(")");
        return sb.toString();
    }

    @Override
    public boolean isValid() {
        return !Utils.isStringEmpty(itemStr);
    }
}

