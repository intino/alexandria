package io.intino.alexandria.allotropy.prototype.serializers;

import io.intino.alexandria.allotropy.prototype.Packet;
import io.intino.alexandria.allotropy.prototype.Sprout;

public class LogError implements Sprout.Serializer {

    private static final String[] Fields = new String[]{"line", "description"};
    private final TSV tsv;

    public LogError() {
        this.tsv = new TSV(Fields);
    }

    @Override
    public String serialize(Packet packet) {
        return packet != null ? tsv.serialize(packet) : null;
    }
}
