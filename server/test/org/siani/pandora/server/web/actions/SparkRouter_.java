package org.siani.pandora.server.web.actions;

import org.junit.Ignore;
import org.siani.pandora.server.actions.Action;
import org.siani.pandora.server.actions.AdapterProxy;
import org.siani.pandora.server.actions.Router;
import org.siani.pandora.server.web.actions.common.InputMessage;
import org.siani.pandora.server.web.actions.common.OutputMessage;
import org.siani.pandora.server.web.actions.common.TestAction;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import static org.siani.pandora.server.actions.Router.Method.*;
import static java.lang.Thread.sleep;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@Ignore
public class SparkRouter_ {

    private static final String BASE_URL = "http://localhost:8080";
    private static SparkRouter router;

    @BeforeClass
    public static void setUpClass() throws Exception {
        router = new SparkRouter(8080, mock(AdapterProxy.class));
        router.staticFiles("web");
        router.route("/fake-request-to-start-server").with(() -> ((input, output) -> {}));
        sleep(1000);
    }

    @Test
    public void should_create_get_action_on_specified_route() throws Exception {
        testMethod(Get);
    }

    @Test
    public void should_create_post_action_on_specified_route() throws Exception {
        testMethod(Post);
    }

    @Test
    public void should_create_put_action_on_specified_route() throws Exception {
        testMethod(Put);
    }

    @Test
    public void should_create_delete_action_on_specified_route() throws Exception {
        testMethod(Delete);
    }

    @Test
    public void should_get_resource_in_static_file_location() throws IOException {
        Scanner scanner = new Scanner(connectTo("/hello.txt", "GET")).useDelimiter("\\A");
        assertThat("Hello world", is(scanner.next()));
    }

    private void testMethod(Router.Method method) throws IOException {
        Action.Task<InputMessage, OutputMessage> task = mockTask();
        router.route("/hello").as(method).with(new TestAction(task));

        connectTo("/hello", method.toString().toUpperCase());

        verify(task).before(any(InputMessage.class), any(OutputMessage.class));
        verify(task).execute(any(InputMessage.class), any(OutputMessage.class));
        verify(task).after(any(InputMessage.class), any(OutputMessage.class));
    }

    @SuppressWarnings("unchecked")
    private Action.Task<InputMessage, OutputMessage> mockTask() {
        return mock(Action.Task.class);
    }

    private InputStream connectTo(String route, String method) throws IOException {
        return connection(route, method).getInputStream();
    }

    private HttpURLConnection connection(String route, String method) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL + route).openConnection();
        connection.setRequestMethod(method);
        return connection;
    }
}
