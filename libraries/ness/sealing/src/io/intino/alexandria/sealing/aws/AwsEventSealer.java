package io.intino.alexandria.sealing.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import io.intino.alexandria.Fingerprint;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.Datalake.Store;
import io.intino.alexandria.datalake.aws.AwsStore;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.Event.Format;
import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.EventWriter;
import io.intino.alexandria.event.measurement.MeasurementEventReader;
import io.intino.alexandria.event.message.MessageEventReader;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.sealing.MessageEventSorter;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AwsEventSealer {

    public static final int N = 500;
    private final Map<Format, Store<? extends Event>> stores;
    private final String bucketName;
    private final Predicate<String> sortingPolicy;
    private final File tempFolder;
    private final AmazonS3 client;

    public AwsEventSealer(Datalake datalake, String bucketName, Predicate<String> sortingPolicy, File tempFolder, AmazonS3 client) {
        this.stores = Map.of(Format.Message, datalake.messageStore(), Format.Measurement, datalake.measurementStore());
        this.bucketName = bucketName;
        this.sortingPolicy = sortingPolicy;
        this.tempFolder = tempFolder;
        this.client = client;
    }

    public void seal(Fingerprint fingerprint, List<File> sessions) throws IOException {
        seal(datalakePrefix(fingerprint), fingerprint.format(), sort(fingerprint, sessions));
    }

    private void seal(File datalakeFile, Format type, List<File> sessions) throws IOException {
        try (final EventWriter<Event> writer = EventWriter.of(datalakeFile)) {
            writer.write((Stream<Event>) streamOf(type, sessions));
        }
        updateDatalake(datalakeFile);
    }

    private Stream<? extends Event> streamOf(Format type, List<File> files) throws IOException {
        if (files.size() == 1) return new EventStream<>(readerOf(type, files.get(0)));
        return EventStream.merge(files.stream().map(file -> {
            try {
                return new EventStream<>(readerOf(type, files.get(0)));
            } catch (IOException e) {
                Logger.error(e);
                return Stream.empty();
            }
        }));
    }

    private EventReader<? extends Event> readerOf(Event.Format type, File file) throws IOException {
        if (!file.exists()) return new EventReader.Empty<>();
        switch (type) {
            case Message:
                return new MessageEventReader(file);
            case Measurement:
                return new MeasurementEventReader(file);
        }
        return new EventReader.Empty<>();
    }

    private List<File> sort(Fingerprint fingerprint, List<File> files) {
        try {
            for (File file : files)
                if (fingerprint.format().equals(Format.Message) && sortingPolicy.test(fingerprint.tank())){
                    new MessageEventSorter(file, tempFolder).sort();
                }
            return files;
        } catch (IOException e) {
            Logger.error(e);
            return Collections.emptyList();
        }
    }

    private void updateDatalake(File file) {
//        String s3Path = createS3PathOf(file);
//        if (S3.keysIn(client, bucketName, s3Path).count() == 0)
//            S3.uploadObjectTo(client, bucketName, s3Path, file.getAbsolutePath());
//        else {
//            try {
//                File tempFile = concatFiles(S3.getObjectFrom(client, bucketName, s3Path), file);
//                new MessageEventSorter(tempFile, tempFolder).sort();
//                S3.uploadObjectTo(client, bucketName, s3Path, file.getAbsolutePath());
//                tempFile.delete();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }

    private File concatFiles(S3Object object, File file) throws IOException {
        File tempFile = new File(tempFolder + "/.tempFile");
        OutputStream out = new FileOutputStream(tempFile);
        byte[] buf = new byte[N];
        write(out, buf, new FileInputStream(file));
        write(out, buf, object.getObjectContent());

        out.close();

        return tempFile;
    }

    private static void write(OutputStream out, byte[] buf, InputStream in) throws IOException {
        int b;
        while ( (b = in.read(buf)) >= 0)
            out.write(buf, 0, b);
        in.close();
    }

    private String createS3PathOf(File file) {
        String substring = file.getAbsolutePath().substring((tempFolder + "\\Datalake\\").length(), file.getAbsolutePath().length() - ".zim".length());
        return substring.replaceAll("\\\\", "_") + "/file.zim";
    }

    private File datalakePrefix(Fingerprint fingerprint) {
        AwsStore store = (AwsStore) stores.get(fingerprint.format());
        File file = new File(tempFolder + "/Datalake", store.prefix() + fingerprint.tank() + File.separator + fingerprint.source() + File.separator + fingerprint.timetag() + store.fileExtension());
        file.getParentFile().mkdirs();
        return file;
    }
}
