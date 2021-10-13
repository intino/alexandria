package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.Resource;
import io.intino.alexandria.UiFrameworkBox;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.File;
import io.intino.alexandria.ui.displays.components.ImageEditable;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class MultipleExamplesMold extends AbstractMultipleExamplesMold<UiFrameworkBox> {

    public MultipleExamplesMold(UiFrameworkBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        multiple1.addAll(List.of("Value 1", "Value 2", "Value 3"));
        multiple2.add("Value 1", "lorem ipsum dolor sit amet");
        multiple2.add("Value 2");
        multiple2.add("Value 3");
        files().forEach(f -> multiple3.add(f));
        multiple3.onChange(event -> {
            ImageEditable<?, ?> display = event.component();
            display.value(save(event.item()));
        });
        multiple3.onRemove(event -> {
            javaFiles().get(event.index()).delete();
        });
    }

    private List<java.io.File> javaFiles() {
        java.io.File[] files = new java.io.File("/tmp/multiple3").listFiles();
        if (files == null) return Collections.emptyList();
        return Arrays.stream(files).collect(toList());
    }

    private List<File> files() {
        return javaFiles().stream().map(f -> {
            try {
                return new File().filename(f.getName()).value(f.toPath().toUri().toURL());
            } catch (MalformedURLException e) {
                Logger.error(e);
                return null;
            }
        }).filter(Objects::nonNull).collect(toList());
    }

    private URL save(Resource resource) {
        try {
            new java.io.File("/tmp/multiple3/").mkdirs();
            Path result = Path.of("/tmp/multiple3/" + resource.name());
            Files.write(result, resource.bytes());
            return result.toUri().toURL();
        } catch (IOException e) {
            Logger.error(e);
            return null;
        }
    }
}