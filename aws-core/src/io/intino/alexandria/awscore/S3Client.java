package io.intino.alexandria.awscore;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class S3Client {
    public static AmazonS3 with(String publicKey, String secretKey) {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(credentials(publicKey, secretKey))
                .withRegion(Regions.EU_WEST_3)
                .build();
    }

    private static AWSStaticCredentialsProvider credentials(String publicKey, String secretKey) {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(publicKey, secretKey));
    }
}
