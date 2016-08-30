package teseo.framework.web.security;

import teseo.framework.security.SecurityManager;
import teseo.framework.web.utils.CertificateVerifier;
import org.apache.commons.codec.binary.Base64;

import java.io.File;

import static teseo.framework.web.utils.CertificateVerifier.TYPE_PKCS.PKCS7;

public class DefaultSecurityManager implements SecurityManager {
    private final String identifier;
    private final File certificateStore;
    private final String password;

    public DefaultSecurityManager(String identifier, File certificateStore, String password) {
        this.identifier = identifier;
        this.certificateStore = certificateStore;
        this.password = password;
    }

    @Override
    public boolean check(String hash, String signature) {
        try {
            byte[] signatureBytes = Base64.decodeBase64(signature);
            String originalContent = CertificateVerifier.getOriginalContentFromSignature(signatureBytes);

            if (originalContent == null || !hash.equals(originalContent))
                return false;

            return CertificateVerifier.checkRootCertificate(signatureBytes, certificateStore, PKCS7, password, identifier);
        } catch (Exception e) {
            return false;
        }
    }
}
