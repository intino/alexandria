package org.siani.pandora.scheduling;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.Set;

public class TeseoScheduler {

	private Scheduler scheduler;

	public TeseoScheduler() {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public void start() throws SchedulerException {
		scheduler.start();
	}

	public void shutdown() throws SchedulerException {
		scheduler.shutdown();
	}

	public boolean isShutdown() throws SchedulerException {
		return scheduler.isShutdown();
	}

	public Date scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
		return scheduler.scheduleJob(jobDetail, trigger);
	}

	public void scheduleJob(JobDetail jobDetail, Set<? extends Trigger> triggersForJob, boolean replace) throws SchedulerException {
		scheduler.scheduleJob(jobDetail, triggersForJob, replace);
	}
}
