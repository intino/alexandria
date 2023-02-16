package test.trees.day;

import com.amazonaws.services.s3.AmazonS3;
import io.intino.alexandria.datalake.Datalake.EntityStore.Tank;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.datalake.aws.trees.day.AwsDayDatalake;
import io.intino.alexandria.datalake.aws.trees.day.AwsDayEntityTank;
import org.junit.Test;
import test.MyClient;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class AwsEntityStoreTest {
    static AmazonS3 client = MyClient.s3Client;

    @Test
    public void should_return_all_entity_tanks() {
        AwsDayDatalake datalake = new AwsDayDatalake(S3.with(client));
        List<String> tanks = datalake.entityStore().tanks().map(Tank::name).collect(Collectors.toList());
        List<String> list = List.of("User");
        assertEquals(tanks, list);
    }

    @Test
    public void should_return_specific_entity_tank() {
        AwsDayDatalake datalake = new AwsDayDatalake(S3.with(client));
        assertEquals(new AwsDayEntityTank("User", S3.with(client)).name(), datalake.entityStore().tank("User").name());
    }
}
