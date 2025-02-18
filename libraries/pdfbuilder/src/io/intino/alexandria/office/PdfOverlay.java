package io.intino.alexandria.office;

import java.io.File;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class PdfOverlay {

    private Position position = Position.FOREGROUND;
    private final EnumMap<Target, File> documents = new EnumMap<>(Target.class);

    public PdfOverlay() {
    }

    public PdfOverlay(Position position) {
        this.position = position;
    }

    public PdfOverlay(File defaultOverlayDocument) {
        this(Position.FOREGROUND, defaultOverlayDocument);
    }

    public PdfOverlay(Position position, File defaultOverlayDocument) {
        this.position = position;
        set(Target.DEFAULT, defaultOverlayDocument);
    }

    public PdfOverlay setPosition(Position position) {
        this.position = requireNonNull(position);
        return this;
    }

    public PdfOverlay set(Target target, File document) {
        this.documents.put(target, requireNonNull(document));
        return this;
    }

    public Position position() {
        return position;
    }

    public Map<Target, File> documents() {
        return Collections.unmodifiableMap(documents);
    }

    public File get(Target target) {
        return documents.get(target);
    }

    public enum Position {
        FOREGROUND, BACKGROUND
    }

    public enum Target {
        DEFAULT, ALL_PAGES, FIRST_PAGE, LAST_PAGE, EVEN_PAGES, ODD_PAGES
    }
}
