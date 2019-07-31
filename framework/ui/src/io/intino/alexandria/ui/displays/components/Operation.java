package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.OperationInfo;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.notifiers.OperationNotifier;
import io.intino.alexandria.ui.resources.Asset;
import io.intino.alexandria.ui.spark.UIFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Operation<DN extends OperationNotifier, B extends Box> extends Component<DN, B> {
    private String title;
    private boolean disabled = false;
    private String icon;
    private Mode mode;

    public enum Mode { Link, Button, IconButton, MaterialIconButton }

    public Operation(B box) {
        super(box);
    }

    public String title() {
        return title;
    }

    public Operation<DN, B> updateIcon(String icon) {
        this.icon = icon;
        refreshIcon();
        return this;
    }

    public boolean disabled() {
        return disabled;
    }

    public Operation<DN, B> disabled(boolean value) {
        this.disabled = value;
        return this;
    }

    public Operation readonly(boolean value) {
        if (value) disable();
        else enable();
        return this;
    }

    public Operation enable() {
        disabled(false);
        notifier.refreshDisabled(disabled);
        return this;
    }

    public Operation disable() {
        disabled(true);
        notifier.refreshDisabled(disabled);
        return this;
    }

    public void refresh() {
        OperationInfo info = new OperationInfo().title(title()).disabled(disabled());
        notifier.refresh(info);
    }

    public void notifyUser(String message) {
        notifyUser(message, UserMessage.Type.Info);
    }

    @Override
    protected void init() {
        super.init();
        if (isResourceIcon()) refreshIcon();
    }

    public Operation<DN, B> title(String title) {
        this.title = title;
        return this;
    }

    public Operation<DN, B> mode(Mode mode) {
        this.mode = mode;
        return this;
    }

    public Operation<DN, B> icon(String icon) {
        this.icon = icon;
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
        return mode == Mode.IconButton && icon != null;
    }

}