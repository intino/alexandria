package archetype;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.archetype.ArchetypeRenderer;
import io.intino.plugin.project.LegioConfiguration;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;

public class Archetype_ {


	@Test
	public void shouldParseArchetype() {
		Settings settings = Mockito.mock(Settings.class);
		com.intellij.openapi.project.Project project = Mockito.mock(com.intellij.openapi.project.Project.class);
		LegioConfiguration configuration = Mockito.mock(LegioConfiguration.class);
		Mockito.when(settings.project()).thenReturn(project);
		Mockito.when(settings.src(Target.Owner)).thenReturn(new File("archetype"));
		Mockito.when(settings.packageName()).thenReturn("io.provista.cuentamaestra");
		Mockito.when(configuration.artifactId()).thenReturn("cuentaMaestra");
		Mockito.when(settings.moduleConfiguration()).thenReturn(configuration);
		Mockito.when(project.getBasePath()).thenReturn("/Users/oroncal/workspace/gestioncomercial");
		new ArchetypeRenderer(settings).execute();
	}
}
