package cesar;

import io.intino.konos.scheduling.KonosTasker;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		KonosTasker tasker = new KonosTasker();
//		Tasks.init(tasker, null);
		Thread.sleep(Long.MAX_VALUE);
	}
}
