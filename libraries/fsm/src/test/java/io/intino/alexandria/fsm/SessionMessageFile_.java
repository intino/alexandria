package io.intino.alexandria.fsm;

import io.intino.alexandria.logger.Logger;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.file.StandardOpenOption.CREATE;
import static org.junit.Assert.*;

public class SessionMessageFile_ {

    private static final String TEST_DIR = "temp_test";

    private static final List<String> Messages = List.of(
            "A", "E", "F", "B", "G", "C", "D"
    );

    @Test
    public void messageList_should_return_all_messages_in_order() {

        SessionMessageFile file = createSessionMessageFile();

        List<String> messages = file.messageList();

        assertEquals(Messages.size(), messages.size());

        int i = 0;
        for(String message : messages) {
            assertEquals(Messages.get(i++), message);
        }
    }

    @Test
    public void messages_should_return_all_messages_in_order() {

        SessionMessageFile file = createSessionMessageFile();

        AtomicInteger i = new AtomicInteger();
        file.messages().forEach(m -> assertEquals(Messages.get(i.getAndIncrement()), m));

        assertEquals(Messages.size(), i.get());
    }

    @Test
    public void iterator_should_return_all_messages_in_order() {

        SessionMessageFile file = createSessionMessageFile();

        Iterator<String> iterator = file.iterator();

        int i = 0;
        while(iterator.hasNext()) {
            String message = iterator.next();
            assertEquals(Messages.get(i++), message);
        }

        assertEquals(Messages.size(), i);
    }

    @Test
    public void moveTo_should_effectively_move_the_file_to_the_new_directory() {

        SessionMessageFile file = createSessionMessageFile();

        File oldFile = file.file();

        File directory = new File(TEST_DIR, "directory");
        directory.deleteOnExit();

        file.moveTo(directory);

        File newFile = new File(directory, oldFile.getName());

        assertFalse(oldFile.exists());
        assertTrue(newFile.exists());
        assertEquals(newFile, file.file());
    }

    private SessionMessageFile createSessionMessageFile() {
        File file = new File(TEST_DIR, "messages.session");
        file.getParentFile().mkdirs();
        file.deleteOnExit();
        try {
            Files.write(file.toPath(), Messages, CREATE);
        } catch (IOException e) {
            Logger.error(e);
        }
        return new SessionMessageFile(file);
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.deleteDirectory(new File(TEST_DIR));
    }
}
