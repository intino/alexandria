package io.intino.alexandria.fsm;

import io.intino.alexandria.logger.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>Represents the chain of actions performed upon a SessionMessageFile. It is supposed to be stateless.</p>
 *
 * <p>A pipeline works with a {@link SessionMessageFile} messageFile and the {@link FileSessionManager} fsm that invokes it.</p>
 *
 * <p>This is a partially complete class. The only method required to be implemented is processMessage(String, FileSessionManager),
 * which is called for each line read from the aforementioned messageFile.</p>
 *
 * <p>A pipeline consists of 3 main stages, in order of execution:</p>
 *
 * <ul>
 *
 *  <il><p>onPending: called when the messageFile is pending to be processed (under the mailbox's pending directory).
 *               The default implementation moves the file to the processing directory and advances to the next stage.</p></il>
 *
 *  <il><p>onProcessing: called when the messageFile is waiting to be processed at the moment. This is marked by moving the file from the
 *                  pending directory to the processing directory. This is the client's responsibility, though the default implementation
 *                  already does this in the previous stage.</p></il>
 *
 *  <il><p>onProcessed: called when the messageFile's processing has finished. The file should be moved from the processing directory
 *                  to avoid processing it again.
 *                  The default implementation already does this, moving the file to the processed directory.</p></il>
 * </ul>
 *
 *  <p>The 2 first stages must return an {@link Operation} object, which values are:
 *      - Continue: tells the pipeline to advance to the next stage.
 *      - Stop or null: tells the pipeline to stop execution and to NOT run the remaining stages.</p>
 *
 * <p>Additionally, the following methods might be called if an error occurred:</p>
 *
 * <ul>
 *  <il><p>onMessageError: called when an individual message (line) causes an error. The rest of the lines will be processed normally.
 *                    The default implementation writes the message to the current error file in the input mailbox.</p></il>
 *  <il><p>onPipelineError: called when a general error causes the pipeline to stop.</p></il>
 * </ul>
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

    protected Operation onProcessing(SessionMessageFile messageFile, FileSessionManager fsm) throws Exception {
        processSessionMessageFile(messageFile, fsm);
        return Operation.Continue;
    }

    private void processSessionMessageFile(SessionMessageFile messageFile, FileSessionManager fsm) throws Exception {
        Iterator<String> messageIterator = messageFile.iterator();

        // Save the last message index that was read.
        IndexFile indexFile = fsm.createIndexFile(messageFile);
        // Save the messages that produced an error
        ErrorFile errorFile = fsm.createErrorFile();

        try {
            skipMessagesBeforeIndex(messageIterator, indexFile.index());

            while(messageIterator.hasNext()) {
                String message = messageIterator.next();
                try {
                    processMessage(message, fsm);
                } catch (Throwable e) {
                    onMessageError(errorFile, message, fsm);
                }
                indexFile.increment();
            }

            indexFile.close();
            errorFile.close();

        } finally {
            if(indexFile != null) indexFile.save();
            if(errorFile != null) errorFile.close();
        }
    }

    protected abstract void processMessage(String message, FileSessionManager fsm) throws Throwable;

    protected void onMessageError(ErrorFile errorFile, String message, FileSessionManager fsm) {
        errorFile.add(message);
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

    private void skipMessagesBeforeIndex(Iterator<String> messageIterator, long toExclusive) {
        for(long index = 0;index < toExclusive && messageIterator.hasNext();index++)
            messageIterator.next();
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
