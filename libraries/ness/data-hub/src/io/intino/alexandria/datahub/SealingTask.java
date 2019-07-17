package io.intino.alexandria.datahub;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.ingestion.EventSession;
import io.intino.alexandria.ingestion.SessionHandler;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageReader;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.scheduler.AlexandriaScheduler;
import io.intino.alexandria.scheduler.ScheduledTrigger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

class SealingTask {

	private final DataHub hub;
	private final AlexandriaScheduler tasker;
	private final String pattern;

	SealingTask(DataHub hub, String pattern) {
		this.hub = hub;
		this.tasker = new io.intino.alexandria.scheduler.AlexandriaScheduler();
		this.pattern = pattern;
	}

	void start() {
		JobDetail job;
		try {
			job = newJob(SealTask.class).withIdentity("seal").build();
			job.getJobDataMap().put("hub", hub);
			tasker.scheduleJob(job, newSet(newTrigger().withIdentity("Hub#seal").withSchedule(cronSchedule(pattern)).build()), true);
			tasker.startSchedules();
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}
	}


	private Set<Trigger> newSet(Trigger... triggers) {
		LinkedHashSet<Trigger> set = new LinkedHashSet<>();
		java.util.Collections.addAll(set, triggers);
		return set;
	}

	public static class SealTask implements ScheduledTrigger {

		public void execute(JobExecutionContext context) {
			DataHub hub = (DataHub) context.getMergedJobDataMap().get("hub");
			Logger.info("Starting seal of tanks");
			new Thread(() -> {
				pushTemporalSessions(hub);
				hub.sessionSealer().seal();
				Logger.info("Sealing of tanks finished successfully");
			}).start();
		}

		private void pushTemporalSessions(DataHub hub) {
			try {
				for (File file : Objects.requireNonNull(hub.brokerStageDirectory().listFiles(f -> f.getName().endsWith(".inl")))) {
					String name = file.getName().replace(".inl", "");
					String[] split = name.split("#");
					SessionHandler handler = new SessionHandler();
					EventSession eventSession = handler.createEventSession();
					for (Message message : new MessageReader(new BufferedInputStream(new FileInputStream(file))))
						eventSession.put(split[0], new Timetag(split[1]), message);
					eventSession.close();
					handler.pushTo(hub.brokerStageDirectory());
					file.delete();
				}
			} catch (FileNotFoundException e) {
				Logger.error(e);
			}
		}
	}
}