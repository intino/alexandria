package test.file.test;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.aws.file.AwsEntityStore;
import io.intino.alexandria.datalake.aws.file.AwsEntityTank;
import io.intino.alexandria.datalake.aws.S3;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class AwsEntityIntegrationTest {
    private static final String BucketName = "datalake";
    private S3 s3Mock;

    @Before
    public void setUp() {
        s3Mock = mock(S3.class);
    }

    @Test
    public void AwsEntityStore_tanks_count_test() {
        new AwsEntityStore(s3Mock, "", "").tanks();
        verify(s3Mock, times(1)).prefixesIn(BucketName, "datalake/entities/");
    }

    @Test
    public void AwsEntityTank_tubs_count_test() {
        new AwsEntityTank(s3Mock, BucketName, "datalake/entities/User/").tubs();
        verify(s3Mock, times(1)).keysIn(BucketName, "datalake/entities/User/");
    }

    @Test
    public void AwsEntityTank_on_count_test() {
        new AwsEntityTank(s3Mock, BucketName, "datalake/entities/User").on(new Timetag("20230101"));
        verify(s3Mock, times(1)).getObjectFrom(BucketName, "datalake/entities/User/20230101.triples");
    }
}
