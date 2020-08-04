package me.fromgate.reactions.activators.triggers;

import me.fromgate.reactions.activators.storages.Storage;
import me.fromgate.reactions.activators.storages.WeSelectionRegionStorage;
import me.fromgate.reactions.externals.worldedit.WeSelection;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;

public class WeSelectionTrigger extends Trigger {
    private final int maxBlocks;
    private final int minBlocks;
    private final String typeSelection;

    private WeSelectionTrigger(ActivatorBase base, int maxBlocks, int minBlocks, String typeSelection) {
        super(base);
        this.maxBlocks = maxBlocks;
        this.minBlocks = minBlocks;
        this.typeSelection = typeSelection;
    }

    public static WeSelectionTrigger create(ActivatorBase base, Parameters param) {
        int minBlocks = param.getInteger("minblocks", 0);
        int maxBlocks = param.getInteger("maxblocks", Integer.MAX_VALUE);
        String typeSelection = param.getString("type", "ANY");
        return new WeSelectionTrigger(base, minBlocks, maxBlocks, typeSelection);
    }

    public static WeSelectionTrigger load(ActivatorBase base, ConfigurationSection cfg) {
        int minBlocks = cfg.getInt("min-blocks", 0);
        int maxBlocks = cfg.getInt("max-blocks", Integer.MAX_VALUE);
        String typeSelection = cfg.getString("type", "ANY");
        return new WeSelectionTrigger(base, minBlocks, maxBlocks, typeSelection);
    }

    @Override
    public boolean proceed(Storage event) {
        WeSelectionRegionStorage e = (WeSelectionRegionStorage) event;
        WeSelection selection = e.getSelection();
        if (!selection.isValid()) return false;
        int selectionBlocks = selection.getArea();
        if (selectionBlocks < minBlocks) return false;
        if (selectionBlocks > maxBlocks && maxBlocks != 0) return false;
        String selType = selection.getSelType();
        if (!checkTypeSelection(selType)) return false;
        String region = selection.getRegion();
        return region != null && !region.isEmpty();
    }

    private boolean checkTypeSelection(String selType) {
        return typeSelection.isEmpty() || typeSelection.equalsIgnoreCase("ANY") || typeSelection.equalsIgnoreCase(selType);
    }

    @Override
    public void saveTrigger(ConfigurationSection cfg) {
        cfg.set("min-blocks", this.minBlocks);
        cfg.set("max-blocks", this.maxBlocks);
        cfg.set("type", this.typeSelection);
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.WE_SELECTION_REGION;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (");
        sb.append("minblocks:").append(minBlocks);
        sb.append("; maxblocks:").append(maxBlocks);
        sb.append("; type:").append(typeSelection);
        sb.append(")");
        return sb.toString();
    }
}