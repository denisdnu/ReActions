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
import me.fromgate.reactions.externals.worldguard.RaWorldGuard;
import me.fromgate.reactions.externals.worldguard.WGBridge;
import me.fromgate.reactions.logic.storages.RegionEnterStorage;
import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

@Getter
public class RegionEnterActivator extends Activator implements Locatable {

    private final String region;

    private RegionEnterActivator(ActivatorBase base, String region) {
        super(base);
        this.region = region;
    }

    public static RegionEnterActivator create(ActivatorBase base, Parameters param) {
        String region = param.getString("region", param.getString("param-line"));
        RaWorldGuard.updateRegionCache();
        return new RegionEnterActivator(base, region);
    }

    public static RegionEnterActivator load(ActivatorBase base, ConfigurationSection cfg) {
        String region = cfg.getString("region", "region");
        return new RegionEnterActivator(base, region);
    }

    @Override
    public boolean activate(Storage event) {
        RegionEnterStorage be = (RegionEnterStorage) event;
        return be.getRegion().equalsIgnoreCase(WGBridge.getFullRegionName(this.region));
    }

    @Override
    public boolean isLocatedAt(Location loc) {
        if (!RaWorldGuard.isConnected()) return false;
        return RaWorldGuard.isLocationInRegion(loc, this.region);
    }

    @Override
    public boolean isLocatedAt(World world, int x, int y, int z) {
        return isLocatedAt(new Location(world, x, y, z));
    }

    @Override
    public void save(ConfigurationSection cfg) {
        cfg.set("region", this.region);
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.REGION_ENTER;
    }

    @Override
    public boolean isValid() {
        return !Utils.isStringEmpty(region);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (");
        sb.append("region:").append(this.region);
        sb.append(")");
        return sb.toString();
    }
}
