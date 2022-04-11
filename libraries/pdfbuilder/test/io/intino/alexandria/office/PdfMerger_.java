package io.intino.alexandria.office;

import java.io.File;
import java.io.IOException;

public class PdfMerger_ {

    public static void main(String[] args) throws IOException {
        PdfMerger merger = new PdfMerger();
        merger.useOnlyTempFiles(true);
        merger.append(new File("temp/pdfbuilder/pdf2.pdf"));
        merger.append(new File("temp/pdfbuilder/pdf3.pdf"));
        merger.append(new File("temp/pdfbuilder/pdf1.pdf"));
        merger.merge(new File("temp/pdfbuilder/destination.pdf"));
    }

}