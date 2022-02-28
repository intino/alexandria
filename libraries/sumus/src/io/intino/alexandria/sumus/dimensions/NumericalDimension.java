package io.intino.alexandria.sumus.dimensions;

import io.intino.alexandria.sumus.Classifier;
import io.intino.alexandria.sumus.Lookup;
import io.intino.alexandria.sumus.Slice;
import io.intino.alexandria.sumus.SumusException;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class NumericalDimension extends AbstractDimension {
	private final String name;

	public NumericalDimension(Lookup lookup, String name, Classifier classifier) {
		super(lookup);
		this.name = name;
		this.slices.addAll(slicesOf(classifier));
		if (lookup.hasNA()) this.slices.add(new DimensionSlice());

	}

	private List<Slice> slicesOf(Classifier classifier) {
		return classifier.categories().stream()
				.map(c -> new DimensionSlice(c, classifier.predicateOf(c)))
				.collect(toList());
	}

	@Override
	public String name() {
		return super.name() + "-" + name;
	}

	@Override
	protected void check() {
		if (lookup.type().isNumeric()) return;
		throw new SumusException("Numerical dimension must use a numeric column");
	}
}
