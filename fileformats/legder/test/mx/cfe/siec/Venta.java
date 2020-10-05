package mx.cfe.siec;

import io.intino.alexandria.Item;

import java.time.LocalDate;

public class Venta implements Item {
	private final long id;
	private final int kwh;
	private final int importe;
	private final int iva;
	private final int dap;
	private final short days;

	public Venta(String ocr, int kwh, int importe, int iva, int dap, int days) {
		this.id = OCR.id(ocr);
		this.kwh = kwh;
		this.importe = importe;
		this.iva = iva;
		this.dap = dap;
		this.days = (short) days;
	}

	@Override
	public long id() {
		return id;
	}

	public String ocr() {
		return OCR.get(id);
	}

	public long rpu() {
		return OCR.rpu(id);
	}

	public LocalDate date() {
		return OCR.date(id);
	}

	public String tipo() {
		return OCR.tipo(id);
	}

	public int kwh() {
		return kwh;
	}

	public int importe() {
		return importe;
	}

	public int iva() {
		return iva;
	}

	public int dap() {
		return dap;
	}

	public int days() {
		return days;
	}

}
