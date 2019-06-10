package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.schemas.DashboardInfo;
import io.intino.alexandria.ui.displays.notifiers.DashboardNotifier;
import org.apache.commons.codec.digest.DigestUtils;
import spark.utils.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Dashboard<DN extends DashboardNotifier, B extends Box> extends AbstractDashboard<B> {
    private String homeDirectory;
    private URL script;
    private java.util.Map<String, String> parameterMap = new HashMap<>();

    private static int freePort = 4000;
    private static int startPort = 4000;
    private static java.util.Map<String, ProcessInstance> processMap = new HashMap<>();
    private static final String PortTag = "port";
    private static final int MaxPort = 100000000;

    public Dashboard(B box) {
        super(box);
    }

    public Dashboard script(URL script) {
        this.script = script;
        return this;
    }

    public Dashboard startPort(int port) {
        startPort = port;
        return this;
    }

    public Dashboard homeDirectory(String directory) {
        homeDirectory = directory.equalsIgnoreCase("$HOME") ? System.getProperty("user.home") : directory;
        return this;
    }

    public Dashboard add(String parameter, String value) {
        parameterMap.put(parameter, value);
        return this;
    }

    @Override
    public void refresh() {
        try {
            notifier.showLoading();
            String location = (script != null && homeDirectory != null) ? execute() : null;
            notifier.refresh(new DashboardInfo().location(location));
        }
        catch (RuntimeException ex) {
            notifier.refreshError(ex.getMessage());
        }
    }

    private String execute() {
        int port = freePort();
        if (port == -1) {
            notifier.refreshError("no ports available");
            return null;
        }
        execute(buildScript(), port);
        return session().browser().baseUrl() + ":" + port;
    }

    private void execute(String script, int port) {
        String hash = new String(DigestUtils.md5(script));
        if (processMap.containsKey(hash)) return;
        Process process = run(hash, script, port);
        processMap.put(hash, new ProcessInstance().port(port).process(process));
    }

    private Process run(String hash, String script, int port) {
        try {
            java.io.File file = new java.io.File(homeDirectory + "/" + hash + ".R");
            Files.write(file.toPath(), replaceTag(script, PortTag, String.valueOf(port)).getBytes());
            return new ProcessBuilder("Rscript", file.getAbsolutePath()).start();
        } catch (IOException e) {
            Logger.error(e);
            return null;
        }
    }

    private String buildScript() {
        try {
            String content = IOUtils.toString(script.openStream());
            parameterMap.forEach((key, value) -> replaceTag(content, key, value));
            return content;
        } catch (IOException e) {
            Logger.error(e);
            return null;
        }
    }

    private String replaceTag(String content, String tag, String value) {
        return content.replaceAll(":" + tag + ":", value);
    }

    private static class ProcessInstance {
        int port;
        Process process;

        public ProcessInstance port(int port) {
            this.port = port;
            return this;
        }

        public ProcessInstance process(Process process) {
            this.process = process;
            return this;
        }
    }

    private int freePort() {
        List<Integer> busyPorts = processMap.values().stream().map(v -> v.port).collect(Collectors.toList());

        freePort = startPort;
        while (busyPorts.contains(freePort) && freePort < MaxPort) freePort++;

        return freePort <= MaxPort ? freePort : -1;
    }

}