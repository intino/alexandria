package io.intino.konos.server.activity.displays.catalogs;

import java.time.Instant;
import java.util.List;

public interface CatalogInstantBlock {
	String catalog();
	Instant instant();
	List<String> entities();
}
