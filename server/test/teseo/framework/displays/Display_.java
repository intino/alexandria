package teseo.framework.displays;

import teseo.framework.core.Client;
import teseo.framework.services.ServiceSupplier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class Display_ {

	@Mock
	private MessageCarrier carrier;
	@Mock
	private DisplayRepository repository;
	@Captor
	private ArgumentCaptor<Map<String, Object>> parametersCaptor;
	private TestDisplay display;

	@Before
	public void setUp() {
		display = new TestDisplay(carrier, repository);
		display.inject(supplier());
	}

	@Test
	public void should_send_id_and_display_name_on_personify() {
		display.personify();

		verify(carrier, times(1)).notify(any(Client.class), eq("personify"), parametersCaptor.capture());

		assertThat(parametersCaptor.getValue().containsKey("id"), is(true));
		assertThat(parametersCaptor.getValue().get("display"), is("test"));
	}

	@Test
	public void should_send_id_and_display_name_on_personify_once() {
		display.personifyOnce();

		verify(carrier, times(1)).notify(any(Client.class), eq("personifyOnce"), parametersCaptor.capture());

		assertThat(parametersCaptor.getValue().containsKey("id"), is(true));
		assertThat(parametersCaptor.getValue().get("display"), is("test"));
	}

	private ServiceSupplier supplier() {
		ServiceSupplier supplier = mock(ServiceSupplier.class);
		return supplier;
	}

	private class TestDisplay extends Display {
		TestDisplay(MessageCarrier carrier, DisplayRepository repository) {
			super(carrier, repository, () -> null);
		}
	}
}
