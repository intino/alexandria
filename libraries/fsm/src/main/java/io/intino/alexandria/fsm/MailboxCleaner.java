package io.intino.alexandria.fsm;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class MailboxCleaner {

    private static final DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyyMM");

    static void clean(Mailbox mailbox, TimePeriod maxFilesAge) {
        long start = System.currentTimeMillis();
        if(isEmpty(mailbox.processed())) return;

        deleteFilesThatExceedTheMaxAge(mailbox, maxFilesAge);
        compressOldFiles(mailbox, mailbox.listProcessedMessages());

        long end = System.currentTimeMillis();
        Logger.info("Mailbox " + mailbox.root().getName() + " cleaned in " + (end - start) / 1000.0 + " seconds");
    }

    private static void deleteFilesThatExceedTheMaxAge(Mailbox mailbox, TimePeriod maxFilesAge) {
        File[] files = mailbox.processed().listFiles(MailboxCleaner::isZipOrSession);
        if(files == null || files.length == 0) return;
        LocalDateTime maxDate = LocalDateTime.now().minus(maxFilesAge.amount(), maxFilesAge.temporalUnit());
        Arrays.stream(files).filter(f -> exceedMaxAge(maxDate, f)).forEach(File::delete);
    }

    private static boolean isZipOrSession(File f) {
        String name = f.getName();
        return name.endsWith(".zip") || name.endsWith(".session");
    }

    private static boolean exceedMaxAge(LocalDateTime maxDate, File file) {
        return file.getName().endsWith(".zip") ? exceedMaxAgeMonths(maxDate, file) : exceedMaxAgeDay(maxDate, file);
    }

    private static boolean exceedMaxAgeMonths(LocalDateTime maxDate, File file) {
        maxDate = LocalDateTime.of(maxDate.getYear(), maxDate.getMonth(), 1, 0, 0);
        return maxDate.isAfter(monthOfZipFile(file));
    }

    private static boolean exceedMaxAgeDay(LocalDateTime maxDate, File file) {
        LocalDateTime datetime = SessionHelper.dateTimeOf(file);
        return datetime != null && maxDate.isAfter(datetime);
    }

    private static LocalDateTime monthOfZipFile(File zipFile) {
        try {
            String name = zipFile.getName();
            name = name.substring(0, name.indexOf('.'));
            return Timetag.of(name).datetime();
        } catch (Exception ignored) {
            return LocalDateTime.now();
        }
    }

    private static void compressOldFiles(Mailbox mailbox, List<SessionMessageFile> files) {
        String thisMonth = Formatter.format(LocalDateTime.now());

        Map<String, List<SessionMessageFile>> filesByMonth = files.stream()
                .filter(file -> isFromAPreviousMonth(thisMonth, file))
                .collect(Collectors.groupingBy(MailboxCleaner::month));

        filesByMonth.entrySet().parallelStream().forEach(entry -> {
            try {
                String month = entry.getKey();
                List<SessionMessageFile> filesOfThisMonth = entry.getValue();
                if(filesOfThisMonth.size() < 10) return;
                zip(mailbox, month, filesOfThisMonth);
                filesOfThisMonth.forEach(SessionMessageFile::delete);
            } catch(Throwable e) {
                Logger.error(e);
            }
        });
    }

    private static boolean isFromAPreviousMonth(String thisMonth, SessionMessageFile file) {
        return thisMonth.compareTo(month(file)) > 0;
    }

    private static void zip(Mailbox mailbox, String month, List<SessionMessageFile> files) {
        File zipFile = new File(mailbox.processed(), month + ".zip");
        if(zipFile.exists())
            ZipUtils.addNewEntriesToExistingZipFile(zipFile, files);
        else
            ZipUtils.createNewZipFile(zipFile, files);
    }

    private static String month(SessionMessageFile file) {
        LocalDateTime date = file.dateTime();
        date = LocalDateTime.of(date.getYear(), date.getMonth(), 1, 0, 0);
        return Formatter.format(date);
    }

    private static boolean isEmpty(File dir) {
        File[] files = dir.listFiles(MailboxCleaner::isZipOrSession);
        return files == null || files.length == 0;
    }
}
