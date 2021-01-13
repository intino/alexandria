package io.intino.test;

import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.buffers.AbstractBitBuffer;
import io.intino.alexandria.led.buffers.BitBuffer;
import io.intino.alexandria.led.buffers.store.ByteBufferStore;
import io.intino.alexandria.led.buffers.store.ByteStore;
import io.intino.alexandria.led.util.BitUtils;
import io.intino.alexandria.led.util.memory.MemoryAddress;
import io.intino.alexandria.led.util.memory.MemoryUtils;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class Venta extends Schema {

    public static final int SIZE = (int)Math.ceil(16.625D);

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        Venta venta = new Venta()
                .ocr(-1)
                .importe(-1)
                .kwh(0b0011001100110011)
                .diasFacturados(-1);

        System.out.println("kwh = " + venta.kwh());

        Field bitBufferField = Schema.class.getDeclaredField("bitBuffer");
        bitBufferField.setAccessible(true);
        BitBuffer bitBuffer = (BitBuffer) bitBufferField.get(venta);
        Field storeField = AbstractBitBuffer.class.getDeclaredField("store");
        storeField.setAccessible(true);
        ByteBufferStore store = (ByteBufferStore) storeField.get(bitBuffer);

        System.out.println(Venta.SIZE);
        System.out.println(bitBuffer.byteSize());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Venta.SIZE; i++) {
            sb.append(BitUtils.toBinaryString(store.getByte(i) & 0xFF, Byte.SIZE, 8));
        }

        System.out.println(sb.toString());
    }


    public Venta() {
        super(defaultByteStore());
    }

    public Venta(ByteStore store) {
        super(store);
    }

    public int size() {
        return SIZE;
    }

    protected long id() {
        return this.ocr();
    }

    public long ocr() {
        return this.bitBuffer.getLongNBits(0, 64);
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

    public Venta ocr(long ocr) {
        this.bitBuffer.setLongNBits(0, 64, ocr);
        return this;
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

    private static ByteStore defaultByteStore() {
        ByteBuffer buffer = MemoryUtils.allocBuffer(133L);
        MemoryAddress address = MemoryAddress.of(buffer);
        return new ByteBufferStore(buffer, address, 0, buffer.capacity());
    }


}
