package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.ActionableInfo;
import io.intino.alexandria.schemas.ActionableSign;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.components.actionable.SignChecker;
import io.intino.alexandria.ui.displays.events.BeforeListener;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.Listener;
import io.intino.alexandria.ui.displays.notifiers.ActionableNotifier;
import io.intino.alexandria.ui.resources.Asset;
import io.intino.alexandria.ui.spark.UIFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public abstract class Actionable<DN extends ActionableNotifier, B extends Box> extends Component<DN, B> {
    private String title;
    private boolean readonly = false;
    private String icon;
    private Mode mode;
    private SignChecker signChecker;
    private String signReason;
    private BeforeListener beforeAffirmListener;
    private Listener cancelAffirmListener;

    public enum Mode { Link, Button, IconButton, MaterialIconButton, Toggle, IconToggle, MaterialIconToggle, SplitButton, IconSplitButton, MaterialIconSplitButton, AvatarIconButton }
    public enum Highlight { None, Outline, Fill }

    public Actionable(B box) {
        super(box);
    }

    @Override
    public void didMount() {
        super.didMount();
        if (isResourceIcon()) refreshIcon();
    }

    public String title() {
        return title;
    }

    public Actionable<DN, B> title(String title) {
        _title(title);
        refresh();
        return this;
    }

    public Actionable<DN, B> icon(String icon) {
        _icon(icon);
        refreshIcon();
        return this;
    }

    public Actionable<DN, B> readonly(boolean readonly) {
        if (readonly) disable();
        else enable();
        return this;
    }

    public boolean disabled() {
        return readonly;
    }

    public Actionable<DN, B> highlight(Highlight highlight) {
        notifier.refreshHighlight(highlight.name());
        return this;
    }

    public Actionable<DN, B> affirmed(String text) {
        notifier.refreshAffirmed(text);
        return this;
    }

    public Actionable<DN, B> enable() {
        _readonly(false);
        notifier.refreshReadonly(readonly);
        return this;
    }

    public Actionable<DN, B> disable() {
        _readonly(true);
        notifier.refreshReadonly(readonly);
        return this;
    }

    public void launch() {
        notifier.launch();
    }

    public void refresh() {
        ActionableInfo info = new ActionableInfo().title(title()).disabled(disabled());
        notifier.refresh(info);
    }

    public void notifyUser(String message) {
        notifyUser(message, UserMessage.Type.Info);
    }

    public void checkSign(ActionableSign info) {
        if (signChecker == null) notifier.checkSignResult(true);
        notifier.checkSignResult(signChecker.check(info.sign(), info.reason()));
        this.signReason = info.reason();
    }

    public void cancelAffirm(Boolean value) {
        if (cancelAffirmListener == null) return;
        cancelAffirmListener.accept(new Event(this));
    }

    public Actionable<DN, B> onBeforeAffirmed(BeforeListener listener) {
        this.beforeAffirmListener = listener;
        return this;
    }

    public Actionable<DN, B> onCancelAffirmed(Listener listener) {
        this.cancelAffirmListener = listener;
        return this;
    }

    public String signReason() {
        return this.signReason;
    }

    @Override
    public void init() {
        super.init();
        if (isResourceIcon()) refreshIcon();
    }

    public final void checkAffirmed() {
        notifier.refreshAffirmedRequired(beforeAffirmListener == null || beforeAffirmListener.accept(new Event(this)));
    }

    protected Actionable<DN, B> _title(String title) {
        this.title = title;
        return this;
    }

    protected Actionable<DN, B> _mode(Mode mode) {
        this.mode = mode;
        return this;
    }

    protected Actionable<DN, B> _icon(String icon) {
        this.icon = icon;
        return this;
    }

    protected Actionable<DN, B> _readonly(boolean value) {
        this.readonly = value;
        return this;
    }

    protected Actionable<DN, B> _signChecker(SignChecker checker) {
        this.signChecker = checker;
        return this;
    }

    UIFile defaultFile() {
        return new UIFile() {
            @Override
            public String label() {
                return title();
            }

            @Override
            public InputStream content() {
                return new ByteArrayInputStream(new byte[0]);
            }
        };
    }

    private void refreshIcon() {
        String icon = isResourceIcon() ? Asset.toResource(baseAssetUrl(), Actionable.class.getResource(this.icon)).toUrl().toString() : this.icon;
        notifier.refreshIcon(icon);
    }

    private boolean isResourceIcon() {
        return (mode == Mode.IconButton || mode == Mode.IconToggle || mode == Mode.IconSplitButton) && icon != null && Actionable.class.getResource(this.icon) != null;
    }

}