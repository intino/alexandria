package test.file.test;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.aws.file.AwsEventStore;
import io.intino.alexandria.datalake.aws.file.AwsEventTank;
import io.intino.alexandria.datalake.aws.S3;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;

public class AwsEventIntegrationTest {

    private static final String BucketName = "datalake";
    private S3 s3Mock;

    @Before
    public void setUp() {
        s3Mock = mock(S3.class);
        createStubsMethodS3();
    }

    @Test
    public void AwsEventTank_tanks_count_test() {
        new AwsEventStore(s3Mock, "", "").tanks();
        verify(s3Mock, times(1)).prefixesIn(BucketName, "datalake/events/");
    }

    @Test
    public void AwsEventTank_tubs_count_test() {
        new AwsEventTank(s3Mock, BucketName, "datalake/events/ps.Anomaly").tubs();
        verify(s3Mock, times(1)).keysIn(BucketName, "datalake/events/ps.Anomaly");
    }

    @Test
    public void AwsEventTank_on_count_test() {
        new AwsEventTank(s3Mock, BucketName, "datalake/events/ps.Anomaly").on(new Timetag("20221114"));
        verify(s3Mock, times(1)).keysIn(BucketName, "datalake/events/ps.Anomaly");
        verify(s3Mock, times(1)).getObjectFrom(BucketName, "datalake/events/ps.Anomaly/20221114.zim");
    }

    @Test
    public void AwsEventTank_last_count_test() {
        new AwsEventTank(s3Mock, BucketName, "datalake/events/ps.Anomaly").last();
        verify(s3Mock, times(1)).keysIn(BucketName, "datalake/events/ps.Anomaly");
        verify(s3Mock, times(1)).getObjectFrom(BucketName, "datalake/events/ps.Anomaly/20221114.zim");
    }


    private void createStubsMethodS3() {
        when(s3Mock.keysIn(BucketName, "datalake/events/ps.Anomaly"))
                .thenReturn(Stream.of("datalake/events/ps.Anomaly/20221114.zim"));
    }
}
