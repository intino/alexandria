package io.intino.alexandria.office;

import static java.util.Objects.requireNonNull;

public class ImageView {

    private final Image image;
    private WrapOption widthWrapping = WrapOption.ClampToTemplate;
    private WrapOption heightWrapping = WrapOption.ClampToTemplate;
    private boolean keepAspectRatio = true;

    public ImageView(Image image) {
        this.image = requireNonNull(image);
    }

    public Image image() {
        return image;
    }

    public WrapOption widthWrapping() {
        return widthWrapping;
    }

    public ImageView widthWrapping(WrapOption widthWrapping) {
        this.widthWrapping = widthWrapping;
        return this;
    }

    public WrapOption heightWrapping() {
        return heightWrapping;
    }

    public ImageView heightWrapping(WrapOption heightWrapping) {
        this.heightWrapping = heightWrapping;
        return this;
    }

    public boolean keepAspectRatio() {
        return keepAspectRatio;
    }

    public ImageView keepAspectRatio(boolean keepAspectRatio) {
        this.keepAspectRatio = keepAspectRatio;
        return this;
    }

    public enum WrapOption {
        /** The image axis will be clamped to the page's max width or height */
        ClampToPage,
        /** The image size will be resize to match the template's extent */
        ClampToTemplate,
    }
}
