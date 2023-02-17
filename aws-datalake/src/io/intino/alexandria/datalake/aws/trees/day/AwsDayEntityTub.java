package io.intino.alexandria.datalake.aws.trees.day;

import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake.EntityStore.Triplet;
import io.intino.alexandria.datalake.Datalake.EntityStore.Tub;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AwsDayEntityTub implements Tub {
    public static final String TripletsDelimiter = "\t";
    private final S3Object object;

    public AwsDayEntityTub(S3Object object) {
        this.object = object;
    }

    public S3Object object() {
        return object;
    }

    @Override
    public Timetag timetag() {
        String[] route = object.getKey().split("/");
        return new Timetag(route[route.length - 1]);
    }

    @Override
    public Scale scale() {
        return timetag().scale();
    }

    @Override
    public Stream<Triplet> triplets() {
        try (Stream<String> lines = new BufferedReader(new InputStreamReader(object.getObjectContent())).lines()) {
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
