package io.intino.alexandria.allotropy.prototype.serializers;

import io.intino.alexandria.allotropy.prototype.Packet;
import io.intino.alexandria.allotropy.prototype.Sprout;

import java.util.Map;

import static java.util.Arrays.stream;

public class TSV implements Sprout.Serializer {
    private final String[] fields;

    public TSV(String... fields) {
        this.fields = fields;
    }

    @Override
    public String serialize(Packet packet) {
        return serialize(packet.map());
    }

    private String serialize(Map<String, String> items) {
        return TSV.serialize(stream(fields).map(items::get).toArray(String[]::new));
    }

    public static String serialize(String... items) {
        return String.join("\t", items) + "\n";
    }
}
