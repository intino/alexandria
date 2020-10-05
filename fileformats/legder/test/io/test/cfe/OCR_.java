package io.test.cfe;

import mx.cfe.siec.OCR;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

public class OCR_ {

	@Test
	public void should_transform_value_into_id() {
		long id = OCR.id("501050913812200301");
		assertThat(id).isEqualTo(8406239408021369601L);
		assertThat(OCR.rpu(id)).isEqualTo(501050913812L);
		assertThat(OCR.date(id)).isEqualTo(LocalDate.of(2020,Month.MARCH,1));
		assertThat(OCR.tipo(id)).isEqualTo("01");
		assertThat(OCR.get(id)).isEqualTo("501050913812200301");

	}
}
