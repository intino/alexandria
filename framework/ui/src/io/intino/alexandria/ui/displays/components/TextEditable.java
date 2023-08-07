package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.KeyPressEventData;
import io.intino.alexandria.schemas.TextEditablePattern;
import io.intino.alexandria.schemas.TextEditablePatternRule;
import io.intino.alexandria.ui.displays.components.editable.Editable;
import io.intino.alexandria.ui.displays.components.text.DefaultTextPatternMatcher;
import io.intino.alexandria.ui.displays.components.text.TextPatternMatcher;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.events.KeyPressEvent;
import io.intino.alexandria.ui.displays.events.KeyPressListener;
import io.intino.alexandria.ui.displays.notifiers.TextEditableNotifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TextEditable<DN extends TextEditableNotifier, B extends Box> extends AbstractTextEditable<DN, B> implements Editable<DN, B> {
    private boolean readonly;
    private String pattern = null;
    private TextPatternMatcher patternMatcher;
    private ChangeListener changeListener = null;
    private KeyPressListener keyPressListener = null;
    private KeyPressListener enterPressListener = null;

    private static final String EnterKeyCode = "Enter";

    public TextEditable(B box) {
        super(box);
    }

    @Override
    public void didMount() {
        super.didMount();
        if (patternMatcher != null) notifier.refreshPattern(patternSchema());
    }

    @Override
    public boolean readonly() {
        return readonly;
    }

    @Override
    public void reload() {
        notifier.refresh(value());
    }

    @Override
    public TextEditable<DN, B> focus() {
        notifier.refreshFocused(true);
        return this;
    }

    @Override
    public TextEditable<DN, B> readonly(boolean readonly) {
        _readonly(readonly);
        notifier.refreshReadonly(readonly);
        return this;
    }

    @Override
    public TextEditable<DN, B> onChange(ChangeListener listener) {
        this.changeListener = listener;
        return this;
    }

    public TextEditable<DN, B> onKeyPress(KeyPressListener listener) {
        this.keyPressListener = listener;
        return this;
    }

    public TextEditable<DN, B> onEnterPress(KeyPressListener listener) {
        this.enterPressListener = listener;
        return this;
    }

    public TextEditable<DN, B> patternMatcher(TextPatternMatcher matcher) {
        this.patternMatcher = matcher;
        return this;
    }

    public void update(String value) {
        if (!notifyChange(value)) return;
        super.value(value);
    }

    public boolean notifyChange(String value) {
        if (invalid(value)) return false;
        _value(value);
        if (changeListener != null) changeListener.accept(new ChangeEvent(this, value()));
        return true;
    }

    @Override
    public String value() {
        String value = super.value();
        return !isNullValue(value) ? adapt(value) : null;
    }

    public void notifyBlur(String value) {
        if (patternMatcher == null) return;
        if (invalid(value)) notifier.refresh(value());
        else if (!value.equals(value())) notifyChange(value);
    }

    public void notifyKeyPress(KeyPressEventData data) {
        if (!invalid(data.value())) _value(data.value());
        KeyPressEvent event = new KeyPressEvent(this, data.value(), data.keyCode());
        if (keyPressListener != null) keyPressListener.accept(event);
        if (enterPressListener != null && data.keyCode().equals(EnterKeyCode)) enterPressListener.accept(event);
    }

    protected TextEditable<DN, B> _readonly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    protected TextEditable<DN, B> _pattern(String pattern) {
        this.pattern = pattern;
        if (patternMatcher == null) patternMatcher = new DefaultTextPatternMatcher();
        return this;
    }

    private TextEditablePattern patternSchema() {
        return new TextEditablePattern().value(pattern).rules(rules()).maskCharacter(String.valueOf(patternMatcher.maskCharacter(pattern)));
    }

    private List<TextEditablePatternRule> rules() {
        return patternMatcher.validationRules().entrySet().stream().map(e -> rule(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    private TextEditablePatternRule rule(String name, String value) {
        return new TextEditablePatternRule().name(name).value(value);
    }

    private boolean invalid(String value) {
        if (patternMatcher == null) return false;
        if (patternMatcher.allowIncompleteValues()) return false;
        if (isNullValue(value)) return false;
        return value.contains(String.valueOf(patternMatcher.maskCharacter(pattern)));
    }

    private boolean isNullValue(String value) {
        if (patternMatcher == null) return false;
        String result = value.replace(String.valueOf(patternMatcher.maskCharacter(pattern)), "");
        return result.isEmpty() || containsAll(pattern, result);
    }

    private boolean containsAll(String pattern, String result) {
        for (int i=0; i<result.length(); i++) {
            if (!pattern.contains(result.substring(i, i+1))) return false;
        }
        return true;
    }

    private String adapt(String value) {
        if (patternMatcher == null) return value;
        String result = patternMatcher.addSpecialCharactersToValue(pattern) ? value : valueWithoutSpecialChars(value);
        return patternMatcher.allowIncompleteValues() ? result.replace(String.valueOf(patternMatcher.maskCharacter(pattern)), "") : result;
    }

    private String valueWithoutSpecialChars(String value) {
        List<Integer> positions = specialCharsPositions();
        StringBuilder result = new StringBuilder();
        for (int i=0; i<value.length(); i++) {
            if (positions.contains(i)) continue;
            result.append(value, i, i+1);
        }
        return result.toString();
    }

    private List<Integer> specialCharsPositions() {
        Set<String> set = patternMatcher.validationRules().keySet();
        List<Integer> positions = new ArrayList<>();
        String pattern = this.pattern.replace("\\", "");
        for (int i=0; i<pattern.length(); i++) {
            if (!set.contains(pattern.substring(i, i+1))) positions.add(i);
        }
        return positions;
    }

}