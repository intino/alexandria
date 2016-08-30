package teseo.exceptions;


public class ErrorUnauthorized extends TeseoException {

    public ErrorUnauthorized(String message) {
        super("401", message);
    }
}
