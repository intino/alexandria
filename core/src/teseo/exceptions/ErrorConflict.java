package teseo.exceptions;


public class ErrorConflict extends TeseoException {

    public ErrorConflict(String message) {
        super("409", message);
    }
}
