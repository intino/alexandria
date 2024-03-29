package io.intino.alexandria.ui.resources;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Asset {
    public Asset() {
    }

    public static InputStream getAsStream(String name) {
        return Asset.class.getResourceAsStream(name);
    }

    public static boolean exists(String name) {
        return Asset.class.getResource(name) != null;
    }

    public static String getFullPath(String name) {
        String directory = Asset.class.getResource(name).getPath();
        return directory.charAt(directory.length() - 1) == 47?directory.substring(0, directory.length() - 1):directory;
    }

    public static Resource toResource(final URL baseRoute, final String resource) {
        return new Resource() {
            private String result = baseRoute.toString() + "/" + Asset.encode(resource);

            public Resource setContentType(String contentType) {
                if(contentType == null) {
                    return this;
                } else {
                    this.result = this.result + Asset.concatEncoded(this.result, "contentType", contentType);
                    return this;
                }
            }

            public Resource setLabel(String label) {
                if(label == null) {
                    return this;
                } else {
                    this.result = this.result + Asset.concat(this.result, "label", label);
                    return this;
                }
            }

            @Override
            public Resource setEmbedded(boolean embedded) {
                if(!embedded) {
                    return this;
                } else {
                    this.result = this.result + Asset.concat(this.result, "embedded", "true");
                    return this;
                }
            }

            public URL toUrl() {
                try {
                    return new URL(this.result);
                } catch (MalformedURLException var2) {
                    return null;
                }
            }
        };
    }

    public static Resource toResource(final URL baseRoute, final URL resource) {
        return toResource(baseRoute, resource.toString());
    }

    private static String concatEncoded(String url, String name, String value) {
        return concat(url, name, encode(value));
    }

    private static String concat(String url, String name, String value) {
        return url.indexOf("?") != -1?"&":"?" + name + "=" + value;
    }

    public static String encode(String content) {
        return URLEncoder.encode(new String(Base64.getEncoder().encode(content.getBytes())), StandardCharsets.UTF_8).replace("%2F", "_sls_");
    }

    public static String decode(String content) {
        return new String(Base64.getDecoder().decode(URLDecoder.decode(content.replace("_sls_", "%2F"), StandardCharsets.UTF_8)));
    }

    public interface Resource {
        Resource setContentType(String contentType);
        Resource setLabel(String label);
        Resource setEmbedded(boolean embedded);
        URL toUrl();
    }
}