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

        ImageView img1 = new ImageView(new Image(imageFile));
        img1.widthWrapping(ClampToPage);
        img1.heightWrapping(ClampToTemplate);
        img1.keepAspectRatio(true);

        ImageView img2 = new ImageView(new Image(new File("temp/image2.jpg"))).widthWrapping(ClampToPage);
        ImageView img3 = new ImageView(new Image(new File("temp/image3.jpg"))).widthWrapping(ClampToPage);
        ImageView img4 = new ImageView(new Image(new File("temp/image4.jpg"))).widthWrapping(ClampToPage);

        new File("temp").mkdirs();
        DocxBuilder db = DocxBuilder.create(new File("temp\\AWEB$General.docx"));
        db.replace("00f44bf7194a", img1);
        db.replace("51701946eba6", img2);
        db.replace("db4490dc705a", img3);
        db.replace("0d960fff6084", img4);
        db.replace("4a92e2403cb3", img2);
        db.replace("1e98781d6845", img1);
        db.replace("d9187186e239", img4);
        db.replace("74111d555837", img2);
        db.replace("043470d5ce8e", img3);
        db.replace("60dc96b1a50d", img1);
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
