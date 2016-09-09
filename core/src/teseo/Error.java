package teseo;

import java.util.Map;

public interface Error {
	String code();

	String label();

	Map<String, String> parameters();
}
