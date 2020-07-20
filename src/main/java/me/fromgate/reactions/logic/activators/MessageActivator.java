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


import me.fromgate.reactions.logic.storages.MessageStorage;
import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;

public class MessageActivator extends Activator {

    private CheckType type;
    private Source source;
    private String mask;

    private MessageActivator(ActivatorBase base, CheckType type, Source source, String mask) {
        super(base);
        this.type = type;
        this.source = source;
        this.mask = mask;
    }

    public static MessageActivator create(ActivatorBase base, Parameters param) {
        CheckType type = CheckType.getByName(param.getParam("type", "EQUAL"));
        Source source = Source.getByName(param.getParam("source", "CHAT_MESSAGE"));
        String mask = param.getParam("mask", param.getParam("message", "Message mask"));
        return new MessageActivator(base, type, source, mask);
    }

    public static MessageActivator load(ActivatorBase base, ConfigurationSection cfg) {
        CheckType type = CheckType.getByName(cfg.getString("type", "EQUAL"));
        Source source = Source.getByName(cfg.getString("source", "CHAT_INPUT"));
        String mask = cfg.getString("mask", "Message mask");
        return new MessageActivator(base, type, source, mask);
    }

    @Override
    public void save(ConfigurationSection cfg) {
        cfg.set("mask", mask);
        cfg.set("type", type.name());
        cfg.set("source", source.name());
    }

    @Override
    public boolean activate(Storage event) {
        MessageStorage e = (MessageStorage) event;
        return e.isForActivator(this);
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.MESSAGE;
    }

    @Override
    public boolean isValid() {
        return !Util.isStringEmpty(mask);
    }

    public boolean filterMessage(Source source, String message) {
        if (source != this.source && this.source != Source.ALL) return false;
        return filter(message);
    }

    private boolean filter(String message) {
        switch (type) {
            case CONTAINS:
                return message.toLowerCase().contains(this.mask.toLowerCase());
            case END:
                return message.toLowerCase().endsWith(this.mask.toLowerCase());
            case EQUAL:
                return message.equalsIgnoreCase(this.mask);
            case REGEX:
                return message.matches(this.mask);
            case START:
                return message.toLowerCase().startsWith(this.mask.toLowerCase());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (");
        sb.append("type:").append(this.type.name());
        sb.append(" source:").append(this.source.name());
        sb.append(" mask:").append(this.mask);
        sb.append(")");
        return sb.toString();
    }

    public enum CheckType {
        REGEX,
        CONTAINS,
        EQUAL,
        START,
        END;

        public static CheckType getByName(String name) {
            if (name.equalsIgnoreCase("contain")) return CheckType.CONTAINS;
            if (name.equalsIgnoreCase("equals")) return CheckType.EQUAL;
            for (CheckType t : CheckType.values()) {
                if (t.name().equalsIgnoreCase(name)) return t;
            }
            return CheckType.EQUAL;
        }

        public static boolean isValid(String name) {
            for (CheckType t : CheckType.values()) {
                if (t.name().equalsIgnoreCase(name)) return true;
            }
            return false;
        }
    }

    public enum Source {
        ALL,
        CHAT_INPUT,
        CONSOLE_INPUT,
        CHAT_OUTPUT,
        LOG_OUTPUT;
        //ANSWER;

        public static Source getByName(String name) {
            for (Source source : Source.values()) {
                if (source.name().equalsIgnoreCase(name)) return source;
            }
            return Source.ALL;
        }

        public static boolean isValid(String name) {
            for (Source source : Source.values()) {
                if (source.name().equalsIgnoreCase(name)) return true;
            }
            return false;
        }
    }
}
