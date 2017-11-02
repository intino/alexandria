package io.intino.alexandria.foundation.activity.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class RequestHelper {
    public RequestHelper() {
    }

    public static URL post(URL url) throws IOException {
        HttpPost post = new HttpPost(url.toString());
        CloseableHttpClient client = HttpClientBuilder.create().build();
        Throwable var3 = null;

        URL var4;
        try {
            var4 = getPostUrl(client.execute(post));
        } catch (Throwable var13) {
            var3 = var13;
            throw var13;
        } finally {
            if(client != null) {
                if(var3 != null) {
                    try {
                        client.close();
                    } catch (Throwable var12) {
                        var3.addSuppressed(var12);
                    }
                } else {
                    client.close();
                }
            }

        }

        return var4;
    }

    private static URL getPostUrl(HttpResponse response) throws IOException {
        return response.getStatusLine().getStatusCode() == 302?toUrl(response.getFirstHeader("Location").getValue().getBytes()):toUrl(toByteArray(response.getEntity().getContent()));
    }

    private static byte[] toByteArray(InputStream content) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        int read;
        while((read = content.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }

        return out.toByteArray();
    }

    private static URL toUrl(byte[] bytes) throws IOException {
        return new URL(new String(bytes));
    }
}
