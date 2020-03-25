package io.intino.alexandria;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.xml.Xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.io.ByteArrayOutputStream;

public class Soap {

	public Soap() {
	}

	public Envelope readEnvelope(String value) {
		return new Envelope(new Xml(value).document().child("S:Envelope"));
	}

	public String writeEnvelope(Object schema) {
		if (schema == null) return render("");
		try {
			Class<?> aClass = schema.getClass();
			JAXBContext context = JAXBContext.newInstance(aClass);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			QName qName = new QName(aClass.getPackageName(), aClass.getSimpleName());
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			m.marshal(new JAXBElement(qName, aClass, schema), outputStream);
			String text = outputStream.toString().replace("\n", "\n\t\t").trim();
			return render(text.substring(text.indexOf("\n") + 1));
		} catch (JAXBException e) {
			Logger.error(e);
			return "";
		}
	}

	private String render(String schema) {
		return envelopTemplate.replace("$schema", schema);
	}


	private static final String envelopTemplate =
			"<?xml version='1.0' encoding='UTF-8'?>\n" +
					"<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
					"    <S:Body>\n" +
					"$schema\n" +
					"    </S:Body>\n" +
					"</S:Envelope>";
}
