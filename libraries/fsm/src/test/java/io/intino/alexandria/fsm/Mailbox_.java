package io.intino.alexandria.fsm;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE;
import static org.junit.Assert.assertTrue;

public class Mailbox_ {

    private static final String TEST_DIR = "temp_test";
    public static final String MAILBOX_DIR = "mailbox";

    @Test
    public void should_generate_all_folders_on_creation() {

        Mailbox mailbox = new Mailbox(new File(TEST_DIR, MAILBOX_DIR));

        assertTrue(mailbox.pending().exists());
        assertTrue(mailbox.processing().exists());
        assertTrue(mailbox.processed().exists());
        assertTrue(mailbox.errors().exists());
    }

    private static final String[] Extensions = {FileSessionManager.MESSAGE_EXTENSION, ".session.temp", ".session.txt", FileSessionManager.ERROR_EXTENSION};

    @Test
    public void list_pending_messages_should_return_all_pending_session_files() {

        Mailbox mailbox = new Mailbox(new File(TEST_DIR, MAILBOX_DIR));

        assertTrue(mailbox.listPendingMessages().isEmpty());

        generateFilesIn(mailbox.pending(), 100, Extensions);

        List<SessionMessageFile> files = mailbox.listPendingMessages();

        for(SessionMessageFile file : files) {
            assertTrue(file.file().getName().endsWith(FileSessionManager.MESSAGE_EXTENSION));
        }
    }

    @Test
    public void list_processing_messages_should_return_all_pending_session_files() {

        Mailbox mailbox = new Mailbox(new File(TEST_DIR, MAILBOX_DIR));

        assertTrue(mailbox.listProcessingMessages().isEmpty());

        generateFilesIn(mailbox.processing(), 100, Extensions);

        List<SessionMessageFile> files = mailbox.listProcessingMessages();

        for(SessionMessageFile file : files) {
            assertTrue(file.file().getName().endsWith(FileSessionManager.MESSAGE_EXTENSION));
        }
    }

    @Test
    public void list_processed_messages_should_return_all_pending_session_files() {

        Mailbox mailbox = new Mailbox(new File(TEST_DIR, MAILBOX_DIR));

        assertTrue(mailbox.listProcessedMessages().isEmpty());

        generateFilesIn(mailbox.processing(), 100, Extensions);

        List<SessionMessageFile> files = mailbox.listProcessedMessages();

        for(SessionMessageFile file : files) {
            assertTrue(file.file().getName().endsWith(FileSessionManager.MESSAGE_EXTENSION));
        }
    }

    @Test
    public void list_errors_should_return_all_pending_session_files() {

        Mailbox mailbox = new Mailbox(new File(TEST_DIR, MAILBOX_DIR));

        assertTrue(mailbox.listErrorFiles().isEmpty());

        generateFilesIn(mailbox.processing(), 100, Extensions);

        List<SessionMessageFile> files = mailbox.listErrorFiles();

        for(SessionMessageFile file : files) {
            assertTrue(file.file().getName().endsWith(FileSessionManager.ERROR_EXTENSION));
        }
    }

    private void generateFilesIn(File directory, int count, String... extensions) {
        for(int i = 0;i < count;i++) {
            try {
                File file = new File(directory, i + extensions[i % extensions.length]);
                file.deleteOnExit();
                Files.writeString(
                        file.toPath(),
                        "Message\n".repeat(i + 1),
                        CREATE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.deleteDirectory(new File(TEST_DIR));
    }
}
