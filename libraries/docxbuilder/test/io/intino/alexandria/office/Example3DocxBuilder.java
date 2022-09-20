package io.intino.alexandria.office;

import io.intino.alexandria.office.components.Image;
import io.intino.alexandria.office.components.ImageView;
import org.apache.commons.imaging.ImageReadException;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static io.intino.alexandria.office.components.ImageView.WrapOption.ClampToPage;
import static io.intino.alexandria.office.components.ImageView.WrapOption.ClampToTemplate;

public class Example3DocxBuilder {

    public static final long EMU_PER_INCH = 914400L;
    public static final int DEFAULT_DPI = 72;

    public static void main(String[] args) throws IOException, ImageReadException {

        ImageView img1 = createImageView(new File("temp/image1.png"));
        ImageView img2 = createImageView(new File("temp/image2.jpg"));
        ImageView img3 = createImageView(new File("temp/image3.jpg"));
        ImageView img4 = createImageView(new File("temp/image4.jpg"));

        new File("temp").mkdirs();
        DocxBuilder db = DocxBuilder.create(new File("temp\\PMBC.docx"));

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

        db.replace("6ff2596a6db22", img1);

        db.replace("edfc5e774433", img1);
        db.replace("c1cbd3688fc6", img4);

        db.save(new File("temp/result.docx"));
    }

    private static ImageView createImageView(File file) throws IOException {
        return createImageView(file, ClampToPage, ClampToTemplate, true);
    }

    private static ImageView createImageView(File file,
                                             ImageView.WrapOption widthWrap, ImageView.WrapOption heightWrap,
                                             boolean keepAspectRatio) throws IOException {
        ImageView imageView = new ImageView(new Image(file));
        imageView.widthWrapping(widthWrap);
        imageView.heightWrapping(heightWrap);
        imageView.keepAspectRatio(keepAspectRatio);
        return imageView;
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
