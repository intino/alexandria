package io.intino.konos.builder.codegeneration.accessor.rest.swagger;

import java.util.*;

public class SwaggerSpec {

	public String swagger = "2.0";
	public Info info;
	public String host;
	public String basePath;
	public List<String> schemes;
	public List<String> consumes = Arrays.asList("text/plain; charset=utf-8", " application/json", "multipart/form-data");
	public List<String> produces = Arrays.asList("text/plain; charset=utf-8", " application/json", "multipart/form-data");
	public Map<String, Path> paths;
	public Map<String, Definition> definitions;


	public static class Info {

		public String version;
		public String title;
		public String description;
		public String termsOfService;
		public Contact contact;
		public License license;

		public Info(String version, String title, String description, String termsOfService, Contact contact, License license) {
			this.version = version.isEmpty() ? null : version;
			this.title = title.isEmpty() ? null : title;
			this.description = description.isEmpty() ? null : description;
			this.termsOfService = termsOfService.isEmpty() ? null : termsOfService;
			this.contact = contact;
			this.license = license;
		}

		public static class Contact {
			public String name;
			public String email;
			public String url;

			public Contact(String name, String email, String url) {
				this.name = name.isEmpty() ? null : name;
				this.email = email.isEmpty() ? null : email;
				this.url = url.isEmpty() ? null : url;
			}
		}

		public static class License {
			public String name;
			public String url;

			public License(String name, String url) {
				this.name = name.isEmpty() ? null : name;
				this.url = url.isEmpty() ? null : url;
			}
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
			public String summary;
			public String description;
			public String operationId;
			public List<String> tags;
			public List<String> consumes = Arrays.asList("text/plain; charset=utf-8", " application/json", "multipart/form-data");
			public List<String> produces = Arrays.asList("text/plain; charset=utf-8", " application/json", "multipart/form-data");
			public List<String> schemes;
			public Boolean deprecated;
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

					public Schema(String type, String $ref) {
						this.type = type;
						this.$ref = $ref;
					}
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
			public Integer minimum;
			public Integer maximum;
		}
	}


}
