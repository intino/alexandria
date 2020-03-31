package io.intino.alexandria.scheduler.directory;

import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.*;

public class DirectorySentinel {

	private DirectoryTask task;
	private Map<File, WatchService> watchers;
	private Thread thread;
	private boolean closed = false;


	public enum Event {OnCreate, OnModify, OnDelete}

	public DirectorySentinel(File directory, DirectoryTask task, Event... events) {
		try {
			this.task = task;
			this.watchers = new HashMap<>();
			watchers.put(directory, FileSystems.getDefault().newWatchService());
			directory.toPath().register(watchers.get(directory), kindsOf(events));
			if (directory.isDirectory() && directory.exists())
				for (File subDir : directory.listFiles(f -> f.isDirectory())) {
					watchers.put(subDir, FileSystems.getDefault().newWatchService());
					subDir.toPath().register(watchers.get(subDir), kindsOf(events));
				}
			buildTask(task);
		} catch (IOException e) {
		}
	}

	private void buildTask(DirectoryTask task) {
		this.thread = new Thread(() -> {
			while (!closed) {
				for (Map.Entry<File, WatchService> entry : watchers.entrySet()) {
					try {
						WatchKey key = entry.getValue().poll(1L, TimeUnit.SECONDS);
						if (key == null) continue;
						for (WatchEvent<?> event : key.pollEvents()) {
							@SuppressWarnings("unchecked")
							WatchEvent<Path> ev = (WatchEvent<Path>) event;
							new Thread(() -> task.execute(new File(entry.getKey(), ev.context().toString()), eventOf(ev.kind()))).start();
						}
						boolean valid = key.reset();
						if (!valid) break;
					} catch (InterruptedException | ClosedWatchServiceException ex) {
						Logger.error(ex);
						return;
					}
				}
			}
		});
	}

	public void watch() {
		thread.start();
	}

	public void stop() {
		try {
			for (WatchService w : watchers.values()) w.close();
			this.closed = true;
		} catch (IOException ignored) {
		}
	}

	private WatchEvent.Kind[] kindsOf(Event[] events) {
		List<WatchEvent.Kind> list = new ArrayList<>();
		for (Event event : events) {
			if (event.equals(Event.OnCreate)) list.add(ENTRY_CREATE);
			else if (event.equals(Event.OnModify)) list.add(ENTRY_MODIFY);
			else if (event.equals(Event.OnDelete)) list.add(ENTRY_DELETE);
		}
		return list.toArray(new WatchEvent.Kind[list.size()]);
	}

	private Event eventOf(WatchEvent.Kind kind) {
		if (kind.equals(ENTRY_CREATE)) return Event.OnCreate;
		else if (kind.equals(ENTRY_MODIFY)) return Event.OnModify;
		return Event.OnDelete;
	}
}
