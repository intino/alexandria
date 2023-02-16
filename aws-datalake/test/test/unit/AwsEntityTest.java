package test.unit;

import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.aws.*;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.alexandria.datalake.Datalake.EntityStore.Triplet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class AwsEntityTest {
    private static final String BucketName = "datalake";
    private static final String FileName = "20230101.triples";

    private S3 s3Mock;
    private AwsDatalake datalake;
    private S3Object s3ObjectMock;

    @Before
    public void setUp(){
        s3Mock = mock(S3.class);
        s3ObjectMock = initializeS3Object();
        datalake = new AwsDatalake(s3Mock, BucketName);
        setS3StubsMethods();
    }

    private void setS3StubsMethods() {
        when(s3Mock.prefixesIn(BucketName, "datalake/entities/"))
                .thenReturn(Stream.of("datalake/entities/User/"));
        when(s3Mock.keysIn(BucketName, "datalake/entities/User/"))
                .thenReturn(Stream.of("datalake/entities/User/" + FileName));
        when(s3Mock.getObjectFrom(BucketName, "datalake/entities/User/" + FileName))
                .thenReturn(s3ObjectMock);
    }

    @Test
    public void should_check_entity_tank() {
        AwsEntityStore store = new AwsEntityStore(s3Mock, BucketName, "datalake/entities/");
        AwsEntityTank tank = (AwsEntityTank) store.tanks().findFirst().get();
        assertEquals(tank, new AwsEntityTank(s3Mock, BucketName, "datalake/entities/User/"));
        verify(s3Mock, times(1)).prefixesIn(BucketName, "datalake/entities/");
    }

    @Test
    public void should_check_entity_tub() {
        datalake.entityStore().tank("User").tubs().findFirst().get();
        AwsEntityTank tank = new AwsEntityTank(s3Mock, BucketName, "datalake/entities/User/");
        assertEquals(tank.tubs().findFirst().get(), new AwsEntityTub(s3ObjectMock));
    }

    @Test
    public void should_check_triplets() {
        assertStreamEquals(tripletsOn("20230101"), expectedTriplets());
        verify(s3Mock, times(1)).getObjectFrom(BucketName, "datalake/entities/User/" + FileName);
    }

    @Test
    public void should_check_event_tubs_in() {
        assertTrue(new AwsEntityTank(s3Mock, BucketName, "datalake/entities/User/").tubs()
                .collect(Collectors.toList()).get(0).timetag().equals("20230101"));
    }

    private S3Object initializeS3Object() {
        S3Object s3Object = new S3Object();
        setParameters(s3Object);
        return s3Object;
    }

    private void setParameters(S3Object object) {
        object.setBucketName(BucketName);
        object.setKey("datalake/entities/User/" + FileName);
        object.setObjectContent(readFile());
    }

    private void assertStreamEquals(Stream<Triplet> triplets, Stream<Triplet> expectedTriples) {
        Iterator<Triplet> tripletIterator = triplets.iterator();
        Iterator<Triplet> expectedTripletsIterator = expectedTriples.iterator();
        while(tripletIterator.hasNext() && expectedTripletsIterator.hasNext())
            assertThat(tripletIterator.next().equals(expectedTripletsIterator.next()));
        assert !tripletIterator.hasNext() && !expectedTripletsIterator.hasNext();
    }

    private Stream<Triplet> tripletsOn(String tag) {
        return datalake.entityStore().tank("User").on(new Timetag(tag)).triplets();
    }

    private Stream<Triplet> expectedTriplets() {
        return new BufferedReader(new InputStreamReader(readFile())).lines()
                .filter(line -> !line.isEmpty())
                .map(line -> new Triplet(line.split("\t", -1)));
    }

    private InputStream readFile() {
        return AwsEntityTest.class.getResourceAsStream("/resource/" + FileName);
    }
}
