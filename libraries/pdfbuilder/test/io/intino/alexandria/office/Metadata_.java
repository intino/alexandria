package io.intino.alexandria.office;

import java.io.File;
import java.io.IOException;

public class Metadata_ {

    public static final File FILE = new File("temp/pdf.pdf");

    public static void main(String[] args) throws IOException {
        PdfMetadata metadata = new PdfMetadata();
        metadata.setCustomValue("input", "{\"key\":\"value\"}");
        new PdfDecorator(FILE).setMetadata(metadata).save(FILE);

        metadata = new PdfMetadata(FILE);
        System.out.println(metadata.getCustomValue("input"));
    }
}
