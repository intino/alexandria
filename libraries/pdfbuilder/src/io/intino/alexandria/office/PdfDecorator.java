package io.intino.alexandria.office;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

public class PdfDecorator {

    private final File pdf;
    private PdfMetadata metadata;

    public PdfDecorator(File pdf) {
        this.pdf = pdf;
    }

    public void save(File destination) throws IOException {
        try(PDDocument document = PDDocument.load(pdf)) {
            if(metadata != null) document.setDocumentInformation(metadata.info());
            document.save(destination);
        }
    }

    public PdfDecorator setMetadata(PdfMetadata metadata) {
        this.metadata = metadata;
        return this;
    }
}
