package utils;

import com.intellij.openapi.module.Module;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.cache.ElementCache;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtil {
	public static final String DIR = "test-gen";

	public static Settings settings(File gen, String packageDir) {
		return settings(gen, packageDir, new ElementCache());
	}

	public static Settings settings(File gen, String packageDir, ElementCache cache) {
		return new Settings(null, gen, gen, gen, packageDir, cache).webModule(webModule());
	}

	private static Module webModule() {
		Module mock = mock(Module.class);
		when(mock.getModuleFilePath()).thenReturn(new File(DIR).getPath() + "/web/parent");
		return mock;
	}

}
