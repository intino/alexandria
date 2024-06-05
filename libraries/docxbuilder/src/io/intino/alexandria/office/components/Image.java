package io.intino.alexandria.office.components;

import io.intino.alexandria.logger.Logger;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingConstants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Objects.requireNonNull;

public class Image {

    private static final int MIN_DPI = 72;

    private final byte[] data;
    private final ImageInfo info;

    public Image(File file) throws IOException {
        this(Files.readAllBytes(file.toPath()));
    }

    public Image(byte[] data) {
        this.data = requireNonNull(data);
        this.info = info(data);
    }

    private ImageInfo info(byte[] data) {
        try {
            return Imaging.getImageInfo(data);
        } catch (IOException | ImageReadException e) {
            try {
                Map<String, Object> params = new TreeMap<>();
                params.put(ImagingConstants.PARAM_KEY_READ_THUMBNAILS, false);
                return Imaging.getImageInfo(data, params);
            }
            catch (IOException | ImageReadException e1) {
                Logger.error(e);
                return null;
            }
        }
	}

    public byte[] data() {
        return data;
    }

    public float aspect() {
        return (float) getWidth() / (float) getHeight();
    }

    public int getHeight() {
        return info != null ? info.getHeight() : 768;
    }

    public int getPhysicalHeightDpi() {
        return info != null ? Math.max(info.getPhysicalHeightDpi(), MIN_DPI) : 768;
    }

    public int getPhysicalWidthDpi() {
        return info != null ? Math.max(info.getPhysicalWidthDpi(), MIN_DPI) : 1024;
    }

    public int getWidth() {
        return info != null ? info.getWidth() : 1024;
    }
}
