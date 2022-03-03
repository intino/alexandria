package io.intino.alexandria.fsm;

import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

/**
 * Represents the chain of actions performed upon a SessionMessageFile. It is supposed to be stateless.
 *
 * A pipeline works with a {@link SessionMessageFile} messageFile and the {@link FileSessionManager} fsm that invokes it.
 *
 * This is a partially complete class. The only method required to be implemented is processMessage(String, FileSessionManager),
 * which is called for each line read from the aforementioned messageFile.
 *
 * A pipeline consists of 3 main stages, in order of execution:
 *
 *  - onPending: called when the messageFile is pending to be processed (under the mailbox's pending directory).
 *               The default implementation moves the file to the processing directory and advances to the next stage.
 *
 *  - onProcessing: called when the messageFile is waiting to be processed at the moment. This is marked by moving the file from the
 *                  pending directory to the processing directory. This is the client's responsibility, though the default implementation
 *                  already does this in the previous stage.
 *
 *  - onProcessed: called when the messageFile's processing has finished. The file should be moved from the processing directory
 *                  to avoid processing it again.
 *                  The default implementation already does this, moving the file to the processed directory.
 *
 *
 *  The 2 first stages must return an {@link Operation} object, which values are:
 *      - Continue: tells the pipeline to advance to the next stage.
 *      - Stop or null: tells the pipeline to stop execution and to NOT run the remaining stages.
 *
 * Additionally, the following methods might be called if an error occurred:
 *
 *  - onMessageError: called when an individual message (line) causes an error. The rest of the lines will be processed normally.
 *                    The default implementation writes the message to the current error file in the input mailbox.
 *
 *  - onPipelineError: called when a general error causes the pipeline to stop.
 *
 * */
public abstract class SessionMessagePipeline {

    public final void execute(SessionMessageFile messageFile, FileSessionManager fsm, Stage... stages) {
        if(messageFile == null) throw new NullPointerException("messageFile is null");
        if(fsm == null) throw new NullPointerException("manager is null");
        Set<Stage> stagesToExecute = isNullOrEmpty(stages)
                ? EnumSet.allOf(Stage.class)
                : Arrays.stream(stages).filter(Objects::nonNull).collect(Collectors.toUnmodifiableSet());
        execute(messageFile, fsm, stagesToExecute);
    }

    protected void execute(SessionMessageFile messageFile, FileSessionManager fsm, Set<Stage> stages) {
        try {
            if (stages.contains(Stage.OnPending)) {
                Operation operation = onPending(messageFile, fsm);
                if(operation == null || operation == Operation.Stop) return;
            }

            if(stages.contains(Stage.OnProcessing)) {
                Operation operation = onProcessing(messageFile, fsm);
                if(operation == null || operation == Operation.Stop) return;
            }

            if(stages.contains(Stage.OnProcessed)) {
                onProcessed(messageFile, fsm);
            }
        } catch (Throwable e) {
            onPipelineError(e, messageFile, fsm);
        }
    }

    protected Operation onPending(SessionMessageFile messageFile, FileSessionManager fsm) {
        messageFile.moveTo(fsm.inputMailbox().processing());
        return Operation.Continue;
    }

    protected Operation onProcessing(SessionMessageFile messageFile, FileSessionManager fsm) {
        processSessionMessageFile(messageFile, fsm);
        return Operation.Continue;
    }

    private void processSessionMessageFile(SessionMessageFile messageFile, FileSessionManager fsm) {
        for(String message : messageFile) {
            try {
                processMessage(message, fsm);
            } catch (Throwable e) {
                onMessageError(message, fsm);
            }
        }
    }

    protected abstract void processMessage(String message, FileSessionManager fsm) throws Throwable;

    protected void onMessageError(String message, FileSessionManager fsm) {
        try {
            File file = fsm.inputMailbox().currentErrorFile();
            file.getParentFile().mkdirs();
            Files.writeString(file.toPath(), message + "\n", CREATE, APPEND);
        } catch (Throwable e) {
            Logger.error(e);
        }
    }

    protected void onProcessed(SessionMessageFile messageFile, FileSessionManager fsm) {
        messageFile.moveTo(fsm.inputMailbox().processed());
    }

    protected void onPipelineError(Throwable e, SessionMessageFile messageFile, FileSessionManager fsm) {
        Logger.error(e);
    }

    private boolean isNullOrEmpty(Stage[] stages) {
        return stages == null || stages.length == 0;
    }

    public enum Operation {
        Continue,
        Stop
    }

    public enum Stage {
        OnPending,
        OnProcessing,
        OnProcessed
    }
}
