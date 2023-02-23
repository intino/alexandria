package test.trees.onedepth.integration;

import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthDatalake;
import io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthEventTub;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthDatalake.*;
import static io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthEventStore.EVENT_EXTENSION;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AwsOneDepthEventTest {
    private S3 s3Mock;

    @Before
    public void setUp(){
        s3Mock = mock(S3.class);
    }

    private void mockS3(String prefix) {
        when(s3Mock.getObjectFrom("", prefix))
                .thenReturn(getS3Object(prefix));
    }


    @Test
    public void should_test_entities_on_function() {
        String tank = "ps.Anomaly";
        Timetag timeTag = new Timetag("20220101");
        String prefix = "events" + PREFIX_DELIMITER + tank + PREFIX_DELIMITER + timeTag + AWS_DELIMITER + FILE_NAME + EVENT_EXTENSION;
        mockS3(prefix);
        assert equalsTubs(new AwsOneDepthDatalake(s3Mock, "").eventStore().tank(tank).on(timeTag),
                new AwsOneDepthEventTub(s3Mock, "", prefix));

    }

    private S3Object getS3Object(String prefix) {
        S3Object s3Object = new S3Object();
        s3Object.setKey(prefix);
        return s3Object;
    }

    private boolean equalsTubs(Object a, Object b) {
        if(a == null || b == null) return false;
        if(a.getClass() != b.getClass()) return false;
        AwsOneDepthEventTub tubA = (AwsOneDepthEventTub) a;
        AwsOneDepthEventTub tubB = (AwsOneDepthEventTub) b;
        return Objects.equals(tubA.timetag(), tubB.timetag());
    }
}
