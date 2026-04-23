package io.intino.alexandria.ui.displays.templates;

import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.components.text.TextPatternMatcher;

import java.util.HashMap;
import java.util.Map;

public class TextExamplesMold extends AbstractTextExamplesMold<AlexandriaUiBox> {

    public TextExamplesMold(AlexandriaUiBox box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        text1.value("lorem ipsum");
        text2.value("lorem ipsum");
        text3.value("lorem ipsum");
        text3.focus();
        //text3.onFocus((event) -> text3.notifyUser("Field focused", UserMessage.Type.Info));
        //text3.onBlur((event) -> text3.notifyUser("Field losses focus", UserMessage.Type.Info));
        text3.onChange((event) -> text3.notifyUser(String.format("Value: %s", (String)event.value()), UserMessage.Type.Info));
        text4.onChange((event) -> text3.notifyUser(String.format("Value: %s", (String)event.value()), UserMessage.Type.Info));
        text4.patternMatcher(new DelphiPatternMatcher());
        text5.error("Field value is wrong");
        textCode2.onChange((event) -> textCode2.notifyUser(String.format("Value: %s", (String)event.value()), UserMessage.Type.Info));
        textCode3.onChange((event) -> textCode2.notifyUser(String.format("Value: %s", (String)event.value()), UserMessage.Type.Info));
    }

    public class DelphiPatternMatcher implements TextPatternMatcher {

        @Override
        public Map<String, String> validationRules() {
            return new HashMap<>() {{
                put("M", "[A-Z]");
                put("m", "[a-z]");
                put("N", "[A-Z0-9]");
                put("n", "[A-Z0-9]| ");
                put("L", "[A-Za-z]");
                put("l", "[A-Za-z]| ");
                put("A", "[A-Za-z0-9]");
                put("a", "[A-Za-z0-9]| ");
                put("C", "[A-Za-z0-9-@#%&/=?¿¡!;:\\.,\\\\@·\"$\\(\\)\\|\\^*¨çÇ+\\{\\}]");
                put("c", "[A-Za-z0-9-@#%&/=?¿¡!;:\\.,\\\\@·\"$\\(\\)\\|\\^*¨çÇ+\\{\\}]| ");
                put("0", "[0-9]");
                put("9", "[0-9]| ");
                put("#", "[0-9]|+|-");
            }};
        }

        @Override
        public boolean allowIncompleteValues() {
            return true;
        }

        @Override
        public boolean addSpecialCharactersToValue(String pattern) {
            String[] content = pattern.split(";");
            return content.length == 1 || (content.length > 2 && content[1].equals("1"));
        }

        @Override
        public Character maskCharacter(String pattern) {
            String[] content = pattern.split(";");
            return content.length > 2 ? content[2].charAt(0) : '_';
        }
    }

}