package io.intino.alexandria.scheduler;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.scheduler.directory.DirectorySentinel;
import io.intino.alexandria.scheduler.directory.DirectoryTask;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AlexandriaScheduler {
	private Scheduler scheduler;
	private final Map<String, DirectorySentinel> sentinels = new HashMap<>();

	public AlexandriaScheduler() {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
			Logger.error(e);
		}
	}

	public AlexandriaScheduler(String threadPoolPrefix) {
		try {
			Properties properties = new Properties();
			properties.load(this.getClass().getClassLoader().getResourceAsStream("org/quartz/quartz.properties"));
			properties.put("org.quartz.threadPool.threadNamePrefix", threadPoolPrefix);
			StdSchedulerFactory fact = new StdSchedulerFactory(properties);
			this.scheduler = fact.getScheduler();
		} catch (SchedulerException | IOException e) {
			Logger.error(e);
		}
	}

	public void startSchedules() throws SchedulerException {
		scheduler.start();
	}

	public void shutdownSchedules() throws SchedulerException {
		scheduler.shutdown();
	}

	public boolean isSchedulerShutdown() throws SchedulerException {
		return scheduler.isShutdown();
	}

	public Date scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
		return scheduler.scheduleJob(jobDetail, trigger);
	}

	public void scheduleJob(JobDetail jobDetail, Set<? extends Trigger> triggersForJob, boolean replace) throws SchedulerException {
		scheduler.scheduleJob(jobDetail, triggersForJob, replace);
	}

	public void unscheduleJob(Trigger trigger) throws SchedulerException {
		scheduler.unscheduleJob(trigger.getKey());
	}


	public void unscheduleJob(TriggerKey triggerKey) throws SchedulerException {
		scheduler.unscheduleJob(triggerKey);
	}

	public void watchDirectory(String name, File directory, DirectoryTask task, DirectorySentinel.Event... events) throws SchedulerException {
		DirectorySentinel sentinel = new DirectorySentinel(directory, task, events);
		sentinels.put(name, sentinel);
		sentinel.watch();
	}

	public void stopSentinel(String name) {
		sentinels.computeIfPresent(name, (k, v) -> {
			v.stop();
			return null;
		});
	}
}
