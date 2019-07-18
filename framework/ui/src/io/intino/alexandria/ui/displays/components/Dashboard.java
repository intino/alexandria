package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Base64;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.drivers.Program;
import io.intino.alexandria.drivers.shiny.Driver;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.schemas.DashboardInfo;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.components.dashboard.DashboardManager;
import io.intino.alexandria.ui.displays.notifiers.DashboardNotifier;
import org.apache.commons.codec.digest.DigestUtils;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class Dashboard<DN extends DashboardNotifier, B extends Box> extends AbstractDashboard<B> {
    private Driver driver;
    private URL serverScript;
    private URL uiScript;
    private java.util.Map<String, String> parameterMap = new HashMap<>();
    private List<URL> resourceList = new ArrayList<>();

    public Dashboard(B box) {
        super(box);
        this.driver = new Driver();
    }

    public Dashboard serverScript(URL script) {
        this.serverScript = script;
        return this;
    }

    public Dashboard uiScript(URL script) {
        this.uiScript = script;
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

    public Dashboard update(java.util.Map<String, String> parameters) {
        this.parameterMap = parameters;
        refresh();
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
                public void run() { notifier.refresh(new DashboardInfo().location(location));
                }
            }, 1000);
        }
        catch (RuntimeException ex) {
            notifier.refreshError(ex.getMessage());
        }
    }

    private String execute() {
        String program = programName();
        DashboardManager dashboardManager = new DashboardManager((AlexandriaUiBox)box(), session(), program);

        if (!driver.isPublished(program))
            driver.publish(program());

        dashboardManager.register(program, username());
        dashboardManager.listen();

        return dashboardManager.dashboardUrl().toString();
    }

    private Program program() {
        String name = programName();
        List<Path> resources = resourceList.stream().map(this::pathOf).collect(Collectors.toList());
        return new Program().name(name).scripts(Arrays.asList(pathOf(serverScript), pathOf(uiScript))).resources(resources).parameters(parameterMap);
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
        String serializedParams = serializeParameters();
        return this.name().toLowerCase() + (!serializedParams.isEmpty() ? "_" + hashOf(serializedParams) : "");
    }

    private String serializeParameters() {
        return parameterMap.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
    }

    private String hashOf(String content) {
        return Base64.encode(DigestUtils.md5(content))
                .replaceAll("/", "A")
                .replaceAll("\\.", "B")
                .replaceAll("\\+", "C")
                .replaceAll("=", "D");
    }

}