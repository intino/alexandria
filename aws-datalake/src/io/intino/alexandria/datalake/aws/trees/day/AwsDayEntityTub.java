package io.intino.alexandria.datalake.aws.trees.day;

import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake.EntityStore.Triplet;
import io.intino.alexandria.datalake.Datalake.EntityStore.Tub;
import io.intino.alexandria.datalake.aws.S3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.file.AwsDatalake.AwsDelimiter;

public class AwsDayEntityTub implements Tub {
    public static final String TripletsDelimiter = "\t";
    private final String bucketName;
    private final String key;
    private final S3 s3;

    public AwsDayEntityTub(String bucketName, String key, S3 s3) {
        this.bucketName = bucketName;
        this.key = key;
        this.s3 = s3;
    }


    public S3Object object() {
        return s3.getObjectFrom(bucketName, key);
    }

    @Override
    public Timetag timetag() {
        String[] route = key.split(AwsDelimiter);
        return new Timetag(route[route.length - 1]);
    }

    @Override
    public Scale scale() {
        return timetag().scale();
    }

    @Override
    public Stream<Triplet> triplets() {
        try (Stream<String> lines = new BufferedReader(new InputStreamReader(object().getObjectContent())).lines()) {
            return lines
                    .parallel()
                    .filter(line -> !line.isEmpty())
                    .map(AwsDayEntityTub::triplet)
                    .collect(Collectors.toList()).stream();
        }
    }

    @Override
    public Stream<Triplet> triplets(Predicate<Triplet> filter) {
        return triplets().filter(filter);
    }

    private static Triplet triplet(String line) {
        return new Triplet(line.split(TripletsDelimiter, -1));
    }
}
