package io.intino.alexandria.datalake.aws.trees.day;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake.EntityStore.Tank;
import io.intino.alexandria.datalake.Datalake.EntityStore.Tub;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.datalake.aws.file.AwsEntityTub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static io.intino.alexandria.datalake.aws.file.AwsEntityStore.Extension;
import static io.intino.alexandria.datalake.aws.trees.day.AwsDayDatalake.*;
import static java.util.concurrent.Executors.newFixedThreadPool;

public class AwsDayEntityTank implements Tank {
    private final String name;
    private final S3 s3;

    public AwsDayEntityTank(String name, S3 s3) {
        this.name = name;
        this.s3 = s3;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Stream<Tub> tubs() {
        try {
            List<Tub> tubs = Collections.synchronizedList(new ArrayList<>());
            process(tubs);
            return tubs.stream();
        } catch (InterruptedException e) { throw new RuntimeException(e); }
    }

    private void process(List<Tub> tubs) throws InterruptedException {
        ExecutorService service = newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (String bucket : buckets())
            service.submit(() -> tubs.addAll(getKeysIn(bucket)));
        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);
    }

    @Override
    public Tub first() {
        return tubs().findFirst().orElse(currentTub());
    }

    @Override
    public Tub last() {
        List<Tub> tubs = tubs().collect(Collectors.toList());
        return tubs.get(tubs.size() - 1);
    }

    @Override
    public Tub on(Timetag tag) {
        return new AwsEntityTub(s3.getObjectFrom(getBucketOf(tag.day()), tubNameOf(tag)));
    }

    @Override
    public Stream<Tub> tubs(int count) {
        return tubs().limit(count);
    }

    @Override
    public Stream<Tub> tubs(Timetag from, Timetag to) {
        return StreamSupport.stream(from.iterateTo(to).spliterator(), true).map(this::on);
    }

    private List<Tub> getKeysIn(String bucket) {
        List<Tub> result = Collections.synchronizedList(new ArrayList<>());
        for (int i = 1; i < months; i++)
            result.addAll(s3.keysIn(bucket, i + AwsDelimiter + "entities" + AwsDelimiter + name + AwsDelimiter)
                    .map(key -> new AwsDayEntityTub(bucket, key, s3)).collect(Collectors.toList()));
        return result;
    }

    private Tub currentTub() {
        return new AwsDayEntityTub("", "", s3);
    }

    private String getBucketOf(int day) {
        for (String bucket : buckets())
            for (String s : bucket.split(AwsBucketDelimiter))
                if (s.equals(String.format("%02d", day))) return bucket;
        return "00";
    }

    private String tubNameOf(Timetag tag) {
        return tag.month() + AwsDelimiter + "entities" + AwsDelimiter + name + AwsDelimiter + tag.year() + Extension;
    }


}
