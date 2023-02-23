package io.intino.alexandria.datalake.aws.trees.onedepth;

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

import static io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthDatalake.AWS_DELIMITER;
import static io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthDatalake.PREFIX_DELIMITER;

public class AwsOneDepthEntityTub implements Tub {

    public static final String TRIPLES_DELIMITER = "\t";
    private final S3 s3;
    private final String bucketName;
    private final String prefix;

    public AwsOneDepthEntityTub(S3 s3, String bucketName, String prefix) {
        this.s3 = s3;
        this.bucketName = bucketName;
        this.prefix = prefix;
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
        try(Stream<String> lines = new BufferedReader(new InputStreamReader(object().getObjectContent())).lines()) {
            return lines
                    .parallel()
                    .filter(line -> !line.isEmpty())
                    .map(AwsOneDepthEntityTub::triplet)
                    .collect(Collectors.toList()).stream();
        }
    }

    @Override
    public Stream<Triplet> triplets(Predicate<Triplet> filter) {
        return triplets().filter(filter);
    }

    private String name() {
        String[] route = prefix.split(AWS_DELIMITER)[0].split(PREFIX_DELIMITER);
        return route[route.length - 1];
    }

    private static Triplet triplet(String line) {
        return new Triplet(line.split(TRIPLES_DELIMITER, -1));
    }

    private S3Object object() {
        return s3.getObjectFrom(bucketName, prefix);
    }
}
