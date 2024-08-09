package io.intino.alexandria.ollama.tools;

import io.intino.alexandria.Json;

import java.lang.reflect.AccessFlag;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.Map;
import java.util.function.Function;

public class OllamaToolCallFunction {

	private String name;
	private Map<String, Object> arguments;
	private transient Class<?> binding;

	public OllamaToolCallFunction() {
	}

	public OllamaToolCallFunction(String name, Map<String, Object> arguments) {
		this.name = name;
		this.arguments = arguments;
	}

	public OllamaToolCallFunction(String name, Map<String, Object> arguments, Class<?> binding) {
		this.name = name;
		this.arguments = arguments;
		this.binding(binding);
	}

	public String name() {
		return name;
	}

	public OllamaToolCallFunction name(String name) {
		this.name = name;
		return this;
	}

	public Map<String, Object> arguments() {
		return arguments;
	}

	public OllamaToolCallFunction arguments(Map<String, Object> arguments) {
		this.arguments = arguments;
		return this;
	}

	public <T> T binding() throws Exception {
		return binding(BindingParser.getDefaultParser());
	}

	public <T> T binding(BindingParser<T> parser) throws Exception {
		return parser.parse(bindingClass(), arguments);
	}

	@SuppressWarnings("unchecked")
	public <T> Class<T> bindingClass() {
		return (Class<T>) binding;
	}

	public OllamaToolCallFunction binding(Class<?> binding) {
		if(binding != null) {
			if(binding.getAnnotation(OllamaFunction.Binding.class) == null) {
				throw new IllegalArgumentException("Binding object must be annotated with OllamaFunction.Binding annotation");
			}
		}
		this.binding = binding;
		return this;
	}

	@Override
	public String toString() {
		return Json.toJson(this);
	}

	public interface BindingParser<T> {

		T parse(Class<T> type, Map<String, Object> arguments) throws BindingParseException;

		static <T> BindingParser<T> getDefaultParser() {
			return getDefaultParser(FieldParser.getDefault());
		}

		static <T> BindingParser<T> getDefaultParser(FieldParser fieldParser) {
			return (type, arguments) -> {
				try {
					if(type == null) return null;

					Constructor<T> constructor;
					try {
						constructor = type.getDeclaredConstructor();
						constructor.setAccessible(true);
					} catch (NoSuchMethodException | InaccessibleObjectException e) {
						throw new RuntimeException("Binding objects need to define an accessible constructor with no parameters", e);
					}

					T instance = constructor.newInstance();

					for(Field field : type.getDeclaredFields()) {
						boolean wasPrivate = field.accessFlags().contains(AccessFlag.PRIVATE);
						try {
							field.setAccessible(true);
							OllamaFunction.Param param = field.getAnnotation(OllamaFunction.Param.class);
							if(param == null) continue;

							String name = param.name().isBlank() ? field.getName() : param.name();

							if(!arguments.containsKey(name)) {
								if(param.required()) throw new BadToolCallException("The parameter " + name + " was specified as required but is not set");
								continue;
							}

							Object value = arguments.get(name);

							if(value != null) {
								field.set(instance, fieldParser.parse(name, value, field, param));
							} else {
								field.set(instance, null);
							}

						} finally {
							if(wasPrivate) field.setAccessible(false);
						}
					}

					return instance;
				} catch (BindingParseException e) {
					throw e;
				} catch (Exception e) {
					throw new BindingParseException(e);
				}
			};
		}

		interface FieldParser {

			Object parse(String name, Object rawValue, Field field, OllamaFunction.Param paramDefinition) throws BindingParseException;

			static FieldParser getDefault() {
				return (name, rawValue, field, paramDefinition) -> {
					try {
						boolean isArray = field.getType().isArray();
						OllamaFunction.ParamType type = OllamaFunctionParamTypeMapper.mapToParamType(field);
						return switch(type) {
							case STRING -> String.valueOf(rawValue);
							case INT -> parseInt(rawValue);
							case LONG -> parseLong(rawValue);
							case FLOAT -> parseFloat(rawValue);
							case DOUBLE -> parseDouble(rawValue);
							case CHAR -> parseChar(rawValue);
							case SHORT -> parseShort(rawValue);
							case BYTE -> parseByte(rawValue);
							default -> {
								if(isArray) yield parseArray(field.getType(), rawValue);
								yield parseCollection(field.getType(), rawValue);
							}
						};
					} catch (Exception e) {
						throw new BindingParseException("Error while parsing field " + name + " from the raw value '"
								+ rawValue + "' to " + field.getType() + ": " + e.getMessage(), e);
					}
				};
			}

			static Object parseArray(Class<?> type, Object rawValue) {
				return Json.fromJson(rawValue instanceof CharSequence ? rawValue.toString() : Json.toJson(rawValue), type);
			}

			static Object parseCollection(Class<?> type, Object rawValue) {
				return Json.fromJson(rawValue instanceof CharSequence ? rawValue.toString() : Json.toJson(rawValue), type);
			}

			private static Character parseChar(Object rawValue) {
				String s = String.valueOf(rawValue);
				return s.isEmpty() ? 0 : s.charAt(0);
			}

			private static Double parseDouble(Object rawValue) {
				return rawValue instanceof Number x ? x.doubleValue() : parseHandlingNull(rawValue, Double::parseDouble);
			}

			private static Float parseFloat(Object rawValue) {
				return rawValue instanceof Number x ? x.floatValue() : parseHandlingNull(rawValue, Float::parseFloat);
			}

			private static Long parseLong(Object rawValue) {
				return rawValue instanceof Number x ? x.longValue() : parseHandlingNull(rawValue, Long::parseLong);
			}

			private static Integer parseInt(Object rawValue) {
				return rawValue instanceof Number x ? x.intValue() : parseHandlingNull(rawValue, Integer::parseInt);
			}

			private static Short parseShort(Object rawValue) {
				return rawValue instanceof Number x ? x.shortValue() : parseHandlingNull(rawValue, Short::parseShort);
			}

			private static Byte parseByte(Object rawValue) {
				return rawValue instanceof Number x ? x.byteValue() : parseHandlingNull(rawValue, Byte::parseByte);
			}

			private static <T extends Number> T parseHandlingNull(Object rawValue, Function<String, T> parser) {
				if(rawValue == null) return null;
				String s = String.valueOf(rawValue).trim();
				if(s.isEmpty() || s.equalsIgnoreCase("null") || s.equalsIgnoreCase("none")) return null;
				return parser.apply(s);
			}
		}
	}
}
