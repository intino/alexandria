package io.intino.alexandria.datalake.aws.trees.tank;

import io.intino.alexandria.datalake.aws.S3;

import java.util.stream.Stream;

import static io.intino.alexandria.datalake.Datalake.EventStore;
import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;

public class AwsEventStore implements EventStore {
    private final S3 s3;

    public AwsEventStore(S3 s3) {
        this.s3 = s3;
    }

    @Override
    public Stream<Tank> tanks() {
        return s3.buckets().filter(bucket -> bucket.startsWith("event")).map(bucket -> new AwsEventTank(s3, bucket));
    }

    @Override
    public Tank tank(String name) {
        return new AwsEventTank(s3, toBucketName(name));
    }

    private String toBucketName(String text) {
        return "event-" + toKebabCase(text);
    }

    private String toKebabCase(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (isStartOfWord(text, i)) sb.append("-");
            sb.append(toLowerCase(text.charAt(i)));
        }
        return sb.toString();
    }

    private boolean isStartOfWord(String text, int i) {
        return isUpperCase(text.charAt(i)) && !isDotPrevCharacter(text, i);
    }

    private boolean isDotPrevCharacter(String text, int i) {
        if (isFirstLetter(i)) return false;
        return text.charAt(i - 1) == '.';
    }

    private boolean isFirstLetter(int i) {
        return i == 0;
    }
}
