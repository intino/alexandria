package io.intino.konos.builder.codegeneration.accessor;

import io.intino.Configuration;
import io.intino.alexandria.logger.Logger;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.CompilerConfiguration;
import io.intino.konos.builder.context.CompilationContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

public class PomGenerator {
	private final CompilerConfiguration conf;
	private final CompilationContext context;

	public PomGenerator(CompilationContext context) {
		this.context = context;
		this.conf = context.configuration();
	}

	public void generate(String serviceType, File dir) {
		createPom(dir, serviceType, context.configuration().groupId().toLowerCase(), dir.getName().split("#")[0], context.configuration().version());
	}

	private void createPom(File root, String serviceType, String group, String artifact, String version) {
		final FrameBuilder builder = new FrameBuilder("pom").add("group", group).add("artifact", artifact).add("version", version);
		if (conf.releaseDistributionRepository() != null)
			buildRepoFrame(builder, conf.releaseDistributionRepository(), true, false);
		if (conf.snapshotDistributionRepository() != null)
			buildRepoFrame(builder, conf.snapshotDistributionRepository(), true, true);
		builder.add("dependency", new FrameBuilder(serviceType).add("value", "").add("version", "$" + serviceType).toFrame());
		final File pomFile = new File(root, "pom.xml");
		write(builder, pomFile);
	}

	private void write(FrameBuilder builder, File pomFile) {
		try {
			Files.writeString(pomFile.toPath(), new AccessorPomTemplate().render(builder.toFrame()));
		} catch (IOException e) {
			Logger.error(e.getMessage());
		}
	}

	private void buildRepoFrame(FrameBuilder builder, Configuration.Repository r, boolean isDistribution, boolean snapshot) {
		builder.add("repository", createRepositoryFrame(r, isDistribution, snapshot));
	}

	private Frame createRepositoryFrame(Configuration.Repository repository, boolean isDistribution, boolean snapshot) {
		FrameBuilder builder = new FrameBuilder("repository").
				add("name", repository.identifier()).
				add("random", UUID.randomUUID().toString()).
				add("url", repository.url());
		if (isDistribution) builder.add("distribution");
		if (snapshot) builder.add("snapshot");
		return builder.toFrame();
	}
}
