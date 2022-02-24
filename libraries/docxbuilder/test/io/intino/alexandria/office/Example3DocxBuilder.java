package io.intino.alexandria.office;

import org.apache.commons.imaging.ImageReadException;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static io.intino.alexandria.office.ImageView.WrapOption.ClampToPage;
import static io.intino.alexandria.office.ImageView.WrapOption.ClampToTemplate;

public class Example3DocxBuilder {

    public static final long EMU_PER_INCH = 914400L;
    public static final int DEFAULT_DPI = 72;

    public static void main(String[] args) throws IOException, ImageReadException {

        File imageFile = new File("C:/Users/naits/Downloads/mexico_c75c744c165d.jpg");

        ImageView imageView = new ImageView(new Image(imageFile));
        imageView.widthWrapping(ClampToPage);
        imageView.heightWrapping(ClampToTemplate);
        imageView.keepAspectRatio(true);

        new File("temp").mkdirs();
        DocxBuilder db = DocxBuilder.create(new File("temp/template.docx"));
        db.replace("Image_A", imageView);
        db.save(new File("temp/result.docx"));
    }

    // English metric units
    private static Dimension getSizeInEMU(int width, int height, int horizontalDpi, int verticalDpi) {
        if(horizontalDpi < 1) horizontalDpi = DEFAULT_DPI;
        if(verticalDpi < 1) verticalDpi = DEFAULT_DPI;
        long imageWidthEMU = (long)((width / (float)horizontalDpi) * EMU_PER_INCH); // cx
        long imageHeightEMU = (long)((height / (float)verticalDpi) * EMU_PER_INCH); // cy
        return new Dimension((int) imageWidthEMU, (int) imageHeightEMU);
    }
}
