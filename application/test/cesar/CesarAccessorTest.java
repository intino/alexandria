package cesar;

import teseo.exceptions.ErrorBadRequest;
import teseo.exceptions.ErrorUnknown;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

public class CesarAccessorTest {

	public static void main(String[] args) throws MalformedURLException, ErrorUnknown, ErrorBadRequest {
		CesarAccessor accessor = new CesarAccessor(new URL("http://localhost:8080"));
		accessor.createSystem("ssss", LocalDateTime.now(), "súper repo");
	}
}
