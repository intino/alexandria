package io.intino.konos.alexandria.activity.displays;

import java.time.Instant;
import java.util.List;

public interface CatalogInstantBlock {
	String catalog();
	Instant instant();
	List<String> items();
}
