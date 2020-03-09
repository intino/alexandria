package io.intino.alexandria.ui.displays.components.dashboard;

import io.intino.alexandria.drivers.Driver;
import io.intino.alexandria.drivers.Program;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.proxy.Network;
import io.intino.alexandria.proxy.Proxy;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.DisplayRouteManager;
import io.intino.alexandria.ui.services.push.UISession;
import io.intino.alexandria.ui.spark.UISparkManager;
import spark.Request;

import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static io.intino.alexandria.drivers.shiny.Driver.LocalUrlParameter;

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

	public DashboardManager(AlexandriaUiBox box, UISession session, String dashboard, Driver driver) {
		this.box = box;
		this.session = session;
		this.dashboard = dashboard;
		this.driver = driver;
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

	private Proxy proxy(String sessionId) {
		if (!proxyMap.containsKey(sessionId))
			proxyMap.put(sessionId, driver != null ? driver.run(program()) : null);
		return proxyMap.get(sessionId);
	}

	private io.intino.alexandria.drivers.Program program() {
		Program result = new Program().name(dashboard);
		result.parameters().put(LocalUrlParameter, dashboardUrl());
		return result;
	}

	private String dashboardPath() {
		return String.format(DashboardPath, dashboard);
	}

	private void proxyGet(UISparkManager manager) {
		try {
			if (!validRequest(manager.request())) return;
			Proxy proxy = proxy(manager.request().session().id());
			if (proxy == null) return;
			proxy.get(manager.request(), manager.response());
		} catch (Throwable ex) {
			try {
				PrintWriter writer = manager.response().raw().getWriter();
				writer.print("Could not load dashboard. Contact administrator.");
				writer.close();
			} catch (Throwable e) {
			}
		}
	}

	private void proxyPost(UISparkManager manager) {
		try {
			if (!validRequest(manager.request())) return;
			Proxy proxy = proxy(manager.request().session().id());
			if (proxy == null) return;
			proxy.post(manager.request(), manager.response());
		} catch (Network.NetworkException e) {
			Logger.debug(e.getMessage());
		}
	}

	private boolean validRequest(Request request) {
		String sessionId = request.session().id();
		return proxy(sessionId) != null;
	}

}
