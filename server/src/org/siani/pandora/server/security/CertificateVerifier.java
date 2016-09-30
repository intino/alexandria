package org.siani.pandora.server.security;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.SignedData;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSSignedData;
import spark.utils.IOUtils;

import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

public class CertificateVerifier {

	public enum TYPE_PKCS {PKCS7, PKCS12}

	static {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	/**
	 * Get the original content from signature
	 *
	 * @param signedBytes
	 * @return
	 * @throws Exception
	 */
	public static String getOriginalContentFromSignature(byte[] signedBytes) throws Exception {
		CMSSignedData s = new CMSSignedData(signedBytes);
		CMSProcessable signedContent = s.getSignedContent();
		return new String((byte[]) signedContent.getContent());
	}

	/**
	 * Check root Certificate for signature
	 *
	 * @param signature
	 * @param storeFile Store file that contains root certificates
	 * @return
	 */
	@SuppressWarnings({"resource", "unchecked", "rawtypes"})
	public static boolean checkRootCertificate(byte[] signature, File storeFile, TYPE_PKCS typePKCSFile, String password) {
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			byte[] storeBytes = storeBytes(storeFile);

			// Get ContentInfo
			//byte[] signature = ... // PKCS#7 signature bytes
			InputStream signatureIn = new ByteArrayInputStream(signature);
			DERObject obj = new ASN1InputStream(signatureIn).readObject();
			ContentInfo contentInfo = ContentInfo.getInstance(obj);

			// Extract certificates
			SignedData signedData = SignedData.getInstance(contentInfo.getContent());
			Enumeration certificates = signedData.getCertificates().getObjects();

			// Build certificate path
			List certList = new ArrayList();
			while (certificates.hasMoreElements()) {
				DERObject certObj = (DERObject) certificates.nextElement();
				InputStream in = new ByteArrayInputStream(certObj.getDEREncoded());
				certList.add(cf.generateCertificate(in));
			}
			CertPath certPath = cf.generateCertPath(certList);

			KeyStore keyStore;
			if (typePKCSFile == TYPE_PKCS.PKCS7) {
				Collection<? extends Certificate> col = cf.generateCertificates(new ByteArrayInputStream(storeBytes));

				keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
				keyStore.load(null, null);
				for (Object aCol : col) {
					X509Certificate cert = (X509Certificate) aCol;
					keyStore.setCertificateEntry(cert.getSerialNumber().toString(Character.MAX_RADIX), cert);
				}
			} else {
				keyStore = KeyStore.getInstance("PKCS12");
				keyStore.load(new ByteArrayInputStream(storeBytes), null);
			}
			// Set validation parameters
			PKIXParameters params = new PKIXParameters(keyStore);
			params.setRevocationEnabled(false); // to avoid exception on empty CRL

			// Validate certificate path
			CertPathValidator validator = CertPathValidator.getInstance("PKIX");
			validator.validate(certPath, params);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}


	public KeyPair create() throws NoSuchAlgorithmException, NoSuchProviderException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		keyGen.initialize(1024, random);
		return keyGen.generateKeyPair();
	}

	public void save(Key key, File destiny) {
		try {
			FileOutputStream outputStream = new FileOutputStream(destiny);
			outputStream.write(key.getEncoded());
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void store(KeyPair pair) throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] privateKeyBytes = pair.getPrivate().getEncoded();
		byte[] publicKeyBytes = pair.getPublic().getEncoded();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
		EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
	}

	private static byte[] storeBytes(File storeFile) throws IOException {
		return IOUtils.toByteArray(new FileInputStream(storeFile));
	}

}
