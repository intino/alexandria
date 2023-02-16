package benchmarks;

import io.intino.alexandria.datalake.aws.S3;
import org.openjdk.jmh.annotations.*;
import test.MyClient;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    @Benchmark
    public void get_multiple_objects_from_bucket() {
        s3.getObjectsFrom("datalakejosejuan", "datalake/events/ps.Anomaly/");
    }
}
