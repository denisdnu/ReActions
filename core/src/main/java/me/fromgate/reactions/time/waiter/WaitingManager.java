package me.fromgate.reactions.time.waiter;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.actions.StoredAction;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WaitingManager {

	private static Set<WaitTask> tasks;
	private static long timeLimit;

	public static void init() {
		tasks = Collections.newSetFromMap(new ConcurrentHashMap<>()); //new HashSet<>();
		load();
	}

	public static void executeDelayed(Player player, StoredAction action, boolean isAction, long time) {
		if (action == null) return;
		List<StoredAction> actions = new ArrayList<>();
		actions.add(action);
		executeDelayed(player, actions, isAction, time);
	}

	public static void executeDelayed(Player player, List<StoredAction> actions, boolean isAction, long time) {
		if (actions.isEmpty()) return;
		String playerStr = player != null ? player.getName() : null;
		WaitTask task = new WaitTask(playerStr, actions, isAction, time);
		tasks.add(task);
		save();
	}

	public static void remove(WaitTask task) {
		tasks.remove(task);
		save();
	}

	public static void load() {
		if (!tasks.isEmpty()) {
			for (WaitTask t : tasks) {
				t.stop();
			}
		}
		tasks.clear();
		YamlConfiguration cfg = new YamlConfiguration();
		File f = new File(ReActions.getPlugin().getDataFolder() + File.separator + "delayed-actions.yml");
		try {
			cfg.load(f);
		} catch (Exception e) {
			Msg.logMessage("Failed to load delayed actions");
			return;
		}
		for (String key : cfg.getKeys(false)) {
			WaitTask t = new WaitTask(cfg, key);
			tasks.add(t);
		}
		Bukkit.getScheduler().runTaskTimerAsynchronously(ReActions.getPlugin(), WaitingManager::refresh, 30, 900);
	}

	public static void refreshPlayer(Player player) {
		refreshPlayer(player.getName());
	}

	// TODO: Use UUIDs
	public static void refreshPlayer(String player) {
		if (tasks.isEmpty()) return;
		int before = tasks.size();
		Iterator<WaitTask> iter = tasks.iterator();
		while(iter.hasNext()) {
			WaitTask t = iter.next();
			if (player.equals(t.getPlayerName()) && t.isTimePassed()) t.execute();
			if (t.isExecuted()) iter.remove();
		}
		if (tasks.size() != before) save();
	}

	public static void refresh() {
		if (tasks.isEmpty()) return;
		int before = tasks.size();
		Iterator<WaitTask> iter = tasks.iterator();
		while(iter.hasNext()) {
			WaitTask t = iter.next();
			if (t.isTimePassed()) t.execute();
			if (t.isExecuted()) iter.remove();
		}
		if (tasks.size() != before) save();
	}

	public static void save() {
		Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), () -> {
			YamlConfiguration cfg = new YamlConfiguration();
			File f = new File(ReActions.getPlugin().getDataFolder() + File.separator + "delayed-actions.yml");
			if (f.exists()) f.delete();
			for (WaitTask t : tasks) {
				if (!t.isExecuted()) t.save(cfg);
			}
			try {
				cfg.save(f);
			} catch (Throwable e) {
				Msg.logMessage("Failed to save delayed actions");
			}
		}, 1);
	}

	public static void updateLimit(int hours) {
		timeLimit = hours * 3600000;
	}

	public static int getTimeLimit() {
		return (int) (timeLimit / 3600000);
	}
}