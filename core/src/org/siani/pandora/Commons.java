package org.siani.pandora;

import java.lang.*;
import java.util.Map;

public class Commons {

	public static org.siani.pandora.Error error(String code, String label, Map<String, String> parameters) {
		return new org.siani.pandora.Error() {
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
