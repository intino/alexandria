package io.test;

import io.intino.alexandria.sumus.parser.LedgerDefinition;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class LedgerDefinition_ {
	@Test
	public void name() {
		String definition =
				"attributes\n" +
				"ea: number\n" +
				"eac: category [A,B, C,D,E ,F,G,H]\n" +
				"language.es\n" +
				"ea: emision anual\n" +
				"ea: categoria emision anual\n" +
				"language.en\n" +
				"ea: annual emissions\n" +
				"eac: annual emissions category\n";
		LedgerDefinition ledgerDefinition = LedgerDefinition.of(definition);
		assertThat(ledgerDefinition.attributes.size()).isEqualTo(2);
		assertThat(ledgerDefinition.attributes.get(0).toString()).isEqualTo("ea:number");
		assertThat(ledgerDefinition.attributes.get(1).toString()).isEqualTo("eac:category");
		assertThat(ledgerDefinition.attributes.get(1).dimensions.size()).isEqualTo(1);
		assertThat(ledgerDefinition.attributes.get(1).dimensions.get(0).toString()).isEqualTo("1[A,B,C,D,E,F,G,H]");
		assertThat(ledgerDefinition.labels.size()).isEqualTo(2);
		assertThat(ledgerDefinition.labels.keySet()).isEqualTo(Set.of("es","en"));
		assertThat(ledgerDefinition.labels.get("en").size()).isEqualTo(2);
		assertThat(ledgerDefinition.labels.get("en").get("ea")).isEqualTo("annual emissions");
	}
}
