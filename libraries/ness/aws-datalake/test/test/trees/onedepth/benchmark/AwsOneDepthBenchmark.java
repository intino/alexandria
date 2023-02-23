package test.trees.onedepth.benchmark;


import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthDatalake;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import test.MyClient;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Fork(value = 1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 5, time = 1)
public class AwsOneDepthBenchmark {

    private final AwsOneDepthDatalake datalake = new AwsOneDepthDatalake(S3.with(MyClient.s3Client), "one-depth-bucket");

//    @Benchmark
//    public void should_get_time_needed_to_get_a_event_tub() {
//        String tank = "ps.Anomaly";
//        Timetag timeTag = new Timetag("20221205");
//        datalake.eventStore().tank(tank).on(timeTag);
//    }
//
//    @Benchmark
//    public void should_get_time_needed_to_get_a_entity_tub() {
//        String tank = "User";
//        Timetag timeTag = new Timetag("20230101");
//        datalake.entityStore().tank(tank).on(timeTag);
//    }

    @Benchmark //Este
    public void should_get_time_needed_to_get_all_tanks_on_events() {
        datalake.eventStore().tanks();
    }

    @Benchmark //Este
    public void should_get_time_needed_to_get_all_tanks_on_entity() {
        datalake.entityStore().tanks();
    }

    @Benchmark //Este
    public void should_get_time_needed_to_get_all_events_on_date() {
        AtomicInteger hash = new AtomicInteger(0);
        String tank = "ps.Anomaly";
        Timetag timeTag = new Timetag("20221223");
        datalake.eventStore().tank(tank).on(timeTag).events()
                .forEachRemaining(t -> hash.addAndGet(t.hashCode()));
    }

    @Benchmark //Este
    public void should_get_time_needed_to_get_all_triplets_on_date() {
        AtomicInteger hash = new AtomicInteger(0);
        String tank = "User";
        Timetag timeTag = new Timetag("20230101");
        datalake.entityStore().tank(tank).on(timeTag).triplets()
                .forEach(t -> hash.addAndGet(t.hashCode()));
    }

    @Benchmark
    public void should_get_time_needed_to_get_all_tubs_in_one_tank_on_events() {
        String tank = "ps.Anomaly"; //Este
        datalake.eventStore().tank(tank).tubs();
    }

    @Benchmark
    public void should_get_time_needed_to_get_all_tubs_in_one_tank_on_entity() {
        String tank = "User"; //Este
        datalake.entityStore().tank(tank).tubs();
    }
}
