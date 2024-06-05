package io.intino.konos.builder.codegeneration.accessor;

import io.intino.Configuration;
import io.intino.alexandria.logger.Logger;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.CompilerConfiguration;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PomGenerator {
	private final CompilerConfiguration conf;
	private final CompilationContext context;

	public PomGenerator(CompilationContext context) {
		this.context = context;
		this.conf = context.configuration();
	}

	public File generate(String serviceType, File dir) throws KonosException {
		return createPom(dir, serviceType, context.configuration().groupId().toLowerCase(), dir.getName().split("#")[1], context.configuration().version());
	}

	private File createPom(File root, String serviceType, String group, String artifact, String version) throws KonosException {
		try {
			final FrameBuilder builder = new FrameBuilder("pom")
					.add("group", group)
					.add("artifact", artifact)
					.add("version", version);
			if (conf.releaseDistributionRepository() != null)
				buildRepoFrame(builder, conf.releaseDistributionRepository(), true, false);
			if (conf.snapshotDistributionRepository() != null)
				buildRepoFrame(builder, conf.snapshotDistributionRepository(), true, true);
			builder.add("dependency", new FrameBuilder(serviceType).add("value", "").add("version", versionOf(serviceType)).toFrame());
			final File pomFile = new File(root, "pom.xml");
			write(builder, pomFile);
			return pomFile;
		} catch (Throwable e) {
			throw new KonosException(e.getMessage(), e);
		}
	}

	public String coors(File dir) {
		return String.join(":", context.configuration().groupId().toLowerCase().toLowerCase(), dir.getName().split("#")[1].toLowerCase(), context.configuration().version());
	}

	private String versionOf(String serviceType) {
		String artifact = "";
		if ("rest".equals(serviceType)) artifact = "io.intino.alexandria:rest-accessor";
		else if ("messaging".equals(serviceType)) artifact = "io.intino.alexandria:terminal-jms";
		else if ("analytic".equals(serviceType)) artifact = "io.intino.alexandria:led";
		List<String> versions = new ArrayList<>(new ArtifactoryConnector(conf.releaseDistributionRepository()).versions(artifact));
		if (versions.isEmpty()) return "";
		Collections.sort(versions);
		return versions.get(versions.size() - 1);
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
