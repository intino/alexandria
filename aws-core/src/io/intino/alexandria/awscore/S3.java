package io.intino.alexandria.awscore;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;

import java.util.stream.Stream;


public class S3 {

    public static Stream<String> buckets(AmazonS3 client) {
        return client.listBuckets().stream().map(Bucket::getName);
    }

    public static Stream<String> prefixesIn(AmazonS3 client, String bucketName, String prefix) {
        ListObjectsRequest request = new ListObjectsRequest().withBucketName(bucketName).withPrefix(prefix).withDelimiter("/");
        return client.listObjects(request).getCommonPrefixes().stream();
    }

    public static Stream<String> keysIn(AmazonS3 client, String bucketName, String prefix) {
        return client.listObjects(bucketName, prefix).getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey);
    }

    public static S3Object getObjectFrom(AmazonS3 client, String bucketName, String keyName) {
        return client.getObject(new GetObjectRequest(bucketName, keyName));
    }

    public static Stream<S3Object> getObjectsFrom(AmazonS3 client, String bucketName, String prefix) {
        return keysIn(client, bucketName, prefix).map(key -> getObjectFrom(client, bucketName, key));
    }
}
