package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.SignActionSetup;
import io.intino.alexandria.schemas.SignActionSignatureFailure;
import io.intino.alexandria.schemas.SignActionSignatureSuccess;
import io.intino.alexandria.ui.AlexandriaUiBox;
import io.intino.alexandria.ui.displays.UserMessage;
import io.intino.alexandria.ui.displays.components.sign.AutoFirmaServer;
import io.intino.alexandria.ui.displays.events.*;
import io.intino.alexandria.ui.displays.notifiers.SignActionNotifier;
import io.intino.icod.core.SignatureInfo;
import io.intino.icod.core.XadesSignatureHelper;

public abstract class SignAction<DN extends SignActionNotifier, B extends Box> extends AbstractSignAction<DN, B> {
	public boolean readonly = false;
	protected AutoFirmaServer server;
	protected SignListener signListener;
	protected String signSuccessMessage = "Sign process finished";
	private SignErrorListener errorListener;
	private SignAction.SignMode mode = SignMode.Sign;
	private SignAction.SignFormat format = SignAction.SignFormat.XAdES;
	private ReadonlyListener readonlyListener = null;

	public enum SignMode { Sign, CounterSign }
	public enum SignFormat { PAdES, XAdES, CAdES }

	public SignAction(B box) {
		super(box);
	}

	public SignAction<DN, B> signSuccessMessage(String message) {
		this.signSuccessMessage = message;
		return this;
	}

	public SignAction.SignFormat signFormat() {
		return format;
	}

	public SignAction<DN, B> signFormat(SignAction.SignFormat format) {
		_signFormat(format);
		notifier.format(format.name());
		return this;
	}

	public SignAction.SignMode signMode() {
		return mode;
	}

	public SignAction<DN, B> signMode(SignAction.SignMode mode) {
		_signMode(mode);
		notifier.mode(mode.name());
		return this;
	}

	@Override
	public void didMount() {
		super.didMount();
		notifier.setup(setup());
		notifier.refreshReadonly(readonly);
	}

	@Override
	public void init() {
		super.init();
		try {
			server = new AutoFirmaServer((AlexandriaUiBox) box(), session()).listen();
			refresh();
		}
		catch (Throwable ignored) {
		}
	}

	@Override
	public void refresh() {
		super.refresh();
		notifier.setup(setup());
	}

	public SignAction<DN, B> readonly(boolean readonly) {
		this.readonly = readonly;
		notifyReadonly(readonly);
		return this;
	}

	public void sign(String content) {
		if (content == null) {
			notifyUser(translate("No data to sign"), UserMessage.Type.Error);
			return;
		}
		notifier.sign(content);
	}

	public void signing() {
		readonly(true);
		notifyUser(translate("Select certificate if requested by @firma application..."), UserMessage.Type.Loading);
	}

	public void success(SignActionSignatureSuccess success) {
		notifyUser(translate(signSuccessMessage), UserMessage.Type.Success);
		signListener.accept(new SignEvent(this, success.signature(), success.certificate(), info(success.signature())));
	}

	public void failure(SignActionSignatureFailure failure) {
		notifyUser(translate("An error ocurred when signing"), UserMessage.Type.Error);
		if (errorListener == null) return;
		errorListener.accept(new SignErrorEvent(this, failure != null ? failure.code() : "-", failure != null ? failure.message() : "-"));
	}

	public SignAction<DN, B> onSign(SignListener listener) {
		this.signListener = listener;
		return this;
	}

	public SignAction<DN, B> onError(SignErrorListener listener) {
		this.errorListener = listener;
		return this;
	}

	public SignAction<DN, B> onReadonly(ReadonlyListener listener) {
		this.readonlyListener = listener;
		return this;
	}

	protected SignAction<DN, B> _signFormat(SignAction.SignFormat format) {
		this.format = format;
		return this;
	}

	protected SignAction<DN, B> _signMode(SignAction.SignMode mode) {
		this.mode = mode;
		return this;
	}

	protected SignatureInfo info(String signature) {
		if (format != SignAction.SignFormat.XAdES) return new SignatureInfo(null, null, null);
		return new XadesSignatureHelper().getInfo(signature);
	}

	private SignActionSetup setup() {
		SignActionSetup result = new SignActionSetup();
		result.downloadUrl(server.downloadUrl());
		result.storageUrl(server.storageUrl());
		result.retrieveUrl(server.retrieveUrl());
		result.batchPreSignerUrl(server.batchPreSignerUrl());
		result.batchPostSignerUrl(server.batchPostSignerUrl());
		return result;
	}

	private void notifyReadonly(boolean value) {
		if (readonlyListener != null) readonlyListener.accept(new ReadonlyEvent(this, value));
		notifier.refreshReadonly(value);
	}

}