package io.intino.alexandria;


import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.xml.Node;

public class Envelope {
	private final Node envelopeNode;

	public Envelope(Node envelopeNode) {
		this.envelopeNode = envelopeNode;
	}

	public Body body() {
		return new Body(envelopeNode.child("S:Body"));
	}

	public static class Body {
		private final Node node;

		public Body(Node node) {
			this.node = node;
		}

		public <T> T schema(Class<T> t) {
			try {
				JsonObject jsonObject = new JsonObject();
				node.getChildren().get(0).getChildren().forEach(childNode -> jsonObject.add(childNode.getNodeName(), new JsonPrimitive(childNode.getTextContent())));
				return Json.gsonReader().fromJson(jsonObject, t);
			} catch (Exception e) {
				Logger.error(e);
				return null;
			}
		}
	}
}
