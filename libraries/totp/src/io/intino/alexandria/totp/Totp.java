package io.intino.alexandria.totp;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import de.taimos.totp.TOTP;
import io.intino.alexandria.logger.Logger;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

public class Totp {

	public static String createSecret() {
		SecureRandom random = new SecureRandom();
		byte[] bytes = new byte[20];
		random.nextBytes(bytes);
		Base32 base32 = new Base32();
		return base32.encodeToString(bytes);
	}

	private static final String QrTextPattern = "otpauth://totp/$company$%3A$email$?secret=$secret$&issuer=$company$";
	public static String qrText(String secret, String email, String company) {
		String result = QrTextPattern.replace("$company$", company != null ? company : "Company");
		result = result.replace("$email$", email != null ? email : "info@company.com");
		result = result.replace("$secret$", secret);
		return result;
	}

	public static byte[] qrImage(String secret, String email, String company) {
		try {
			BitMatrix matrix = new MultiFormatWriter().encode(qrText(secret, email, company), BarcodeFormat.QR_CODE, 200, 200);
			try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
				MatrixToImageWriter.writeToStream(matrix, "png", out);
				return out.toByteArray();
			} catch (IOException e) {
				Logger.error(e);
				return null;
			}
		} catch (WriterException e) {
			Logger.error(e);
			return null;
		}
	}

	public static boolean check(String secret, String sixDigitCode) {
		if (secret == null) return false;
		Base32 base32 = new Base32();
		byte[] bytes = base32.decode(secret);
		String hexKey = Hex.encodeHexString(bytes);
		return TOTP.getOTP(hexKey).equals(sixDigitCode);
	}

}
