package io.intino.alexandria.fsm;

import io.intino.alexandria.logger.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;

/**
 * Represents an open session file for writing messages.
 * */
public class Session implements AutoCloseable {

    private final File file;
    private BufferedOutputStream writer;
    private int byteCount;
    private Instant lastWriting;

    public Session(File file) throws IOException {
        this(file, true);
    }

    public Session(File file, boolean append) throws IOException {
        this.file = file;
        this.writer = new BufferedOutputStream(new FileOutputStream(file, append));
    }

    void write(String message) {
        write(message.concat("\n").getBytes());
    }

    void write(byte[] bytes) {
        try {
            writer.write(bytes);
            byteCount += bytes.length;
            lastWriting = Instant.now();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int byteCount() {
        return byteCount;
    }

    public Instant lastWriting() {
        return lastWriting;
    }

    public boolean isClosed() {
        return writer == null;
    }

    @Override
    public void close() {
        try {
            if(writer == null) return;
            writer.close();
            writer = null;
        } catch (IOException e) {
            Logger.error("Error while closing session: " + e.getMessage(), e);
        }
    }

    public File file() {
        return file;
    }
}
