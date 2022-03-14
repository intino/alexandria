package io.intino.alexandria.fsm;

import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.StandardOpenOption.CREATE;

public class IndexFile implements AutoCloseable {

    private static final int SAVE_TIME_MS = 5 * 1000;

    private final File file;
    private long index;
    private volatile boolean closed;
    private long lastTimeSaved;

    IndexFile(Mailbox mailbox, SessionMessageFile messageFile) {
        this.file = new File(mailbox.processing(), messageFile.getName() + ".last-index");
        this.index = getNextIndex();
        this.lastTimeSaved = System.currentTimeMillis();
    }

    private FileWriter open(File file) throws IOException {
        return new FileWriter(file);
    }

    public File file() {
        return file;
    }

    public long index() {
        return index;
    }

    public void increment() {
        ++index;
        trySave();
    }

    private void trySave() {
        long now = System.currentTimeMillis();
        if(now - lastTimeSaved >= SAVE_TIME_MS) {
            save();
            lastTimeSaved = now;
        }
    }

    private long getNextIndex() {
        return readLastIndex() + 1;
    }

    private long readLastIndex() {
        if(!file.exists()) return -1;
        try {
            return Long.parseLong(Files.readString(file.toPath()));
        } catch (IOException | NumberFormatException e) {
            return -1;
        }
    }

    public void save() {
        if(closed) return;
        try {
            Files.writeString(file.toPath(), String.valueOf(index), CREATE);
        } catch (IOException e) {
            Logger.error(e);
        }
    }

    public void delete() {
        file.delete();
    }

    @Override
    public void close() {
        if(closed) return;
        save();
        delete();
        closed = true;
    }
}
