package io.intino.alexandria.datahub;

import io.intino.alexandria.Scale;
import io.intino.alexandria.datahub.bus.BrokerManager;
import io.intino.alexandria.datahub.bus.BrokerService;
import io.intino.alexandria.datahub.bus.PipeManager;
import io.intino.alexandria.datahub.datalake.DatalakeCloner;
import io.intino.alexandria.datahub.datalake.SealingTask;
import io.intino.alexandria.datahub.datalake.TankManager;
import io.intino.alexandria.datahub.model.Broker;
import io.intino.alexandria.datahub.model.Configuration;
import io.intino.alexandria.datahub.model.Configuration.Local;
import io.intino.alexandria.datahub.model.Configuration.Mirror;
import io.intino.alexandria.datahub.model.Configuration.Service;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FileDatalake;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.sealing.FileSessionManager;
import io.intino.alexandria.sealing.SessionManager;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static java.io.File.separator;

public class DataHub {
	private final File brokerSessionsDirectory;
	private final Configuration configuration;
	private Datalake datalake;
	private BrokerManager brokerManager;
	private BrokerService brokerService;
	private AdminService adminService;
	private SessionManager sessionManager;
	private PipeManager pipeManager;
	private SealingTask sealingTask;

	public DataHub(Configuration configuration) {
		this.configuration = configuration;
		this.adminService = new AdminService(this);
		this.brokerSessionsDirectory = new File(workspace(), "stage_inl");
		this.brokerSessionsDirectory.mkdirs();
		new File(workspace(), "stage").mkdirs();
	}

	public void start() {
		if (isService()) configureDatalake(((Service) configuration.dataSource()).path());
		if (isLocal()) configureDatalake(((Local) configuration.dataSource()).path());
		else configureMirror((Mirror) configuration.dataSource());

		if (configuration.broker() != null) configureBroker();
	}

	public void stop() {
		Logger.info("Shutting down datalake...");
		sealingTask = null;
		if (brokerManager != null) brokerManager.stop();
	}

	public void restartBroker() {
		brokerManager.restart(true);
		startServices();
	}

	public String workspace() {
		return configuration.workingDirectory();
	}

	public Datalake datalake() {
		return datalake;
	}

	public SessionManager sessionManager() {
		return sessionManager;
	}

	public BrokerService brokerService() {
		return brokerService;
	}

	BrokerManager brokerManager() {
		return brokerManager;
	}

	public File temporalSessionDirectory() {
		return brokerSessionsDirectory;
	}

	private void configureMirror(Mirror mirror) {
		configureDatalake(mirror.datalakeOriginPath());
		cloneDatalake();
	}

	private void configureDatalake(String path) {
		this.datalake = new FileDatalake(new File(path));
		this.sessionManager = new FileSessionManager((FileDatalake) datalake, new File(workspace(), "stage"));
	}

	private void configureBroker() {
		brokerService = new BrokerService(brokerPort(), mqttPort(), true, new File(brokerDirectory()), users(), Collections.emptyList());
		brokerManager = new BrokerManager(configuration.broker().connectorId(), brokerService);
		brokerManager.start();
		pipeManager = new PipeManager(brokerManager);
		startServices();
	}

	private void cloneDatalake() {
		new DatalakeCloner((Mirror) configuration.dataSource(), configuration.tanks());
	}

	private void startServices() {
		if (isService() && configuration.broker() != null) {
			startTanks(((Service) configuration.dataSource()).scale());
			startPipes();
			startAdminService();
		}
		if (isService() && ((Service) configuration.dataSource()).sealingPattern() != null)
			startSealingTask(((Service) configuration.dataSource()).sealingPattern());
	}

	private void startSealingTask(String pattern) {
		sealingTask = new SealingTask(this, pattern);
		sealingTask.start();
	}

	private void startPipes() {
		for (Broker.Pipe pipe : configuration.broker().pipes()) pipeManager.start(pipe);
	}

	private void startAdminService() {
		brokerManager.registerConsumer("service.ness.admin", adminService);
	}

	private Map<String, String> users() {
		return configuration.broker().users().stream().collect(Collectors.toMap(user -> user.name() == null ? user.name() : user.name(), Broker.User::password));
	}

	private void startTanks(Scale scale) {
		datalake.eventStore().tanks().forEach(t -> new TankManager(brokerManager, temporalSessionDirectory(), t, scale).register());
	}

	private String brokerDirectory() {
		return workspace() + separator + "broker";
	}

	private int brokerPort() {
		return configuration.broker().port();
	}

	private int mqttPort() {
		return configuration.broker().mqtt_port();
	}

	private boolean isService() {
		return configuration.dataSource() instanceof Service;
	}

	private boolean isLocal() {
		return configuration.dataSource() instanceof Local;
	}
}