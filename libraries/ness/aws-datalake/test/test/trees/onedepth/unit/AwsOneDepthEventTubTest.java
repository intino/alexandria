package test.trees.onedepth.unit;

import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthEventTub;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AwsOneDepthEventTubTest {

    private final static String FILE_NAME = "";
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
    public void should_test_timetag() {
        String timeTag = "20230101";
        String prefix = "events_ps.Anomaly_" + timeTag + "/event_test.triples";
        assertEquals(new AwsOneDepthEventTub(s3Mock, "", prefix).timetag(), new Timetag(timeTag));
    }

    @Test
    public void should_test_event() {

    }

    private S3Object getS3Object(String prefix) {
        S3Object s3Object = new S3Object();
        s3Object.setKey(prefix);
        s3Object.setObjectContent(readFile());
        return s3Object;
    }

    private InputStream readFile() {
        return AwsOneDepthEntityTubTest.class.getResourceAsStream("resource/" + FILE_NAME);
    }
}
