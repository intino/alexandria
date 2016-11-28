package cesar;

import io.intino.pandora.scheduling.PandoraTasker;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		PandoraTasker tasker = new PandoraTasker();
//		Tasks.init(tasker, null);
		Thread.sleep(Long.MAX_VALUE);
	}
}
