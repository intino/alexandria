package io.intino.konos.server.activity.dialogs;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class Dialog_ {

    @Test
    public void should_create_form_with_input() throws Exception {
        Map<String, Object> paths = new HashMap() {{ put("Campo 1.0", "2"); }};


        Form form = Form.fromMap(null, paths, input -> "text");
    }

}