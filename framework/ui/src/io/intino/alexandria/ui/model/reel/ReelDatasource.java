package io.intino.alexandria.ui.model.reel;

import io.intino.alexandria.Scale;

import java.time.Instant;
import java.util.List;

public interface ReelDatasource {
	String name();
	List<SignalDefinition> signals();
	Signal signal(SignalDefinition definition);
	List<Scale> scales();

	Instant from(Scale scale);
	Instant to(Scale scale);
	Instant previous(Instant date, Scale scale);
	Instant next(Instant date, Scale scale);

	default SignalDefinition signalDefinition(String name) {
		return signals().stream().filter(d -> d.name().equals(name)).findFirst().orElse(null);
	}

	default Signal signal(String signalName) {
		SignalDefinition signal = signalDefinition(signalName);
		return signal != null ? signal(signal) : null;
	}

	interface Signal {
		SignalDefinition definition();
		String reel(Scale scale, Instant start, Instant end);
	}

}
