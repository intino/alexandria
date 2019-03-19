package io.intino.alexandria.ui.displays.molds;

import io.intino.alexandria.schemas.Event;
import io.intino.alexandria.schemas.Method;
import io.intino.alexandria.schemas.MethodParameter;
import io.intino.alexandria.schemas.Property;

import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class Model {
	private static Map<WidgetType, List<Property>> propertiesMap = new HashMap<>();
	private static Map<WidgetType, List<Method>> methodsMap = new HashMap<>();
	private static Map<WidgetType, List<Event>> eventsMap = new HashMap<>();

	public enum WidgetType {
		Text, Image, Date
	}

	static {
		initialize();
	}

	private static void initialize() {
		propertiesMap.put(WidgetType.Text, new ArrayList<Property>() {{
			add(property("format", Property.Type.Word, "used to...", "H1", "H2", "H3", "H4", "H5", "H6", "Subtitle1", "Subtitle2", "Body1", "Body2", "Button", "Caption", "Overline", "Default"));
			add(property("mode", Property.Type.Word, "used to...", "Uppercase", "Lowercase", "Capitalize", "Normal"));
			add(property("value", Property.Type.Text, "default value for component", ""));
		}});
		propertiesMap.put(WidgetType.Date, new ArrayList<Property>() {{
			add(property("format", Property.Type.Text, "used to...", "dd/MM/YYYY"));
			add(property("mode", Property.Type.Word, "used to...", "FromNow", "ToNow"));
		}});
		methodsMap.put(WidgetType.Date, new ArrayList<Method>() {{
			add(method("get", emptyList(), "returns ....", "java.time.Instant"));
			add(method("update", singletonList(methodParameter("instant", "java.time.Instant")), "updates ...", "void"));
		}});
	}

	public static List<Property> properties(WidgetType type) {
		if (!propertiesMap.containsKey(type)) return emptyList();
		return propertiesMap.get(type);
	}

	public static List<Method> methods(WidgetType type) {
		if (!methodsMap.containsKey(type)) return emptyList();
		return methodsMap.get(type);
	}

	public static List<Event> events(WidgetType type) {
		if (!eventsMap.containsKey(type)) return emptyList();
		return eventsMap.get(type);
	}

	private static Property property(String name, Property.Type type, String description, String... values) {
		return new Property().name(name).type(type).description(description).values(Arrays.asList(values));
	}

	private static Method method(String name, List<MethodParameter> params, String description, String returnType) {
		return new Method().name(name).params(params).description(description).returnType(returnType);
	}

	private static MethodParameter methodParameter(String name, String type) {
		return new MethodParameter().name(name).type(type);
	}

}
