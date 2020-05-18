package io.intino.alexandria.allotropy.prototype.deserializers;

import io.intino.alexandria.allotropy.FormatError;
import io.intino.alexandria.allotropy.prototype.Packet;
import io.intino.alexandria.allotropy.prototype.Seed;

import java.util.HashMap;
import java.util.Map;

public class DelimitedDeserializer implements Seed.Deserializer {
    private final String delimiter;
    private final Map<Integer, String> names;
    private String name;
    private boolean fixSize;

    public DelimitedDeserializer(String delimiter) {
        this(delimiter, true);
    }

    public DelimitedDeserializer(String delimiter, boolean fixSize) {
        this.delimiter = delimiter;
        this.names = new HashMap<>();
        this.fixSize = fixSize;
    }

    public DelimitedDeserializer name(String name) {
        this.name = name;
        return this;
    }

    public DelimitedDeserializer add(String name, int pos) {
        names.put(pos, name);
        return this;
    }

    @Override
    public Packet deserialize(String line) throws FormatError {
        return deserialize(line.split(delimiter));
    }

    private Packet deserialize(String[] split) throws FormatError {
        if (fixSize && split.length != names.size())
            throw new FormatError(names.size() + " fields expected, but it were found " + split.length + " fields");
        return map(split);
    }

    private Packet map(String[] data) {
        return new Packet() {

            @Override
            public String name() {
                return name;
            }

            @Override
            public Item[] items() {
                return names.keySet().stream()
                        .map(this::item)
                        .toArray(Item[]::new);
            }

            private Item item(int pos) {
                return new Item(names.get(pos), dataOf(pos));
            }

            private String dataOf(int pos) {
                return pos < data.length ? data[pos] : "";
            }
        };
    }
}
