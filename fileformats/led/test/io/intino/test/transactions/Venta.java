package io.intino.test.transactions;

import io.intino.alexandria.led.Transaction;
import io.intino.alexandria.led.buffers.store.ByteBufferStore;
import io.intino.alexandria.led.buffers.store.ByteStore;
import io.intino.alexandria.led.util.MemoryAddress;
import io.intino.alexandria.led.util.MemoryUtils;

import java.nio.ByteBuffer;

public class Venta extends Transaction {

    public static final int SIZE = (int)Math.ceil(16.25D);

    public Venta() {
        super(defaultByteStore());
    }

    public Venta(ByteStore store) {
        super(store);
    }

    public int size() {
        return SIZE;
    }

    public long id() {
        return this.bitBuffer.getAlignedLong(0);
    }

    public Venta id(long id) {
        this.bitBuffer.setAlignedLong(0, id);
        return this;
    }

    public Integer importe() {
        return this.bitBuffer.getAlignedInteger(64);
    }

    public Integer diasFacturados() {
        return this.bitBuffer.getIntegerNBits(96, 16);
    }

    public Integer kwh() {
        return this.bitBuffer.getIntegerNBits(112, 16);
    }

    public short concepto() {
        short value = this.bitBuffer.getShortNBits(128, 2);
        return value;
    }

    public Venta importe(int importe) {
        this.bitBuffer.setAlignedInteger(64, importe);
        return this;
    }

    public Venta diasFacturados(int diasFacturados) {
        this.bitBuffer.setIntegerNBits(96, 16, diasFacturados);
        return this;
    }

    public Venta kwh(int kwh) {
        this.bitBuffer.setIntegerNBits(112, 16, kwh);
        return this;
    }

    public Venta concepto(short concepto) {
        this.bitBuffer.setIntegerNBits(128, 2, concepto);
        return this;
    }

    private static ByteStore defaultByteStore() {
        ByteBuffer buffer = MemoryUtils.allocBuffer(130L);
        MemoryAddress address = MemoryAddress.of(buffer);
        return new ByteBufferStore(buffer, address, 0, buffer.capacity());
    }

}
