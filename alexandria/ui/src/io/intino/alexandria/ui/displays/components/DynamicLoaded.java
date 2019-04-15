package io.intino.alexandria.ui.displays.components;

public interface DynamicLoaded {
	enum LoadTime { VerySlow, Slow, Fast, VeryFast }

	LoadTime loadTime();
	void showLoading();
	void hideLoading();
}
