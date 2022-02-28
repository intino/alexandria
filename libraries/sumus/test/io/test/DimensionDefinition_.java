package io.test;

import io.intino.alexandria.sumus.Classifier;
import io.intino.alexandria.sumus.parser.DimensionDefinition;
import org.junit.Test;

import java.util.List;

import static io.intino.alexandria.sumus.Attribute.Type.integer;
import static org.assertj.core.api.Assertions.assertThat;

public class DimensionDefinition_ {
	@Test
	public void should_create_classifier() {
		DimensionDefinition definition = new DimensionDefinition("A", integer, "0,10,20,-");
		Classifier classifier = definition.classifier();
		assertThat(classifier.categories()).isEqualTo(List.of("[0-10]", "[10-20]", "[20-]"));
		assertThat(classifier.predicateOf("[0-10]").test(-5L)).isFalse();
		assertThat(classifier.predicateOf("[0-10]").test(0L)).isTrue();
		assertThat(classifier.predicateOf("[0-10]").test(5L)).isTrue();
		assertThat(classifier.predicateOf("[0-10]").test(10L)).isFalse();
		assertThat(classifier.predicateOf("[0-10]").test(20L)).isFalse();
		assertThat(classifier.predicateOf("[20-]").test(-5L)).isFalse();
		assertThat(classifier.predicateOf("[20-]").test(0L)).isFalse();
		assertThat(classifier.predicateOf("[20-]").test(5L)).isFalse();
		assertThat(classifier.predicateOf("[20-]").test(20L)).isTrue();
		assertThat(classifier.predicateOf("[20-]").test(40L)).isTrue();
	}
}
