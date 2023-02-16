package file.test.unit;

import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.datalake.aws.*;
import io.intino.alexandria.datalake.aws.file.AwsEventStore;
import io.intino.alexandria.datalake.aws.file.AwsEventTank;
import io.intino.alexandria.datalake.aws.file.AwsEventTub;
import io.intino.alexandria.event.EventReader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AwsEventTest {
    private static final String BucketName = "datalake";
    private static final String FileName = "20221107.zim";

    private S3 s3Mock;
    private AwsEventTub eventTubMock;
    private S3Object s3ObjectMock;

    @Before
    public void setUp() {
        s3Mock = mock(S3.class);
        s3ObjectMock = initializeS3Object();
        eventTubMock = mock(AwsEventTub.class);
        setS3StubsMethods();
        setEventTubStubsMethods();
    }

    private void setS3StubsMethods() {
        when(s3Mock.prefixesIn(BucketName, "datalake/events/"))
                .thenReturn(Stream.of("datalake/events/ps.Anomaly", "datalake/events/ps.Diagnostic", "datalake/events/ps.EnergyConsumption"));
        when(s3Mock.keysIn(BucketName, "datalake/events/ps.Anomaly"))
                .thenReturn(Stream.of("datalake/events/ps.Anomaly/20221107.zim"));
        when(s3Mock.getObjectFrom(BucketName, "datalake/events/ps.Anomaly/20221107.zim"))
                .thenReturn(s3ObjectMock);
    }

    private void setEventTubStubsMethods() {
        when(eventTubMock.events()).thenReturn(new EventReader(readFile()));
    }

    @Test
    public void should_check_event_tank() {
        assertEquals(new AwsEventStore(s3Mock).tanks().findFirst().get(),
                new AwsEventTank(s3Mock, BucketName, "datalake/events/ps.Anomaly"));
        verify(s3Mock).prefixesIn(BucketName, "datalake/events/");
    }

    @Test
    public void should_check_event_tub() {
        AwsEventTub tub = (AwsEventTub) new AwsEventTank(s3Mock, BucketName, "datalake/events/ps.Anomaly").first();
        assertEquals(tub, new AwsEventTub(s3ObjectMock));
        verify(s3Mock).keysIn(BucketName, "datalake/events/ps.Anomaly");
        verify(s3Mock).getObjectFrom(BucketName, "datalake/events/ps.Anomaly/20221107.zim");
    }

    @Test
    public void should_check_events_of_tub() {
        assertThat(eventTubMock.events().equals(new EventReader(readFile())));
        verify(eventTubMock).events();
    }

    private static S3Object initializeS3Object() {
        try {
            S3Object object = new S3Object();
            setParameters(object);
            return object;
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    private static void setParameters(S3Object object) throws IOException {
        object.setBucketName(BucketName);
        object.setKey("datalake/events/ps.Anomaly/" + FileName);
        object.setObjectContent(new FileInputStream(readFile()));
    }

    private static File readFile() {
        try {
            return new File(AwsEventTest.class.getClassLoader().getResource(FileName).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
