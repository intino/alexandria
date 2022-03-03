package io.test.leds;

import io.intino.alexandria.led.GenericSchema;
import io.intino.alexandria.led.LedStream;
import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.buffers.BitBuffer;
import io.intino.alexandria.led.legacy.LegacyLedReader;
import io.intino.alexandria.sumus.Attribute;
import io.intino.alexandria.sumus.parser.AttributeDefinition;
import io.intino.alexandria.sumus.parser.LedgerDefinition;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static io.test.leds.DistritoLed.LedFile;

// Read a generic led file and interpret its fields with the ledger definition
// All its fields must be aligned
public class ReadLedFactWithLedgerDefinition {

    public static void main(String[] args) throws Exception {
        LedgerDefinition def = LedgerDefinition.read(new File("C:\\Users\\naits\\Desktop\\MonentiaDev\\alexandria\\libraries\\sumus\\res\\ledgers\\distritos.ledger"));
        SchemaLayout layout = new SchemaLayout(def.attributes);

        try(LedStream<? extends Schema> schemas = new LegacyLedReader(LedFile).read(GenericSchema.class)) {
            for(Schema schema : schemas.iterable()) {
                printAttributes(schema, layout);
                System.out.println();
            }
        }
    }

    private static void printAttributes(Schema schema, SchemaLayout layout) {
        for(int i = 0;i < layout.attributes.size();i++) {
            printAttribute(schema, layout.attributes.get(i));
        }
    }

    private static void printAttribute(Schema schema, SchemaAttribute<?> attrib) {
        System.out.println(attrib.name + " = " + attrib.get(schema.bitBuffer()));
    }

    private static class SchemaLayout {

        private final List<SchemaAttribute<?>> attributes = new ArrayList<>();

        public SchemaLayout(List<AttributeDefinition> attributes) {
            for(AttributeDefinition attributeDefinition : attributes) {
                add(attributeDefinition);
            }
        }

        public void add(AttributeDefinition attribute) {
            attributes.add(attribFromType(attribute));
        }

        private SchemaAttribute<?> attribFromType(AttributeDefinition attrib) {
            int offset = attributes.isEmpty() ? 0 : getNextOffset();
            switch(attrib.type) {
                case label: return new LongAttribute(attrib.name, offset);
                case number: return new FloatAttribute(attrib.name, offset);
                case integer: return new IntAttribute(attrib.name, offset);
                case category: return new IntAttribute(attrib.name, offset);
                case date: return new DateAttribute(attrib.name, offset);
            }
            return null;
        }

        private int getNextOffset() {
            SchemaAttribute<?> attrib = attributes.get(attributes.size() - 1);
            return attrib.offset + attrib.size;
        }
    }

    private static abstract class SchemaAttribute<T> {

        private final String name;
        protected final int offset; // in bits
        protected final int size; // in bits

        public SchemaAttribute(String name, int offset, int size) {
            this.name = name;
            this.offset = offset;
            this.size = size;
        }

        public abstract T get(BitBuffer bitBuffer);
    }

    public static class LongAttribute extends SchemaAttribute<Long> {

        public LongAttribute(String name, int offset) {
            super(name, offset, Long.SIZE);
        }

        @Override
        public Long get(BitBuffer bitBuffer) {
            return bitBuffer.getAlignedLong(offset);
        }
    }

    public static class IntAttribute extends SchemaAttribute<Integer> {

        public IntAttribute(String name, int offset) {
            super(name, offset, Integer.SIZE);
        }

        @Override
        public Integer get(BitBuffer bitBuffer) {
            return bitBuffer.getAlignedInteger(offset);
        }
    }

    public static class FloatAttribute extends SchemaAttribute<Float> {

        public FloatAttribute(String name, int offset) {
            super(name, offset, Float.SIZE);
        }

        @Override
        public Float get(BitBuffer bitBuffer) {
            return bitBuffer.getAlignedReal32Bits(offset);
        }
    }

    public static class DateAttribute extends SchemaAttribute<LocalDateTime> {

        public DateAttribute(String name, int offset) {
            super(name, offset, Float.SIZE);
        }

        @Override
        public LocalDateTime get(BitBuffer bitBuffer) {
            return LocalDateTime.ofEpochSecond(bitBuffer.getAlignedLong(offset), 0, ZoneOffset.UTC);
        }
    }
}
