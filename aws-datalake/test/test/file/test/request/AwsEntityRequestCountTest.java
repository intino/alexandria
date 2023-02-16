package test.file.test.request;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.aws.file.AwsEntityStore;
import io.intino.alexandria.datalake.aws.file.AwsEntityTank;
import io.intino.alexandria.datalake.aws.S3;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.DefaultMockingDetails;
import org.mockito.invocation.InvocationOnMock;

import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AwsEntityRequestCountTest {
    private static final String BucketName = "datalake";
    private S3 s3Mock;

    @Before
    public void setUp() {
        s3Mock = mock(S3.class);
        createStubsMethodS3();
    }

    private void createStubsMethodS3() {
        when(s3Mock.keysIn(BucketName, "datalake/entites/User"))
                .thenReturn(Stream.of("datalake/entities/User/20230101"));
    }

    @Test
    public void AwsEntityStore_tanks_count_test() {
        new AwsEntityStore(s3Mock, "", "").tanks();
        System.out.println("Request nedeed: " + numberOfRequest());
    }

    @Test
    public void AwsEntityTank_tubs_count_test() {
        new AwsEntityTank(s3Mock, BucketName, "datalake/entities/User/").tubs();
        System.out.println("Request nedeed: " + numberOfRequest());
    }

    @Test
    public void AwsEntityTank_on_count_test() {
        new AwsEntityTank(s3Mock, BucketName, "datalake/entities/User").on(new Timetag("20230101"));
        System.out.println("Request nedeed: " + numberOfRequest());
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
