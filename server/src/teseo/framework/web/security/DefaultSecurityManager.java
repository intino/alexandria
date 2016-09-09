package teseo.framework.web.security;

import org.apache.commons.codec.binary.Base64;
import teseo.framework.security.TeseoSecurityManager;
import teseo.framework.web.utils.CertificateVerifier;

import java.io.File;

import static teseo.framework.web.utils.CertificateVerifier.TYPE_PKCS.PKCS7;

public class DefaultSecurityManager implements TeseoSecurityManager {
	private final File certificateStore;
	private final String password;

	public DefaultSecurityManager(File certificateStore, String password) {
		this.certificateStore = certificateStore;
		this.password = password;
	}

	@Override
	public boolean check(String hash, String signature) {
		try {
			byte[] signatureBytes = Base64.decodeBase64(signature);
			String originalContent = CertificateVerifier.getOriginalContentFromSignature(signatureBytes);

			return !(originalContent == null || !hash.equals(originalContent)) &&
					CertificateVerifier.checkRootCertificate(signatureBytes, certificateStore, PKCS7, password);
		} catch (Exception e) {
			return false;
		}
	}
}
