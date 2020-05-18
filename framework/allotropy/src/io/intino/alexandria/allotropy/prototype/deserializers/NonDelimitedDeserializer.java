package io.intino.alexandria.allotropy.prototype.deserializers;

import io.intino.alexandria.allotropy.FormatError;
import io.intino.alexandria.allotropy.prototype.Packet;
import io.intino.alexandria.allotropy.prototype.Seed;

import java.util.HashMap;
import java.util.Map;

public class NonDelimitedDeserializer implements Seed.Deserializer {
    private final Map<int[], String> names;
    private String name;
    private boolean fixSize;
    private Integer size = null;

    public NonDelimitedDeserializer() {
        this(true);
    }

    public NonDelimitedDeserializer(boolean fixSize) {
        this.fixSize = fixSize;
        this.names = new HashMap<>();
    }

    public NonDelimitedDeserializer name(String name) {
        this.name = name;
        return this;
    }

    public NonDelimitedDeserializer add(String field, int from, int length) {
        names.put(new int[]{from, length}, field);
        return this;
    }

    public NonDelimitedDeserializer add(String constant, int from) {
        names.put(new int[]{from, constant.length()}, "~" + constant);
        return this;
    }

    private int size() {
        if (size == null) size = names.keySet().stream().mapToInt(k -> k[1]).sum();
        return size;
    }

    @Override
    public Packet deserialize(String line) throws FormatError {
        if (checkSize(line))
            throw new FormatError(size() + " characters expected, but it were found " + line.length() + " characters");
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

            private Item item(int[] pos) {
                return new Item(names.get(pos), line.substring(pos[0], pos[0] + pos[1]));
            }
        };
    }

    private boolean checkSize(String line) {
        return fixSize ? line.length() != size() : line.length() < size();
    }
}
