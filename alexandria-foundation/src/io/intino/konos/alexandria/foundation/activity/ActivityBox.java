package io.intino.konos.alexandria.foundation.activity;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.foundation.activity.displays.Soul;

public abstract class ActivityBox extends Box {
	public abstract void registerSoul(String clientId, Soul soul);
	public abstract void unRegisterSoul(String id);
}
