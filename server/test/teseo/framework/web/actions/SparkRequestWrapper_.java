package teseo.framework.web.actions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import teseo.framework.actions.RequestAdapter;
import teseo.framework.web.actions.common.AdapterProxy;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import spark.Request;
import spark.Session;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static teseo.framework.web.actions.SparkWrapper.wrap;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SparkRequestWrapper_ {

	private static AdapterProxy adapter;

	@BeforeClass
	public static void setupClass() {
		adapter = new AdapterProxy()
				.registerAdapter("person", new PersonAdapter())
				.registerAdapter("personList", new PersonAdapter());
	}

	@Test
	public void should_map_parameter_from_query_params_to_interface_method() {
		Request request = request();
		when(request.queryParams("message")).thenReturn("Hello world");
		assertThat(input(request).message(), is("Hello world"));
	}

	@Test
	public void should_map_parameter_from_url_to_interface_method() {
		Request request = request();
		when(request.params("message")).thenReturn("Hello world");
		assertThat(input(request).message(), is("Hello world"));
	}

	@Test
	public void should_get_session_id_from_request() {
		Request request = request();
		Session session = mock(Session.class);
		when(session.id()).thenReturn("sessionId");
		when(request.session()).thenReturn(session);
		assertThat(input(request).sessionId(), is("sessionId"));
	}

	@Test
    @Ignore("// TODO DELETE OR MAINTAIN")
	public void should_use_adapter_to_map_json_string_to_object() {
		Request request = request();
		when(request.params("person")).thenReturn("{\"id\":\"id\",\"name\":\"name\"}");
		assertThat(input(request).person(), is(new Person("id", "name")));
	}

	@Test
    @Ignore("// TODO DELETE OR MAINTAIN")
	public void should_use_list_adapter_when_method_name_ends_with_List() {
		Request request = request();
		when(request.params("personList")).thenReturn("[{\"id\":\"id\",\"name\":\"name\"},{\"id\":\"id2\",\"name\":\"name2\"}]");
		assertThat(input(request).personList(), is(asList(new Person("id", "name"), new Person("id2", "name2"))));
	}

	private InputMessage input(Request request) {
		return SparkWrapper.wrap(request).with(adapter).as(InputMessage.class);
	}

	private Request request() {
		Request request = mock(Request.class);
		when(request.raw()).thenReturn(mock(HttpServletRequest.class));
		return request;
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
			return name.equals(((Person) o).name) && id.equals(((Person) o).id);
		}
	}

	public static class PersonAdapter implements RequestAdapter {

		private final JsonParser parser = new JsonParser();

		@SuppressWarnings("unchecked")
		@Override
		public Person adapt(String value) {
			JsonObject content = parser.parse(value).getAsJsonObject();
			return new Person(getString(content, "id"), getString(content, "name"));
		}

		@Override
		public List<Person> adaptList(String value) {
			JsonArray array = parser.parse(value).getAsJsonArray();
			List<Person> result = new ArrayList<>();
			array.forEach(element -> result.add(adapt(element.toString())));
			return result;
		}

		private String getString(JsonObject content, String member) {
			return content.get(member).getAsString();
		}
	}

	public interface InputMessage extends teseo.framework.web.actions.common.InputMessage {
		Person person();
		List<Person> personList();
	}
}
