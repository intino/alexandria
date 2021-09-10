package org.eclipse.jetty.server.session;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.StacklessLogging;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DuplicateCookieTest {
	@Test
	public void testMultipleSessionCookiesOnlyOneExists() throws Exception {
		String contextPath = "";
		String servletMapping = "/server";
		HttpClient client = null;

		DefaultSessionCacheFactory cacheFactory = new DefaultSessionCacheFactory();
		SessionDataStoreFactory storeFactory = new TestSessionDataStoreFactory();

		TestServer server1 = new TestServer(0, -1, -1, cacheFactory, storeFactory);
		TestServlet servlet = new TestServlet();
		ServletHolder holder = new ServletHolder(servlet);
		ServletContextHandler contextHandler = server1.addContext(contextPath);
		contextHandler.addServlet(holder, servletMapping);
		server1.start();
		int port1 = server1.getPort();

		try (StacklessLogging stackless = new StacklessLogging(Log.getLogger("org.eclipse.jetty.server.session"))) {
			//create a valid session
			Session s4422 = createUnExpiredSession(contextHandler.getSessionHandler().getSessionCache(),
					contextHandler.getSessionHandler().getSessionCache().getSessionDataStore(),
					"4422");

			client = new HttpClient();
			client.start();

			//make a request with another session cookie in there that does not exist
			Request request = client.newRequest("http://localhost:" + port1 + contextPath + servletMapping + "?action=check");
			request.header("Cookie", "JSESSIONID=123"); //doesn't exist
			request.header("Cookie", "JSESSIONID=4422"); //does exist
			ContentResponse response = request.send();
			assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			assertEquals("4422", response.getContentAsString());
			assertEquals(0, s4422.getRequests());
		} finally {
			server1.stop();
			client.stop();
		}
	}

	@Test
	public void testMultipleSessionCookiesOnlyOneValid() throws Exception {
		String contextPath = "";
		String servletMapping = "/server";
		HttpClient client = null;

		DefaultSessionCacheFactory cacheFactory = new DefaultSessionCacheFactory();
		SessionDataStoreFactory storeFactory = new TestSessionDataStoreFactory();

		TestServer server1 = new TestServer(0, -1, -1, cacheFactory, storeFactory);
		TestServlet servlet = new TestServlet();
		ServletHolder holder = new ServletHolder(servlet);
		ServletContextHandler contextHandler = server1.addContext(contextPath);
		contextHandler.addServlet(holder, servletMapping);
		server1.start();
		int port1 = server1.getPort();

		try (StacklessLogging stackless = new StacklessLogging(Log.getLogger("org.eclipse.jetty.server.session"))) {
			//create a valid session
			Session s1122 = createUnExpiredSession(contextHandler.getSessionHandler().getSessionCache(),
					contextHandler.getSessionHandler().getSessionCache().getSessionDataStore(),
					"1122");
			//create an invalid session
			createInvalidSession(contextHandler.getSessionHandler().getSessionCache(),
					contextHandler.getSessionHandler().getSessionCache().getSessionDataStore(),
					"2233");

			client = new HttpClient();
			client.start();

			//make a request with another session cookie in there that is not valid
			Request request = client.newRequest("http://localhost:" + port1 + contextPath + servletMapping + "?action=check");
			request.header("Cookie", "JSESSIONID=1122"); //is valid
			request.header("Cookie", "JSESSIONID=2233"); //is invalid
			ContentResponse response = request.send();
			assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			assertEquals("1122", response.getContentAsString());
			assertEquals(0, s1122.getRequests());
		} finally {
			server1.stop();
			client.stop();
		}
	}

	@Test
	public void testMultipleSessionCookiesMultipleExists() throws Exception {
		String contextPath = "";
		String servletMapping = "/server";
		HttpClient client = null;

		DefaultSessionCacheFactory cacheFactory = new DefaultSessionCacheFactory();
		SessionDataStoreFactory storeFactory = new TestSessionDataStoreFactory();

		TestServer server1 = new TestServer(0, -1, -1, cacheFactory, storeFactory);
		TestServlet servlet = new TestServlet();
		ServletHolder holder = new ServletHolder(servlet);
		ServletContextHandler contextHandler = server1.addContext(contextPath);
		contextHandler.addServlet(holder, servletMapping);
		server1.start();
		int port1 = server1.getPort();

		try (StacklessLogging stackless = new StacklessLogging(Log.getLogger("org.eclipse.jetty.server.session"))) {
			//create some of unexpired sessions
			Session s1234 = createUnExpiredSession(contextHandler.getSessionHandler().getSessionCache(),
					contextHandler.getSessionHandler().getSessionCache().getSessionDataStore(),
					"1234");
			Session s5678 = createUnExpiredSession(contextHandler.getSessionHandler().getSessionCache(),
					contextHandler.getSessionHandler().getSessionCache().getSessionDataStore(),
					"5678");
			Session s9111 = createUnExpiredSession(contextHandler.getSessionHandler().getSessionCache(),
					contextHandler.getSessionHandler().getSessionCache().getSessionDataStore(),
					"9111");

			client = new HttpClient();
			client.start();

			//check that the request count is 0
			assertEquals(0, s1234.getRequests());
			assertEquals(0, s5678.getRequests());
			assertEquals(0, s9111.getRequests());

			//make a request with multiple valid session ids
			Request request = client.newRequest("http://localhost:" + port1 + contextPath + servletMapping + "?action=check");
			request.header("Cookie", "JSESSIONID=1234");
			request.header("Cookie", "JSESSIONID=5678");
			ContentResponse response = request.send();
			assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());

			//check that all valid sessions have their request counts decremented correctly after the request, back to 0
			assertEquals(0, s1234.getRequests());
			assertEquals(0, s5678.getRequests());
			assertEquals(0, s9111.getRequests());
		} finally {
			server1.stop();
			client.stop();
		}
	}

	public Session createUnExpiredSession(SessionCache cache, SessionDataStore store, String id) throws Exception {
		long now = System.currentTimeMillis();
		SessionData data = store.newSessionData(id, now - 20, now - 10, now - 20, TimeUnit.MINUTES.toMillis(10));
		data.setExpiry(now + TimeUnit.DAYS.toMillis(1));
		Session s = cache.newSession(data);
		cache.add(id, s);
		s.complete(); //pretend a request that created the session is finished
		return s;
	}

	public Session createInvalidSession(SessionCache cache, SessionDataStore store, String id) throws Exception {
		Session session = createUnExpiredSession(cache, store, id);
		session._state = Session.State.INVALID;
		return session;
	}

	public static class TestServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;

		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse httpServletResponse) throws ServletException, IOException {
			String action = request.getParameter("action");
			if (StringUtil.isBlank(action))
				return;

			if (action.equalsIgnoreCase("check")) {
				HttpSession session = request.getSession(false);
				assertNotNull(session);
				httpServletResponse.getWriter().print(session.getId());
			} else if (action.equalsIgnoreCase("create")) {
				request.getSession(true);
			}
		}
	}
}
