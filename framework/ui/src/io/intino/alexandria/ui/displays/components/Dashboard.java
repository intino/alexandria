package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Base64;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.driver.Program;
import io.intino.alexandria.drivers.shiny.Driver;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.schemas.DashboardInfo;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.components.dashboard.Proxy;
import io.intino.alexandria.ui.displays.notifiers.DashboardNotifier;
import org.apache.commons.codec.digest.DigestUtils;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static io.intino.alexandria.ui.utils.IOUtils.readAllLines;

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
        Proxy proxy = new Proxy((AlexandriaUiBox)box(), session().browser().baseUrl(), program);

        proxy.listen();
        if (!driver.isPublished(program))
            driver.publish(program());

        return proxy.dashboardUrl().toString();
    }

    private Program program() {
        String name = programName();
        List<Path> resources = resourceList.stream().map(this::pathOf).collect(Collectors.toList());
        return new Program().name(name).algorithms(Arrays.asList(pathOf(serverScript), pathOf(uiScript))).resources(resources);
    }

    private Path pathOf(URL serverScript) {
        try {
            return Paths.get(serverScript.toURI());
        } catch (URISyntaxException e) {
            Logger.error(e);
            return null;
        }
    }

    private String replaceTag(String content, String tag, String value) {
        return content.replaceAll(":" + tag + ":", value);
    }

    private String programName() {
        String serverScriptContent = readAllLines(serverScript);
        String uiScriptContent = readAllLines(uiScript);
        String content = replaceParameters(serverScriptContent + uiScriptContent);
        return hashOf(content);
    }

    private String replaceParameters(String script) {
        parameterMap.forEach((key, value) -> replaceTag(script, key, value));
        return script;
    }

    private String hashOf(String script) {
        return Base64.encode(DigestUtils.md5(script));
    }

}