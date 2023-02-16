package benchmarks;

import io.intino.alexandria.datalake.aws.S3;
import org.junit.jupiter.api.Test;
import test.MyClient;

import java.util.stream.Collectors;

public class AwsS3ResourcesBenchmark {
    private static final S3 s3 = S3.with(MyClient.s3Client);
    // DO NOT TOUCH UNLESS IT IS STRICTLY NECESSARY

    @Test
    public void get_one_single_object_from_bucket() {
        int i = 0;
        while (i < 1000000000) {
            s3.getObjectFrom("datalakejosejuan", "datalake/events/ps.Anomaly/20221107.zim");
            ++i;
        }
    }

    @Test
    public void get_keys_in_prefix() {
        int i = 0;
        while (i < 1000000000) {
            s3.keysIn("datalakejosejuan", "datalake/events/ps.Anomaly/");
            ++i;
        }
    }

    @Test
    public void get_prefixes_in_prefix() {
        int i = 0;
        while (i < 1000000000) {
            s3.prefixesIn("datalakejosejuan", "datalake/events/").collect(Collectors.toList());
            ++i;
        }
    }

    @Test
    public void get_multiple_objects_from_bucket() {
        int i = 0;
        while (i < 1000000000) {
            s3.getObjectsFrom("datalakejosejuan", "datalake/events/ps.Anomaly/");
            ++i;
        }
    }
}
