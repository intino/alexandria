package io.intino.alexandria.fsm;

import io.intino.alexandria.logger.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.Semaphore;

/**
 * Represents an open session file for writing messages.
 * */
public class Session implements AutoCloseable {

    private final File file;
    private volatile BufferedOutputStream writer;
    private volatile int byteCount;
    private volatile Instant lastWriting;
    private final Semaphore semaphore = new Semaphore(1);

    public Session(File file) throws IOException {
        this(file, true);
    }

    public Session(File file, boolean append) throws IOException {
        this.file = file;
        this.writer = new BufferedOutputStream(new FileOutputStream(file, append));
    }

    boolean write(String message) {
        return write(message.concat("\n").getBytes());
    }

    synchronized boolean write(byte[] bytes) {
        try {
            semaphore.acquire();
            if(isClosed()) return false;
            writer.write(bytes);
            byteCount += bytes.length;
            lastWriting = Instant.now();
            return true;
        } catch (Exception e) {
            Logger.error(e);
            return false;
        } finally {
            semaphore.release();
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
    public synchronized void close() {
        try {
            semaphore.acquire();
            if(writer == null) return;
            writer.close();
            writer = null;
        } catch (Exception e) {
            Logger.error("Error while closing session: " + e.getMessage(), e);
        } finally {
            semaphore.release();
        }
    }

    public File file() {
        return file;
    }
}
