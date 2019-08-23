package io.intino.alexandria.drivers.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class StringUtils {

	public String fillString(String string, Integer count) {

		Integer size = count - string.length();
		char[] fill = new char[size];
		Arrays.fill(fill, '0');
		String zeroes = new String(fill);

		return zeroes + string;
	}

	public String removeZeros(String string) {

		while (string.substring(0, 1).equals("0")) {
			string = string.substring(1);
		}

		return string;
	}

	public String getResponse(InputStream stdin) {
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stdin));
		String line = "";
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append(System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			sb = new StringBuffer();
		} finally {
			try {
				reader.close();
			} catch (IOException ex) {
			}
			;
		}
		return sb.toString();
	}

	public boolean checkIfInteger(String in) {
		try {
			Integer.parseInt(in);
		} catch (NumberFormatException ex) {
			return false;
		}

		return true;
	}
}
