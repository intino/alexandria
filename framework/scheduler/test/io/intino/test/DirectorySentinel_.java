package io.intino.test;

import org.quartz.SchedulerException;

public class DirectorySentinel_ {

	public static void main(String[] args) throws SchedulerException {
		shouldListenDirectoryChanges();
	}
	public static void shouldListenDirectoryChanges() throws SchedulerException {
		io.intino.alexandria.scheduler.AlexandriaScheduler scheduler = new io.intino.alexandria.scheduler.AlexandriaScheduler();
		scheduler.watchDirectory("directory", new java.io.File("./temp"), (file, event) -> {
			System.out.println(file.getAbsolutePath());
			System.out.println(event);
			System.out.println();
		}, io.intino.alexandria.scheduler.directory.DirectorySentinel.Event.OnModify, io.intino.alexandria.scheduler.directory.DirectorySentinel.Event.OnCreate, io.intino.alexandria.scheduler.directory.DirectorySentinel.Event.OnDelete);
	}
}
