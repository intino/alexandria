package io.intino.konos.alexandria.activity;

public class AlexandriaActivityConfiguration extends io.intino.konos.alexandria.BoxConfiguration {

	ActivityElementsActivityConfiguration activityElementsConfiguration;

	public AlexandriaActivityConfiguration(String[] args) {
		super(args);
		fillWithArgs();
	}

	private void fillWithArgs() {

		if (args.containsKey("activityElements_port")) {
			activityElementsConfiguration(toInt(args.remove("activityElements_port")), args.remove("activityElements_webDirectory"));

		}
	}



	public AlexandriaActivityConfiguration activityElementsConfiguration(int port, String webDirectory) {
		this.activityElementsConfiguration = new ActivityElementsActivityConfiguration();
		this.activityElementsConfiguration.port = port;
		this.activityElementsConfiguration.webDirectory = webDirectory == null ? "www/" : webDirectory;

		return this;
	}

	public AlexandriaActivityConfiguration activityElementsConfiguration(int port) {
		return activityElementsConfiguration(port, "www/");
	}

	public ActivityElementsActivityConfiguration activityElementsConfiguration() {
		return activityElementsConfiguration;
	}

	public static class ActivityElementsActivityConfiguration {
		public int port;
		public String webDirectory;
		public io.intino.konos.alexandria.activity.services.AuthService authService;



	}
}