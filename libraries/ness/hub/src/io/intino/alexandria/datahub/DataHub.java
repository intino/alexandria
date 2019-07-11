package io.intino.alexandria.datahub;

import io.intino.alexandria.Scale;
import io.intino.alexandria.datahub.bus.BrokerManager;
import io.intino.alexandria.datahub.bus.BrokerService;
import io.intino.alexandria.datahub.bus.PipeManager;
import io.intino.alexandria.datahub.datalake.DatalakeCloner;
import io.intino.alexandria.datahub.datalake.TankManager;
import io.intino.alexandria.datahub.model.Broker;
import io.intino.alexandria.datahub.model.Configuration;
import io.intino.alexandria.datahub.model.Configuration.Local;
import io.intino.alexandria.datahub.model.Configuration.Mirror;
import io.intino.alexandria.datahub.model.Configuration.Service;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FileDatalake;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.sealing.FileSessionSealer;
import io.intino.alexandria.sealing.SessionSealer;
import org.apache.log4j.Level;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class DataHub {
	static {
		io.intino.alexandria.logger4j.Logger.init(Level.WARN);
	}

	private final File brokerStage;
	private final Configuration configuration;
	private Datalake datalake;
	private BrokerManager brokerManager;
	private BrokerService brokerService;
	private AdminService adminService;
	private SessionSealer sessionSealer;
	private PipeManager pipeManager;
	private SealingTask sealingTask;

	public DataHub(Configuration configuration) {
		this.configuration = configuration;
		this.adminService = new AdminService(this);
		this.brokerStage = new File(workspaceDirectory(), "broker_stage");
		workspaceDirectory().mkdirs();
		this.brokerStage.mkdirs();
		stageFolder().mkdirs();
	}

	public void start() {
		if (isService()) configureDatalake(((Service) configuration.dataSource()).path());
		else if (isLocal()) configureDatalake(((Local) configuration.dataSource()).path());
		else configureMirror((Mirror) configuration.dataSource());
		if (configuration.broker() != null) configureBroker();
	}

	public void stop() {
		Logger.info("Shutting down datalake...");
		sealingTask = null;
		if (brokerManager != null) brokerManager.stop();
	}

	public Datalake datalake() {
		return datalake;
	}

	public SessionSealer sessionSealer() {
		return sessionSealer;
	}

	public BrokerService brokerService() {
		return brokerService;
	}

	public File stageFolder() {
		return new File(workspaceDirectory(), "stage");
	}

	BrokerManager brokerManager() {
		return brokerManager;
	}

	File brokerStageDirectory() {
		return brokerStage;
	}

	private void configureMirror(Mirror mirror) {
		configureDatalake(mirror.datalakeOriginPath());
		cloneDatalake();
	}

	private void configureDatalake(String path) {
		this.datalake = new FileDatalake(new File(path));
		this.sessionSealer = new FileSessionSealer((FileDatalake) datalake, stageFolder());
	}

	private void configureBroker() {
		brokerService = new BrokerService(brokerPort(), mqttPort(), true, brokerDirectory(), users(), Collections.emptyList());
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
		datalake.eventStore().tanks().forEach(t -> new TankManager(brokerManager, brokerStageDirectory(), t, scale).register());
	}

	private File workspaceDirectory() {
		return new File(configuration.workingDirectory());
	}

	private File brokerDirectory() {
		return new File(workspaceDirectory(), "broker");
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