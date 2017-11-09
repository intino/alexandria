package io.intino.konos.alexandria.activity.box;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.activity.box.displays.Soul;

public abstract class ActivityBox extends Box {
	public abstract void registerSoul(String clientId, Soul soul);
	public abstract void unRegisterSoul(String id);
}
