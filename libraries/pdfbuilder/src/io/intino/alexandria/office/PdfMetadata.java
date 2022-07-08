package io.intino.alexandria.office;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Set;

public class PdfMetadata {

    private final PDDocumentInformation info;

    public PdfMetadata() {
        this.info = new PDDocumentInformation();
    }

    public PdfMetadata(File pdf) throws IOException {
        PDDocumentInformation info;
        try(PDDocument doc = PDDocument.load(pdf)) {
            info = doc.getDocumentInformation();
        }
        this.info = info;
    }

    public String getCustomValue(String fieldName) {
        return info.getCustomMetadataValue(fieldName);
    }

    public PdfMetadata setCustomValue(String fieldName, String fieldValue) {
        info.setCustomMetadataValue(fieldName, fieldValue);
        return this;
    }

    public Object getPropertyStringValue(String propertyKey) {
        return info.getPropertyStringValue(propertyKey);
    }

    public String getTitle() {
        return info.getTitle();
    }

    public PdfMetadata setTitle(String title) {
        info.setTitle(title);
        return this;
    }

    public String getAuthor() {
        return info.getAuthor();
    }

    public PdfMetadata setAuthor(String author) {
        info.setAuthor(author);
        return this;
    }

    public String getSubject() {
        return info.getSubject();
    }

    public PdfMetadata setSubject(String subject) {
        info.setSubject(subject);
        return this;
    }

    public String getKeywords() {
        return info.getKeywords();
    }

    public PdfMetadata setKeywords(String keywords) {
        info.setKeywords(keywords);
        return this;
    }

    public String getCreator() {
        return info.getCreator();
    }

    public PdfMetadata setCreator(String creator) {
        info.setCreator(creator);
        return this;
    }

    public String getProducer() {
        return info.getProducer();
    }

    public PdfMetadata setProducer(String producer) {
        info.setProducer(producer);
        return this;
    }

    public Calendar getCreationDate() {
        return info.getCreationDate();
    }

    public PdfMetadata setCreationDate(Calendar date) {
        info.setCreationDate(date);
        return this;
    }

    public Calendar getModificationDate() {
        return info.getModificationDate();
    }

    public PdfMetadata setModificationDate(Calendar date) {
        info.setModificationDate(date);
        return this;
    }

    public String getTrapped() {
        return info.getTrapped();
    }

    public Set<String> getMetadataKeys() {
        return info.getMetadataKeys();
    }

    public PdfMetadata setTrapped(String value) {
        info.setTrapped(value);
        return this;
    }

    PDDocumentInformation info() {
        return info;
    }
}
