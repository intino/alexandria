package io.intino.alexandria.ollama.tools;

import io.intino.alexandria.ollama.tools.OllamaFunction.ParamType;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

public class OllamaFunctionParamTypeMapper {

	public static ParamType mapToParamType(Field field) {
		Class<?> fieldType = field.getType();
		if (fieldType.isArray()) {
			return mapType(fieldType.getComponentType()).asCollection();
		} else if (Collection.class.isAssignableFrom(fieldType)) {
			return mapType(getCollectionElementType(field)).asCollection();
		} else {
			return mapType(fieldType);
		}
	}

	private static ParamType mapType(Class<?> type) {
		if (type == String.class) {
			return ParamType.STRING;
		} else if (type == int.class || type == Integer.class) {
			return ParamType.INT;
		} else if (type == long.class || type == Long.class) {
			return ParamType.LONG;
		} else if (type == float.class || type == Float.class) {
			return ParamType.FLOAT;
		} else if (type == double.class || type == Double.class) {
			return ParamType.DOUBLE;
		} else if (type == char.class || type == Character.class) {
			return ParamType.CHAR;
		} else {
			throw new IllegalArgumentException("Unsupported field type: " + type.getName());
		}
	}

	public static Class<?> getCollectionElementType(Field field) {
		if (field.getGenericType() instanceof ParameterizedType parameterizedType) {
			Type[] typeArguments = parameterizedType.getActualTypeArguments();
			if (typeArguments.length == 1) return (Class<?>) typeArguments[0];
		}
		throw new IllegalArgumentException("Field is not parameterized or is not a Collection");
	}
}
