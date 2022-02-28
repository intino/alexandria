package io.intino.alexandria.sumus.dimensions;

import io.intino.alexandria.sumus.Lookup;
import io.intino.alexandria.sumus.Slice;
import io.intino.alexandria.sumus.SumusException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static io.intino.alexandria.sumus.Attribute.Type.category;
import static java.util.stream.Collectors.toList;

public class CategoricalDimension extends AbstractDimension {
	public CategoricalDimension(Lookup lookup) {
		super(lookup);
		this.slices.addAll(buildSlices());
		if (lookup.hasNA()) this.slices.add(new DimensionSlice());
	}

	@Override
	protected void check() {
		if (lookup.type() == category) return;
		throw new SumusException("Categorical dimension must use a categorical column");
	}

	private List<Slice> buildSlices() {
		return buildSlices(categories());
	}

	private List<Category> categories() {
		return lookup.uniques().stream().map(this::category)
				.sorted((a,b)->b.label.length() - a.label.length())
				.collect(toList());
	}

	private List<Slice> buildSlices(List<Category> categories) {
		List<Slice> result = new ArrayList<>();
		for (Category category : categories) add(result, category);
		return result.stream().sorted(this::compare).collect(toList());
	}

	private int compare(Slice a, Slice b) {
		return a.level() - b.level();
	}

	private DimensionSlice add(List<Slice> result, Category category) {
		if (category == null) return null;
		DimensionSlice slice = sliceIn(result, category);
		if (slice != null) return slice;
		slice = slice(category, add(result, category.parent()));
		result.add(slice);
		return slice;
	}

	private DimensionSlice sliceIn(List<Slice> result, Category category) {
		return result.stream()
				.map(s -> (DimensionSlice) s)
				.filter(s -> s.name().equals(category.label))
				.findFirst()
				.orElse(null);
	}

	private DimensionSlice slice(Category category, DimensionSlice parent) {
		return new DimensionSlice(category.label, category.isChild() ? v -> v == category : match(category.label), parent);
	}

	private Predicate<Object> match(String name) {
		return v -> v != null && category(v).label.startsWith(name);
	}

	private Category category(Object v) {
		return (Category) v;
	}

	@Override
	public int levels() {
		return slices.stream().mapToInt(Slice::level).max().orElse(1);
	}

	@Override
	public List<Slice> slices(int level) {
		return slices.stream()
				.filter(s->s.level() == level || s.level() < level && !hasChildren(s))
				.collect(toList());
	}

	private boolean hasChildren(Slice slice) {
		return slices.stream()
				.map(s -> (DimensionSlice) s)
				.anyMatch(s->s.parent == slice);
	}

}
