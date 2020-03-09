package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.drivers.Driver;
import io.intino.alexandria.drivers.Program;
import io.intino.alexandria.drivers.program.Resource;
import io.intino.alexandria.drivers.program.Script;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.schemas.DashboardInfo;
import io.intino.alexandria.schemas.DashboardSettingsInfo;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.components.dashboard.DashboardManager;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.Listener;
import io.intino.alexandria.ui.displays.notifiers.DashboardNotifier;
import io.intino.alexandria.ui.utils.DelayerUtil;
import io.intino.alexandria.ui.utils.IOUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class Dashboard<DN extends DashboardNotifier, B extends Box> extends AbstractDashboard<B> {
    private Driver driver;
    private URL serverScript;
    private URL uiScript;
    private java.util.Map<String, Object> parameterMap = new HashMap<>();
    private List<URL> resourceList = new ArrayList<>();
    private boolean adminMode = false;
    private DashboardNameProvider dashboardNameProvider = null;
    private Listener closeSettingsListener = null;

    public Dashboard(B box) {
        super(box);
    }

    public Dashboard parameters(java.util.Map<String, Object> parameters) {
        this.parameterMap = parameters;
        refresh();
        return this;
    }

	public Dashboard<DN, B> serverScript(URL script) {
		_serverScript(script);
		return this;
	}

	public Dashboard<DN, B> uiScript(URL script) {
		_uiScript(script);
		return this;
	}

	public Dashboard<DN, B> addResource(URL resource) {
    	this.resourceList.add(resource);
    	return this;
	}

    public Dashboard<DN, B> adminMode(boolean value) {
    	_adminMode(value);
    	return this;
	}

    public Dashboard dashboardNameProvider(DashboardNameProvider provider) {
		this.dashboardNameProvider = provider;
		return this;
	}

	public Dashboard onCloseSettings(Listener listener) {
		this.closeSettingsListener = listener;
		return this;
	}

	public Dashboard driver(Driver driver) {
		this.driver = driver;
		return this;
	}

	protected Dashboard _adminMode(boolean value) {
		this.adminMode = value;
		return this;
	}

	protected Dashboard _serverScript(URL script) {
		this.serverScript = script;
		return this;
	}

	protected Dashboard _uiScript(URL script) {
		this.uiScript = script;
		return this;
	}

	protected Dashboard _add(String parameter, String value) {
		parameterMap.put(parameter, value);
		return this;
	}

	protected Dashboard _add(URL resource) {
		resourceList.add(resource);
		return this;
	}

	@Override
    public void init() {
        super.init();
        refresh();
    }

    @Override
    public void refresh() {
        try {
            notifier.showLoading();
            String location = execute();
            Timer timer = new Timer("Dashboard notifier");
            timer.schedule(new TimerTask() {
                @Override
                public void run() { notifier.refresh(new DashboardInfo().location(location).driverDefined(driver != null).adminMode(adminMode));
                }
            }, 1000);
        }
        catch (RuntimeException ex) {
            notifier.refreshError(ex.getMessage());
        }
    }

    public void showSettings() {
    	notifier.showSettings(new DashboardSettingsInfo().serverScript(contentOf(serverScript)).uiScript(contentOf(uiScript)));
	}

	public void hideSettings() {
		notifier.hideSettings();
		if (this.closeSettingsListener != null) this.closeSettingsListener.accept(new Event(this));
	}

	public void saveServerScript(String content) {
    	saveScript(serverScript, decode(content));
    	DelayerUtil.execute(this, aVoid -> notifyUser("Server script saved", UserMessage.Type.Info), 1000);
	}

	public void saveUiScript(String content) {
		saveScript(uiScript, decode(content));
		DelayerUtil.execute(this, aVoid -> notifyUser("UI script saved", UserMessage.Type.Info), 1000);
	}

	private String decode(String content) {
		return new String(Base64.getDecoder().decode(content.replace(" ", "+")));
	}

	private String execute() {
        String program = programName();
        DashboardManager dashboardManager = new DashboardManager((AlexandriaUiBox)box(), session(), program, driver);

        if (driver == null) return null;

        if (!driver.isPublished(program))
			driver.publish(program());

        dashboardManager.listen();

        return dashboardManager.dashboardUrl().toString();
    }

    private Program program() {
        String name = programName();
		List<Script> scripts = Arrays.asList(scriptOf(serverScript), scriptOf(uiScript));
		List<Resource> resources = resourceList.stream().map(this::resourceOf).collect(Collectors.toList());
		return new Program().name(name).parameters(parameterMap).scripts(scripts).resources(resources).parameters(parameterMap);
    }

	private Program programOf(URL script) {
		return new Program().name(programName()).parameters(parameterMap).scripts(Collections.singletonList(scriptOf(script)));
	}

    private String contentOf(URL file) {
		try {
			return new String(IOUtils.toByteArray(file.openStream()));
		} catch (IOException e) {
			return "";
		}
	}

	private Script scriptOf(URL script) {
    	try {
			String filename = filenameOf(script);
			return new Script().name(filename).content(script.openStream());
		} catch (Exception e) {
			Logger.error(e);
			return null;
		}
	}

	private Resource resourceOf(URL resource) {
		try {
			String filename = filenameOf(resource);
			return new Resource().name(filename).content(resource.openStream());
		}
		catch (Exception ex) {
			Logger.error(ex);
			return null;
		}
	}

	private String filenameOf(URL url) {
    	String data = url.toString();
    	return data.substring(data.lastIndexOf('/')+1);
    }

    private String programName() {
    	return dashboardNameProvider != null ? dashboardNameProvider.name() : name().toLowerCase();
    }

	private void saveScript(URL script, String content) {
		try {
			if (driver != null) driver.update(programOf(script));
			Files.write(Paths.get(script.toURI()), content.getBytes());
		} catch (URISyntaxException | IOException e) {
			Logger.error(e);
		}
	}

	public interface DashboardNameProvider {
    	String name();
	}
}