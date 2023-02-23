package test.facade.benchmarks;

import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.zim.ZimReader;
import org.openjdk.jmh.annotations.*;
import test.MyClient;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Fork(value = 1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 5, time = 1)
public class AwsS3TimeBenchmark {
    private final static S3 s3 = S3.with(MyClient.s3Client);

    @Benchmark
    public void get_one_single_object_from_bucket() {
        s3.getObjectFrom("datalakejosejuan", "datalake/events/ps.Anomaly/20221107.zim");
    }

    @Benchmark
    public void get_keys_in_prefix() {
        s3.keysIn("datalakejosejuan", "datalake/events/ps.Anomaly/");
    }

    @Benchmark
    public void get_prefixes_in_prefix() {
        s3.prefixesIn("datalakejosejuan", "datalake/events/").collect(Collectors.toList());
    }

    public static void main(String[] args) {
        S3 s3 = S3.with(MyClient.s3Client);
        AtomicInteger count = new AtomicInteger();
        Stream<S3Object> stream = s3.getObjectsFrom("datalakejosejuan", "datalake/events/ps.Anomaly/");
        stream.parallel().map(o-> new ZimReader(o.getObjectContent())).forEach(e-> e.forEachRemaining(ev-> count.incrementAndGet()));
        System.out.println(count.get());
    }
}
