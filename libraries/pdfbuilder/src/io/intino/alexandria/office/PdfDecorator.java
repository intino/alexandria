package io.intino.alexandria.office;

import org.apache.pdfbox.multipdf.Overlay;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static io.intino.alexandria.office.PdfOverlay.Target.*;

public class PdfDecorator {

    private final File pdf;
    private PdfMetadata metadata;
    private PdfOverlay overlay;

    public PdfDecorator(File pdf) {
        this.pdf = pdf;
    }

    public void save(File destination) throws IOException {
        File sourcePdf = overlay == null ? pdf : applyOverlay();
        try(PDDocument document = PDDocument.load(sourcePdf)) {
            if(metadata != null) document.setDocumentInformation(metadata.info());
            document.save(destination);
        }
    }

    private File applyOverlay() throws IOException {
        File temp = File.createTempFile("alexandria.office.pdfdecorator", ".pdf");
        temp.deleteOnExit();

        try(PDDocument sourceDocument = PDDocument.load(pdf);
            Overlay overlayApplier = new Overlay()) {

            overlayApplier.setInputPDF(sourceDocument);

            Optional.ofNullable(overlay.get(DEFAULT)).map(File::getAbsolutePath).ifPresent(overlayApplier::setDefaultOverlayFile);
            Optional.ofNullable(overlay.get(ALL_PAGES)).map(File::getAbsolutePath).ifPresent(overlayApplier::setAllPagesOverlayFile);
            Optional.ofNullable(overlay.get(FIRST_PAGE)).map(File::getAbsolutePath).ifPresent(overlayApplier::setFirstPageOverlayFile);
            Optional.ofNullable(overlay.get(LAST_PAGE)).map(File::getAbsolutePath).ifPresent(overlayApplier::setLastPageOverlayFile);
            Optional.ofNullable(overlay.get(EVEN_PAGES)).map(File::getAbsolutePath).ifPresent(overlayApplier::setEvenPageOverlayFile);
            Optional.ofNullable(overlay.get(ODD_PAGES)).map(File::getAbsolutePath).ifPresent(overlayApplier::setOddPageOverlayFile);

            overlayApplier.setOverlayPosition(overlay.position() == PdfOverlay.Position.FOREGROUND
                    ? Overlay.Position.FOREGROUND
                    : Overlay.Position.BACKGROUND);

            try(PDDocument outputDocument = overlayApplier.overlay(Collections.emptyMap())) {
                outputDocument.save(temp);
            }
        }

        return temp;
    }

    public PdfDecorator setMetadata(PdfMetadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public PdfDecorator setOverlay(PdfOverlay overlay) {
        this.overlay = overlay;
        return this;
    }
}
