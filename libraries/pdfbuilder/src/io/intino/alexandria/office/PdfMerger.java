package io.intino.alexandria.office;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

public class PdfMerger {

    private final List<File> pdfsToMerge;
    private boolean useOnlyTempFiles = false;

    public PdfMerger() {
        this.pdfsToMerge = new ArrayList<>();
    }

    public PdfMerger append(File pdfFile) {
        if(pdfFile == null) throw new NullPointerException("PDF file cannot be null");
        this.pdfsToMerge.add(pdfFile);
        return this;
    }

    public int size() {
        return pdfsToMerge.size();
    }

    public void merge(File destinationPdf) throws IOException {
        if(size() == 0) throw new NoSuchFileException("No pdfs to merge");

        PDFMergerUtility merger = new PDFMergerUtility();
        merger.setDocumentMergeMode(PDFMergerUtility.DocumentMergeMode.OPTIMIZE_RESOURCES_MODE);
        merger.setDestinationFileName(destinationPdf.getAbsolutePath());

        for(File pdf : pdfsToMerge) {
            merger.addSource(pdf);
        }

        merger.mergeDocuments(getMemoryUsageSettings());
    }

    private MemoryUsageSetting getMemoryUsageSettings() {
        return useOnlyTempFiles ? MemoryUsageSetting.setupTempFileOnly() : MemoryUsageSetting.setupMainMemoryOnly();
    }

    public PdfMerger useOnlyTempFiles(boolean useOnlyTempFiles) {
        this.useOnlyTempFiles = useOnlyTempFiles;
        return this;
    }
}
