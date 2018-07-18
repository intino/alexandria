package io.intino.konos.datalake.feeders;

import io.intino.konos.datalake.Feeder;

public abstract class UserDocumentSignatureFeeder extends Feeder {
	private final SignType signType;
	private final SignFormat signFormat;

	public enum SignType { Sign, CoSign, CounterSign };
	public enum SignFormat { Pkcs7, XadesAttached, XadesDetached, CadesAttached, CadesDetached };

	public UserDocumentSignatureFeeder(String signType, String signFormat) {
		this.signType = SignType.valueOf(signType);
		this.signFormat = SignFormat.valueOf(signFormat);
	}

	public SignType signType() {
		return signType;
	}

	public SignFormat signFormat() {
		return signFormat;
	}
}
