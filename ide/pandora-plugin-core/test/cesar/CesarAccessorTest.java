package cesar;

import org.siani.pandora.exceptions.ErrorBadRequest;
import org.siani.pandora.exceptions.ErrorUnknown;

import java.net.MalformedURLException;
import java.net.URL;

public class CesarAccessorTest {

	public static void main(String[] args) throws MalformedURLException, ErrorUnknown, ErrorBadRequest {
		CesarAccessor accessor = new CesarAccessor(new URL("http://localhost:8080"));
		System.out.println(accessor.server("hola"));
	}
}
