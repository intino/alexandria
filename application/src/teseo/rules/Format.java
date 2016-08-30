package teseo.rules;

import tara.lang.model.Rule;

public enum Format implements Rule<Enum> {

    json, xml;

    @Override
    public boolean accept(Enum value) {
        return value instanceof Format;
    }
}
