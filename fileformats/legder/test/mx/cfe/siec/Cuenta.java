package mx.cfe.siec;

import io.intino.alexandria.Aggregation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Cuenta implements Aggregation<Cuenta, Venta> {
	private final String label;
	private final Predicate<Venta> predicate;
	private List<Venta> ventas;
	private int count;
	private long kwh;
	private long importe;
	private long iva;
	private long dap;
	private long days;

	public Cuenta(String label,Predicate<Venta> predicate) {
		this.label = label;
		this.predicate = predicate;
		this.ventas = new ArrayList<>();
		this.count = 0;
		this.kwh = 0;
		this.importe = 0;
		this.iva = 0;
		this.dap = 0;
		this.days = 0;
	}

	@Override
	public String label() {
		return label;
	}

	@Override
	public Predicate<Venta> predicate() {
		return predicate;
	}

	@Override
	public Cuenta add(Venta venta) {
		this.ventas.add(venta);
		this.count++;
		this.kwh += venta.kwh();
		this.importe += venta.importe();
		this.iva += venta.iva();
		this.dap += venta.dap();
		this.days += venta.days();
		return this;
	}

	public long kwh() {
		return kwh;
	}

	public long importe() {
		return importe;
	}

	public long iva() {
		return iva;
	}

	public long dap() {
		return dap;
	}

	public double days() {
		return days / (double) count;
	}

	public int count() {
		return count;
	}

	public List<Venta> adeudos() {
		return ventas;
	}
}
