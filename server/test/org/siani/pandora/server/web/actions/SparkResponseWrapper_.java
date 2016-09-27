package org.siani.pandora.server.web.actions;

import com.google.gson.JsonObject;
import org.siani.pandora.server.actions.ResponseAdapter;
import org.siani.pandora.server.web.actions.common.AdapterProxy;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Response;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.siani.pandora.server.web.actions.SparkWrapper.wrap;
import static org.mockito.Mockito.*;

public class SparkResponseWrapper_ {

    private static AdapterProxy adapterProxy;

    @BeforeClass
    public static void setUpClass() {
        adapterProxy = new AdapterProxy().registerAdapter("person", new PersonAdapter());
    }

    @Test
    public void should_write_string_into_response() throws IOException {
        PrintWriter writer = mock(PrintWriter.class);
        inputFor(response(writer)).write("Hello world");
        verify(writer).println("Hello world");
    }

    @Test
    public void should_write_string_into_response_using_adapter() throws IOException {
        PrintWriter writer = mock(PrintWriter.class);
        inputFor(response(writer)).person(new Person("id", "name"));
        verify(writer).println("{\"id\":\"id\",\"name\":\"name\"}");
    }

    private OutputMessage inputFor(Response response) throws IOException {
        return SparkWrapper.wrap(response).with(adapterProxy).as(OutputMessage.class);
    }

    private Response response(PrintWriter writer) throws IOException {
        Response response = mock(Response.class);
        HttpServletResponse servletResponse = mock(HttpServletResponse.class);
        when(servletResponse.getWriter()).thenReturn(writer);
        when(response.raw()).thenReturn(servletResponse);
        return response;
    }

    public static class Person {
        private final String id;
        private final String name;

        public Person(String id, String name) {
            this.id = id;
            this.name = name;
        }

        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        @Override
        public boolean equals(Object o) {
            return name.equals(((Person)o).name) && id.equals(((Person)o).id);
        }
    }

    public static class PersonAdapter implements ResponseAdapter<Person> {
        @Override
        public String adapt(Person person) {
            JsonObject object = new JsonObject();
            object.addProperty("id", person.id);
            object.addProperty("name", person.name);
            return object.toString();
        }

        @Override
        public String adaptList(List<Person> value) {
            return "";
        }
    }

    public interface OutputMessage extends org.siani.pandora.server.web.actions.common.OutputMessage {
        String person(Person person);
    }
}
