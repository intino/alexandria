package io.intino.alexandria.restaccessor;

import io.intino.alexandria.Json;
import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.restaccessor.RequestBuilder.Request;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OutBox {
	private static final String JSON = ".json";
	protected final File directory;
	protected List<File> files;

	OutBox(File directory, int retryIntervalSeconds) {
		this.directory = directory;
		this.reloadOutBox();
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(this::retry, 15, retryIntervalSeconds, TimeUnit.SECONDS);
	}

	private void retry() {
		Request peek = peek();
		if (peek != null) {
			try {
				peek.execute();
				pop();
			} catch (AlexandriaException e) {
				Logger.error(e);
			}
		}
	}

	void push(Request request) {
		try {
			File file = new File(directory, UUID.randomUUID().toString() + JSON);
			Files.write(file.toPath(), request.toString().getBytes());
			files.add(file);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private Request peek() {
		String requestContent = get();
		if (requestContent == null) return null;
		try {
			return Json.fromString(requestContent, RequestBuilder.class).build();
		} catch (URISyntaxException e) {
			Logger.error(e);
			pop();
			return null;
		}
	}

	private String get() {
		if (files.isEmpty()) reloadOutBox();
		if (files.isEmpty()) return null;
		try {
			File file = files.get(0);
			if (!file.exists()) {
				files.remove(file);
				return null;
			}
			return Files.readString(file.toPath());
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
	}

	private void pop() {
		reloadOutBox();
		if (files.isEmpty()) return;
		files.get(0).delete();
		files.remove(0);
	}

	protected synchronized void reloadOutBox() {
		if (files.isEmpty()) {
			files = new ArrayList<>(Arrays.asList(Objects.requireNonNull(directory.listFiles(f -> f.getName().endsWith(JSON)))));
			files.sort(Comparator.comparingLong(File::lastModified));
		}
	}
}
