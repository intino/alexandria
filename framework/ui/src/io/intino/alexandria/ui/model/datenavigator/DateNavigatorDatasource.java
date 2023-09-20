package io.intino.alexandria.ui.model.datenavigator;

import io.intino.alexandria.Scale;

import java.time.Instant;
import java.util.List;

public interface DateNavigatorDatasource {
	List<Scale> scales();
	Instant from(Scale scale);
	Instant previous(Scale scale, Instant date);
	Instant next(Scale scale, Instant date);
	Instant to(Scale scale);
}
