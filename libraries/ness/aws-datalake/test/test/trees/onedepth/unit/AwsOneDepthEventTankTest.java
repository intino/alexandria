package test.trees.onedepth.unit;


import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthEntityTank;
import io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthEventTank;
import io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthEventTub;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthDatalake.AWS_DELIMITER;
import static io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthDatalake.FILE_NAME;
import static io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthEventStore.EVENT_EXTENSION;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AwsOneDepthEventTankTest {

    private S3 s3Mock;

    @Before
    public void setUp(){
        s3Mock = mock(S3.class);
    }

    @Test
    public void should_test_name() {
        assertEquals(new AwsOneDepthEntityTank(s3Mock, "", "events_ps.Anomaly_").name(),
                "ps.Anomaly");
    }

    @Test
    public void should_test_on() {
        Timetag timetag = new Timetag("20220101");
        String prefix = "events_ps.Anomaly_" + timetag + AWS_DELIMITER + FILE_NAME + EVENT_EXTENSION;
        mockS3(prefix);
        assert equalsTubs(new AwsOneDepthEventTub(s3Mock, "", prefix),
                new AwsOneDepthEventTank(s3Mock, "", "events_ps.Anomaly_").on(timetag));
    }

    @Test
    public void should_test_tubs() {
        String prefix = "events_ps.Anomaly_20220101" + AWS_DELIMITER + FILE_NAME + EVENT_EXTENSION;
        mockS3(prefix);
        assert equalsTubs(new AwsOneDepthEventTank(s3Mock, "", "events_ps.Anomaly_")
                        .tubs().findFirst().get(),
                new AwsOneDepthEventTub(s3Mock, "", prefix));
    }

    private void mockS3(String prefix) {
        when(s3Mock.getObjectFrom("", prefix))
                .thenReturn(getS3Object(prefix));
        when(s3Mock.keysIn("", "events_ps.Anomaly_"))
                .thenReturn(Stream.of("events_ps.Anomaly_20220101/1.zim", "events_ps.Anomaly_20220102/1.zim"));
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
