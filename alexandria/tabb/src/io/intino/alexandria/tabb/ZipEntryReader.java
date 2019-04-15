package io.intino.alexandria.tabb;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class ZipEntryReader {

	static InputStream openEntry(File file, String entryName) throws IOException {
		ZipFile zipFile = new ZipFile(file);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			if (entryName.equals(entry.getName())) return zipFile.getInputStream(entry);
		}
		return null;
	}

}