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
import io.intino.alexandria.ui.displays.notifiers.DashboardNotifier;
import io.intino.alexandria.ui.utils.DelayerUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public Dashboard(B box) {
        super(box);
    }

    public Dashboard update(java.util.Map<String, Object> parameters) {
        this.parameterMap = parameters;
        refresh();
        return this;
    }

	public Dashboard serverScript(URL script) {
		this.serverScript = script;
		return this;
	}

	public Dashboard uiScript(URL script) {
		this.uiScript = script;
		return this;
	}

	public Dashboard adminMode(boolean value) {
		this.adminMode = value;
		return this;
	}

	public Dashboard driver(Driver driver) {
		this.driver = driver;
		return this;
	}

	public Dashboard add(String parameter, String value) {
		parameterMap.put(parameter, value);
		return this;
	}

	public Dashboard add(URL resource) {
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
            Timer timer = new Timer();
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

    public void settings() {
    	notifier.showSettings(new DashboardSettingsInfo().serverScript(contentOf(serverScript)).uiScript(contentOf(uiScript)));
	}

	public void saveServerScript(String content) {
    	saveScript(serverScript, content);
    	DelayerUtil.execute(this, aVoid -> notifyUser("Server script saved", UserMessage.Type.Info), 1000);
	}

	public void saveUiScript(String content) {
		saveScript(uiScript, content);
		DelayerUtil.execute(this, aVoid -> notifyUser("UI script saved", UserMessage.Type.Info), 1000);
	}

	private String execute() {
        String program = programName();
        DashboardManager dashboardManager = new DashboardManager((AlexandriaUiBox)box(), session(), program, driver);

        if (driver == null) return null;

        if (!driver.isPublished(program))
            driver.publish(program());

        dashboardManager.register(program, username());
        dashboardManager.listen();

        return dashboardManager.dashboardUrl().toString();
    }

    private Program program() {
        String name = programName();
		List<Script> scripts = Arrays.asList(scriptOf(serverScript), scriptOf(uiScript));
		List<Resource> resources = resourceList.stream().map(this::resourceOf).collect(Collectors.toList());
		return new Program().name(name).scripts(scripts).resources(resources).parameters(parameterMap);
    }

	private Program programOf(URL script) {
		return new Program().name(programName()).scripts(Collections.singletonList(scriptOf(script)));
	}

    private String contentOf(URL file) {
		try {
			return new String(Files.readAllBytes(Paths.get(file.toURI())), StandardCharsets.UTF_8);
		} catch (IOException | URISyntaxException e) {
			return "";
		}
	}

	private Script scriptOf(URL script) {
    	try {
			Path path = pathOf(script);
			if (path == null) return null;
			return new Script().name(path.getFileName().toString()).content(new FileInputStream(path.toFile()));
		} catch (FileNotFoundException e) {
			Logger.error(e);
			return null;
		}
	}

	private Resource resourceOf(URL resource) {
		try {
			Path path = pathOf(resource);
			if (path == null) return null;
			return new Resource().name(path.getFileName().toString()).content(new FileInputStream(path.toFile()));
		} catch (FileNotFoundException e) {
			Logger.error(e);
			return null;
		}
	}

	private Path pathOf(URL serverScript) {
        try {
            return Paths.get(serverScript.toURI());
        } catch (URISyntaxException e) {
            Logger.error(e);
            return null;
        }
    }

    private String programName() {
        return Program.name(name(), parameterMap);
    }

	private void saveScript(URL script, String content) {
		try {
			Files.write(Paths.get(script.toURI()), content.getBytes());
			if (driver != null) driver.update(programOf(script));
		} catch (URISyntaxException | IOException e) {
			Logger.error(e);
		}
	}

}