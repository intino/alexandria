package teseo.framework.web.actions;

import teseo.framework.web.actions.common.AdapterProxy;
import teseo.framework.web.actions.common.InputMessage;
import org.junit.Test;
import spark.Request;

import javax.servlet.http.HttpServletRequest;

import static teseo.framework.web.actions.SparkWrapper.wrap;
import static java.util.stream.IntStream.range;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SparkWrapperTimeout_ {

    private final AdapterProxy adapter = new AdapterProxy();

    @Test(timeout = 1000)
    public void should_test_time() {
        Request request = request();
        when(request.queryParams("message")).thenReturn("Hello world");
        sendRequest(request, 10000);
    }

    private Request request() {
        Request request = mock(Request.class);
        when(request.raw()).thenReturn(mock(HttpServletRequest.class));
        return request;
    }

    private void sendRequest(Request request, int times) {
        range(0, times).parallel().forEach(i -> SparkWrapper.wrap(request).with(adapter).as(InputMessage.class).message());
    }
}
