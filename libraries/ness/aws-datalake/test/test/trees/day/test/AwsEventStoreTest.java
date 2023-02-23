package test.trees.day.test;

import com.amazonaws.services.s3.AmazonS3;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.Datalake.EventStore.Tank;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.datalake.aws.trees.day.AwsDayDatalake;
import io.intino.alexandria.datalake.aws.trees.day.AwsDayEntityTank;
import io.intino.alexandria.datalake.aws.trees.day.AwsDayEventTank;
import org.junit.Test;
import test.MyClient;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class AwsEventStoreTest {
    static AmazonS3 client = MyClient.s3Client;
    static Datalake datalake = new AwsDayDatalake(S3.with(client));

    @Test
    public void should_return_all_event_tanks() {
        List<String> tanks = datalake.eventStore().tanks().map(Tank::name).collect(Collectors.toList());
        List<String> list = List.of("ps.Anomaly", "ps.Diagnostic", "ps.EnergyConsumption");
        assertEquals(tanks, list);
    }

    @Test
    public void should_return_specific_entity_tank() {
        assertEquals(new AwsDayEventTank("ps.Anomaly", S3.with(MyClient.s3Client)).name(), datalake.entityStore().tank("ps.Anomaly").name());
    }
}
