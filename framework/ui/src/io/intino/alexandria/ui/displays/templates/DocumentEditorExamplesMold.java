package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.ui.displays.components.documenteditor.DocumentManager;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class DocumentEditorExamplesMold extends AbstractDocumentEditorExamplesMold<UiFrameworkBox> {

	public DocumentEditorExamplesMold(UiFrameworkBox box) {
		super(box);
	}

	@Override
	public void init() {
		super.init();
		documentEditor1.documentManager(new DocumentManager() {
			@Override
			public DocumentInfo info(String id) {
				return new DocumentInfo(id, "Document example", "Example author");
			}

			@Override
			public InputStream load(String id) {
				try {
					return new FileInputStream(new File("/tmp/example.odt"));
				} catch (FileNotFoundException e) {
					return new ByteArrayInputStream(new byte[0]);
				}
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
	}
}