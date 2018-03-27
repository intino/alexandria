package io.intino.konos.alexandria.schema;


import io.intino.konos.alexandria.schema.Formatters.Formatter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static io.intino.konos.alexandria.schema.Accessory.valueOf;
import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Modifier.isTransient;

public class Serializer {
	private final Object object;
	private final String path;
	private final Accessory.Mapping mapping;
	private ResourceStore resourceStore;

	public static Serializer serialize(Object object) {
		return serialize(object, ResourceStore.collector());
	}

	public static Serializer serialize(Object object, ResourceStore resourceStore) {
		return new Serializer(object, "", new Accessory.Mapping(), resourceStore);
	}

	private Serializer(Object object, String path, Accessory.Mapping mapping, ResourceStore resourceStore) {
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
		return path + (path.isEmpty() ? "" : ".") + map(className());
	}

	private String map(String text) {
		return mapping.get(text);
	}

	private String body() {
		return serializeAttributes() + serializeComponents();
	}

	private String serializeAttributes() {
		StringBuilder result = new StringBuilder();
		for (Field field : Accessory.fieldsOf(object).asList()) {
			if (isTransient(field.getModifiers())) continue;
			if (isStatic(field.getModifiers())) continue;
			if (!isAttribute(field)) continue;
			Object value = valueOf(field, this.object);
			if (isEmpty(value)) continue;
			result.append(serializeAttribute(field)).append("\n");
			collectResources(value);
		}
		return result.toString();
	}

	private String serializeComponents() {
		StringBuilder result = new StringBuilder();
		for (Field field : Accessory.fieldsOf(object).asList()) {
			if (isTransient(field.getModifiers())) continue;
			if (isStatic(field.getModifiers())) continue;
			if (isEmpty(valueOf(field, this.object))) continue;
			if (isAttribute(field)) continue;
			result.append(serializeComponent(field));
		}
		return result.toString();
	}

	private void collectResources(Object value) {
		if (value instanceof Resource) resourceStore.add((Resource) value);
		if (value instanceof Resource[]) Arrays.stream((Resource[]) value).forEach(r -> resourceStore.add(r));
		if (value instanceof List && isResourceList((List) value)) ((List<?>) value).forEach(o -> resourceStore.add((Resource) o));
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

	private Formatter formatterOf(Object value) {
		return Formatters.get(value.getClass());
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
		return "\n" + (isPrimitive(aClass) || isArrayOfPrimitives(aClass) ? value.toString() : new Serializer(value, type(), mapping, resourceStore).toInl());
	}

	static boolean isAttribute(Field field) {
		Class<?> aClass = field.getType();
		return isPrimitive(aClass) || isArrayOfPrimitives(aClass) || isListOfPrimitives(field);
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


	public Serializer map(String from, String to) {
		mapping.put(from, to);
		return this;
	}
}
