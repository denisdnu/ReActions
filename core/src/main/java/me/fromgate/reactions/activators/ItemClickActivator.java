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

package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storage.ItemClickStorage;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.item.VirtualItem;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.simpledata.HandType;
import org.bukkit.configuration.ConfigurationSection;

public class ItemClickActivator extends Activator {
	private final String item;
	private final HandType hand;

	public ItemClickActivator(ActivatorBase base, String item, HandType hand) {
		super(base);
		this.item = item;
		this.hand = hand;
	}

	@Override
	public boolean activate(RAStorage event) {
		if (item.isEmpty() || (ItemUtil.parseItemStack(item) == null)) {
			Msg.logOnce(getBase().getName() + "activatoritemempty", "Failed to parse item of activator " + getBase().getName());
			return false;
		}
		ItemClickStorage ie = (ItemClickStorage) event;
		if(hand.checkOff(ie.isMainHand())) return false;
		if (ItemUtil.compareItemStr(ie.getItem(), this.item)) {
			VirtualItem vi = VirtualItem.fromItemStack(ie.getItem());
			if (vi != null) {
				Variables.setTempVar("item", vi.toString());
				Variables.setTempVar("item-str", vi.toDisplayString());
			}
			Variables.setTempVar("hand", ie.isMainHand() ? "MAIN" : "OFF");
			return Actions.executeActivator(ie.getPlayer(), this);
		}
		return false;
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("item", this.item);
		cfg.set("hand", this.hand);
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.ITEM_CLICK;
	}

	@Override
	public boolean isValid() {
		return !Util.emptyString(item);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
		if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
		if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
		if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
		sb.append(" (").append(this.item).append(")");
		return sb.toString();
	}

	public static ItemClickActivator create(ActivatorBase base, Param param) {
		String item = param.getParam("item", param.getParam("param-line"));
		HandType hand = HandType.getByName(param.getParam("hand", "ANY"));
		return new ItemClickActivator(base, item, hand);
	}

	public static ItemClickActivator load(ActivatorBase base, ConfigurationSection cfg) {
		String item = cfg.getString("item", "");
		HandType hand = HandType.getByName(cfg.getString("hand", "ANY"));
		return new ItemClickActivator(base, item, hand);
	}
}
