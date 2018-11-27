import io.intino.alexandria.nessaccessor.local.LocalDatalake;
import io.intino.alexandria.nessaccessor.stages.LocalStage;

import java.io.File;

public class Sealer {


	public static void main(String[] args) {
		File root = new File("/Users/oroncal/Downloads/data");
		new LocalDatalake(root).seal();
		LocalStage localStage = new LocalStage(root);
	}
}