package io.intino.alexandria.fsm;

import io.intino.alexandria.fsm.SessionMessagePipeline.Stage;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.alexandria.fsm.StatefulScheduledService.State.*;
import static java.util.Objects.requireNonNull;

public class FileSessionManager {

    public static final String MESSAGE_EXTENSION = ".session";
    public static final String ERROR_EXTENSION = ".errors";
    private static final long DEFAULT_TIMEOUT = 10;
    private static final TimeUnit DEFAULT_TIMEOUT_UNIT = TimeUnit.MINUTES;
    public static final String TEMP_EXTENSION = ".temp";
    public static final long MIN_BYTES_PER_SESSION = 1024 * 1024; // 1KB

    private final String id;
    private final Mailbox inputMailbox;
    private final Mailbox outputMailbox;
    private final StatefulScheduledService service;
    private final long maxBytesPerSession;
    private final TimePeriod sessionTimeout;
    private final TimePeriod lockTimeout;
    private final SessionMessagePipeline messagePipeline;
    private final LockFile lockFile;
    private volatile Session session;

    public FileSessionManager(String id, Mailbox inputMailbox, Mailbox outputMailbox, TimePeriod readInputMailboxRate, long maxBytesPerSession,
                              TimePeriod sessionTimeout, TimePeriod lockTimeout, SessionMessagePipeline messagePipeline) {
        this.id = id;
        this.inputMailbox = requireNonNull(inputMailbox);
        this.outputMailbox = requireNonNull(outputMailbox);
        this.lockTimeout = lockTimeout;
        if(inputMailbox.equals(outputMailbox)) throw new IllegalArgumentException("Input and output mailbox cannot be the same");
        this.service = new StatefulScheduledService(readInputMailboxRate);
        if(maxBytesPerSession <= 0) throw new IllegalArgumentException("maxBytesPerSession must be > " + 0);
        this.maxBytesPerSession = maxBytesPerSession;
        this.sessionTimeout = sessionTimeout;
        this.messagePipeline = requireNonNull(messagePipeline);
        this.lockFile = new LockFile(this);
    }

    public PublishResult publish(String message) {
        if(message == null) throw new NullPointerException("Message cannot be null");

        Session session = getCurrentSession();
        if(session == null) return PublishResult.NullSession;

        byte[] bytes = message.concat("\n").getBytes();

        if(bytes.length > maxBytesPerSession) {
            Logger.warn("Message is too long for this session's size limit: size: " + bytes.length + " bytes vs max: " + maxBytesPerSession + " bytes");
            return PublishResult.MessageTooLong;
        }

        if(session.byteCount() + bytes.length > maxBytesPerSession) {
            closeSession();
            session = getCurrentSession();
        }

        session.write(bytes);
        return PublishResult.Ok;
    }

    public boolean start() {
        if(service.start(this::consume)) {
            Logger.info("FileSessionManager " + id + " started");
            return true;
        }
        return false;
    }

    private void consume() {
        if(shouldCloseSession()) closeSession();
        if(!validateMailboxOwnership()) return;
        consumeMessages();
    }

    private void consumeMessages() {
        List<SessionMessageFile> processingMessages = inputMailbox.listProcessingMessages();
        List<SessionMessageFile> pendingMessages = inputMailbox.listPendingMessages();

        consumeMessages(processingMessages, Stage.OnProcessing, Stage.OnProcessed);
        consumeMessages(pendingMessages);

        if(service.state() != Paused) {
            List<SessionMessageFile> messages = Stream.concat(processingMessages.stream(), pendingMessages.stream()).collect(Collectors.toList());
            logProgressInLockFile(messages);
        }
    }

    private void logProgressInLockFile(List<SessionMessageFile> messages) {
        lockFile.write("Sleeping (next iteration begins in " + service.period()
                + ")\nMessage Files Consumed (" + messages.size() + "):\n\t"
                + messages.stream().map(SessionMessageFile::toString).collect(Collectors.joining("\n\t")));
    }

    private void consumeMessages(List<SessionMessageFile> files, Stage... stages) {
        for(SessionMessageFile messageFile : files) {
            if(service.state() == Paused) return;
            lockFile.write("Consuming " + messageFile);
            executeMessagePipeline(messageFile, stages);
        }
    }

