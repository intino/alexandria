package teseo.rules;

import tara.lang.model.Rule;

public enum ResponseCodes implements Rule<Enum> {

    SuccessOk("200"),
    SuccessCreated("201"),
    SuccessNoContent("0"),
    ErrorBadRequest("400"),
    ErrorUnauthorized("401"),
    ErrorForbidden("403"),
    ErrorNotFound("404"),
    ErrorConflict("409");

    private String code;

    ResponseCodes(String code) {
        this.code = code;
    }

    public String value(){
        return code;
    }

    @Override
    public boolean accept(Enum value) {
        return value instanceof ResponseCodes;
    }
}
