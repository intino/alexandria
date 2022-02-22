package io.intino.alexandria.office;

import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.util.Objects.requireNonNull;

public class Image {

    private static final int MIN_DPI = 72;

    private final File file;
    private final ImageInfo info;

    public Image(File file) throws IOException {
        this.file = requireNonNull(file);
        try {
            this.info = Imaging.getImageInfo(file);
        } catch (ImageReadException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] pixels() {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public float aspect() {
        return (float) getWidth() / (float) getHeight();
    }

    public int getHeight() {
        return info.getHeight();
    }

    public int getPhysicalHeightDpi() {
        return Math.max(info.getPhysicalHeightDpi(), MIN_DPI);
    }

    public int getPhysicalWidthDpi() {
        return Math.max(info.getPhysicalWidthDpi(), MIN_DPI);
    }

    public int getWidth() {
        return info.getWidth();
    }

    public File file() {
        return file;
    }

    public ImageInfo info() {
        return info;
    }
}
