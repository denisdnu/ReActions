package me.fromgate.reactions.logic.activators;

import me.fromgate.reactions.logic.storages.GameModeStorage;
import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Created by MaxDikiy on 2017-10-27.
 */
public class GamemodeActivator extends Activator {
    private final GameMode gameMode;

    private GamemodeActivator(ActivatorBase base, GameMode gameMode) {
        super(base);
        this.gameMode = gameMode;
    }

    public static GamemodeActivator create(ActivatorBase base, Parameters param) {
        GameMode gameMode = Util.getEnum(GameMode.class, param.getParam("gamemode", "ANY"));
        return new GamemodeActivator(base, gameMode);
    }

    public static GamemodeActivator load(ActivatorBase base, ConfigurationSection cfg) {
        GameMode gameMode = Util.getEnum(GameMode.class, cfg.getString("gamemode", "ANY"));
        return new GamemodeActivator(base, gameMode);
    }

    @Override
    public boolean activate(Storage event) {
        GameModeStorage e = (GameModeStorage) event;
        return gameModeCheck(e.getGameMode());
    }

    private boolean gameModeCheck(GameMode gm) {
        if (gameMode == null) return true;
        return gm == gameMode;
    }

    @Override
    public void save(ConfigurationSection cfg) {
        cfg.set("gamemode", gameMode == null ? "ANY" : gameMode.name());
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.GAMEMODE;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (");
        sb.append("gamemode:").append(gameMode == null ? "ANY" : gameMode.name());
        sb.append(")");
        return sb.toString();
    }
}
