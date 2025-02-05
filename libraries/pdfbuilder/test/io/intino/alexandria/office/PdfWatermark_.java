package io.intino.alexandria.office;

import java.io.File;
import java.io.IOException;

public class PdfWatermark_ {

    public static void main(String[] args) throws IOException {
        var decorator = new PdfDecorator(new File("temp/document.pdf"));
        decorator.setOverlay(new PdfOverlay(PdfOverlay.Position.FOREGROUND, new File("temp/watermark.pdf")));
        decorator.save(new File("temp/pdf-with-watermark.pdf"));
    }
}
