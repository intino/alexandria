package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.schemas.BoardApplication;
import io.intino.alexandria.schemas.BoardInfo;
import io.intino.alexandria.ui.displays.notifiers.BoardNotifier;
import io.intino.alexandria.ui.utils.IOUtils;
import org.glassfish.tyrus.core.wsadl.model.Application;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Board<DN extends BoardNotifier, B extends Box> extends AbstractBoard<B> {
    private String icon;
    private java.util.List<BoardApplication> applicationList = new ArrayList<>();

    public Board(B box) {
        super(box);
    }

    protected Board<DN, B> icon(String icon) {
        _icon(icon);
        refresh();
        return this;
    }

    public Board<DN, B> source(java.io.File file, String separator) {
        _source(file, separator);
        refresh();
        return this;
    }

    public Board<DN, B> source(URL url, String separator) {
        _source(url, separator);
        refresh();
        return this;
    }

    protected Board<DN, B> source(java.util.List<BoardApplication> applicationList) {
        _source(applicationList);
        refresh();
        return this;
    }

    protected Board<DN, B> _source(java.io.File file, String separator) {
        try {
            _source(Files.readAllLines(file.toPath(), StandardCharsets.UTF_8), separator);
        } catch (IOException e) {
            Logger.error(e);
        }
        return this;
    }

    protected Board<DN, B> _source(URL url, String separator) {
        try {
            _source(IOUtils.readLines(url.openStream(), StandardCharsets.UTF_8), separator);
        } catch (IOException e) {
            Logger.error(e);
        }
        return this;
    }

    public Board<DN, B> _source(List<String> lines, String separator) {
        _source(applicationsOf(lines, separator));
        return this;
    }

    protected Board<DN, B> _source(java.util.List<BoardApplication> applicationList) {
        this.applicationList = applicationList;
        return this;
    }

    protected Board<DN, B> _add(String name, String url) {
        applicationList.add(new BoardApplication().name(name).url(url));
        return this;
    }

    protected Board<DN, B> _icon(String icon) {
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
        notifier.refresh(new BoardInfo().icon(icon).applications(applicationList));
    }

    private List<BoardApplication> applicationsOf(List<String> lines, String separator) {
        return lines.stream().map(l -> applicationOf(l, separator)).collect(Collectors.toList());
    }

    private BoardApplication applicationOf(String line, String separator) {
        BoardApplication result = new BoardApplication();
        String[] data = line.split(separator);
        result.name(data[0]);
        result.url(data[1]);
        return result;
    }

}