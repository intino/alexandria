package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Json;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.schemas.AppDirectoryApplication;
import io.intino.alexandria.schemas.AppDirectoryInfo;
import io.intino.alexandria.ui.displays.notifiers.AppDirectoryNotifier;
import io.intino.alexandria.ui.utils.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.stream.Collectors;

public class AppDirectory<DN extends AppDirectoryNotifier, B extends Box> extends AbstractAppDirectory<B> {
    private String icon;
    private java.util.List<Application> applicationList = new ArrayList<>();
    private String selectedApplication;

    public AppDirectory(B box) {
        super(box);
    }

    public AppDirectory<DN, B> icon(String icon) {
        _icon(icon);
        refresh();
        return this;
    }

    public AppDirectory<DN, B> source(java.io.File file, String separator) {
        _source(file, separator);
        refresh();
        return this;
    }

    public AppDirectory<DN, B> source(URL url, String separator) {
        _source(url, separator);
        refresh();
        return this;
    }

    public AppDirectory<DN, B> source(java.util.List<Application> applicationList) {
        _source(applicationList);
        refresh();
        return this;
    }

    protected AppDirectory<DN, B> _source(java.io.File file, String separator) {
        try {
            _source(Files.readAllLines(file.toPath(), StandardCharsets.UTF_8), separator);
        } catch (IOException e) {
            Logger.error(e);
        }
        return this;
    }

    protected AppDirectory<DN, B> _source(URL url, String separator) {
        try {
            _source(IOUtils.readLines(url.openStream(), StandardCharsets.UTF_8), separator);
        } catch (IOException e) {
            Logger.error(e);
        }
        return this;
    }

    public AppDirectory<DN, B> _source(List<String> lines, String separator) {
        _source(applicationsOf(lines, separator));
        return this;
    }

    public AppDirectory<DN, B> selected(String application) {
        this.selectedApplication = application;
        return this;
    }

    protected AppDirectory<DN, B> _source(java.util.List<Application> applicationList) {
        this.applicationList = applicationList;
        return this;
    }

    protected Application _add(String name, String url) {
        Application application = new Application(name, url);
        applicationList.add(application);
        return application;
    }

    protected AppDirectory<DN, B> _icon(String icon) {
        this.icon = icon;
        return this;
    }

    @Override
    public void init() {
        super.init();
        refresh();
    }

    @Override
    public void refresh() {
        super.refresh();
        notifier.refresh(new AppDirectoryInfo().icon(icon).applications(applications()));
    }

    private List<AppDirectoryApplication> applications() {
        return applicationList.stream().map(this::schemaOf).collect(Collectors.toList());
    }

    private AppDirectoryApplication schemaOf(Application application) {
        return new AppDirectoryApplication().name(application.label(language())).url(application.url).selected(isSelected(application));
    }

    private Boolean isSelected(Application application) {
        return selectedApplication != null && selectedApplication.equalsIgnoreCase(application.name);
    }

    private List<Application> applicationsOf(List<String> lines, String separator) {
        return lines.stream().map(l -> applicationOf(l, separator)).sorted(Comparator.comparing(a -> a.label(language()))).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private Application applicationOf(String line, String separator) {
        String[] data = line.split(separator);
        Application result = new Application(data[0], data[1]);
        result.translations = data.length > 2 ? Json.fromString(data[2], Map.class) : Collections.emptyMap();
        return result;
    }

    public static class Application {
        public String name;
        public String url;
        public java.util.Map<String, String> translations = new HashMap<>();

        public Application(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public String label(String language) {
            return translation(language);
        }

        public Application translation(String language, String translation) {
            this.translations.put(language, translation);
            return this;
        }

        protected String translation(String language) {
            return translations.getOrDefault(language, name);
        }
    }
}