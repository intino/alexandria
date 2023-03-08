package io.intino.alexandria.datalake.aws.trees.tank;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.aws.S3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.Datalake.EntityStore.Triplet;
import static io.intino.alexandria.datalake.Datalake.EntityStore.Tub;
import static io.intino.alexandria.datalake.aws.trees.tank.AwsDatalake.KeyDelimiter;

public class AwsEntityTub implements Tub {
    public static final String EntityExtension = ".triples";
    public static final String TripletsDelimiter = "\t";
    private final S3 s3;
    private final String bucketName;
    private final String key;

    public AwsEntityTub(S3 s3, String bucketName, String key) {
        this.s3 = s3;
        this.bucketName = bucketName;
        this.key = key;
    }

    @Override
    public Timetag timetag() {
        return new Timetag(name());
    }

    @Override
    public Scale scale() {
        return timetag().scale();
    }

    @Override
    public Stream<Triplet> triplets() {
        try(Stream<String> lines = new BufferedReader(new InputStreamReader(s3.getObjectFrom(bucketName, key).getObjectContent())).lines()) {
            return lines.filter(line -> !line.isEmpty()).map(AwsEntityTub::triplet).collect(Collectors.toList()).stream();
        }
    }

    @Override
    public Stream<Triplet> triplets(Predicate<Triplet> filter) {
        return triplets().filter(filter);
    }

    private String name() {
        String[] route = key.split(KeyDelimiter);
        return route[route.length - 1].replace(EntityExtension, "");
    }

    private static Triplet triplet(String line) {
        return new Triplet(line.split(TripletsDelimiter, -1));
    }
}
