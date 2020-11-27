package io.intino.alexandria.led.util;

import io.intino.alexandria.led.util.sorting.LedExternalMergeSort;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;

public final class LedUtils {

    public static void sort(File srcFile, File destFile) {
        createIfNotExists(destFile);
        new LedExternalMergeSort().sort(srcFile, destFile);
    }

    public static void sort(File tempDir, File srcFile, File destFile, int numTransactionsInMemory) {
        createIfNotExists(destFile);
        new LedExternalMergeSort().sort(tempDir, srcFile, destFile, numTransactionsInMemory);
    }

    public static void sort(File srcFile, File destFile, int numTransactionsInMemory) {
        createIfNotExists(destFile);
        new LedExternalMergeSort().sort(srcFile, destFile, numTransactionsInMemory);
    }

    private static void createIfNotExists(File destFile) {
        if(!destFile.exists()) {
            try {
                destFile.createNewFile();
            } catch (IOException e) {
                Logger.error(e);
                throw new RuntimeException(e);
            }
        }
    }

    private LedUtils() {}
}
