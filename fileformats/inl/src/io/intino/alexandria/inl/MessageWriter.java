package io.intino.alexandria.inl;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Map;

public class MessageWriter implements AutoCloseable {
    private final OutputStream os;

    public MessageWriter(OutputStream os) {
        this.os = new BufferedOutputStream(os);
    }

    public void write(Message message) throws IOException {
        write(message.toString());
        if (message.hasAttachments()) write(message.allAttachments());
    }

    private void write(Map<String, byte[]> attachments) throws IOException {
        for (Map.Entry<String, byte[]> entry : attachments.entrySet())
            write(entry.getKey(), Base64.getEncoder().encode(entry.getValue()));
    }

    private void write(String key, byte[] encode) throws IOException {
        os.write(key.getBytes());
        os.write(':');
        os.write(String.valueOf(encode.length).getBytes());
        os.write('\n');
        os.write(encode);
        os.write('\n');
        os.write('\n');
    }

    private void write(String str) throws IOException {
        os.write(str.getBytes());
    }

    public void close() throws IOException {
        os.close();
    }



}
