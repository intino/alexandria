package io.intino.konos.alexandria.ui.spark.actions;

import java.io.InputStream;
import java.util.function.Consumer;

public abstract class AlexandriaEditorAction extends AlexandriaResourceAction {
	private Consumer<AlexandriaEditorSaveParameters> saveListener = null;
	public InputStream document;

	public AlexandriaEditorAction(String uiServiceName) {
		super(uiServiceName);
	}

	public void onSave(Consumer<AlexandriaEditorSaveParameters> listener) {
		this.saveListener = listener;
	}

	public void save(InputStream document, boolean completed) {
		this.saveListener.accept(new AlexandriaEditorSaveParameters() {
			@Override
			public InputStream document() {
				return document;
			}

			@Override
			public boolean completed() {
				return completed;
			}
		});
	}

}
