package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.OperationInfo;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.notifiers.OperationNotifier;
import io.intino.alexandria.ui.resources.Asset;

public class Operation<DN extends OperationNotifier, B extends Box> extends Component<DN, B> {
    private String title;
    private boolean disabled = true;
    private String icon;
    private Mode mode;

    public enum Mode { Link, Button, IconButton, MaterialIconButton }

    public Operation(B box) {
        super(box);
    }

    public String title() {
        return title;
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

    public boolean disabled() {
        return disabled;
    }

    public Operation<DN, B> disabled(boolean value) {
        this.disabled = value;
        return this;
    }

    public void refresh() {
        OperationInfo info = new OperationInfo().title(title()).disabled(disabled());
        notifier.refresh(info);
    }

    public void execute() {
    }

    @Override
    protected void init() {
        super.init();
        if (isResourceIcon()) refreshIcon();
    }

    private void refreshIcon() {
        notifier.refreshIcon(Asset.toResource(baseAssetUrl(), Operation.class.getResource(icon)).toUrl().toString());
    }

    private boolean isResourceIcon() {
        return mode == Mode.IconButton && icon != null;
    }
}