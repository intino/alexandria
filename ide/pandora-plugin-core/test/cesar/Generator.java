package cesar;

import io.swagger.codegen.DefaultCodegen;

import java.io.File;

public class Generator {


	public void generate() {
		DefaultCodegen generator = new DefaultCodegen();
	}

	protected static File getTmpFolder() {
		try {
			File outputFolder = File.createTempFile("codegen-", "-tmp");
			outputFolder.delete();
			outputFolder.mkdir();
			outputFolder.deleteOnExit();
			return outputFolder;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
