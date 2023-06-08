package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;
import io.intino.icod.core.SignatureInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

public class SignEvent extends Event {
	private final java.util.List<SignEventInfo> infoList;

	public SignEvent(Display sender, String signature, String certificate, SignatureInfo info) {
		super(sender);
		this.infoList = singletonList(new SignEventInfo(null, signature, certificate, info));
	}

	public SignEvent(Display sender, List<SignEventInfo> infoList) {
		super(sender);
		this.infoList = infoList;
	}

	public String signature() {
		return infoList.get(0).signature;
	}

	public String certificate() {
		return infoList.get(0).certificate;
	}

	public SignatureInfo info() {
		return infoList.get(0).info;
	}

	public Map<String, String> signatures() {
		return infoList.stream().collect(Collectors.toMap(i -> i.id, i -> i.signature));
	}

	public Map<String, String> signaturesCertificate() {
		return infoList.stream().collect(Collectors.toMap(i -> i.id, i -> i.certificate));
	}

	public Map<String, SignatureInfo> signaturesInfo() {
		return infoList.stream().collect(Collectors.toMap(i -> i.id, i -> i.info));
	}

	public static class SignEventInfo {
		public String id;
		public String signature;
		public String certificate;
		public SignatureInfo info;

		public SignEventInfo(String id, String signature, String certificate, SignatureInfo info) {
			this.id = id;
			this.signature = signature;
			this.certificate = certificate;
			this.info = info;
		}
	}

}
