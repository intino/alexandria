package teseo;

import java.util.Map;

public class Commons {

	public static teseo.Error error(String code, String label, Map<String, String> parameters) {
		return new Error() {
			@Override
			public String code() {
				return code;
			}

			@Override
			public String label() {
				return label;
			}

			@Override
			public Map<String, String> parameters() {
				return parameters;
			}
		};
	}
}
