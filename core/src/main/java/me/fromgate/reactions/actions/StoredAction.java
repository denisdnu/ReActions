package me.fromgate.reactions.actions;

import lombok.Getter;

public class StoredAction {
	@Getter private final Actions action;
	@Getter private final String value;

	public StoredAction(String a, String v) {
		this.action = Actions.getByName(a);
		this.value = v;
	}

	public StoredAction(Actions a, String v) {
		this.action = a;
		this.value = v;
	}

	public String getActionName() {
		return action == null ? "UNKNOWN" : action.name();
	}

	@Override
	public String toString() {
		return action.name() + "=" + value;
	}
}