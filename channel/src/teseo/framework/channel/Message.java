package teseo.framework.channel;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Message implements Cloneable {

	private LocalDateTime date;
	private Map<String, String> map = new HashMap<>();

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public LocalDateTime date() {
		return date;
	}
}
