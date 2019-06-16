package io.intino.alexandria.ui.displays.components.dashboard;

import io.intino.alexandria.Driver;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.proxy.Network;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.DisplayRouteManager;
import io.intino.alexandria.ui.spark.UISparkManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static io.intino.alexandria.drivers.shiny.Driver.LocalUrlParameter;
import static io.intino.alexandria.drivers.shiny.Driver.Program;

public class Proxy {
	private final AlexandriaUiBox box;
	private final String baseUrl;
	private final String dashboard;
	private final Driver<URL, io.intino.alexandria.proxy.Proxy> driver;

	private static boolean routeManagerReady = false;
	private static final Map<String, io.intino.alexandria.proxy.Proxy> proxyMap = new HashMap<>();
	private static final String DashboardsPathPattern = "/dashboards/:name/*";
	private static final String DashboardPathPattern = "/dashboards/:name";
	private static final String DashboardPath = "/dashboards/%s";

	public Proxy(AlexandriaUiBox box, String baseUrl, String dashboard) {
		this.box = box;
		this.baseUrl = baseUrl;
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

	public URL dashboardUrl() {
		try {
			return new URL(baseUrl + dashboardPath());
		} catch (MalformedURLException e) {
			return null;
		}
	}

	private boolean listening() {
		return routeManagerReady;
	}

	private io.intino.alexandria.proxy.Proxy proxy() {
		if (!proxyMap.containsKey(dashboard))
			proxyMap.put(dashboard, driver.run(parameters()));
		return proxyMap.get(dashboard);
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
			proxy().get(manager.request(), manager.response());
		} catch (Network.NetworkException e) {
			Logger.error(e);
		}
	}

	private void proxyPost(UISparkManager manager) {
		try {
			proxy().post(manager.request(), manager.response());
		} catch (Network.NetworkException e) {
			Logger.error(e);
		}
	}

}
