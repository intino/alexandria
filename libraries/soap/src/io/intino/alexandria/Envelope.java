package io.intino.alexandria;


import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.xml.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

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
				JAXBContext context = JAXBContext.newInstance(t);
				Unmarshaller m = context.createUnmarshaller();
				return (T) m.unmarshal(node.getChildren().get(0).get());
			} catch (Exception e) {
				Logger.error(e);
				return null;
			}
		}
	}
}
