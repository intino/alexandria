package io.intino.alexandria.allotropy.prototype.packets;

import io.intino.alexandria.allotropy.FormatError;
import io.intino.alexandria.allotropy.prototype.Packet;

public class ErrorPacket implements Packet {
    private final Integer index;
    private final FormatError error;
    private String line = "";

    public ErrorPacket(int index, FormatError error) {
        this.index = index;
        this.error = error;
    }

    public ErrorPacket(int index, String line, FormatError error) {
        this.index = index;
        this.line = line;
        this.error = error;
    }

    @Override
    public String name() {
        return "FORMAT ERROR";
    }

    @Override
    public int line() {
        return index;
    }

    @Override
    public Item[] items() {
        return new Item[]{
                new Item("line", String.valueOf(index)),
                new Item("content", line),
                new Item("description", error.getMessage())
        };
    }
}
