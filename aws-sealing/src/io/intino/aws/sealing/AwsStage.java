package io.intino.aws.sealing;

import com.amazonaws.services.s3.AmazonS3;
import io.intino.alexandria.Session;
import io.intino.alexandria.awscore.S3;
import io.intino.alexandria.sealing.Stage;

import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Stream;

import static io.intino.alexandria.event.Event.Format;

public class AwsStage implements Stage {

    private final String bucketName;

    private final AmazonS3 client;
    private final String localStageFolder;

    public AwsStage(String bucketName, AmazonS3 client, String localStageFolder) {
        this.bucketName = bucketName;
        this.client = client;
        this.localStageFolder = localStageFolder;
    }

    @Override
    public Stream<Session> sessions() {
        return files().map(prefix -> new AwsSession(prefix, bucketName, client));
    }

    @Override
    public void push(Stream<Session> sessions) {
        sessions.forEach(session -> push(session, localStageFolder));
    }

    @Override
    public void clear() {
        S3.deleteContent(client, bucketName);
    }

    private Stream<String> files() {
        return S3.keysIn(client, bucketName, "");
    }

    private void push(Session session, String stageFolder) {
        S3.uploadObjectTo(client, bucketName, stageFolder, filename(session));
    }

    private String filename(Session session) {
        return session.name() + "." + session.format() + Session.SessionExtension;
    }

    private boolean isNotTreated(String prefix) {
        return !prefix.endsWith(".treated");
    }

    private static class AwsSession implements Session {

        private static final String AwsDelimeter = "/";
        private final String prefix;
        private final String fileName;
        private final Format format;
        private final String bucketName;
        private final AmazonS3 client;

        public AwsSession(String prefix, String bucketName, AmazonS3 client) {
            this.prefix = prefix;
            this.fileName = fileName();
            this.format = formatOf();
            this.bucketName = bucketName;
            this.client = client;
        }
        @Override
        public String name() {
            return fileName.substring(0, fileName.lastIndexOf("."));
        }

        @Override
        public Format format() {
            return format;
        }

        @Override
        public InputStream inputStream() {
            return S3.getObjectFrom(client, bucketName, prefix).getObjectContent();
        }

        private Format formatOf() {
            return Arrays.stream(Format.values())
                    .filter(format -> fileName.endsWith(extensionOf(format)))
                    .findFirst()
                    .orElse(null);
        }

        private String extensionOf(Format type) {
            return "." + type.name().toLowerCase() + Session.SessionExtension;
        }

        private String fileName(){
            String[] prefixArray = prefix.split(AwsDelimeter);
            return prefixArray[prefixArray.length - 1];
        }
    }
}
