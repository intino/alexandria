package teseo;

import teseo.object.ObjectData;

public class Teseo {
	public static String schemaElementName(ObjectData self) {
		return Character.toUpperCase(self.schema().element().name().charAt(0)) + self.schema().element().name().substring(1);
	}
}
