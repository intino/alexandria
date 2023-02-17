package io.intino.test;

import io.intino.alexandria.message.MessageReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Adeudos {

	public static void main(String[] args) throws FileNotFoundException {
		while (true) {
			MessageReader messageReader = new MessageReader(new FileInputStream("./messaging/message/test-res/adeudos.inl"));
			messageReader.forEachRemaining(message -> {
			});
		}
	}
}
