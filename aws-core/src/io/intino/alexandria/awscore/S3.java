package io.intino.alexandria.awscore;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;

import java.io.File;
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

    public static void uploadObjectTo(AmazonS3 client, String bucketName, String key, String fileName) {
        PutObjectRequest objectRequest = new PutObjectRequest(bucketName, key, new File(fileName));
        client.putObject(objectRequest);
    }

    public static void deleteContent(AmazonS3 client, String bucketName) {
        ObjectListing objectListing = client.listObjects(bucketName);
        while (true) {
            deleteObjectSummary(bucketName, client, objectListing);
            if (!objectListing.isTruncated()) break;
            objectListing = client.listNextBatchOfObjects(objectListing);
        }
    }

    private static void deleteObjectSummary(String bucketName, AmazonS3 client, ObjectListing objectListing) {
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries())
            client.deleteObject(bucketName, objectSummary.getKey());
    }
}
