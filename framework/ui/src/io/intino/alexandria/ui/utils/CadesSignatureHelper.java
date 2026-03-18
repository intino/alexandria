package io.intino.alexandria.ui.utils;

import io.intino.alexandria.Base64;
import io.intino.alexandria.logger.Logger;
import io.intino.icod.core.SignatureInfo;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class CadesSignatureHelper {

	public static SignatureInfo getInfo(String certificate) {
		return infoOf(getCertificate(certificate));
	}

	public static X509Certificate getCertificate(String certificate) {
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(Base64.decode(certificate)));
		} catch (CertificateException e) {
			Logger.error(e);
			return null;
		}
	}

	public static SignatureInfo infoOf(X509Certificate certificate) {
		SignatureInfo info = new SignatureInfo(null, null, null);
		if (certificate == null) return info;
		String content = certificate.getSubjectX500Principal().getName();
		for (String part : content.split(",")) {
			String[] kv = part.trim().split("=");
			if (kv.length == 2) {
				if (kv[0].equalsIgnoreCase("CN")) {
					info.setUsername(kv[1]);
					info.setFullName(kv[1]);
				}
				if (kv[0].equalsIgnoreCase("EMAILADDRESS") || kv[0].equalsIgnoreCase("E")) {
					info.setEmail(kv[1]);
				}
			}
		}
		return info;
	}

}
