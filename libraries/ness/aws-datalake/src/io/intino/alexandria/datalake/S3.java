package io.intino.alexandria.datalake;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;

import java.util.stream.Stream;


public class S3 {
    private final AmazonS3 client;

    private S3(AmazonS3 client) {
        this.client = client;
    }

    public static S3 with(AmazonS3 s3) {
        return new S3(s3);
    }

    public Stream<String> buckets() {
        return client.listBuckets().stream().map(Bucket::getName);
    }

    public Stream<String> prefixesIn(String bucketName, String prefix) {
        ListObjectsRequest request = new ListObjectsRequest().withBucketName(bucketName).withPrefix(prefix).withDelimiter("AwsDelimiter");
        return client.listObjects(request).getCommonPrefixes().stream();
    }

    public Stream<String> keysIn(String bucketName, String prefix) {
        return client.listObjects(bucketName, prefix).getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey);
    }

    public S3Object getObjectFrom(String bucketName, String keyName) {
        return client.getObject(new GetObjectRequest(bucketName, keyName));
    }

    public Stream<S3Object> getObjectsFrom(String bucketName, String prefix) {
        return keysIn(bucketName, prefix).map(key -> getObjectFrom(bucketName, key));
    }
}
