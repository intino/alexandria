package io.intino.test;

import io.intino.alexandria.scheduler.AlexandriaScheduler;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import java.time.Instant;
import java.util.Set;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class SchedulerTest {

	public static void main(String[] args) throws SchedulerException {
		AlexandriaScheduler scheduler = new AlexandriaScheduler("alexandria");
		JobDetail job = newJob(CheckServerInfoChangesSentinel.class).withIdentity("checkServerInfoChanges").build();
		scheduler.scheduleJob(job, Set.of(newTrigger().withIdentity("CheckServerInfoChangesSentinel")
				.startNow()
				.withSchedule(cronSchedule("* * * 1/1 * ? *")).build()), true);
		scheduler.startSchedules();
	}

	public static class CheckServerInfoChangesSentinel implements Job {
		@Override
		public void execute(JobExecutionContext jobExecutionContext) {
			System.out.println("running " + Instant.now());
		}
	}
}
