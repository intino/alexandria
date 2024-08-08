package io.intino.alexandria.ollama.tools;

import com.google.gson.annotations.SerializedName;
import io.intino.alexandria.Json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class OllamaFunction {

	public static OllamaFunction from(Class<?> binding) {
		OllamaFunction function = new OllamaFunction();
		function.binding(binding);

		Binding definition = binding.getAnnotation(Binding.class);
		function.name(definition.name().isBlank() ? binding.getSimpleName() : definition.name());
		function.description(definition.description());

		for(Field field : binding.getDeclaredFields()) {
			field.setAccessible(true);
			Param param = field.getAnnotation(Param.class);
			if(param == null) continue;

			String name = param.name().isBlank() ? field.getName() : param.name();
			String type = param.type().isBlank() ? OllamaFunctionParamTypeMapper.mapToParamType(field).label() : param.type();
			function.parameter(name, type, param.description(), param.required(), param.enumValues());
		}

		return function;
	}

	private String name;
	private String description;
	private Parameters parameters = new Parameters();
	private transient Class<?> binding;

	public OllamaFunction() {
	}

	public OllamaFunction(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public OllamaFunction(String name, String description, Parameters parameters) {
		this.name = name;
		this.description = description;
		this.parameters = parameters;
	}

	public OllamaFunction(String name, String description, Parameters parameters, Class<?> binding) {
		this.name = name;
		this.description = description;
		this.parameters = parameters;
		this.binding(binding);
	}

	public String name() {
		return name;
	}

	public OllamaFunction name(String name) {
		this.name = name;
		return this;
	}

	public String description() {
		return description;
	}

	public OllamaFunction description(String description) {
		this.description = description;
		return this;
	}

	public OllamaFunction parameter(String name, String type, String description) {
		return parameter(name, type, description, false);
	}

	public OllamaFunction parameter(String name, String type, String description, boolean required) {
		this.parameters.properties.put(name, new Parameters.Property(type, description));
		if(required) this.parameters.required.add(name);
		return this;
	}

	public OllamaFunction parameter(String name, String type, String description, String... enumValues) {
		return parameter(name, type, description, false, enumValues);
	}

	public OllamaFunction parameter(String name, String type, String description, boolean required, String... enumValues) {
		this.parameters.properties.put(name, new Parameters.Property(type, description, Arrays.stream(enumValues).collect(Collectors.toSet())));
		if(required) parameters.required.add(name);
		return this;
	}

	public Parameters parameters() {
		return parameters;
	}

	public OllamaFunction parameters(Parameters parameters) {
		this.parameters = parameters;
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T> Class<T> binding() {
		return (Class<T>) binding;
	}

	public OllamaFunction binding(Class<?> binding) {
		if(binding != null) {
			if(binding.getAnnotation(Binding.class) == null) {
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

	public static class Parameters {

		private String type = "object";
		private Properties properties = new Properties();
		private Set<String> required = new LinkedHashSet<>();

		public Parameters() {
		}

		public String type() {
			return type;
		}

		public Parameters type(String type) {
			this.type = type;
			return this;
		}

		public Properties properties() {
			return properties;
		}

		public Parameters properties(Properties properties) {
			this.properties = properties;
			return this;
		}

		public Set<String> required() {
			return required;
		}

		public Parameters required(Set<String> required) {
			this.required = required;
			return this;
		}

		@Override
		public String toString() {
			return Json.toJson(this);
		}

		public static class Properties extends LinkedHashMap<String, Property> {}

		public static class Property {

			private String type;
			private String description;
			@SerializedName("enum")
			private LinkedHashSet<String> enumValues;

			public Property() {
			}

			public Property(String type, String description) {
				this.type = type;
				this.description = description;
			}

			public Property(String type, String description, Collection<String> enumValues) {
				this.type = type;
				this.description = description;
				this.enumValues = new LinkedHashSet<>(enumValues);
			}

			public String typeAsString() {
				return type;
			}

			public ParamType type() {
				return type == null ? null : ParamType.from(type);
			}

			public Property type(String type) {
				this.type = type;
				return this;
			}

			public Property type(ParamType type) {
				return this.type(type == null ? null : type.label());
			}

			public String description() {
				return description;
			}

			public Property description(String description) {
				this.description = description;
				return this;
			}

			public Set<String> enumValues() {
				return enumValues;
			}

			public Property enumValues(Collection<String> enumValues) {
				this.enumValues = new LinkedHashSet<>(enumValues);
				return this;
			}

			@Override
			public String toString() {
				return Json.toJson(this);
			}
		}
	}

	public enum ParamType {

		STRING("string"),
		INT("int"),
		LONG("long"),
		FLOAT("float"),
		DOUBLE("double"),
		CHAR("char"),
		STRING_COLLECTION("[string]"),
		INT_COLLECTION("[int]"),
		LONG_COLLECTION("[long]"),
		FLOAT_COLLECTION("[float]"),
		DOUBLE_COLLECTION("[double]"),
		CHAR_COLLECTION("[char]");

		private final String label;
		private final boolean isCollection;

		ParamType(String label) {
			this.label = label;
			this.isCollection = label.startsWith("[");
		}

		public String label() {
			return label;
		}

		public boolean isCollection() {
			return isCollection;
		}

		public ParamType asCollection() {
			if(isCollection) return this;
			return ParamType.from("[" + label + "]");
		}

		public ParamType asNonArray() {
			if(!isCollection) return this;
			return ParamType.from(label.substring(1, label.length()-1));
		}

		public static ParamType from(String nameOrLabel) {
			return Arrays.stream(values())
					.filter(p -> p.name().equalsIgnoreCase(nameOrLabel) || p.label().equalsIgnoreCase(nameOrLabel))
					.findFirst().orElse(null);
		}
	}

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Binding {
		String name() default "";
		String description() default "";
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Param {
		String name() default "";
		String type() default "";
		String description() default "";
		boolean required() default true;
		String[] enumValues() default {};
	}
}
