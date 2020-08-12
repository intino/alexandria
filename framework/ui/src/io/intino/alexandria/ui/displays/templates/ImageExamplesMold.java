package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.Resource;
import io.intino.alexandria.ui.AlexandriaUiBox;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageExamplesMold extends AbstractImageExamplesMold<AlexandriaUiBox> {

    public ImageExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        image3.onChange(event -> {
            try {
                Resource value = event.value();
                File file = value != null ? toFile("/tmp/updated-test-image.png", value) : null;
                image3.value(file != null ? file.toURI().toURL() : null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static File toFile(String path, Resource resource) throws IOException {
        File file = new File(path);
        FileOutputStream stream = new FileOutputStream(file, false);
        stream.write(toByteArray(resource));
        stream.close();
        return file;
    }

    private static byte[] toByteArray(Resource resource) throws IOException {
        InputStream stream = resource.stream();
        byte[] targetArray = new byte[stream.available()];
        stream.read(targetArray);
        return targetArray;
    }
}