package io.intino.alexandria.ui.displays.components.dashboard;

import io.intino.alexandria.drivers.Driver;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.proxy.Network;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.DisplayRouteManager;
import io.intino.alexandria.ui.services.push.UISession;
import io.intino.alexandria.ui.spark.UISparkManager;
import spark.Request;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static io.intino.alexandria.drivers.shiny.Driver.LocalUrlParameter;
import static io.intino.alexandria.drivers.shiny.Driver.Program;

public class DashboardManager {
	private final AlexandriaUiBox box;
	private final UISession session;
	private final String dashboard;
	private final Driver<URL, io.intino.alexandria.proxy.Proxy> driver;

	private static boolean routeManagerReady = false;
	private static final String DashboardsPathPattern = "/dashboards/:name/*";
	private static final String DashboardPathPattern = "/dashboards/:name";
	private static final String DashboardPath = "/dashboards/%s";

	private static final Map<String, io.intino.alexandria.proxy.Proxy> proxyMap = new HashMap<>();
	private static final Map<String, String> sessionMap = new HashMap<>();

	public DashboardManager(AlexandriaUiBox box, UISession session, String dashboard) {
		this.box = box;
		this.session = session;
		this.dashboard = dashboard;
		this.driver = new io.intino.alexandria.drivers.shiny.Driver();
	}

	public void listen() {
		if (listening()) return;
		DisplayRouteManager routeManager = box.routeManager();
		routeManager.get(DashboardsPathPattern, this::proxyGet);
		routeManager.get(DashboardPathPattern, this::proxyGet);
		routeManager.post(DashboardsPathPattern, this::proxyPost);
		routeManager.post(DashboardPathPattern, this::proxyPost);
		routeManagerReady = true;
	}

	public void register(String dashboard, String username) {
		String userKey = username != null ? username : "";
		proxyMap.put(dashboard + userKey, driver.run(parameters()));
		sessionMap.put(session.id(), dashboard + userKey);
	}

	public URL dashboardUrl() {
		try {
			return new URL(session.browser().baseUrl() + dashboardPath());
		} catch (MalformedURLException e) {
			return null;
		}
	}

	private boolean listening() {
		return routeManagerReady;
	}

	private io.intino.alexandria.proxy.Proxy proxy(String sessionId) {
		if (!sessionMap.containsKey(sessionId)) return null;
		return proxyMap.get(sessionMap.get(sessionId));
	}

	private Map<String, Object> parameters() {
		return new HashMap<String, Object>() {{
			put(Program, dashboard);
			put(LocalUrlParameter, dashboardUrl());
		}};
	}

	private String dashboardPath() {
		return String.format(DashboardPath, dashboard);
	}

	private void proxyGet(UISparkManager manager) {
		try {
			if (!validRequest(manager.request())) return;
			proxy(manager.request().session().id()).get(manager.request(), manager.response());
		} catch (Throwable ex) {
			try {
				PrintWriter writer = manager.response().raw().getWriter();
				writer.print("Could not load dashboard. Contact administrator.");
				writer.close();
			} catch (IOException e) {
			}
		}
	}

	private void proxyPost(UISparkManager manager) {
		try {
			if (!validRequest(manager.request())) return;
			proxy(manager.request().session().id()).post(manager.request(), manager.response());
		} catch (Network.NetworkException e) {
			Logger.error(e);
		}
	}

	private boolean validRequest(Request request) {
		String sessionId = request.session().id();
		return sessionMap.containsKey(sessionId);
	}

}
