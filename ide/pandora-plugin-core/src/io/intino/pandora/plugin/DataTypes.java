package io.intino.pandora.plugin;

import io.intino.pandora.plugin.object.ObjectData;
import org.siani.itrules.engine.formatters.StringFormatter;

public class DataTypes {
	public static String schemaName(ObjectData self) {
		return StringFormatter.get().get("firstuppercase").format(self.schema().name()).toString();
	}
}
