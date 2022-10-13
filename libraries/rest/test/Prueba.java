import io.intino.alexandria.rest.RequestAdapter;
import org.junit.Test;

public class Prueba {
	public enum TipoAutenticacion {
		Password, Google, Apple, Facebook
	}

	@Test
	public void name() {
		TipoAutenticacion google = RequestAdapter.adapt(null, TipoAutenticacion.class);

		System.out.println(google.name());
	}
}
