package io.intino.konos.builder.codegeneration.cache;

import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.io.Stash;
import io.intino.tara.magritte.stores.FileSystemStore;
import io.intino.tara.magritte.utils.StoreAuditor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.util.stream.Collectors.toMap;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

public class CacheReader extends HashMap<String, Integer> {
	private final File folder;

	private static Logger logger = LoggerFactory.getLogger(ROOT_LOGGER_NAME);

	public CacheReader(File folder) {
		this.folder = folder;
	}

	public ElementCache load(KonosGraph graph) {
		return load(graph, null);
	}

	public ElementCache load(KonosGraph graph, Stash konosStash) {
		StoreAuditor auditor = loadAuditor(graph, konosStash);
		return loadCacheFile(auditor);
	}

	private StoreAuditor loadAuditor(KonosGraph graph, Stash konosStash) {
		StoreAuditor auditor = new StoreAuditor(store(konosStash));
		Arrays.stream(graph.core$().openedStashes()).forEach(auditor::trace);
		return auditor;
	}

	@NotNull
	private FileSystemStore store(Stash konosStash) {
		return new FileSystemStore(folder) {
			@Override
			public Stash stashFrom(String path) {
				if (path.equalsIgnoreCase("konos.stash")) return konosStash;
				return super.stashFrom(path);
			}
		};
	}

	private ElementCache loadCacheFile(StoreAuditor auditor) {
		try {
			File cacheFile = new File(folder + "/.cache");
			if (!cacheFile.exists()) return new ElementCache(auditor);
			Properties properties = new Properties();
			properties.load(new FileInputStream(cacheFile));
			Map<String, String> marks = properties.entrySet().stream().collect(toMap(e -> (String)e.getKey(), e -> (String)e.getValue()));
			return new ElementCache(auditor, marks);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
}
