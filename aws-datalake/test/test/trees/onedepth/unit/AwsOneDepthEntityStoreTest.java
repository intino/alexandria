package test.trees.onedepth.unit;

import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthEntityStore;
import io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthEntityTank;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthDatalake.PREFIX_DELIMITER;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AwsOneDepthEntityStoreTest {

    private S3 s3Mock;

    @Before
    public void setUp(){
        s3Mock = mock(S3.class);
        mockS3();
    }

    private void mockS3() {
        when(s3Mock.keysIn("", "entities_"))
                .thenReturn(Stream.of("entities_User_20220101/1.triples", "entities_User_20230101/1.triples"));
    }

    @Test
    public void should_test_tank() {
        String tankName = "User";
        assert equalsTanks(new AwsOneDepthEntityStore(s3Mock, "").tank(tankName),
                new AwsOneDepthEntityTank(s3Mock, "", tankPrefix("entities_", tankName)));
    }

    @Test
    public void should_test_tanks() {
        assert equalsTanks(new AwsOneDepthEntityStore(s3Mock, "").tanks().findFirst().get(),
                new AwsOneDepthEntityTank(s3Mock, "", "entities_User_"));
    }

    private static String tankPrefix(String prefix, String tankName) {
        return prefix + tankName + PREFIX_DELIMITER;
    }

    private boolean equalsTanks(Object a, Object b) {
        if(a == null || b == null) return false;
        if(a.getClass() != b.getClass()) return false;
        AwsOneDepthEntityTank tankA = (AwsOneDepthEntityTank) a;
        AwsOneDepthEntityTank tankB = (AwsOneDepthEntityTank) b;
        return Objects.equals(tankA.name(), tankB.name());
    }
}