    private void executeMessagePipeline(SessionMessageFile messageFile, Stage... stages) {
        try {
            messagePipeline.execute(messageFile, this, stages);
        } catch (Throwable e) {
            try {
                messagePipeline.onPipelineError(e, messageFile, this);
            } catch (Throwable ex) {
                Logger.error(e);
            }
        }
    }

    private boolean validateMailboxOwnership() {
        try {
            lockFile.validate();
            return true;
        } catch (LockFile.LockFileException e) {
            Logger.error(getLockValidationErrorMessage(e));
            return waitForRelease();
        }
    }

    private boolean waitForRelease() {
        boolean mailboxWasReleased = lockFile.waitForRelease(lockTimeout);
        if(!mailboxWasReleased) {
            Logger.warn(id + ": " + inputMailbox.root().getPath() + " was not released after the established timeout." +
                    " This FSM will cancel its consumer routine.");
            cancel();
        }
        return mailboxWasReleased;
    }

    private String getLockValidationErrorMessage(LockFile.LockFileException e) {
        return e.getMessage() + ". Waiting until lock is released "
                + (lockTimeout != null ? "(timeout=" + lockTimeout + ")" : "")
                + ". If not released, this FSM will cancel execution.";
    }

    public void pause() {
        Logger.info("Pausing FileSessionManager " + id);
        service.pause();
        lockFile.write("Paused");
        Logger.info("FileSessionManager " + id + " paused");
    }

    public void resume() {
        Logger.info("Resuming FileSessionManager " + id);
        service.resume();
        lockFile.write("Resumed");
        Logger.info("FileSessionManager " + id + " resumed");
    }

    public void cancel() {
        Logger.info("Cancelling FileSessionManager " + id);
        service.cancel();
        lockFile.delete();
        Logger.info("FileSessionManager " + id + " cancelled");
    }

    public void terminate() {
        terminate(DEFAULT_TIMEOUT, DEFAULT_TIMEOUT_UNIT);
    }

    public void terminate(long timeout, TimeUnit timeUnit) {
        Logger.info("Terminating FileSessionManager " + id);
        service.stop(timeout, timeUnit);
        lockFile.delete();
        Logger.info("FileSessionManager " + id + " terminated");
    }

    private boolean shouldCloseSession() {
        if(session == null) return false;
        if(state() == Cancelled || state() == Terminated) return true;
        if(session.byteCount() >= maxBytesPerSession) return true;
        return session.lastWriting() != null && sessionTimeout != null && sessionTimeout();
    }

    private boolean sessionTimeout() {
        return !session.lastWriting().plus(sessionTimeout.amount(), sessionTimeout.temporalUnit()).isBefore(Instant.now());
    }

    private synchronized void closeSession() {
        if(session == null) return;
        session.close();
        File file = session.file();
        try {
            Files.move(file.toPath(), new File(file.getAbsolutePath().replace(TEMP_EXTENSION, "")).toPath());
        } catch (IOException e) {
            Logger.error(e);
        }
        session = null;
    }

    private Session getCurrentSession() {
        if(session != null) return session;
        try {
            session = new Session(getSessionTempFile());
            return session;
        } catch (IOException e) {
            Logger.error(e);
            return null;
        }
    }

    private File getSessionTempFile() {
        return new File(outputMailbox.pending(), ts() + "." + id() + ".session" + TEMP_EXTENSION);
    }

    private String ts() {
        return Instant.now().toString().replaceAll("[:-]", "");
    }

    public String id() {
        return id;
    }

    public StatefulScheduledService.State state() {
        return service.state();
    }

    public Mailbox inputMailbox() {
        return inputMailbox;
    }

    public Mailbox outputMailbox() {
        return outputMailbox;
    }

    public enum PublishResult {
        Ok,
        MessageTooLong,
        NullSession
    }

    public static class Builder {

        private String id = UUID.randomUUID().toString();
        private Mailbox input;
        private Mailbox output;
        private TimePeriod readInputMailboxRate = new TimePeriod(10, TimeUnit.SECONDS);
        private long maxBytesPerSession = MIN_BYTES_PER_SESSION;
        private TimePeriod sessionTimeout = new TimePeriod(10, TimeUnit.SECONDS);
        private TimePeriod lockTimeout = new TimePeriod(5, TimeUnit.MINUTES);
        private SessionMessagePipeline messagePipeline;

