package io.intino.test.schemas;

import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.Schema.Definition;
import io.intino.alexandria.led.buffers.store.ByteBufferStore;
import io.intino.alexandria.led.buffers.store.ByteStore;
import io.intino.alexandria.led.util.memory.MemoryAddress;
import io.intino.alexandria.led.util.memory.MemoryUtils;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;

@Definition(name = "VentaEnergia", size = VentaEnergia.SIZE)
public class VentaEnergia extends Schema {

    public static void main(String[] args) {

        VentaEnergia ventaEnergia = new VentaEnergia().claseVenta(1).concepto(3).total(12345).ocr(98765);

        System.out.println(ventaEnergia);

    }

    public static final int SIZE = 52;

    public VentaEnergia() {
        super(defaultByteStore());
    }

    public VentaEnergia(ByteStore store) {
        super(store);
    }

    public int size() {
        return 52;
    }

    public Map<String, Object> values() {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("total", this.total());
        values.put("dap", this.dap());
        values.put("iva", this.iva());
        values.put("importe", this.importe());
        values.put("kwh", this.kwh());
        values.put("ocr", this.ocr());
        values.put("diasFacturados", this.diasFacturados());
        values.put("claseVenta", this.claseVenta());
        values.put("concepto", this.concepto());
        return values;
    }

    @Override
    protected long id() {
        return this.ocr();
    }

    @Attribute(name = "total", type = DataType.LONG, index = 0, size = Long.SIZE)
    public long total() {
        return this.bitBuffer.getAlignedLong(0);
    }

    @Attribute(name = "dap", type = DataType.LONG, index = 64, size = Long.SIZE)
    public long dap() {
        return this.bitBuffer.getAlignedLong(64);
    }

    @Attribute(name = "iva", type = DataType.LONG, index = 128, size = Long.SIZE)
    public long iva() {
        return this.bitBuffer.getAlignedLong(128);
    }

    @Attribute(name = "importe", type = DataType.LONG, index = 192, size = Long.SIZE)
    public long importe() {
        return this.bitBuffer.getLongNBits(192, 64);
    }

    @Attribute(name = "kwh", type = DataType.LONG, index = 256, size = Long.SIZE)
    public long kwh() {
        return this.bitBuffer.getAlignedLong(256);
    }

    @Attribute(name = "ocr", type = DataType.LONG, index = 320, size = Long.SIZE)
    public long ocr() {
        return this.bitBuffer.getLongNBits(320, 64);
    }

    @Attribute(name = "diasFacturados", type = DataType.LONG, index = 384, size = 16)
    public int diasFacturados() {
        return this.bitBuffer.getIntegerNBits(384, 16);
    }

    @Attribute(name = "claseVenta", type = DataType.LONG, index = 400, size = 8)
    public int claseVenta() {
        return this.bitBuffer.getIntegerNBits(400, 8);
    }

    @Attribute(name = "concepto", type = DataType.LONG, index = 408, size = 7)
    public int concepto() {
        return this.bitBuffer.getIntegerNBits(408, 7);
    }

    public VentaEnergia total(long total) {
        this.bitBuffer.setAlignedLong(0, total);
        return this;
    }

    public VentaEnergia dap(long dap) {
        this.bitBuffer.setAlignedLong(64, dap);
        return this;
    }

    public VentaEnergia iva(long iva) {
        this.bitBuffer.setAlignedLong(128, iva);
        return this;
    }

    public VentaEnergia importe(long importe) {
        this.bitBuffer.setLongNBits(192, 64, importe);
        return this;
    }

    public VentaEnergia kwh(long kwh) {
        this.bitBuffer.setAlignedLong(256, kwh);
        return this;
    }

    public VentaEnergia ocr(long ocr) {
        this.bitBuffer.setLongNBits(320, 64, ocr);
        return this;
    }

    public VentaEnergia diasFacturados(int diasFacturados) {
        this.bitBuffer.setIntegerNBits(384, 16, diasFacturados);
        return this;
    }

    public VentaEnergia claseVenta(int claseVenta) {
        this.bitBuffer.setIntegerNBits(400, 8, claseVenta);
        return this;
    }

    public VentaEnergia concepto(int concepto) {
        this.bitBuffer.setIntegerNBits(408, 7, concepto);
        return this;
    }

    private static ByteStore defaultByteStore() {
        ByteBuffer buffer = MemoryUtils.allocBuffer(52L);
        MemoryAddress address = MemoryAddress.of(buffer);
        return new ByteBufferStore(buffer, address, 0, buffer.capacity());
    }

}
