package me.fromgate.reactions.storages;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.util.item.VirtualItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ItemConsumeStorage extends Storage {

	@Getter private final ItemStack item;
	@Getter private final boolean mainHand;

	public ItemConsumeStorage(Player p, ItemStack item, boolean mainHand) {
		super(p, ActivatorType.ITEM_CONSUME);
		this.item = item;
		this.mainHand = mainHand;
	}

	@Override
	void defaultVariables(Map<String, String> tempVars) {
		VirtualItem vItem = VirtualItem.fromItemStack(item);
		if(item != null) {
			tempVars.put("item", vItem.toString());
			tempVars.put("item-str", vItem.toDisplayString());
		}
		tempVars.put("hand", mainHand ? "MAIN" : "OFF");
	}
}
