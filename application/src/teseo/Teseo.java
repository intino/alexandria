package teseo;

import org.siani.itrules.engine.formatters.StringFormatter;
import teseo.object.ObjectData;

public class Teseo {
	public static String schemaName(ObjectData self) {
		return StringFormatter.get().get("firstuppercase").format(self.schema().name()).toString();
	}
}
