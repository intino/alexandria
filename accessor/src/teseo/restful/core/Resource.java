package teseo.restful.core;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Resource {
    private final InputStream content;
    private final String contentType;
    private Map<String, String> parameters;

    public Resource(InputStream content, String contentType) {
        this.content = content;
        this.contentType = contentType;
        this.parameters = new HashMap<>();
    }

    public void addParameter(String name, String value) {
        parameters.put(name, value);
    }

    public InputStream content() {
        return content;
    }

    public String contentType() {
        return contentType;
    }

    public Map<String, String> parameters() {
        return parameters;
    }
}
