package io.intino.alexandria.fsm;

import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * The lock file indicates ownership of a mailbox by a FileSessionManager as a consumer (read operations). Its name is <fsm-id>.lock.
 *
 * Only 1 FileSessionManager may be a mailbox consumer at a given time, hence only 1 lock file should be present in the mailbox
 * root directory.
 *
 * Lock files are deleted when the owner FileSessionManager finishes or automatically when JVM exits.
 *
 * Although being a mere lock mechanism, it is also used as a small log file of the owner FileSessionManager.
 * */
public class LockFile {

    public static final String LOCK_EXTENSION = ".lock";

    public static List<File> getLockFilesOf(Mailbox mailbox) {
        File[] files = mailbox.root().listFiles(f -> f.getName().endsWith(LOCK_EXTENSION));
        return files == null ? Collections.emptyList() : Arrays.asList(files);
    }

    private final String fileSessionManagerId;
    private final Mailbox inputMailbox;
    private final File file;

    public LockFile(FileSessionManager fsm) {
        this.fileSessionManagerId = requireNonNull(fsm).id();
        this.inputMailbox = fsm.inputMailbox();
        this.file = new File(inputMailbox.root(), fileSessionManagerId + LOCK_EXTENSION);
    }

    public void delete() {
        file.delete();
    }

    public boolean exists() {
        return file.exists();
    }

    public void validate() {
        ensureNotExistOtherLockFile();
        createIfNotExists();
    }

    public void write(String message) {
        try {
            if(!file.exists()) return;
            Files.writeString(file.toPath(), format(LocalDateTime.now()) + message + "\n");
        } catch (IOException e) {
            Logger.error("Could not write message to " + fileSessionManagerId + " lock file: " + e.getMessage(), e);
        }
    }

    private static final DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SS");
    private String format(LocalDateTime ts) {
        return "[" + Formatter.format(ts) + "]: ";
    }

    public boolean waitForRelease(TimePeriod timeout) {
        if(timeout == null) {
            Logger.warn("FSM " + fileSessionManagerId + " will wait indefinitely until lock from " + inputMailbox.root().getPath() + " is released." +
                    " This could result in an endless wait loop.");
        }

        Instant startTime = Instant.now();
        Instant waitLimit = timeout == null ? null : Instant.now().plus(timeout.amount(), timeout.temporalUnit());
        int elapsedHours = 0;

        while(true) {
            List<File> lockFiles = getLockFilesOf(inputMailbox);

            if(lockFiles.isEmpty()) return true;
            if(lockFiles.size() == 1 && lockFiles.get(0).equals(file)) return true;
            if(hasReachedTimeout(waitLimit)) return false;

            sleep(5000);

            int hoursSinceStart = (int) Math.abs(ChronoUnit.HOURS.between(startTime, Instant.now()));
            if(hoursSinceStart > elapsedHours) {
                Logger.warn("FSM " + fileSessionManagerId + " is still waiting for lock of " + inputMailbox.root().getPath()
                        + " to be released (elapsed hours=" + hoursSinceStart + ")...");
                elapsedHours = hoursSinceStart;
            }
        }
    }

    private void createIfNotExists() {
        if(!exists()) {
            try {
                inputMailbox.mkdirs();
                file.createNewFile();
                file.deleteOnExit();
            } catch (IOException e) {
                Logger.error("Failed to create " + fileSessionManagerId + " lock file: " + e.getMessage(), e);
            }
        }
    }

    public String fileSessionManagerId() {
        return fileSessionManagerId;
    }

    public Mailbox inputMailbox() {
        return inputMailbox;
    }

    public File file() {
        return file;
    }

    private void ensureNotExistOtherLockFile() {
        File[] files = inputMailbox.root().listFiles(f -> f.getName().endsWith(LOCK_EXTENSION));
        if(files == null || files.length == 0) return;
        if(files.length > 1) throw new LockFileException("There are multiple lock files in " + inputMailbox.root().getPath());
        if(!files[0].equals(file)) throw new LockFileException("There is already a lock file of other FSM in " + inputMailbox.root().getPath() + ": " + files[0]);
    }

    private boolean hasReachedTimeout(Instant timeout) {
        return timeout != null && Instant.now().isBefore(timeout);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    public static class LockFileException extends IllegalStateException {
        public LockFileException(String s) {
            super(s);
        }
    }
}