        /**
         * Specifies the id of the FSM. Must be unique between different FSM instances.
         * */
        public Builder id(String id) {
            this.id = id;
            return this;
        }

        /**
         * The input mailbox
         * */
        public Builder readsFrom(Mailbox mailbox) {
            this.input = mailbox;
            return this;
        }

        /**
         * The input mailbox root directory
         * */
        public Builder readsFrom(File mailbox) {
            return readsFrom(new Mailbox(mailbox));
        }

        /**
         * The output mailbox
         * */
        public Builder writesTo(Mailbox mailbox) {
            this.output = mailbox;
            return this;
        }

        /**
         * The output mailbox root directory
         * */
        public Builder writesTo(File mailbox) {
            return writesTo(new Mailbox(mailbox));
        }

        /**
         * The time interval in which the FSM will read new messages from the input mailbox
         * */
        public Builder atFixedRate(long period, TimeUnit timeUnit) {
            this.readInputMailboxRate = new TimePeriod(period, timeUnit);
            return this;
        }

        /**
         * The maximum size in bytes that a (writing) session could reach until it has to be closed
         * */
        public Builder maxBytesPerSession(long bytes) {
            this.maxBytesPerSession = Math.max(bytes, MIN_BYTES_PER_SESSION);
            return this;
        }

        /**
         * The maximum amount of time a session can be open for writing
         * */
        public Builder sessionTimeout(long period, TimeUnit timeUnit) {
            this.sessionTimeout = new TimePeriod(period, timeUnit);
            return this;
        }

        /**
         * The maximum amount of time a FSM will wait until the input mailbox is released from its previous owner
         * */
        public Builder lockTimeout(long period, TimeUnit timeUnit) {
            this.lockTimeout = new TimePeriod(period, timeUnit);
            return this;
        }

        /**
         * Specifies that this FSM will wait indefinitely until the input mailbox is released.
         * */
        public Builder withoutLockTimeout() {
            this.lockTimeout = null;
            return this;
        }

        /**
         * Sets a custom SessionMessagePipeline for this FSM
         * */
        public Builder setMessagePipeline(SessionMessagePipeline pipeline) {
            messagePipeline = requireNonNull(pipeline);
            return this;
        }

        /**
         * Sets the onMessageProcess phase of the SessionMessagePipeline. The rest of the pipeline stages will be the default ones
         * This version takes only the next line of text from the session file that's being read as a parameter
         * */
        public Builder onMessageProcess(MessageConsumer consumer) {
            if(consumer == null) return this;
            return setMessagePipeline(new SessionMessagePipeline() {
                @Override
                protected void processMessage(String message, FileSessionManager fsm) throws Throwable {
                    consumer.accept(message);
                }
            });
        }

        /**
         * Sets the onMessageProcess phase of the SessionMessagePipeline. The rest of the pipeline stages will be the default ones
         * This version takes the next line of text from the session file that's being read and the owner FileSessionManager as parameters
         * */
        public Builder onMessageProcess(MessageBiConsumer consumer) {
            if(consumer == null) return this;
            return setMessagePipeline(new SessionMessagePipeline() {
                @Override
                protected void processMessage(String message, FileSessionManager fsm) throws Throwable {
                    consumer.accept(message, fsm);
                }
            });
        }

        /**
         * Instantiates a new FileSessionManager
         * */
        public FileSessionManager build() {
            return new FileSessionManager(id, input, output, readInputMailboxRate, maxBytesPerSession, sessionTimeout, lockTimeout, messagePipeline);
        }
    }

    @FunctionalInterface
    public interface MessageConsumer {
        /**
         * Consumes the next message (line of text) from a .session file
         * */
        void accept(String message) throws Throwable;
    }

    @FunctionalInterface
    public interface MessageBiConsumer {
        /**
         * Consume the next message (line of text) from a .session file. The owner FileSessionManager is also passed in as a parameter
         * */
        void accept(String message, FileSessionManager manager) throws Throwable;
    }
}
