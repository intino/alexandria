package io.intino.konos.alexandria.activity;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.displays.Soul;

public abstract class ActivityBox extends Box {
	public abstract void registerSoul(String clientId, Soul soul);
	public abstract void unRegisterSoul(String id);
}
