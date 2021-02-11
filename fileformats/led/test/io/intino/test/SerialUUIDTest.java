package io.intino.test;

import io.intino.alexandria.led.*;
import io.intino.alexandria.led.allocators.SchemaFactory;
import io.intino.alexandria.led.buffers.store.ByteBufferStore;
import io.intino.alexandria.led.buffers.store.ByteStore;
import io.intino.alexandria.led.util.SchemaSerialBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.UUID;
import java.util.stream.Stream;

public class SerialUUIDTest {

    private static final File FOLDER = new File("temp/uuid");
    static {
        FOLDER.mkdirs();
    }
    private static final String FILENAME = "contratos-uuid.led";

    @Test
    public void testUUIDBuilderNotEquals() {
        UUID uuid1 = new SchemaSerialBuilder("X")
                .add("a", "int", 0, 64)
                .add("b", "double", 64, 64)
                .buildSerialId();

        UUID uuid2 = new SchemaSerialBuilder("X")
                .add("a", "float", 0, 64)
                .add("b", "double", 64, 64)
                .buildSerialId();

        Assert.assertNotEquals(uuid1, uuid2);
    }

    @Test
    public void testUUIDBuilderEquals() {
        UUID uuid1 = new SchemaSerialBuilder("X")
                .add("a", "int", 0, 64)
                .add("b", "double", 64, 64)
                .buildSerialId();

        UUID uuid2 = new SchemaSerialBuilder("X")
                .add("a", "int", 0, 64)
                .add("b", "double", 64, 64)
                .buildSerialId();

        Assert.assertEquals(uuid1, uuid2);
    }

    @Test
    public void testSerializingWithSameUUID() {
        Contrato.SERIAL_UUID = UUID.randomUUID();
        writeLedStream();
        readLedStream();
    }

    @Test(expected = SchemaSerialUUIDMismatchException.class)
    public void testSerializingWithDifferentUUIDThrowsException() {
        Contrato.SERIAL_UUID = UUID.nameUUIDFromBytes(new byte[]{1});
        writeLedStream();
        Contrato.SERIAL_UUID = UUID.nameUUIDFromBytes(new byte[]{2});
        readLedStream();
    }

    private void readLedStream() {
        LedReader reader = new LedReader(new File(FOLDER, FILENAME));
        reader.read(Contrato.class);
    }

    private void writeLedStream() {
        LedStream<Contrato> contratos = LedStream.fromStream(Contrato.class, contratos());
        LedWriter writer = new LedWriter(new File(FOLDER, FILENAME));
        writer.write(contratos);
    }

    private Stream<Contrato> contratos() {
        return Stream.of(new Contrato().id(1), new Contrato().id(2), new Contrato().id(3));
    }

    public static class Contrato extends Schema {

        public static final int SIZE = 16;
        public static UUID SERIAL_UUID = null; // Set by tests
        public static final SchemaFactory<Contrato> FACTORY = new SchemaFactory<>(Contrato.class) {
            @Override
            public Contrato newInstance(ByteStore store) {
                return new Contrato(store);
            }
        };

        public Contrato() {
            this(new ByteBufferStore(SIZE));
        }

        public Contrato(ByteStore store) {
            super(store);
        }

        @Override
        public long id() {
            return bitBuffer.getAlignedLong(0);
        }

        public Contrato id(long id) {
            bitBuffer.setAlignedLong(0, id);
            return this;
        }

        @Override
        public int size() {
            return SIZE;
        }

        @Override
        public UUID serialUUID() {
            return SERIAL_UUID;
        }
    }
}
