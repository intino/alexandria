package io.intino.alexandria.sealing;

import io.intino.alexandria.Fingerprint;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FileStore;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.EventReader;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.EventWriter;
import io.intino.alexandria.event.measurement.MeasurementEventReader;
import io.intino.alexandria.event.message.MessageEventReader;
import io.intino.alexandria.event.resource.ResourceEventReader;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.sealing.SessionSealer.TankNameFilter;
import io.intino.alexandria.sealing.sorters.MessageEventSorter;
import io.intino.alexandria.sealing.sorters.ResourceEventSorter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class EventSealer {
	private final Map<Event.Format, Datalake.Store<? extends Event>> stores;
	private final TankNameFilter tankNameFilter;
	private final File tempFolder;
	private boolean multithreading;

	public EventSealer(Datalake datalake, TankNameFilter tankNameFilter, File tempFolder) {
		this(datalake, tankNameFilter, tempFolder, true);
	}

	public EventSealer(Datalake datalake, TankNameFilter tankNameFilter, File tempFolder, boolean multithreading) {
		this.stores = Map.of(Event.Format.Message, datalake.messageStore(), Event.Format.Measurement, datalake.measurementStore(), Event.Format.Resource, datalake.resourceStore());
		this.tankNameFilter = requireNonNull(tankNameFilter, "tankNameFilter cannot be null");
		this.tempFolder = requireNonNull(tempFolder, "tempFolder cannot be null");
		this.multithreading = multithreading;
	}

	public EventSealer multithreading(boolean multithreading) {
		this.multithreading = multithreading;
		return this;
	}

	public void seal(Fingerprint fingerprint, List<File> sessions) throws IOException {
		seal(datalakeFile(fingerprint), fingerprint.format(), sort(fingerprint, sessions));
	}

	@SuppressWarnings("unchecked")
	private void seal(File datalakeFile, Event.Format type, List<File> sessions) throws IOException {
		try (final EventWriter<Event> writer = EventWriter.of(datalakeFile)) {
			writer.write((Stream<Event>) streamOf(type, sessions));
		}
	}

	private List<File> sort(Fingerprint fingerprint, List<File> files) {
		try {
			EventSorter.Factory sorter = sorterFactoryOf(fingerprint.format());
			if(!tankNameFilter.accepts(fingerprint.tank()) || sorter == null) return Collections.emptyList();
			return (shouldSortInParallel(files)) ? parallelSort(sorter, files) : sequentialSort(sorter, files);
		} catch (Throwable e) {
			Logger.error(e);
			return Collections.emptyList();
		}
	}

	private boolean shouldSortInParallel(List<File> files) {
		return multithreading && files.size() > 1 && Runtime.getRuntime().availableProcessors() > 1;
	}

	private List<File> parallelSort(EventSorter.Factory sorter, List<File> files) throws Throwable {
		ExecutorService threadPool = Executors.newFixedThreadPool(Math.min(4, Runtime.getRuntime().availableProcessors()));
		Throwable[] error = new Throwable[1];

		threadPool.invokeAll(files.stream().map(file -> sort(sorter, file, error)).collect(Collectors.toList()));
		threadPool.shutdown();

		if(error[0] != null) throw error[0];

		return files;
	}

	private Callable<Void> sort(EventSorter.Factory sorter, File file, Throwable[] error) {
		return () -> {
			try {
				sorter.of(file, tempFolder).sort();
			} catch (Throwable e) {
				error[0] = new RuntimeException("Error while sorting " + file + ": " + e.getMessage(), e);
			}
			return null;
		};
	}

	private List<File> sequentialSort(EventSorter.Factory sorter, List<File> files) throws Throwable {
		for(File file : files) sorter.of(file, tempFolder).sort();
		return files;
	}

	public EventSorter.Factory sorterFactoryOf(Event.Format format) {
		switch(format) {
			case Message: return MessageEventSorter::new;
//			case Measurement: return new MessageEventSorter(); TODO?
			case Resource: return ResourceEventSorter::new;
		}
		return null;
	}

	private Stream<? extends Event> streamOf(Event.Format type, List<File> files) throws IOException {
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
			case Resource:
				return new ResourceEventReader(file);
		}
		return new EventReader.Empty<>();
	}

	private File datalakeFile(Fingerprint fingerprint) {
		FileStore store = (FileStore) stores.get(fingerprint.format());
		File datalakeFile = new File(store.directory(), filenameOf(fingerprint) + store.fileExtension());
		datalakeFile.getParentFile().mkdirs();
		return datalakeFile;
	}

	private String filenameOf(Fingerprint fp) {
		return fp.tank() + File.separator + fp.source() + File.separator + fp.timetag();
	}
}
