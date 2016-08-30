package teseo.exceptions;


public class ErrorForbbiden extends TeseoException {

    public ErrorForbbiden(String message) {
        super("403", message);
    }
}
