package io.intino.konos.builder.codegeneration.cache;

import io.intino.alexandria.logger.Logger;
import io.intino.konos.dsl.KonosGraph;
import io.intino.magritte.framework.stores.FileSystemStore;
import io.intino.magritte.framework.utils.StoreAuditor;
import io.intino.magritte.io.model.Stash;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.util.stream.Collectors.toMap;

public class CacheReader extends HashMap<String, Integer> {
	private final File folder;

	public CacheReader(File folder) {
		this.folder = folder;
	}

	public LayerCache load(KonosGraph graph) {
		return load(graph, null);
	}

	public LayerCache load(KonosGraph graph, Stash[] stashes) {
		StoreAuditor auditor = loadAuditor(graph, stashes);
		return loadCacheFile(auditor);
	}

	private StoreAuditor loadAuditor(KonosGraph graph, Stash[] stashes) {
		StoreAuditor auditor = new StoreAuditor(store(stashes));
		//Arrays.stream(graph.core$().openedStashes()).forEach(auditor::trace);TODO
		return auditor;
	}


	private FileSystemStore store(Stash[] stashes) {
		return new FileSystemStore(folder) {
			@Override
			public Stash stashFrom(String path) {
				Stash stash = Arrays.stream(stashes).filter(s -> s.path.equals(path)).findFirst().orElse(null);
				if (stash != null) return stash;
				return super.stashFrom(path);
			}
		};
	}

	private LayerCache loadCacheFile(StoreAuditor auditor) {
		try {
			File cacheFile = new File(folder + "/.cache");
			if (!cacheFile.exists()) return new LayerCache(auditor);
			Properties properties = new Properties();
			properties.load(new FileInputStream(cacheFile));
			Map<String, String> marks = properties.entrySet().stream().collect(toMap(e -> (String) e.getKey(), e -> (String) e.getValue()));
			return new LayerCache(auditor, marks);
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
	}
}
