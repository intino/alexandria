package io.intino.fsm;

import io.intino.alexandria.logger.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Deprecated
class FileSessionManagerConcept {

    private final List<Consumer<File>> consumers = new ArrayList<>();
    private final File listen;
    private final File write;
    private int bytesThreshold = 1024 * 1024;
    private boolean running;
    private Session currentSession;
    private File sessionFile;
    private Instant lastWriting = Instant.now();
    private boolean paused = false;

    public FileSessionManagerConcept(File listen, File write) {
        this.listen = listen;
        this.write = write;
        init();
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    private void init() {
        createFolders();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (running && paused) return;
                closeSession();
                running = true;
                Files.walk(new File(listen, "pending").toPath())
                        .filter(f -> f.toFile().getName().endsWith(".session"))
                        .forEach(f -> consumers.forEach(c -> c.accept(f.toFile())));
                running = false;
            } catch (Throwable e) {
                Logger.error(e);
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    private void closeSession() {
        if (lastWriting.plus(10, ChronoUnit.SECONDS).isAfter(Instant.now()))
            closeSession(currentSession);
    }

    private void createFolders() {
        new File(listen, "pending").mkdirs();
        new File(listen, "processing").mkdirs();
        new File(listen, "processed").mkdirs();
        new File(listen, "error").mkdirs();
        new File(write, "pending").mkdirs();
        new File(write, "processing").mkdirs();
        new File(write, "processed").mkdirs();
        new File(write, "error").mkdirs();
    }

    public void onArrival(Consumer<File> consumer) {
        consumers.add(consumer);
    }

    public File setAsProcessing(File file) {
        return setAs(file, "processing");
    }

    public File setAsProcessed(File file) {
        try {
            File newFile = new File(listen, "processed/"
                    + LocalDate.now().toString().replace("-", "/") + "/" + file.getName());
            newFile.getParentFile().mkdirs();
            Files.move(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return newFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private File setAs(File file, String state) {
        try {
            File newFile = new File(listen, state + "/" + file.getName());
            Files.move(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return newFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void write(String message) {
        try {
            Session session = session();
            session.write(message);
            lastWriting = Instant.now();
            if (session.byteCount >= bytesThreshold) closeSession(session);
        } catch (Throwable e) {
            Logger.error(e);
        }
    }

    private synchronized void closeSession(Session session) {
        session.close();
        sessionFile.renameTo(new File(sessionFile.getAbsolutePath().replace(".temp", ".session")));
        currentSession = null;
    }

    private Session session() {
        if (currentSession != null) return currentSession;
        try {
            return currentSession = new Session(new BufferedWriter(new FileWriter(sessionFile = newTempFile())));
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    private File newTempFile() {
        return new File(write, Instant.now().toString().replace(":", "_") + ".temp");
    }

    public void saveError(String line) {
        try {
            Files.writeString(new File(listen, "error/" + todayFile() + ".messages").toPath(), line + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String todayFile() {
        return LocalDate.now().toString();
    }

    private class Session {
        private final BufferedWriter currentSession;
        private int byteCount;

        private Session(BufferedWriter currentSession) {
            this.currentSession = currentSession;
        }

        public void write(String message) {
            try {
                byteCount += message.length() + 1;
                currentSession.write(message + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public int byteCount() {
            return byteCount;
        }

        public void close() {
            try {
                currentSession.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
