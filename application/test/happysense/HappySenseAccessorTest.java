package happysense;

import happysense.schemas.FilterList;
import happysense.schemas.Range;
import teseo.exceptions.ErrorUnknown;

import java.net.MalformedURLException;
import java.net.URL;

public class HappySenseAccessorTest {

	public static void main(String[] args) throws MalformedURLException, ErrorUnknown {
		RestAccessor accessor = new RestAccessor(new URL("http://localhost:8080"));
		accessor.snapshot("alpha", new Range(), new FilterList());
	}
}
