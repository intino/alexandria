package io.intino.konos.alexandria.ui;

import io.intino.konos.alexandria.Box;
import io.intino.konos.alexandria.ui.displays.Soul;

public abstract class UIBox extends Box {
	public abstract void registerSoul(String clientId, Soul soul);
	public abstract void unRegisterSoul(String id);

	public interface SoulsClosed {
		void accept();
	}
}
