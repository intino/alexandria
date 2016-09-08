package teseo.rules;

import tara.lang.model.Rule;

public enum ExceptionCodes implements Rule<Enum> {

    ErrorBadRequest("400"),
    ErrorUnauthorized("401"),
    ErrorForbidden("403"),
    ErrorNotFound("404"),
    ErrorConflict("409");

    private String code;

    ExceptionCodes(String code) {
        this.code = code;
    }

    public String value(){
        return code;
    }

    @Override
    public boolean accept(Enum value) {
        return value instanceof ExceptionCodes;
    }
}
