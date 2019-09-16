package me.fromgate.reactions.storages;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.data.DoubleValue;
import me.fromgate.reactions.util.data.ItemStackValue;
import me.fromgate.reactions.util.location.LocationUtil;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Created by MaxDikiy on 2017-09-04.
 */
public class PickupItemStorage extends Storage {
	@Getter private ItemStack item;
	@Getter private int pickupDelay;
	private final Location dropLoc;

	public PickupItemStorage(Player p, Item item, int pickupDelay) {
		super(p, ActivatorType.PICKUP_ITEM);
		this.item = item.getItemStack();
		this.pickupDelay = pickupDelay;
		this.dropLoc = item.getLocation();
	}

	@Override
	void defaultVariables(Map<String, String> tempVars) {
		tempVars.put("droplocation", LocationUtil.locationToString(dropLoc));
	}

	@Override
	void defaultChangeables(Map<String, DataValue> changeables) {
		changeables.put(Storage.CANCEL_EVENT, new BooleanValue(false));
		changeables.put("pickupdelay", new DoubleValue(pickupDelay));
		changeables.put("item", new ItemStackValue(item));
	}
}
