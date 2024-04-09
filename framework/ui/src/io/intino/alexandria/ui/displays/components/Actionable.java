package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Base64;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.ActionableInfo;
import io.intino.alexandria.schemas.ActionableSign;
import io.intino.alexandria.schemas.ActionableSignInfo;
import io.intino.alexandria.totp.Totp;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.components.actionable.SignChecker;
import io.intino.alexandria.ui.displays.components.actionable.SignInfo;
import io.intino.alexandria.ui.displays.components.actionable.SignInfoProvider;
import io.intino.alexandria.ui.displays.events.*;
import io.intino.alexandria.ui.displays.notifiers.ActionableNotifier;
import io.intino.alexandria.ui.resources.Asset;
import io.intino.alexandria.ui.spark.UIFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

public abstract class Actionable<DN extends ActionableNotifier, B extends Box> extends Component<DN, B> {
    private String title;
    private boolean readonly = false;
    private String icon;
    private Mode mode;
    private SignChecker signChecker;
    private SignMode signMode;
    private String signReason;
    private SignInfoProvider signInfoProvider;
    private SignInfo signInfo;
    private BeforeListener beforeAffirmListener;
    private Listener cancelAffirmListener;
    private ReadonlyListener readonlyListener = null;

    public enum Mode { Link, Button, IconButton, MaterialIconButton, Toggle, IconToggle, MaterialIconToggle, SplitButton, IconSplitButton, MaterialIconSplitButton, AvatarIconButton }
    public enum Highlight { None, Outline, Fill }
    public enum SignMode { SimplePassword, OneTimePassword }

    public Actionable(B box) {
        super(box);
    }

    @Override
    public void didMount() {
        super.didMount();
        refreshIconIfRequired();
        refreshSignInfo();
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
        notifyReadonly(readonly);
        return this;
    }

    public Actionable<DN, B> disable() {
        _readonly(true);
        notifyReadonly(readonly);
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

    public void setupSign(ActionableSign info) {
        this.signInfo = new SignInfo().secret(info.secret()).canSetup(true);
        if (signChecker == null) notifier.setupSignResult(true);
        notifier.setupSignResult(signChecker.check(info.sign(), "Setting up sign configuration"));
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

    public Actionable<DN, B> onReadonly(ReadonlyListener listener) {
        this.readonlyListener = listener;
        return this;
    }

    public String signReason() {
        return this.signReason;
    }

    @Override
    public void init() {
        super.init();
        refreshIconIfRequired();
        refreshSignInfo();
    }

    public final void checkAffirmed() {
        notifier.refreshAffirmedRequired(beforeAffirmListener == null || beforeAffirmListener.accept(new Event(this)));
    }

    public final void beforeSigned() {
        this.signInfo = signInfoProvider != null ? signInfoProvider.signInfo() : this.signInfo;
        refreshSignInfo();
        notifier.continueSigned();
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

    protected Actionable<DN, B> _signMode(SignMode mode) {
        this.signMode = mode;
        return this;
    }

    protected Actionable<DN, B> _signInfoProvider(SignInfoProvider provider) {
        this.signInfoProvider = provider;
        return this;
    }

    protected String _signSecret() {
        return this.signInfo != null ? this.signInfo.secret() : null;
    }

    protected Actionable<DN, B> _signChecker(SignChecker checker) {
        this.signChecker = checker;
        return this;
    }

    protected SignChecker _oneTimePassword() {
        return (sign, reason) -> Totp.check(signInfo.secret(), sign);
    }

    protected void refreshSignInfo() {
        if (signMode != SignMode.OneTimePassword) return;
        String secret = signSecret();
        notifier.refreshSignInfo(new ActionableSignInfo().canSetup(signCanSetup()).setupRequired(signSetupRequired()).secret(secret).secretImage(signSecretImage(secret)));
    }

    private String signSecretImage(String secret) {
        String company = signInfo != null && signInfo.company() != null ? signInfo.company() : "Company";
        String email = signInfo != null && signInfo.email() != null ? signInfo.email() : "info@company.com";
        return Base64.encode(Totp.qrImage(secret, email, company));
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

    private void refreshIconIfRequired() {
        if (isResourceIcon()) refreshIcon();
        if (isMaterialIcon() && session().browser().isMobile()) refreshIcon();
    }

    private void refreshIcon() {
        notifier.refreshIcon(icon());
    }

    private static final String PngMaterialIcon = "/icons/mobile/%s.png";
    private String icon() {
        if (isResourceIcon()) return Asset.toResource(baseAssetUrl(), Actionable.class.getResource(this.icon)).toUrl().toString();
        if (isMaterialIcon() && session().browser().isMobile()) {
            URL iconResource = Actionable.class.getResource(String.format(PngMaterialIcon, this.icon));
            return iconResource != null ? Asset.toResource(baseAssetUrl(), iconResource).setLabel(String.format(PngMaterialIcon, this.icon)).toUrl().toString() : this.icon;
        }
        return this.icon;
    }

    private boolean isResourceIcon() {
        return (mode == Mode.IconButton || mode == Mode.IconToggle || mode == Mode.IconSplitButton) && icon != null && Actionable.class.getResource(this.icon) != null;
    }

    private boolean isMaterialIcon() {
        return (mode == Mode.MaterialIconButton || mode == Mode.MaterialIconToggle || mode == Mode.MaterialIconSplitButton) && icon != null;
    }

    private boolean signCanSetup() {
        return signInfoProvider != null ? signInfoProvider.canSetup() : signInfo == null || signInfo.canSetup();
    }

    private boolean signSetupRequired() {
        return this.signMode == SignMode.OneTimePassword && (signInfo == null || signInfo.secret() == null);
    }

    private String signSecret() {
        if (signMode != SignMode.OneTimePassword) return null;
        if (signInfo != null && signInfo.secret() != null) return signInfo.secret();
        return Totp.createSecret();
    }

    private void notifyReadonly(boolean readonly) {
        if (readonlyListener != null) readonlyListener.accept(new ReadonlyEvent(this, readonly));
        notifier.refreshReadonly(readonly);
    }

}