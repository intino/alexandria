package io.intino.alexandria.fsm;

import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Objects.requireNonNull;

public class SessionMessageFile implements Iterable<String> {

    private File file;

    public SessionMessageFile(File file) {
        this.file = requireNonNull(file);
    }

    public String getName() {
        return file.getName();
    }

    public File file() {
        return file;
    }

    public void delete() {
        file.delete();
    }

    public Stream<String> messages() {
        return StreamSupport.stream(spliterator(), false);
    }

    public List<String> messageList() {
        return messages().collect(Collectors.toList());
    }

    @Override
    public Iterator<String> iterator() {
        return new SessionMessageFileIterator();
    }

    public boolean moveTo(File directory) {
        return moveTo(directory, "");
    }

    public boolean moveTo(File directory, String prefix) {
        try {
            directory.mkdirs();
            File dst = new File(directory, prefix + file.getName());
            Files.move(file.toPath(), dst.toPath(), REPLACE_EXISTING, ATOMIC_MOVE);
            file = dst;
            return true;
        } catch (IOException e) {
            Logger.error(e);
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionMessageFile strings = (SessionMessageFile) o;
        return Objects.equals(file, strings.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }

    @Override
    public String toString() {
        return file.toString() + " (size=" + kbSize() + ")";
    }

    private String kbSize() {
        return String.format("%.2f KB", file.length() / 1024.0f);
    }

    public LocalDateTime dateTime() {
        return SessionHelper.dateTimeOf(file);
    }

    private class SessionMessageFileIterator implements Iterator<String> {

        private final Iterator<String> iterator;

        public SessionMessageFileIterator() {
            Iterator<String> iterator;
            try {
                iterator = Files.lines(file.toPath()).iterator();
            } catch (IOException e) {
                Logger.error(e);
                iterator = Stream.<String>empty().iterator();
            }
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public String next() {
            return iterator.next();
        }
    }
}
