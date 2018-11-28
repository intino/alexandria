package io.intino.alexandria.inl;


import io.intino.alexandria.Resource;
import io.intino.alexandria.ResourceStore;
import io.intino.alexandria.inl.helpers.Fields;
import io.intino.alexandria.inl.helpers.Mapping;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static io.intino.alexandria.inl.helpers.Fields.valueOf;
import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Modifier.isTransient;

public class InlSerializer {
	private final Object object;
	private final String path;
	private final Mapping mapping;
	private ResourceStore resourceStore;

	public static InlSerializer serialize(Object object) {
		return serialize(object, new ResourceStore.Memory());
	}

	public static InlSerializer serialize(Object object, ResourceStore resourceStore) {
		return new InlSerializer(object, "", new Mapping(), resourceStore);
	}

	private InlSerializer(Object object, String path, Mapping mapping, ResourceStore resourceStore) {
		this.object = object;
		this.path = path;
		this.mapping = mapping;
		this.resourceStore = resourceStore;
	}

	public String toInl() {
		return object instanceof List ? toInl((List) object) : header() + body();
	}

	private String toInl(List list) {
		StringBuilder result = new StringBuilder();
		for (Object o : list)
			result.append("\n").append(serialize(o).toInl());
		return result.substring(1);
	}

	private String header() {
		return "[" + type() + "]" + "\n";
	}

	private String type() {
		return path + (path.isEmpty() ? "" : "") + map(className());
	}

	private String map(String text) {
		return mapping.get(text);
	}

	private String body() {
		return serializeAttributes() + serializeComponents();
	}

	private String serializeAttributes() {
		StringBuilder result = new StringBuilder();
		for (Field field : Fields.of(object).asList()) {
			if (isTransient(field.getModifiers())) continue;
			if (isStatic(field.getModifiers())) continue;
			if (!isAttribute(field)) continue;
			Object value = Fields.valueOf(field, this.object);
			if (isEmpty(value)) continue;
			result.append(serializeAttribute(field)).append("\n");
			collectResources(value);
		}
		return result.toString();
	}

	private String serializeComponents() {
		StringBuilder result = new StringBuilder();
		for (Field field : Fields.of(object).asList()) {
			if (isTransient(field.getModifiers())) continue;
			if (isStatic(field.getModifiers())) continue;
			if (isEmpty(Fields.valueOf(field, this.object))) continue;
			if (isAttribute(field)) continue;
			result.append(serializeComponent(field));
		}
		return result.toString();
	}

	private void collectResources(Object value) {
		if (value instanceof Resource) resourceStore.put((Resource) value);
		if (value instanceof Resource[]) Arrays.stream((Resource[]) value).forEach(r -> resourceStore.put(r));
		if (value instanceof List && isResourceList((List) value)) ((List<?>) value).forEach(o -> resourceStore.put((Resource) o));
	}

	private boolean isResourceList(List list) {
		return !list.isEmpty() && list.get(0) instanceof Resource;
	}

	private String serializeAttribute(Field field) {
		return map(field.getName()) + separatorFor(serializeAttributeValue(valueOf(field, this.object)));
	}

	private String serializeAttributeValue(Object value) {
		if (value == null) return "";
		return value instanceof List ? serializeAttributeValue(toArray((List) value)) : formatterOf(value).format(value);
	}

	private String separatorFor(String value) {
		return value.startsWith("\n") ? ":" + value : ": " + value;
	}

	private InlFormatters.Formatter formatterOf(Object value) {
		return InlFormatters.get(value.getClass());
	}

	@SuppressWarnings("unchecked")
	private <T> T[] toArray(List<T> list) {
		T[] result = (T[]) java.lang.reflect.Array.newInstance(list.get(0).getClass(), list.size());
		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}

	private String serializeComponent(Field field) {
		Object object = valueOf(field, this.object);
		if (object == null) return "";
		return isList(field) ? serializeTable((List) object) : serializeItem(object);
	}

	private String serializeTable(List list) {
		StringBuilder result = new StringBuilder();
		for (Object item : list)
			result.append(serializeItem(item));
		return result.toString();
	}

	private String serializeItem(Object value) {
		Class<?> aClass = value.getClass();
		return "\n" + (isPrimitive(aClass) || isArrayOfPrimitives(aClass) ? value.toString() : new InlSerializer(value, type(), mapping, resourceStore).toInl());
	}

	static boolean isAttribute(Field field) {
		Class<?> aClass = field.getType();
		return isPrimitive(aClass) || isArrayOfPrimitives(aClass) || isListOfPrimitives(field);
	}

	static boolean isAttachment(Field field) {
		final Class<?> aClass = field.getType();
		return aClass.equals(Resource.class);
	}

	private static boolean isArrayOfPrimitives(Class<?> aClass) {
		return aClass.isArray() && isPrimitive(aClass.getComponentType());
	}

	private static boolean isListOfPrimitives(Field field) {
		return field.getType().isAssignableFrom(List.class) && isPrimitive(field.getGenericType().toString());
	}

	private static boolean isPrimitive(Class<?> aClass) {
		return isPrimitive(aClass.getName()) || aClass.isPrimitive();
	}


	private static String[] primitives = {"java.lang", Resource.class.getName(), "java.time"};

	private static boolean isPrimitive(String className) {
		if (className.contains("<")) className = className.substring(className.indexOf('<') + 1);
		for (String primitive : primitives) if (className.startsWith(primitive)) return true;
		return false;
	}

	static boolean isEmpty(Object value) {
		return value == null
				|| value.getClass().isArray() && ((Object[]) value).length == 0
				|| value instanceof List && ((List) value).isEmpty();
	}

	private boolean isList(Field field) {
		return field.getType().isAssignableFrom(List.class);
	}

	private String className() {
		return object.getClass().getSimpleName();
	}


	public InlSerializer map(String from, String to) {
		mapping.put(from, to);
		return this;
	}
}
