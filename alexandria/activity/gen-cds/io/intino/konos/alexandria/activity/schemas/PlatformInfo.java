package io.intino.konos.alexandria.activity.schemas;

public class PlatformInfo implements java.io.Serializable {

	private String title = "";
	private String subtitle = "";
	private String logo = "";
	private String favicon = "";
	private String authServiceUrl = "";

	public String title() {
		return this.title;
	}

	public String subtitle() {
		return this.subtitle;
	}

	public String logo() {
		return this.logo;
	}

	public String favicon() {
		return this.favicon;
	}

	public String authServiceUrl() {
		return this.authServiceUrl;
	}

	public PlatformInfo title(String title) {
		this.title = title;
		return this;
	}

	public PlatformInfo subtitle(String subtitle) {
		this.subtitle = subtitle;
		return this;
	}

	public PlatformInfo logo(String logo) {
		this.logo = logo;
		return this;
	}

	public PlatformInfo favicon(String favicon) {
		this.favicon = favicon;
		return this;
	}

	public PlatformInfo authServiceUrl(String authServiceUrl) {
		this.authServiceUrl = authServiceUrl;
		return this;
	}
}