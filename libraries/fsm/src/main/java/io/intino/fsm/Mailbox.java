package io.intino.fsm;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * A mailbox is a directory which contains the following subdirectories:
 *
 * - pending: Contains unprocessed session files
 * - processing: Contains session files that are being processed at the moment
 * - processed: Contains fully-processed session files
 * - errors: Contains files describing errors encountered while reading session files from this mailbox
 * */
public class Mailbox {

    public static final String PENDING_DIR_NAME = "00_pending";
    public static final String PROCESSING_DIR_NAME = "01_processing";
    public static final String PROCESSED_DIR_NAME = "02_processed";
    public static final String ERRORS_DIR_NAME = "03_errors";

    private final File root;
    private final File pending;
    private final File processing;
    private final File processed;
    private final File error;

    public Mailbox(File root) {
        this.root = requireNonNull(root);
        this.pending = new File(root, PENDING_DIR_NAME);
        this.processing = new File(root, PROCESSING_DIR_NAME);
        this.processed = new File(root, PROCESSED_DIR_NAME);
        this.error = new File(root, ERRORS_DIR_NAME);
        mkdirs();
    }

    public File root() {
        return root;
    }

    public File pending() {
        return pending;
    }

    public List<SessionMessageFile> listPendingMessages() {
        return listFiles(pending, FileSessionManager.MESSAGE_EXTENSION).map(SessionMessageFile::new).collect(Collectors.toList());
    }

    public File processing() {
        return processing;
    }

    public List<SessionMessageFile> listProcessingMessages() {
        return listFiles(processing, FileSessionManager.MESSAGE_EXTENSION).map(SessionMessageFile::new).collect(Collectors.toList());
    }

    public File processed() {
        return processed;
    }

    public List<SessionMessageFile> listProcessedMessages() {
        return listFiles(processed, FileSessionManager.MESSAGE_EXTENSION).map(SessionMessageFile::new).collect(Collectors.toList());
    }

    public File errors() {
        return error;
    }

    public File currentErrorFile() {
        return new File(error, today() + FileSessionManager.ERROR_EXTENSION);
    }

    private static final DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private String today() {
        return LocalDate.now(ZoneId.of(ZoneOffset.UTC.getId())).format(Formatter);
    }

    public List<SessionMessageFile> listErrorFiles() {
        return listFiles(error, FileSessionManager.ERROR_EXTENSION).map(SessionMessageFile::new).collect(Collectors.toList());
    }

    private Stream<File> listFiles(File directory, String extension) {
        File[] children = directory.listFiles(f -> f.getName().endsWith(extension));
        if(children == null) return Stream.empty();
        return Arrays.stream(children).sorted();
    }

    public void mkdirs() {
        pending.mkdirs();
        processing.mkdirs();
        processed.mkdirs();
        error.mkdirs();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mailbox mailbox = (Mailbox) o;
        return Objects.equals(root, mailbox.root);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root);
    }

    @Override
    public String toString() {
        return root.getPath();
    }
}
