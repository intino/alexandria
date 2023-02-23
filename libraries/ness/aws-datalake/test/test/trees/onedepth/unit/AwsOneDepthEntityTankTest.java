package test.trees.onedepth.unit;

import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthEntityTank;
import io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthEntityTub;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthDatalake.AWS_DELIMITER;
import static io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthDatalake.FILE_NAME;
import static io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthEntityStore.ENTITY_EXTENSION;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AwsOneDepthEntityTankTest {

    private S3 s3Mock;

    @Before
    public void setUp(){
        s3Mock = mock(S3.class);
    }

    private void mockS3(String prefix) {
        when(s3Mock.getObjectFrom("", prefix))
                .thenReturn(getS3Object(prefix));
        when(s3Mock.keysIn("", "entities_User_"))
                .thenReturn(Stream.of("entities_User_20220101/1.triples", "entities_User_20220102/1.triples"));
    }

    @Test
    public void should_test_name() {
        assertEquals(new AwsOneDepthEntityTank(s3Mock, "", "entities_User_").name(),
                "User");
    }

    @Test
    public void should_test_on() {
        Timetag timetag = new Timetag("20220101");
        String prefix = "entities_User_" + timetag + AWS_DELIMITER + FILE_NAME + ENTITY_EXTENSION;
        mockS3(prefix);
        assert equalsTubs(new AwsOneDepthEntityTub(s3Mock, "", prefix),
                new AwsOneDepthEntityTank(s3Mock, "", "entities_User_").on(timetag));

    }

    @Test
    public void should_test_tubs() {
        String prefix = "entities_User_20220101" + AWS_DELIMITER + FILE_NAME + ENTITY_EXTENSION;
        mockS3(prefix);
        assert equalsTubs(new AwsOneDepthEntityTank(s3Mock, "", "entities_User_").tubs()
                .findFirst().get(),
                new AwsOneDepthEntityTub(s3Mock, "", prefix));
    }

    private S3Object getS3Object(String prefix) {
        S3Object s3Object = new S3Object();
        s3Object.setKey(prefix);
        return s3Object;
    }

    private boolean equalsTubs(Object a, Object b) {
        if(a == null || b == null) return false;
        if(a.getClass() != b.getClass()) return false;
        AwsOneDepthEntityTub tubA = (AwsOneDepthEntityTub) a;
        AwsOneDepthEntityTub tubB = (AwsOneDepthEntityTub) b;
        return Objects.equals(tubA.timetag(), tubB.timetag());
    }
}
