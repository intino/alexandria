package io.intino.alexandria.ui.utils;

import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipHelper {

	public static void extract(File zipFile, File destiny) {
		try {
			extract(new FileInputStream(zipFile), destiny);
		} catch (FileNotFoundException e) {
			Logger.error(e);
		}
	}

	public static void extract(InputStream zipStream, File destiny) {
		if (!destiny.exists()) destiny.mkdirs();

		try (ZipInputStream zipInputStream = new ZipInputStream(zipStream)) {
			ZipEntry entry;
			while ((entry = zipInputStream.getNextEntry()) != null) {
				File destFile = new File(destiny, entry.getName());
				if (entry.isDirectory()) destFile.mkdirs();
				else {
					File parentDir = destFile.getParentFile();
					if (!parentDir.exists()) parentDir.mkdirs();
					try (FileOutputStream fos = new FileOutputStream(destFile)) {
						byte[] buffer = new byte[1024];
						int bytesRead;
						while ((bytesRead = zipInputStream.read(buffer)) != -1) {
							fos.write(buffer, 0, bytesRead);
						}
					}
				}
				zipInputStream.closeEntry();
			}
		} catch (IOException e) {
			Logger.error(e);
		}
	}
}
