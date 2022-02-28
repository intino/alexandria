package io.test;

import io.intino.alexandria.sumus.*;
import io.intino.alexandria.sumus.filters.SliceFilter;
import io.intino.alexandria.sumus.builders.CellBuilder;
import io.intino.alexandria.sumus.builders.CubeBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class CubeBuilder_ {
	@Test
	public void should_create_cell_builders_with_no_drill() {
		CubeBuilder builder = new CubeBuilder(none(), Filter.None);
		assertThat(toString(builder.builders())).isEqualTo(List.of(""));
	}

	private Ledger none() {
		return null;
	}

	private List<String> toString(List<CellBuilder> cells) {
		return cells.stream()
				.map(CellBuilder::toString)
				.collect(toList());
	}

	@Test
	public void should_create_cell_builders_with_one_drill() {
		CubeBuilder builder = new CubeBuilder(none(), Filter.None, dimension("A","B","C"));
		assertThat(toString(builder.builders())).isEqualTo(List.of("A","B","C",""));
	}

	@Test
	public void should_create_cell_builders_with_one_drill_and_filter() {
		Dimension dimension = dimension("A", "B", "C");
		SliceFilter filter = new SliceFilter(dimension.slices().get(0), dimension.slices().get(1));
		CubeBuilder builder = new CubeBuilder(none(), filter, dimension);
		assertThat(toString(builder.builders())).isEqualTo(List.of("A","B",""));
	}

	@Test
	public void should_create_cell_builders_with_two_drills() {
		CubeBuilder builder = new CubeBuilder(none(), Filter.None, dimension("A","B","C","D"),dimension("X","Y","Z"));
		assertThat(toString(builder.builders())).isEqualTo(List.of("A","A-X","A-Y","A-Z","B","B-X","B-Y","B-Z","C","C-X","C-Y","C-Z","D","D-X","D-Y","D-Z","X","Y","Z",""));
	}

	@Test
	public void should_create_cell_builders_with_two_drills_and_filter() {
		Dimension dimension = dimension("A", "B", "C","D");
		SliceFilter filter = new SliceFilter(dimension.slices().get(0), dimension.slices().get(1));
		CubeBuilder builder = new CubeBuilder(none(), filter, dimension,dimension("X","Y","Z"));
		assertThat(toString(builder.builders())).isEqualTo(List.of("A","A-X","A-Y","A-Z","B","B-X","B-Y","B-Z","X","Y","Z",""));
	}

	@Test
	public void should_create_cell_builders_with_three_drills() {
		CubeBuilder builder = new CubeBuilder(none(), Filter.None, dimension("A","B"),dimension("X","Y"),dimension("1","2"));
		assertThat(toString(builder.builders())).isEqualTo(List.of("A","A-X","A-X-1","A-X-2","A-Y","A-Y-1","A-Y-2","B","B-X","B-X-1","B-X-2","B-Y","B-Y-1","B-Y-2","X","X-1","X-2","Y","Y-1","Y-2","1","2",""));
	}

	@Test
	public void should_create_cell_builders_with_three_drills_and_filter() {
		Dimension dimension_0 = dimension("A", "B");
		Dimension dimension_1 = dimension("X", "Y");
		Dimension dimension_2 = dimension("1", "2");
		SliceFilter filter = new SliceFilter(dimension_0.slices().get(0), dimension_1.slices().get(1));
		CubeBuilder builder = new CubeBuilder(none(), filter, dimension_0, dimension_1, dimension_2);
		assertThat(toString(builder.builders())).isEqualTo(List.of("A","A-Y","A-Y-1","A-Y-2","Y","Y-1","Y-2","1","2",""));
	}

	private Dimension dimension(String... slices) {
		return new TestDimension(slices);
	}

	private static class TestDimension implements Dimension {
		private final List<Slice> slices;

		public TestDimension(String[] slices) {
			this.slices = Arrays.stream(slices)
					.map(TestSlice::new)
					.collect(toList());
		}

		@Override
		public String name() {
			return "test";
		}

		@Override
		public Attribute.Type type() {
			return null;
		}

		@Override
		public boolean hasNA() {
			return false;
		}

		@Override
		public List<Slice> slices() {
			return slices;
		}

		private class TestSlice implements Slice {
			private final String name;

			public TestSlice(String name) {
				this.name = name;
			}

			@Override
			public String name() {
				return name;
			}

			@Override
			public Dimension dimension() {
				return TestDimension.this;
			}

			@Override
			public boolean isNA() {
				return false;
			}

			@Override
			public Index index() {
				return null;
			}
		}
	}
}
