package io.intino.alexandria.scheduler;

import io.intino.alexandria.scheduler.directory.DirectoryTask;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import io.intino.alexandria.scheduler.directory.DirectorySentinel;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AlexandriaScheduler {

	private Scheduler scheduler;
	private Map<String, DirectorySentinel> sentinels = new HashMap<>();


	public AlexandriaScheduler() {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
			e.printStackTrace();
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

	public void watchDirectory(String name, File directory, DirectoryTask task, DirectorySentinel.Event... events) throws SchedulerException {
		DirectorySentinel sentinel = new DirectorySentinel(directory, task, events);
		sentinels.put(name, sentinel);
		sentinel.watch();
	}

	public void stopSentinel(String name) {
		sentinels.get(name).stop();
	}
}
