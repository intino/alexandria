package io.test.cfe;

import mx.cfe.siec.Cuenta;
import mx.cfe.siec.Venta;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Venta_ {

	@Test
	public void should_store_ocr() {
		Venta venta = new Venta("501050913812200301", 200, 3000, 10, 5, 20);
		assertThat(venta.rpu()).isEqualTo(501050913812L);
		assertThat(venta.date().toString()).isEqualTo("2020-03-01");
		assertThat(venta.tipo()).isEqualTo("01");
		assertThat(venta.ocr()).isEqualTo("501050913812200301");
	}

	@Test
	public void can_be_aggregated_into_cuenta() {
		Venta a1 = new Venta("501050913812200301", 200, 3000, 10, 5, 40);
		Venta a2 = new Venta("501050913812200401", 220, 4000, 12, 6, 20);
		Venta a3 = new Venta("501050913812200501", 140, 2700, 9, 4, 0);
		Cuenta cuenta = new Cuenta("Example",i->i.importe() >= 3000).add(a1).add(a2);
		assertThat(cuenta.label()).isEqualTo("Example");
		assertThat(cuenta.count()).isEqualTo(2);
		assertThat(cuenta.kwh()).isEqualTo(420);
		assertThat(cuenta.importe()).isEqualTo(7000);
		assertThat(cuenta.iva()).isEqualTo(22);
		assertThat(cuenta.dap()).isEqualTo(11);
		assertThat(cuenta.days()).isEqualTo(30.0);
		assertThat(cuenta.adeudos().contains(a1)).isTrue();
		assertThat(cuenta.adeudos().contains(a2)).isTrue();
		assertThat(cuenta.adeudos().contains(a3)).isFalse();
	}

}
