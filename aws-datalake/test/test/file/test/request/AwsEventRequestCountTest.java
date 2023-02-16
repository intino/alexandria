package file.test.request;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.aws.file.AwsEventStore;
import io.intino.alexandria.datalake.aws.file.AwsEventTank;
import io.intino.alexandria.datalake.aws.S3;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.DefaultMockingDetails;
import org.mockito.invocation.InvocationOnMock;

import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AwsEventRequestCountTest {

    private static final String BucketName = "datalake";
    private S3 s3Mock;

    @Before
    public void setUp() {
        s3Mock = mock(S3.class);
        createStubsMethodS3();
    }

    @Test
    public void AwsEventTank_tanks_count_test() {
        new AwsEventStore(s3Mock).tanks();
        System.out.println("Request nedeed: " + numberOfRequest());
    }


    @Test
    public void AwsEventTank_on_request_count_test() {
        new AwsEventTank(s3Mock, BucketName, "datalake/events/ps.Anomaly").on(new Timetag("20221114"));
        System.out.println("Request nedeed: " + numberOfRequest());
    }

    @Test
    public void AwsEventTank_tubs_request_count_test() {
        new AwsEventTank(s3Mock, BucketName, "datalake/events/ps.Anomaly").tubs();
        System.out.println("Request nedeed: " + numberOfRequest());
    }

    @Test
    public void AwsEventTank_keys_count_test() {
        new AwsEventTank(s3Mock, BucketName, "datalake/events/ps.Anomaly").last();
        System.out.println("Request nedeed: " + numberOfRequest());
    }

    private void createStubsMethodS3() {
        when(s3Mock.keysIn(BucketName, "datalake/events/ps.Anomaly"))
                .thenReturn(Stream.of("datalake/events/ps.Anomaly/20221114.zim"));
    }

    private int numberOfRequest() {
        return new DefaultMockingDetails(s3Mock).getInvocations().size();
    }

    private int numberOfRequest(String method) {
        return Math.toIntExact(new DefaultMockingDetails(s3Mock).getInvocations()
                .stream()
                .map(InvocationOnMock::getMethod)
                .filter(s -> s.getName().equals(method))
                .count());
    }
}
