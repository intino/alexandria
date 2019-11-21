package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.OperationInfo;
import io.intino.alexandria.schemas.OperationSign;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.components.operation.SignChecker;
import io.intino.alexandria.ui.displays.notifiers.OperationNotifier;
import io.intino.alexandria.ui.resources.Asset;
import io.intino.alexandria.ui.spark.UIFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Operation<DN extends OperationNotifier, B extends Box> extends Component<DN, B> {
    private String title;
    private boolean readonly = false;
    private String icon;
    private Mode mode;
    private SignChecker signChecker;
	private String signReason;

	public enum Mode { Link, Button, IconButton, MaterialIconButton }

    public Operation(B box) {
        super(box);
    }

    public String title() {
        return title;
    }

    public Operation<DN, B> title(String title) {
        _title(title);
        refresh();
        return this;
    }

    public Operation<DN, B> icon(String icon) {
        _icon(icon);
        refreshIcon();
        return this;
    }

    public Operation readonly(boolean readonly) {
        if (readonly) disable();
        else enable();
        return this;
    }

    public boolean disabled() {
        return readonly;
    }

    public Operation enable() {
        _readonly(false);
        notifier.refreshReadonly(readonly);
        return this;
    }

    public Operation disable() {
        _readonly(true);
        notifier.refreshReadonly(readonly);
        return this;
    }

    public void refresh() {
        OperationInfo info = new OperationInfo().title(title()).disabled(disabled());
        notifier.refresh(info);
    }

    public void notifyUser(String message) {
        notifyUser(message, UserMessage.Type.Info);
    }

	public void checkSign(OperationSign info) {
		if (signChecker == null) notifier.checkSignResult(true);
		notifier.checkSignResult(signChecker.check(info.sign(), info.reason()));
		this.signReason = info.reason();
	}

	public String signReason() {
		return this.signReason;
	}

	@Override
    protected void init() {
        super.init();
        if (isResourceIcon()) refreshIcon();
    }

    protected Operation<DN, B> _title(String title) {
        this.title = title;
        return this;
    }

    protected Operation<DN, B> _mode(Mode mode) {
        this.mode = mode;
        return this;
    }

    protected Operation<DN, B> _icon(String icon) {
        this.icon = icon;
        return this;
    }

    protected Operation<DN, B> _readonly(boolean value) {
        this.readonly = value;
        return this;
    }

    protected Operation<DN, B> _signChecker(SignChecker checker) {
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
        String icon = isResourceIcon() ? Asset.toResource(baseAssetUrl(), Operation.class.getResource(this.icon)).toUrl().toString() : this.icon;
        notifier.refreshIcon(icon);
    }

    private boolean isResourceIcon() {
        return mode == Mode.IconButton && icon != null && Operation.class.getResource(this.icon) != null;
    }

}