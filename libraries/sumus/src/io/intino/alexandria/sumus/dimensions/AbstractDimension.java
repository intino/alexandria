package io.intino.alexandria.sumus.dimensions;

import io.intino.alexandria.sumus.*;

import java.util.*;
import java.util.function.Predicate;

public abstract class AbstractDimension implements Dimension, Iterable<Slice> {
	public final Lookup lookup;
	public final List<Slice> slices;

	public AbstractDimension(Lookup lookup) {
		this.lookup = lookup;
		this.slices = new ArrayList<>();
		this.check();
	}

	protected abstract void check();

	public String name() {
		return lookup.name();
	}

	@Override
	public Attribute.Type type() {
		return lookup.type();
	}

	@Override
	public List<Slice> slices() {
		return slices;
	}

	@Override
	public boolean hasNA() {
		return slices.stream().anyMatch(Slice::isNA);
	}

	@Override
	public Iterator<Slice> iterator() {
		return slices.iterator();
	}

	@Override
	public boolean equals(Object o) {
		return this == o || o != null && getClass() == o.getClass();
	}

	@Override
	public int hashCode() {
		return Objects.hash(name());
	}

	@Override
	public String toString() {
		return name();
	}

	protected class DimensionSlice implements Slice {
		public final String name;
		public final DimensionSlice parent;
		private final Predicate<Object> predicate;

		public DimensionSlice(String name, Predicate<Object> predicate) {
			this(name, predicate, null);
		}

		public DimensionSlice(String name, Predicate<Object> predicate, DimensionSlice parent) {
			this.name = name;
			this.predicate = predicate;
			this.parent = parent;
		}

		public DimensionSlice() {
			this.name = "NA";
			this.predicate = Objects::isNull;
			this.parent = null;
		}

		@Override
		public Slice parent() {
			return parent;
		}

		public Dimension dimension() {
			return AbstractDimension.this;
		}

		public Index index() {
			return lookup.index(predicate);
		}

		@Override
		public int level() {
			return parent == null ? 1 : parent.level() + 1;
		}

		@Override
		public String name() {
			return name;
		}

		public boolean isNA() {
			return name.equals("NA");
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
