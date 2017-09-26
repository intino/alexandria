package io.intino.konos.server.activity.dialogs;

import com.google.gson.GsonBuilder;
import io.intino.konos.server.activity.dialogs.adapters.gson.FormAdapter;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class Form_ {

    @Test
    public void should_create_form_with_input() throws Exception {
        Map<String, Object> paths = new HashMap() {{ put("Campo 1", "10"); }};
        Form form = Form.fromMap(null, paths, input -> "text");

        assertNotNull(form.input("Campo 1"));
        assertEquals(form.input("Campo 1").value().asInteger(), 10);
    }

    @Test
    public void should_create_form_with_multiple_input() throws Exception {
        Map<String, Object> paths = new HashMap() {{ put("Campo 1.0", "10"); put("Campo 1.1", "5"); }};
        Form form = Form.fromMap(null, paths, input -> "text");

        assertNotNull(form.input("Campo 1"));
        assertEquals(form.input("Campo 1").value().asInteger(), 10);
        assertEquals((int)form.input("Campo 1").values().asInteger().get(0), 10);
        assertEquals((int)form.input("Campo 1").values().asInteger().get(1), 5);
    }

    @Test
    public void should_create_form_with_section() throws Exception {
        Map<String, Object> paths = new HashMap() {{ put("Campo 1.Campo 2", "10"); }};
        Form form = Form.fromMap(null, paths, input -> "text");

        assertNotNull(form.input("Campo 1"));
        assertNotNull(((Form.Section)form.input("Campo 1")).input("Campo 2"));
        assertEquals(((Form.Section)form.input("Campo 1")).input("Campo 2").value().asInteger(), 10);
    }

    @Test
    public void should_create_form_with_multiple_section() throws Exception {
        Map<String, Object> paths = new HashMap() {{ put("Campo 1.0.Campo 2", "10"); put("Campo 1.1.Campo 2", "5"); }};
        Form form = Form.fromMap(null, paths, input -> "text");

        assertNotNull(form.input("Campo 1"));
        assertNotNull(form.inputs("Campo 1").get(0));
        assertNotNull(form.inputs("Campo 1").get(1));
        assertEquals(((Form.Section) form.inputs("Campo 1").get(0)).input("Campo 2").value().asInteger(), 10);
        assertEquals(((Form.Section) form.inputs("Campo 1").get(1)).input("Campo 2").value().asInteger(), 5);
    }

    @Test
    public void should_create_form_with_section_and_multiple_input() throws Exception {
        Map<String, Object> paths = new HashMap() {{ put("Campo 1.Campo 2.0", "10"); put("Campo 1.Campo 2.1", "5"); }};
        Form form = Form.fromMap(null, paths, input -> "text");

        assertNotNull(form.input("Campo 1"));
        assertEquals(form.inputs("Campo 1").size(), 1);
        assertNotNull(form.inputs("Campo 1").get(0));
        assertNotNull(((Form.Section) form.input("Campo 1")).input("Campo 2"));
        assertEquals(((Form.Section) form.input("Campo 1")).input("Campo 2").values().asInteger().size(), 2);
    }

    @Test
    public void should_create_form_with_section_and_multiple_section() throws Exception {
        Map<String, Object> paths = new HashMap() {{ put("Campo 1.Campo 2.1.Campo 3", "10"); put("Campo 1.Campo 2.0.Campo 3", "5"); }};
        Form form = Form.fromMap(null, paths, input -> "text");

        assertNotNull(form.input("Campo 1"));
        assertEquals(form.inputs("Campo 1").size(), 1);
        assertNotNull(form.inputs("Campo 1").get(0));
        assertEquals(((Form.Section) form.input("Campo 1")).inputs("Campo 2").size(), 2);
        assertNotNull(((Form.Section) ((Form.Section) form.input("Campo 1")).inputs("Campo 2").get(0)).input("Campo 3"));
        assertEquals(((Form.Section) ((Form.Section) form.input("Campo 1")).inputs("Campo 2").get(0)).input("Campo 3").value().asInteger(), 5);
        assertNotNull(((Form.Section) ((Form.Section) form.input("Campo 1")).inputs("Campo 2").get(1)).input("Campo 3"));
        assertEquals(((Form.Section) ((Form.Section) form.input("Campo 1")).inputs("Campo 2").get(1)).input("Campo 3").value().asInteger(), 10);
    }

    @Test
    public void should_load_single_path() throws Exception {
        Map<String, Object> paths = new HashMap() {{ put("Campo 1", "10"); }};
        Form form = Form.fromMap(null, paths, input -> "text");

        assertNotNull(form.input("Campo 1"));
        assertEquals(form.input("Campo 1").value().asInteger(), 10);
    }

    @Test
    public void should_load_double_path() throws Exception {
        Map<String, Object> paths = new HashMap() {{ put("Campo 1.Campo 2", "10"); }};
        Form form = Form.fromMap(null, paths, input -> "text");

        assertNotNull(form.input("Campo 1.Campo2"));
        assertEquals(form.input("Campo 1.Campo 2").value().asInteger(), 10);
    }

    @Test
    public void should_load_path_with_multiple() throws Exception {
        Map<String, Object> paths = new HashMap() {{ put("Campo 1.Campo 2.0.Campo 3", "2"); }};
        Form form = Form.fromMap(null, paths, input -> "text");

        assertNotNull(form.input("Campo 1.Campo2.0.Campo 3"));
        assertEquals(form.input("Campo 1.Campo 2.0.Campo 3").value().asInteger(), 2);
    }

    @Test
    public void should_remove_single_path() throws Exception {
        Map<String, Object> paths = new HashMap() {{ put("Campo 1", "2"); }};
        Form form = Form.fromMap(null, paths, input -> "text");

        form.unRegister("Campo 1");
        assertNull(form.input("Campo 1"));
    }

    @Test
    public void should_remove_multiple_path() throws Exception {
        Map<String, Object> paths = new HashMap() {{ put("Campo 1.0.Campo 2", "2"); }};
        Form form = Form.fromMap(null, paths, input -> "text");

        form.unRegister("Campo 1.0.Campo 2");
        assertNotNull(form.input("Campo 1.0"));
        assertNull(form.input("Campo 1.0.Campo 2"));
    }

    @Test
    public void should_serialize_to_gson() throws Exception {
        Map<String, Object> paths = new HashMap() {{ put("Campo 1.0.Campo 2", "2"); put("Campo 1.1.Campo 2.0", "3"); put("Campo 1.1.Campo 2.1", "4"); }};
        Form form = Form.fromMap(null, paths, input -> "text");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Form.class, new FormAdapter());
        gsonBuilder.setPrettyPrinting();
        System.out.println(gsonBuilder.create().toJson(form));
    }

}