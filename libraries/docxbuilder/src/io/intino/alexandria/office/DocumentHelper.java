package io.intino.alexandria.office;

import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

class DocumentHelper {

	static File createTmpDocument(File dir, Document document) throws IOException {

		File tmp = new File(dir, "doc_" + System.nanoTime() + ".xml");
		tmp.deleteOnExit();

		try(OutputStream output = Files.newOutputStream(tmp.toPath())) {

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			// The default add many empty new line, not sure why?
			// https://mkyong.com/java/pretty-print-xml-with-java-dom-and-xslt/
			Transformer transformer = transformerFactory.newTransformer();

			// add a xslt to remove the extra newlines
			//Transformer transformer = transformerFactory.newTransformer(new StreamSource(new File(FORMAT_XSLT)));

			// pretty print
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "no");

			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(output);

			transformer.transform(source, result);

		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}

		return tmp;
	}
}
