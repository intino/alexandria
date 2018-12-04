package io.intino.alexandria.zet;

import org.junit.Test;

import java.io.File;

public class ReadZInputStream {


	@Test
	public void sxx() {
		File root = new File("test-res/testsets");
		File[] files = root.listFiles(f -> f.getName().endsWith(".zet"));
		for (File file : files) {
			Zet zet = new Zet(new ZetReader(file));
			new ZetWriter(file).write(zet.ids());
		}

	}

	@Test
	public void name1() {
		File root = new File("test-res/Singleton/Servicio.Solicitud.zet");
		ZetReader reader = new ZetReader(root);
		iterSout(reader);
	}

	@Test
	public void name() {
		File root = new File("test-res/Singleton2");
		File[] files = root.listFiles(f -> f.getName().endsWith(".zet"));
		for (File file : files) {
			System.out.println(file.getName());
			ZetReader reader = new ZetReader(file);
			iter(reader);

		}
	}

	private void iterSout(ZetStream stream) {
		while (stream.hasNext()) {
			System.out.println(stream.next());
		}

	}

	private void iter(ZetStream stream) {
		while (stream.hasNext()) {
			stream.next();
		}

	}
}
