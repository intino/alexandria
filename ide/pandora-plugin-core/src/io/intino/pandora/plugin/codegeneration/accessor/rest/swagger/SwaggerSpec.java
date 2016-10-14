package io.intino.pandora.plugin.codegeneration.accessor.rest.swagger;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SwaggerSpec {

	public String swagger = "2.0";
	public String host;
	public String basePath;
	public List<String> schemas;
	public List<String> consumes = Collections.singletonList("application/json");
	public List<String> produces = Collections.singletonList("application/json");
	public Info info;
	public Map<String, Path> paths;
	public Map<String, Definition> definitions;


	public static class Info {

		public String version;
		public String title;
		public String description;
		public String termsOfService;
		public Contact contact;
		public Licence licence;

		public static class Contact {
			public String name;
			public String email;
			public String url;
		}

		public static class Licence {
			public String name;
			public String url;

		}
	}

	public static class Path {
		public Operation get;
		public Operation post;
		public Operation put;
		public Operation delete;
		public Operation head;
		public Operation patch;

		public static class Operation {
			public List<String> tags;
			public String summary;
			public String description;
			public String operationId;
			public List<String> consumes;
			public List<String> produces;
			public List<String> schemes;
			public boolean deprecated;
			public List<Parameter> parameters;
			public Map<String, Response> responses = new LinkedHashMap<>();

			public static class Parameter {
				public String name;
				public String in;
				public String description;
				public boolean required;
				public String type;
				public String format;
				public Map<String, String> items;
				public String collectionFormat;
			}

			public static class Response {
				public String description;
				public Schema schema;
				public List<Header> headers;
				public List<Example> examples;

				public static class Schema {
					public String type;
					public String $ref;
				}

				public static class Header {
					public String description;
					public String type;
					public String format;
					public Map<String, String> items;
					public String collectionFormat;

				}

				public static class Example {

				}
			}
		}
	}

	public static class Definition {
		public String type;
		public List<String> required;
		public Map<String, Property> properties;

		public static class Property {
			public String type;
			public String $ref;
			public String format;
			public int minimum;
			public int maximum;
		}
	}


}
