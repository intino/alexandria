package io.intino.pandora.plugin;

import io.intino.pandora.plugin.object.ObjectData;
import org.siani.itrules.engine.formatters.StringFormatter;

public class DataTypes {
	public static String formatName(ObjectData self) {
		return StringFormatter.get().get("firstuppercase").format(self.format().name()).toString();
	}
}
