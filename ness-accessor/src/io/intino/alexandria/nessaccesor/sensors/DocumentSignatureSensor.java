package io.intino.alexandria.nessaccesor.sensors;

public abstract class DocumentSignatureSensor extends UserSensor {
	private final SignType signType;
	private final SignFormat signFormat;

	public enum SignType { Sign, CoSign, CounterSign };
	public enum SignFormat { Pkcs7, XadesAttached, XadesDetached, CadesAttached, CadesDetached };

	public DocumentSignatureSensor(String signType, String signFormat) {
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
