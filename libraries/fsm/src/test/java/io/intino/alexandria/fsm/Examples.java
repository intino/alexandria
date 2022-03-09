package io.intino.alexandria.fsm;

import io.intino.alexandria.fsm.StatefulScheduledService.State;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.Future;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Examples {

    public static void main(String[] args) throws Exception {
        pauseFsmAndResume();
    }

    private static void pauseFsmAndResume() throws Exception {
        FileSessionManager fsm = new FileSessionManager.Builder()
                .readsFrom(new File("temp/mailbox_1"))
                .writesTo(new File("temp/mailbox_2"))
                .onMessageProcess(System.out::println)
                .build();

        fsm.start();
        assert fsm.state() == State.Running;
        Thread.sleep(1000);

        // If this FSM is currently processing a file, it will not pause execution immediately until it finishes reading said file
        // Due to that, we need a future object if we want to wait for this FSM to be fully paused
        Future<Instant> pause = fsm.pause();
        assert fsm.state() == State.Paused;

        Instant pausedTs = pause.get(); // Block this thread until the FSM is completely paused
        if(pausedTs == null) {
            System.out.println("FSM was resumed/cancelled/terminated before went completely paused. Current state is " + fsm.state());
        } else {
            System.out.println("FSM went paused at " + pausedTs);
        }

        fsm.resume();
        assert fsm.state() == State.Running;
    }

    private static void useFsm() {
        FileSessionManager fsm = new FileSessionManager.Builder()
                .readsFrom(new File("temp/mailbox_1"))
                .writesTo(new File("temp/mailbox_2"))
                .onMessageProcess(System.out::println)
                .build();

        fsm.start();
    }

    private static void basicFsm() {
        FileSessionManager fsm = new FileSessionManager.Builder()
                .readsFrom(new File("temp/mailbox_1"))
                .writesTo(new File("temp/mailbox_2"))
                .onMessageProcess(System.out::println)
                .build();
    }

    private static void fsmWithCustomParameters() {
        FileSessionManager fsm = new FileSessionManager.Builder()
                .id("prm")
                .readsFrom(new File("temp", "mailbox_1"))
                .writesTo(new File("temp", "mailbox_2"))
                .atFixedRate(10, SECONDS)
                .maxBytesPerSession(1024 * 1024 * 1024) // 1MB
                .sessionTimeout(10, SECONDS)
                .lockTimeout(2, MINUTES)
                //.withoutLockTimeout()
                .onMessageProcess(System.out::println)
                .build();
    }

    private static void fsmWithCustomMessagePipeline() {
        FileSessionManager fsm = new FileSessionManager.Builder()
                .readsFrom(new File("temp", "mailbox_1"))
                .writesTo(new File("temp", "mailbox_2"))
                .setMessagePipeline(new MyMessagePipeline())
                .build();
    }

    // You can customize each pipeline stage. Check what the default implementation does before writing your own.
    private static class MyMessagePipeline extends SessionMessagePipeline {

        @Override
        protected void execute(SessionMessageFile messageFile, FileSessionManager fsm, Set<Stage> stages) {
            super.execute(messageFile, fsm, stages);
        }

        @Override
        protected Operation onPending(SessionMessageFile messageFile, FileSessionManager fsm) {
            messageFile.moveTo(fsm.inputMailbox().processing(), Instant.now().toString()); // Add ts as prefix
            return Operation.Continue;
        }

        @Override
        protected Operation onProcessing(SessionMessageFile messageFile, FileSessionManager fsm) {
            for(String message : messageFile) {
                try {
                    processMessage(message, fsm);
                } catch (Throwable e) {
                    onMessageError(message, fsm);
                    // Let's stop the pipeline and move the file to the error's directory
                    // (If file is not moved, it will be processed again in the next iteration)
                    messageFile.moveTo(fsm.inputMailbox().errors());
                    return Operation.Stop;
                }
            }
            return Operation.Continue;
        }

        @Override
        protected void processMessage(String message, FileSessionManager fsm) throws Throwable {
            Logger.info("Message: " + message + ", fsm = " + fsm.id());
            Files.writeString(new File(fsm.outputMailbox().pending(), "messages.session.temp").toPath(),
                    message + "\n",
                    CREATE, APPEND);
        }

        @Override
        protected void onMessageError(String message, FileSessionManager fsm) {
            super.onMessageError(message, fsm);
            Logger.error("ERROR: " + message);
        }

        @Override
        protected void onProcessed(SessionMessageFile messageFile, FileSessionManager fsm) {
            super.onProcessed(messageFile, fsm);
            Logger.info(messageFile + " has been processed!");
            try {
                Files.move(new File(fsm.outputMailbox().pending(), "messages.session.temp").toPath(),
                        new File(fsm.outputMailbox().pending(), "messages.session").toPath(),
                        REPLACE_EXISTING);
            } catch (IOException e) {
                Logger.error(e);
            }
        }

        @Override
        protected void onPipelineError(Throwable e, SessionMessageFile messageFile, FileSessionManager fsm) {
            super.onPipelineError(e, messageFile, fsm);
            Logger.error("Pipeline error: " + e.getMessage());
        }
    }
}
