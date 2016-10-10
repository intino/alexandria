package cesar;

import org.siani.pandora.server.PandoraSpark;

public class CesarServerTest {

	public static void main(String[] args) {
		CesarResources.setup(new PandoraSpark(8080), null);
	}
}
