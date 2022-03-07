package io.intino.alexandria.fsm;

import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class IndexFile implements AutoCloseable {

    private static final int SAVE_FREQUENCY = 500;

    private final File file;
    private volatile long index;
    private volatile boolean closed;

    IndexFile(Mailbox mailbox, SessionMessageFile messageFile) {
        this.file = new File(mailbox.processing(), messageFile.getName() + ".last-index");
        this.index = getNextIndex();
    }

    public File file() {
        return file;
    }

    public long index() {
        return index;
    }

    public void increment() {
        if(++index % SAVE_FREQUENCY == 0)
            save();
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
            Files.writeString(file.toPath(), String.valueOf(index));
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
