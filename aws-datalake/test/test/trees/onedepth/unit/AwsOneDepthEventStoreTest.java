package test.trees.onedepth.unit;

import io.intino.alexandria.datalake.Datalake.EventStore.Tank;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthEventStore;
import io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthEventTank;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthDatalake.PREFIX_DELIMITER;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AwsOneDepthEventStoreTest {

    private S3 s3Mock;

    @Before
    public void setUp(){
        s3Mock = mock(S3.class);mockS3();
    }

    private void mockS3() {
        when(s3Mock.keysIn("", "events_"))
                .thenReturn(Stream.of( "events_ps.Anomaly_20220101/1.zim", "events_Test_20230101/1.zim"));
    }

    @Test
    public void should_test_tank() {
        String tankName = "ps.Anomaly";
        assert equalsTanks(new AwsOneDepthEventStore(s3Mock, "").tank(tankName),
                new AwsOneDepthEventTank(s3Mock, "", tankPrefix("events_", tankName)));
    }

    @Test
    public void should_test_tanks() {
        assertStreamEquals(new AwsOneDepthEventStore(s3Mock, "").tanks(),
                Stream.of(new AwsOneDepthEventTank(s3Mock, "", "events_ps.Anomaly_"),
                        new AwsOneDepthEventTank(s3Mock, "", "events_Test_")));
    }

    private void assertStreamEquals(Stream<Tank> tanks, Stream<Tank> expectedTanks) {
        Iterator<Tank> tanksIterator = tanks.iterator();
        Iterator<Tank> expectedTanksIterator = expectedTanks.iterator();
        while(tanksIterator.hasNext() && expectedTanksIterator.hasNext())
            assert equalsTanks(tanksIterator.next(), expectedTanksIterator.next());
        assert !tanksIterator.hasNext() && !expectedTanksIterator.hasNext();
    }

    private boolean equalsTanks(Object a, Object b) {
        if(a == null || b == null) return false;
        if(a.getClass() != b.getClass()) return false;
        AwsOneDepthEventTank tankA = (AwsOneDepthEventTank) a;
        AwsOneDepthEventTank tankB = (AwsOneDepthEventTank) b;
        return Objects.equals(tankA.name(), tankB.name());
    }

    private static String tankPrefix(String prefix, String tankName) {
        return prefix + tankName + PREFIX_DELIMITER;
    }
}
