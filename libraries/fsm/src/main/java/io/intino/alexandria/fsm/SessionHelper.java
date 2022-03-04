package io.intino.alexandria.fsm;

import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.intino.alexandria.fsm.FileSessionManager.MESSAGE_EXTENSION;
import static io.intino.alexandria.fsm.FileSessionManager.TEMP_EXTENSION;

public class SessionHelper {

    private static final DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SS");

    public static File newTempSessionFile(File pendingDir, LocalDateTime ts, String name) {
        return new File(pendingDir, format(ts) + "." + name + MESSAGE_EXTENSION + TEMP_EXTENSION);
    }

    public static LocalDateTime dateTimeOf(File sessionFile) {
        try {
            String name = sessionFile.getName();
            String ts = name.substring(0, name.indexOf('.'));
            return LocalDateTime.parse(ts, Formatter);
        } catch (Throwable e) {
            Logger.error(e);
        }
        return null;
    }

    private static String format(LocalDateTime ts) {
        return Formatter.format(ts);
    }
}
