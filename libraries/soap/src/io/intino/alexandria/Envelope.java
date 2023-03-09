package io.intino.alexandria;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.xml.Node;

import java.util.Objects;

public class Envelope {
	private final Node envelopeNode;

	public Envelope(Node envelopeNode) {
		this.envelopeNode = envelopeNode;
	}

	public Body body() {
		final Node node = envelopeNode.getChildNodes().stream().filter(n -> n.getNodeName().toLowerCase().contains("body")).findFirst().orElse(null);
		return node == null ? null : new Body(node);
	}

	public static class Body {
		private final Node node;

		public Body(Node node) {
			this.node = node;
		}

		public <T> T schema(Class<T> t) {
			try {
				JsonObject jsonObject = new JsonObject();
				node.getChildren().get(0).getChildren().stream().filter(Objects::nonNull).forEach(childNode -> jsonObject.add(removeNameSpace(childNode.getNodeName()), new JsonPrimitive(childNode.getTextContent())));
				return Json.fromJson(jsonObject, t);
			} catch (Exception e) {
				Logger.error(e);
				return null;
			}
		}

		private String removeNameSpace(String nodeName) {
			if (nodeName.contains(":")) {
				final String[] split = nodeName.split(":");
				return split[split.length - 1];
			}
			return nodeName;
		}
	}
}
