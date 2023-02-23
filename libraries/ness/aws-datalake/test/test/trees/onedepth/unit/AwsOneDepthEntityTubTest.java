package test.trees.onedepth.unit;

import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.Datalake.EntityStore.Triplet;
import io.intino.alexandria.datalake.aws.S3;
import io.intino.alexandria.datalake.aws.trees.onedepth.AwsOneDepthEntityTub;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AwsOneDepthEntityTubTest {

    private static final String FILE_NAME = "entity_test.triples";
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
        String prefix = "entities_User_" + timeTag + "/entity_test.triples";
        assertEquals(new AwsOneDepthEntityTub(s3Mock, "", prefix).timetag(), new Timetag(timeTag));
    }

    @Test
    public void should_check_triplets() {
        String prefix = "entities_User_20230101/1.triples";
        mockS3(prefix);
        assertStreamEquals(new AwsOneDepthEntityTub(s3Mock, "", prefix).triplets(), expectedTriplets());
    }

    private S3Object getS3Object(String prefix) {
        S3Object s3Object = new S3Object();
        s3Object.setKey(prefix);
        s3Object.setObjectContent(readFile());
        return s3Object;
    }

    private Stream<Triplet> expectedTriplets() {
        return new BufferedReader(new InputStreamReader(readFile())).lines()
                .filter(line -> !line.isEmpty())
                .map(line -> new Triplet(line.split("\t", -1)));
    }

    private InputStream readFile() {
        return AwsOneDepthEntityTubTest.class.getResourceAsStream("resource/" + FILE_NAME);
    }

    private void assertStreamEquals(Stream<Triplet> triplets, Stream<Triplet> expectedTriples) {
        Iterator<Triplet> tripletIterator = triplets.iterator();
        Iterator<Triplet> expectedTripletsIterator = expectedTriples.iterator();
        while(tripletIterator.hasNext() && expectedTripletsIterator.hasNext())
            assertThat(tripletIterator.next().equals(expectedTripletsIterator.next()));
        assert !tripletIterator.hasNext() && !expectedTripletsIterator.hasNext();
    }
}
