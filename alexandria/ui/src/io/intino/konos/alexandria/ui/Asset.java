package io.intino.konos.alexandria.ui;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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

    public static Asset.Resource toResource(final URL baseRoute, final String resource) {
        return new Asset.Resource() {
            private String result = baseRoute.toString() + "/" + Asset.encode(resource);

            public Asset.Resource setContentType(String contentType) {
                if(contentType == null) {
                    return this;
                } else {
                    this.result = this.result + Asset.concatEncoded(this.result, "contentType", contentType);
                    return this;
                }
            }

            public Asset.Resource setLabel(String label) {
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

    public static Asset.Resource toResource(final URL baseRoute, final URL resource) {
        return toResource(baseRoute, resource.toString());
    }

    private static String concatEncoded(String url, String name, String value) {
        return concat(url, name, encode(value));
    }

    private static String concat(String url, String name, String value) {
        return url.indexOf("?") != -1?"&":"?" + name + "=" + value;
    }

    private static String encode(String content) {
        return new String(Base64.getEncoder().encode(content.getBytes()));
    }

    public interface Resource {
        Asset.Resource setContentType(String contentType);
        Asset.Resource setLabel(String label);
        Asset.Resource setEmbedded(boolean embedded);
        URL toUrl();
    }
}