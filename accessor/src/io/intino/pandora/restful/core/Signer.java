package io.intino.pandora.restful.core;

import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.util.encoders.Base64;

import java.net.URL;
import java.security.*;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Map;

public class Signer {
	private static final String ParameterMask = "%s=%s";

	static {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	public String hash(Map<String, Object> parameters, long timestamp) {
		StringBuilder result = new StringBuilder();

		for (Map.Entry<String, Object> param : parameters.entrySet()) {
			if (!(param.getValue() instanceof String))
				continue;
			result.append(String.format(ParameterMask, param.getKey(), param.getValue()));
			result.append("&");
		}

		result.append(String.format(ParameterMask, "timestamp", String.valueOf(timestamp)));

		return result.toString();
	}

	public String sign(String text, URL certificate, String password) throws Exception {
		byte[] data = signText(text.getBytes(), certificate, password);
		return new String(Base64.encode(data));
	}

	private byte[] signText(byte[] text, URL certificate, String password) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		X509Certificate[] chain = new X509Certificate[1];

		keyStore.load(certificate.openStream(), password.toCharArray());
		String certificateAlias = keyStore.aliases().nextElement();
		chain[0] = (X509Certificate) keyStore.getCertificate(certificateAlias);

		return createSignature(text, certificateAlias, keyStore, chain, password.toCharArray());
	}

	private byte[] createSignature(byte[] hash, String privateKeyAlias, KeyStore keyStore, X509Certificate[] chain, char[] password) throws Exception {
		ExternalSignatureCMSSignedDataGenerator generator = new ExternalSignatureCMSSignedDataGenerator();
		ExternalSignatureSignerInfoGenerator si = new ExternalSignatureSignerInfoGenerator(CMSSignedDataGenerator.DIGEST_SHA1,
			CMSSignedDataGenerator.ENCRYPTION_RSA);

		byte[] signedBytes = signMessage(hash, keyStore, privateKeyAlias, password);

		si.setCertificate(chain[0]);
		si.setSignedBytes(signedBytes);

		generator.addSignerInf(si);
		generator.addCertificatesAndCRLs(getCertStore(chain));
		CMSSignedData signedData = generator.generate(new CMSProcessableByteArray(hash), true);

		return signedData.getEncoded();
	}

	private CertStore getCertStore(Certificate[] certs) throws GeneralSecurityException {
		ArrayList<Certificate> list = new ArrayList<Certificate>();

		for (int i = 0, length = certs == null ? 0 : certs.length; i < length; i++) {
			list.add(certs[i]);
		}

		return CertStore.getInstance("Collection", new CollectionCertStoreParameters(list), "BC");
	}

	private byte[] signMessage(byte[] textBytes, KeyStore keyStore, String certificateAlias, char[] password) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		PrivateKey privatekey = (PrivateKey) keyStore.getKey(certificateAlias, password);

		if (privatekey == null) {
			return null;
		} else {
			Signature signature = Signature.getInstance("SHA1withRSA", keyStore.getProvider());
			signature.initSign(privatekey);
			signature.update(textBytes);
			byte abyte1[] = signature.sign();

			return abyte1;
		}
	}

}
