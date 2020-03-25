package io.intino.alexandria;

import com.google.gson.JsonElement;
import io.intino.alexandria.xml.Xml;

import java.util.Map;

public class Soap {

	public Envelope readEnvelope(String value) {
		return new Envelope(new Xml(value).document().child("S:Envelope"));
	}

	public String writeEnvelope(Object schema, String xmlns) {
		if (schema == null) return render(schema, "", xmlns);
		JsonElement jsonElement = Json.gsonWriter().toJsonTree(schema);
		return render(schema, parameters(jsonElement), xmlns);
	}

	private String parameters(JsonElement jsonElement) {
		StringBuilder result = new StringBuilder();
		for (Map.Entry<String, JsonElement> child : jsonElement.getAsJsonObject().entrySet()) {
			result.append(parameterTemplate.replace("$name", child.getKey()).replace("$value", child.getValue().getAsString()));
		}
		return result.toString();
	}

	private String render(Object schema, String parameters, String xmlns) {
		String template = schemaTemplate.replace("$name", firstLowerCase(schema.getClass().getSimpleName())).replace("$xmlns", xmlns);
		return envelopTemplate.replace("$schema", template.replace("$parameter", parameters));
	}

	public static String firstLowerCase(String value) {
		return value.substring(0, 1).toLowerCase() + value.substring(1);
	}

	private static final String envelopTemplate =
			"<?xml version='1.0' encoding='UTF-8'?>\n" +
					"<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
					"\t<S:Body>\n" +
					"\t\t$schema\n" +
					"\t</S:Body>\n" +
					"</S:Envelope>";

	private static final String schemaTemplate =
			"<$name xmlns=\"$xmlns\">\n" +
					"$parameter" +
					"\t\t</$name>";

	private static final String parameterTemplate = "\t\t\t<$name>$value</$name>\n";
}
