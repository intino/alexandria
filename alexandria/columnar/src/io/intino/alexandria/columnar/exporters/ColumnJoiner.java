package io.intino.alexandria.columnar.exporters;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.assa.AssaReader;
import io.intino.alexandria.assa.AssaStream;
import io.intino.alexandria.columnar.Columnar;

import java.util.List;
import java.util.stream.Collectors;

class ColumnJoiner {
	private final Timetag timetag;
	private final List<TemporalReader> readers;
	private final List<Columnar.Select.ColumnFilter> filters;

	ColumnJoiner(Timetag timetag, List<AssaReader> readers, List<Columnar.Select.ColumnFilter> filters) {
		this.timetag = timetag;
		this.readers = readers.stream().map(TemporalReader::new).collect(Collectors.toList());
		this.filters = filters;
	}

	String[] next() {
		TemporalReader lowest = getFilteredActiveCandidate();
		if (lowest == null) return null;
		String[] fields = new String[readers.size() + 2];
		long lowestKey = lowest.current.key();
		fields[0] = lowestKey + "";
		fields[1] = timetag.value();
		for (int i = 0; i < readers.size(); i++) {
			TemporalReader reader = readers.get(i);
			if (reader.current != null && reader.current.key() == lowestKey) {
				fields[i + 2] = reader.current.value();
				reader.next();
			} else fields[i + 2] = null;
		}
		return fields;
	}

	boolean hasNext() {
		return readers.stream().anyMatch(TemporalReader::hasNext);
	}

	private TemporalReader getFilteredActiveCandidate() {
		TemporalReader candidate;
		while (!satisfies(candidate = getCandidate())) {
			if (candidate == null) break;
			candidate.next();
		}
		return candidate;
	}

	private boolean satisfies(TemporalReader candidate) {
		return candidate != null && filters.stream().allMatch(filter -> filter.test(candidate.current.key()));
	}

	private TemporalReader getCandidate() {
		List<TemporalReader> readers = getActiveCandidates();
		if (readers.isEmpty()) return null;
		long reference = readers.get(0).current.key();
		TemporalReader candidate = readers.get(0);
		for (int i = 1; i < readers.size(); i++) {
			if (readers.get(i).current == null) continue;
			long newId = readers.get(i).current.key();
			if (newId < reference) {
				reference = newId;
				candidate = readers.get(i);
			}
		}
		return candidate;
	}

	private List<TemporalReader> getActiveCandidates() {
		return readers.stream().filter(TemporalReader::hasNext).collect(Collectors.toList());
	}

	private class TemporalReader {

		private final AssaReader reader;
		private AssaStream.Item current;

		TemporalReader(AssaReader reader) {
			this.reader = reader;
			current = reader.next();
		}

		void next() {
			this.current = reader.next();
		}

		public boolean hasNext() {
			return reader.hasNext();
		}
	}
}
