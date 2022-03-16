package io.intino.alexandria.fsm;

import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

class ErrorFile implements AutoCloseable {

    private static final int BATCH_SIZE = 1024;

    private final File file;
    private final List<String> errors;
    private volatile boolean closed;

    public ErrorFile(Mailbox mailbox) {
        this.file = mailbox.currentErrorFile();
        this.errors = new ArrayList<>(BATCH_SIZE);
    }

    public void add(String error) {
        if(errors.size() >= BATCH_SIZE) {
            save(errors);
            errors.clear();
        }
        errors.add(error);
    }

    @Override
    public void close() {
        if(closed) return;
        save(List.copyOf(errors));
        closed = true;
        this.errors.clear();
    }

    public void save(List<String> errors) {
        try {
            file.getParentFile().mkdirs();
            Files.write(file.toPath(), errors, CREATE, APPEND);
        } catch (IOException e) {
            Logger.error(e);
        }
    }
}
