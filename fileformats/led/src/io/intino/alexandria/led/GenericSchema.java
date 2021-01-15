package io.intino.alexandria.led;

import io.intino.alexandria.led.buffers.store.ByteStore;

public final class GenericSchema extends Schema {

    public GenericSchema(ByteStore store) {
        super(store);
    }

    @Override
    public long id() {
        return bitBuffer.getAlignedLong(0);
    }

    @Override
    public int size() {
        return (int) bitBuffer.byteSize();
    }

    @Override
    public String toString() {
        return "GenericTransaction{id=" + id() + "}";
    }
}
