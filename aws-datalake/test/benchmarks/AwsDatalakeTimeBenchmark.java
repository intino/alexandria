package benchmarks;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.aws.AwsDatalake;
import io.intino.alexandria.datalake.aws.S3;
import org.openjdk.jmh.annotations.*;
import test.MyClient;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Fork(value = 1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 5, time = 1)
public class AwsDatalakeTimeBenchmark {
    private final AwsDatalake datalake = new AwsDatalake(S3.with(MyClient.s3Client), "datalakejosejuan");

    @Benchmark
    public void get_tanks_from_event_store() {
        datalake.eventStore().tanks();
    }

    @Benchmark
    public void get_tanks_from_entity_store() {
        datalake.entityStore().tanks();
    }

    @Benchmark
    public void get_tubs_from_event_tank() {
        datalake.eventStore().tank("ps.Anomaly").tubs();
    }

    @Benchmark
    public void get_tubs_from_entities_tank() {
        datalake.eventStore().tank("User").tubs();
    }

    @Benchmark
    public void get_event_from_tub() {
        AtomicInteger hash = new AtomicInteger(0);
        datalake.eventStore().tank("ps.Anomaly").on(new Timetag("20221223")).events()
                .forEachRemaining(t -> hash.addAndGet(t.hashCode()));
    }

    @Benchmark
    public void get_entity_from_tub() {
        AtomicInteger hash = new AtomicInteger(0);
        datalake.entityStore().tank("User").on(new Timetag("20230101")).triplets()
                .forEach(t -> hash.addAndGet(t.hashCode()));
    }
}
