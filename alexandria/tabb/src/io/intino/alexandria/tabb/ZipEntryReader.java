package io.intino.alexandria.tabb;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class ZipEntryReader {

	private static Map<File, File> zipFilesCache = new HashMap<>();
//
//	static InputStream openEntry(File file, String entryName) throws IOException {
//		ZipFile zipFile = new ZipFile(file);
//		Enumeration<? extends ZipEntry> entries = zipFile.entries();
//		while (entries.hasMoreElements()) {
//			ZipEntry entry = entries.nextElement();
//			if (entryName.equals(entry.getName())) return zipFile.getInputStream(entry);
//		}
//		return null;
//	}

	static InputStream openEntry(File file, String entryName) throws IOException {
		if (zipFilesCache.containsKey(file)) return new FileInputStream(new File(zipFilesCache.get(file), entryName));
		File destDir = Files.createTempDirectory("tabbc").toFile();
		destDir.deleteOnExit();
		zipFilesCache.put(file, destDir);
		byte[] buffer = new byte[1024];
		ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
		ZipEntry zipEntry = zis.getNextEntry();
		File selected = null;
		while (zipEntry != null) {
			File newFile = newFile(destDir, zipEntry);
			newFile.deleteOnExit();
			if (entryName.equals(newFile.getName())) selected = newFile;
			FileOutputStream fos = new FileOutputStream(newFile);
			int len;
			while ((len = zis.read(buffer)) > 0) fos.write(buffer, 0, len);
			fos.close();
			zipEntry = zis.getNextEntry();
		}
		zis.closeEntry();
		zis.close();

		return new FileInputStream(selected);
	}

	private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());
		if (!destFile.getCanonicalPath().startsWith(destinationDir.getCanonicalPath() + File.separator))
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		return destFile;
	}

}