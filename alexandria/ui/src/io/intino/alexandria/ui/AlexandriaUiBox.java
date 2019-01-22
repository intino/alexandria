package io.intino.alexandria.ui;

import io.intino.alexandria.core.Box;

public abstract class AlexandriaUiBox extends Box {
	public abstract void registerSoul(String clientId, Soul soul);

	public abstract void unRegisterSoul(String id);

	public interface SoulsClosed {
		void accept();
	}
}
