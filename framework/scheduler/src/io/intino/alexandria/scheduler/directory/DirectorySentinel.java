package io.intino.alexandria.scheduler.directory;

import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.*;

public class DirectorySentinel {

	private DirectoryTask task;
	private WatchService watcher;
	private Thread thread;
	private boolean closed = false;


	public enum Event {OnCreate, OnModify, OnDelete}

	public DirectorySentinel(File directory, DirectoryTask task, Event... events) {
		try {
			this.watcher = FileSystems.getDefault().newWatchService();
			this.task = task;
			directory.toPath().register(watcher, kindsOf(events));
			buildTask(task);
		} catch (IOException e) {
			this.watcher = null;
		}
	}

	private void buildTask(DirectoryTask task) {
		this.thread = new Thread(() -> {
			while (!closed) {
				try {
					WatchKey key = watcher.take();
					for (WatchEvent<?> event : key.pollEvents()) {
						@SuppressWarnings("unchecked")
						WatchEvent<Path> ev = (WatchEvent<Path>) event;
						new Thread(() -> task.execute(ev.context().toFile(), eventOf(ev.kind()))).start();
					}
					boolean valid = key.reset();
					if (!valid) break;
				} catch (InterruptedException | ClosedWatchServiceException ex) {
					Logger.error(ex);
					return;
				}
			}
		});
	}

	public void watch() {
		thread.start();
	}

	public void stop() {
		try {
			watcher.close();
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
