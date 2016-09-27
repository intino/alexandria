package org.siani.pandora.server.web.security;

import org.apache.commons.codec.binary.Base64;
import org.siani.pandora.server.security.TeseoSecurityManager;
import org.siani.pandora.server.web.utils.CertificateVerifier;

import java.io.File;

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
					CertificateVerifier.checkRootCertificate(signatureBytes, certificateStore, CertificateVerifier.TYPE_PKCS.PKCS7, password);
		} catch (Exception e) {
			return false;
		}
	}
}
