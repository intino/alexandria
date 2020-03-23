package io.intino.alexandria.xml;

import io.intino.alexandria.logger.Logger;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Xml {
	private final InputStream source;
	private final io.intino.alexandria.xml.Document document;

	public Xml(String xmlText) {
		this(new ByteArrayInputStream(xmlText.getBytes()));
	}

	public Xml(InputStream xmlStream) {
		source = xmlStream;
		document = new io.intino.alexandria.xml.Document(this.getDOM());
	}

	public io.intino.alexandria.xml.Document document() {
		return document;
	}

	private Document getDOM() {
		Document document = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			document = dBuilder.parse(source);
			document.getDocumentElement().normalize();
		} catch (Exception e) {
			Logger.error(e);
		}
		return document;
	}
}
