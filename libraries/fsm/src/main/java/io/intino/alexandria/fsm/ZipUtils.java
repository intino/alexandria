package io.intino.alexandria.fsm;

import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

class ZipUtils {

    public static void createNewZipFile(File zipFile, List<SessionMessageFile> files) {
        try(ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for(SessionMessageFile file : files) {
                try(InputStream inputStream = newInputStream(file.file().toPath())) {
                    zipFile(zip, file.getName(), inputStream);
                }
            }
        } catch (IOException e) {
            Logger.error(e);
        }
    }

    public static void addNewEntriesToExistingZipFile(File zipFile, List<SessionMessageFile> files) {
        File tempZipFile = new File(zipFile.getAbsolutePath() + ".temp");
        try(ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile));
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(tempZipFile))) {

            ZipEntry entry;
            while((entry = zipIn.getNextEntry()) != null)
                zipFile(zipOut, entry.getName(), zipIn);

            for(SessionMessageFile file : files) {
                try(InputStream inputStream = newInputStream(file.file().toPath())) {
                    zipFile(zipOut, file.getName(), inputStream);
                }
            }

        } catch (IOException e) {
            Logger.error(e);
        }

        try {
            Files.move(tempZipFile.toPath(), zipFile.toPath(), REPLACE_EXISTING);
        } catch (IOException e) {
            Logger.error(e);
        }
    }

    private static void zipFile(ZipOutputStream zip, String entryName, InputStream inputStream) throws IOException {
        zip.putNextEntry(new ZipEntry(entryName));
        byte[] buffer = new byte[1024];
        int read;
        while((read = inputStream.read(buffer)) > 0) {
            zip.write(buffer, 0, read);
        }
    }
}
