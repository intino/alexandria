package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;
import io.intino.icod.core.SignatureInfo;

public class SignEvent extends Event {
	private final String signature;
	private final String certificate;
	private final SignatureInfo info;

	public SignEvent(Display sender, String signature, String certificate, SignatureInfo info) {
		super(sender);
		this.signature = signature;
		this.certificate = certificate;
		this.info = info;
	}

	public String signature() {
		return signature;
	}

	public String certificate() {
		return certificate;
	}

	public SignatureInfo info() {
		return info;
	}
}
