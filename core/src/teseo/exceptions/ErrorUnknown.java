package teseo.exceptions;

public class ErrorUnknown extends TeseoException {

    public ErrorUnknown(String message) {
        super("500", message);
    }

}
