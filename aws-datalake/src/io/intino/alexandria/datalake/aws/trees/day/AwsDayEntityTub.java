package io.intino.alexandria.datalake.aws.trees.day;

import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake.EntityStore.Triplet;
import io.intino.alexandria.datalake.Datalake.EntityStore.Tub;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class AwsDayEntityTub implements Tub {
    private final S3Object object;

    public AwsDayEntityTub(S3Object object) {
        this.object = object;
    }

    public S3Object object() {
        return object;
    }

    @Override
    public Timetag timetag() {
        return null;
    }

    @Override
    public Scale scale() {
        return null;
    }

    @Override
    public Stream<Triplet> triplets() {
        return null;
    }

    @Override
    public Stream<Triplet> triplets(Predicate<Triplet> filter) {
        return null;
    }
}
