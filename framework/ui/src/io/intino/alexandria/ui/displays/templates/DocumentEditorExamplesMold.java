package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.components.documenteditor.DocumentManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DocumentEditorExamplesMold extends AbstractDocumentEditorExamplesMold<UiFrameworkBox> {

	public DocumentEditorExamplesMold(UiFrameworkBox box) {
		super(box);
	}

	@Override
	public void init() {
		super.init();
		editDocumentDialog.onOpen(e -> refreshDialog());
	}

	private void refreshDialog() {
		documentEditor1.documentManager(new DocumentManager() {
			@Override
			public DocumentInfo info(String id) {
				return new DocumentInfo(id, "Document example", "Example author", false);
			}

			@Override
			public InputStream load(String id) {
				return DocumentEditorExamplesMold.class.getResourceAsStream("/data/example.odt");
			}

			@Override
			public void save(String id, InputStream content) {
				try {
					FileUtils.copyInputStreamToFile(content, new File("/tmp/example.odt"));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
		documentEditor1.refresh();
	}

}