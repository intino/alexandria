package io.intino.alexandria.columnar;

import io.intino.alexandria.assa.AssaReader;
import io.intino.alexandria.assa.AssaStream;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;

class AssaMerger {
	private final List<TemporalReader> readers;
	private final List<Columnar.Select.ColumnFilter> filters;

	AssaMerger(List<AssaReader<String>> readers, List<Columnar.Select.ColumnFilter> filters) {
		this.readers = readers.stream().map(TemporalReader::new).collect(Collectors.toList());
		this.filters = filters;
	}

	String[] next() {
		TemporalReader lowest = getFilteredActiveCandidate();
		if (lowest == null) return null;
		String[] fields = new String[readers.size() + 1];
		long lowestKey = lowest.current.key();
		fields[0] = valueOf(lowestKey);
		for (int i = 0; i < readers.size(); i++) {
			TemporalReader reader = readers.get(i);
			if (reader.current != null && reader.current.key() == lowestKey) {
				fields[i + 1] = reader.current.object().toString();
				reader.next();
			} else fields[i + 1] = null;
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
