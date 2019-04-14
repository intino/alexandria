package io.intino.alexandria.ui.displays.components;

public interface TimeConsuming {
	enum LoadTime { Low, Medium, High }

	LoadTime loadTime();
	void showLoading();
	void hideLoading();
}
