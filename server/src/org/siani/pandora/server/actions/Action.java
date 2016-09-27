package org.siani.pandora.server.actions;

import org.siani.pandora.Error;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface Action {
    Task task();

    @FunctionalInterface
    interface Task<I extends Input, O extends Output> {

        default void before(I input, O output) {
        }

        void execute(I input, O output);

        default void after(I input, O output) {
        }
    }

    interface Input extends Message {
        String displayId();
        String operation();
        String sessionId();
        String clientId();
        String requestUrl();
        String baseUrl();
        String languageFromUrl();
        String languageFromMetadata();
        Map<String, String> metadata();
        Map<String, String> parameters();
        List<File> files();

        interface File {
            String name();
            int size();
            String contentType();
            InputStream content() throws IOException;
        }
    }

    interface Output extends Message {
        void redirect(URL url);
        void write(String content);
        void write(String content, String contentType);
        void write(File file);
        void write(byte[] content);
        void write(byte[] content, String filename);
        void write(InputStream stream);
        void write(InputStream stream, String filename);
        void error(Error error);
    }

    interface Message {
    }

}
