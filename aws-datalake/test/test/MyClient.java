package test;

import com.amazonaws.services.s3.AmazonS3;
import io.intino.alexandria.datalake.aws.S3Client;

import java.io.*;

public class MyClient {
    private static final String PUBLIC_KEY = publicKey();
    private static final String PRIVATE_KEY = privateKey();
    public static final AmazonS3 s3Client = S3Client.with(PUBLIC_KEY, PRIVATE_KEY);

    private static String publicKey() {
        try {
            BufferedReader reader = getReader();
            return reader.readLine();
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    private static String privateKey() {
        try {
            BufferedReader reader = getReader();
            reader.readLine();
            return reader.readLine();
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    private static BufferedReader getReader() {
        InputStream stream = MyClient.class.getResourceAsStream("/resource/credentials.txt");
        assert stream != null;
        return new BufferedReader(new InputStreamReader(stream));
    }
}