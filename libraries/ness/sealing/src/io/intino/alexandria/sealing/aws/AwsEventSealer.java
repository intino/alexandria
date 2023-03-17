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

    private void seal(File eventFile, Format type, List<File> sessions) throws IOException {
        try (final EventWriter<Event> writer = EventWriter.of(eventFile)) {
            writer.write((Stream<Event>) streamOf(type, sessions));
        }
        updateDatalake(eventFile);
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
        String s3Path = createS3PathOf(file);
        S3.uploadObjectTo(client, bucketName, s3Path, merge(file, s3Path));
    }

    private String merge(File file, String s3Path) {
        return (S3.keysIn(client, bucketName, s3Path).findAny().isPresent() ? mergeWithAws(file, s3Path) : file).getAbsolutePath();
    }

    private File mergeWithAws(File file, String s3Path) {
        try {
            file = concatFiles(S3.getObjectFrom(client, bucketName, s3Path), file);
            new MessageEventSorter(file, tempFolder).sort();
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File concatFiles(S3Object objectFromAWS, File file) throws IOException {
        File tempFile = File.createTempFile("seal", file.getName(), tempFolder);
        write(Zim.decompressing(objectFromAWS.getObjectContent()), open(tempFile, false));
        write(read(file), open(tempFile, true));
        tempFile.deleteOnExit();
        return tempFile;
    }

    private static OutputStream open(File tempFile, boolean append) throws IOException {
        return Zim.compressing(new BufferedOutputStream(new FileOutputStream(tempFile, append)));
    }

    private static void write(InputStream source, OutputStream destination) throws IOException {
        try (destination; source) {
            source.transferTo(destination);
        }
    }

    private String createS3PathOf(File file) {
        String substring = file.getAbsolutePath().substring((tempFolder + "\\Datalake\\").length(), file.getAbsolutePath().length() - ".zim".length());
        return substring.replaceAll("/", "_") + "/file.zim";
    }

    private File datalakePrefix(Fingerprint fingerprint) {
        AwsStore store = (AwsStore) stores.get(fingerprint.format());
        File file = new File(tempFolder + "/Datalake", store.prefix() + fingerprint.tank() + File.separator + fingerprint.source() + File.separator + fingerprint.timetag() + store.fileExtension());
        file.getParentFile().mkdirs();
        return file;
    }
}
