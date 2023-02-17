package io.intino.alexandria.datalake.aws.trees.day;

import io.intino.alexandria.datalake.aws.S3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.Datalake.EntityStore;
import static io.intino.alexandria.datalake.aws.trees.day.AwsDayDatalake.*;
import static java.util.concurrent.Executors.newFixedThreadPool;

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
        try {
            List<String> result = new ArrayList<>();
            processTank(result);
            return result.stream().distinct().map(route -> new AwsDayEntityTank(name(route), s3));
        } catch (InterruptedException e) { throw new RuntimeException(e); }
    }

    private void processTank(List<String> result) throws InterruptedException {
        ExecutorService service = newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (String bucket : buckets())
            service.submit(() -> result.addAll(iterateOver(bucket)));
        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);
    }

    @Override
    public Tank tank(String s) {
        return new AwsDayEntityTank(s, s3);
    }

    private List<String> iterateOver(String bucket) throws InterruptedException {
        ArrayList<String> result = new ArrayList<>();
        processTank(bucket, result);
        return result;
    }

    private void processTank(String bucket, ArrayList<String> result) throws InterruptedException {
        ExecutorService service = newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 1; i <= months; i++) {
            int finalI = i;
            service.submit(() -> result.addAll(s3.prefixesIn(bucket, key(finalI)).collect(Collectors.toList())));
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);
    }

    private static String key(int i) {
        return i + AwsDelimiter + "entities" + AwsDelimiter;
    }

    private static String name(String str) {
        return str.split(AwsDelimiter)[2];
    }
}
