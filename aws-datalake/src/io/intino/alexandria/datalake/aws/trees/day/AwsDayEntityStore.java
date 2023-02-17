package io.intino.alexandria.datalake.aws.trees.day;

import io.intino.alexandria.datalake.aws.S3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.Datalake.EntityStore;
import static io.intino.alexandria.datalake.aws.trees.day.AwsDayDatalake.*;

public class AwsDayEntityStore implements EntityStore {
    private final S3 s3;

    private AwsDayEntityStore(S3 s3) {
        this.s3 = s3;
    }

    public static EntityStore with(S3 s3) {
        return new AwsDayEntityStore(s3);
    }

    @Override
    public Stream<Tank> tanks() {
        List<String> result = new ArrayList<>();
        for (String bucket : buckets())
            result.addAll(iterateOver(bucket));
        return result.stream().distinct().map(str -> new AwsDayEntityTank(str.split(AwsDelimiter)[2], s3));
    }

    private List<String> iterateOver(String bucket) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 1; i <= 12; i++)
            result.addAll(s3.prefixesIn(bucket, i + AwsDelimiter + "entities" + AwsDelimiter)
                    .collect(Collectors.toList()));
        return result;
    }

    @Override
    public Tank tank(String s) {
        return new AwsDayEntityTank(s, s3);
    }
}
