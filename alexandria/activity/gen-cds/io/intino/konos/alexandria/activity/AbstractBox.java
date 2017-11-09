package io.intino.konos.alexandria.activity;

import java.util.Map;

import io.intino.konos.alexandria.activity.AlexandriaActivityBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

import io.intino.konos.alexandria.activity.displays.Soul;

public abstract class AbstractBox extends io.intino.konos.alexandria.Box {
	private static Logger logger = LoggerFactory.getLogger(ROOT_LOGGER_NAME);
	protected AlexandriaActivityConfiguration configuration;
    protected Map<String, Soul> activitySouls = new java.util.HashMap<>();

	public AbstractBox(String[] args) {
		this(new AlexandriaActivityConfiguration(args));
	}

	public AbstractBox(AlexandriaActivityConfiguration configuration) {

		this.configuration = configuration;
		if (configuration().activityElementsConfiguration != null)
			io.intino.konos.alexandria.activity.ActivityAlexandriaSpark.setup(configuration().activityElementsConfiguration.port, configuration().activityElementsConfiguration.webDirectory, configuration().activityElementsConfiguration.authService);
	}

	public AlexandriaActivityConfiguration configuration() {
		return (AlexandriaActivityConfiguration) configuration;
	}

	@Override
	public io.intino.konos.alexandria.Box put(Object o) {

		return this;
	}

	public io.intino.konos.alexandria.Box open() {
		if(owner != null) owner.open();
		initActivities();
		initRESTServices();
		initJMXServices();
		initJMSServices();
		initDataLake();
		initTasks();
		initSlackBots();
		return this;
	}

	public void close() {
		if(owner != null) owner.close();
		io.intino.konos.alexandria.activity.ActivityAlexandriaSpark.instance().stop();


	}

	public void registerSoul(String clientId, Soul soul) {

	activitySouls.put(clientId, soul);
}

public void unRegisterSoul(String clientId) {

	activitySouls.remove(clientId);
}







	private void initRESTServices() {

	}

	private void initJMSServices() {


	}

	private void initJMXServices() {

	}

	private void initSlackBots() {

	}

	private void initActivities() {
		if (configuration().activityElementsConfiguration == null) return;
		ActivityElementsActivity.init(io.intino.konos.alexandria.activity.ActivityAlexandriaSpark.instance(), (AlexandriaActivityBox) this).start();
		logger.info("Activity ActivityElements: started!");
	}

	private void initDataLake() {

	}

	private void initTasks() {

	}

	private void initLogger() {
		final java.util.logging.Logger logger = java.util.logging.Logger.getGlobal();
		final ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.INFO);
		handler.setFormatter(new io.intino.konos.alexandria.LogFormatter("log"));
		logger.addHandler(handler);
	}
}