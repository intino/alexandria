package io.intino.alexandria.datalake.aws.trees.day;

import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake.EventStore.Tank;
import io.intino.alexandria.datalake.Datalake.EventStore.Tub;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.datalake.aws.file.AwsEventTub;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.file.AwsEntityStore.Extension;
import static io.intino.alexandria.datalake.aws.trees.day.AwsDayDatalake.*;
import static java.util.concurrent.Executors.newFixedThreadPool;

public class AwsDayEventTank implements Tank {
    private final String name;
    private final S3 s3;

    public AwsDayEventTank(String name, S3 s3) {
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
            List<Tub> tubs = new ArrayList<>();
            process(tubs);
            return tubs.stream();
        } catch (InterruptedException e) { throw new RuntimeException(e); }
    }

    private void process(List<Tub> tubs) throws InterruptedException {
        ExecutorService service = newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (String bucketName : buckets())
            service.submit(() -> tubs.addAll(getKeysIn(bucketName)));
        service.shutdown();
        service.awaitTermination(2, TimeUnit.MINUTES);
    }

    @Override
    public Scale scale() {
        return first().timetag().scale();
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
        return new AwsEventTub(s3.getObjectFrom(bucketOf(tag.day()), tubNameOf(tag)));
    }

    private List<Tub> getKeysIn(String bucketName) {
        List<Tub> result = new ArrayList<>();
        for (int i = 1; i < months; i++)
            result.addAll(s3.keysIn(bucketName, i + AwsDelimiter + "events" + AwsDelimiter + name + AwsDelimiter)
                    .map(key -> new AwsDayEventTub(bucketName, key, s3)).collect(Collectors.toList()));
        return result;
    }

    private AwsEventTub currentTub() {
        return new AwsEventTub(new S3Object());
    }


    private String bucketOf(int day) {
        for (String bucket : buckets())
            for (String s : bucket.split(AwsBucketDelimiter))
                if (s.equals(String.format("%02d", day))) return bucket;
        return "00";
    }

    private String tubNameOf(Timetag tag) {
        return tag.month() + AwsDelimiter + "entities" + AwsDelimiter + name + AwsDelimiter + tag.value() + Extension;
    }
}
