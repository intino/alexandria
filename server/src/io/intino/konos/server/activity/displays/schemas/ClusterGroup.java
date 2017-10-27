package io.intino.konos.server.activity.displays.schemas;

public class ClusterGroup implements java.io.Serializable {

	private String name = "";
	private String cluster = "";
	private java.util.List<String> items = new java.util.ArrayList<>();

	public String name() {
		return this.name;
	}

	public String cluster() {
		return this.cluster;
	}

	public java.util.List<String> items() {
		return this.items;
	}

	public ClusterGroup name(String name) {
		this.name = name;
		return this;
	}

	public ClusterGroup cluster(String cluster) {
		this.cluster = cluster;
		return this;
	}

	public ClusterGroup items(java.util.List<String> items) {
		this.items = items;
		return this;
	}
}