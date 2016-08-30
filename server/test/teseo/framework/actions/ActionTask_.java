package teseo.framework.actions;

import teseo.framework.core.Client;
import teseo.framework.services.BrowserService;
import teseo.framework.services.ServiceSupplier;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static teseo.framework.actions.Action.Input;
import static teseo.framework.actions.Action.Output;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ActionTask_ {

    private ServiceSupplier serviceSupplier;
    private BrowserService browserService;

    @Before
    public void setUp() {
        serviceSupplier = mock(ServiceSupplier.class);
        browserService = mock(BrowserService.class, RETURNS_MOCKS);
        when(serviceSupplier.service(BrowserService.class)).thenReturn(browserService);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should_link_request_with_session_on_calling_to_before() {
        ActionTask task = new TestableActionTask(serviceSupplier);

        task.before(new InputMessage(), mock(Output.class));

        verify(browserService).linkToThread(any(Client.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should_unlink_request_for_current_thread_on_calling_to_after() {
        ActionTask task = new TestableActionTask(serviceSupplier);

        task.after(new InputMessage(), any(Output.class));

        verify(browserService).unlinkFromThread();
    }

    public interface OutputMessage extends Action.Output {
        void write(String message);
    }

    private class InputMessage implements Input {

        @Override
        public String sessionId() {
            return "anyString";
        }

        @Override
        public String clientId() {
            return "anyString";
        }

        @Override
        public String requestUrl() {
            return "anyString";
        }

        @Override
        public String baseUrl() {
            return "anyString";
        }

        @Override
        public String languageFromUrl() {
            return "en";
        }

        @Override
        public String languageFromMetadata() {
            return null;
        }

        @Override
        public Map<String, String> metadata() {
            return null;
        }

        @Override
        public Map<String, String> parameters() {
            return null;
        }

        @Override
        public List<File> files() {
            return null;
        }

        @Override
        public String displayId() {
            return "anyThing";
        }

        @Override
        public String operation() {
            return "anyThing";
        }
    }

    private class TestableActionTask extends ActionTask<Input, OutputMessage> {

        public TestableActionTask(ServiceSupplier serviceSupplier) {
            super(serviceSupplier);
        }

        @Override
        public void execute(Input input, OutputMessage output) {
        }

        @Override
        protected Client clientOf(String id) {
            return mock(Client.class);
        }
    }
}
