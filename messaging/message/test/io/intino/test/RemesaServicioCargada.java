package io.intino.test;

import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class RemesaServicioCargada {


	public static void main(String[] args) throws FileNotFoundException {
		long start = System.currentTimeMillis();
		MessageReader messageReader = new MessageReader(new FileInputStream(new File("./inls/remesaServicioCargada.inl")));
		messageReader.forEachRemaining(Message::toString);
		System.out.println("v3: " + (System.currentTimeMillis() - start));

	}
}
