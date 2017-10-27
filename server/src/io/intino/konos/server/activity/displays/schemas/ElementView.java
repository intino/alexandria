package io.intino.konos.server.activity.displays.schemas;

public class ElementView implements java.io.Serializable {

	private String name = "";
	private String label = "";
	private String type = "";
	private Boolean embeddedElement = true;
	private Integer width = 0;
	private Boolean canSearch = true;
	private Boolean canCreateClusters = true;
	private java.util.List<String> clusters = new java.util.ArrayList<>();
	private String emptyMessage = "";
	private Toolbar toolbar;
	private Mold mold;
	private Center center;
	private Zoom zoom;

	public String name() {
		return this.name;
	}

	public String label() {
		return this.label;
	}

	public String type() {
		return this.type;
	}

	public Boolean embeddedElement() {
		return this.embeddedElement;
	}

	public Integer width() {
		return this.width;
	}

	public Boolean canSearch() {
		return this.canSearch;
	}

	public Boolean canCreateClusters() {
		return this.canCreateClusters;
	}

	public java.util.List<String> clusters() {
		return this.clusters;
	}

	public String emptyMessage() {
		return this.emptyMessage;
	}

	public Toolbar toolbar() {
		return this.toolbar;
	}

	public Mold mold() {
		return this.mold;
	}

	public Center center() {
		return this.center;
	}

	public Zoom zoom() {
		return this.zoom;
	}

	public ElementView name(String name) {
		this.name = name;
		return this;
	}

	public ElementView label(String label) {
		this.label = label;
		return this;
	}

	public ElementView type(String type) {
		this.type = type;
		return this;
	}

	public ElementView embeddedElement(Boolean embeddedElement) {
		this.embeddedElement = embeddedElement;
		return this;
	}

	public ElementView width(Integer width) {
		this.width = width;
		return this;
	}

	public ElementView canSearch(Boolean canSearch) {
		this.canSearch = canSearch;
		return this;
	}

	public ElementView canCreateClusters(Boolean canCreateClusters) {
		this.canCreateClusters = canCreateClusters;
		return this;
	}

	public ElementView clusters(java.util.List<String> clusters) {
		this.clusters = clusters;
		return this;
	}

	public ElementView emptyMessage(String emptyMessage) {
		this.emptyMessage = emptyMessage;
		return this;
	}

	public ElementView toolbar(Toolbar toolbar) {
		this.toolbar = toolbar;
		return this;
	}

	public ElementView mold(Mold mold) {
		this.mold = mold;
		return this;
	}

	public ElementView center(Center center) {
		this.center = center;
		return this;
	}

	public ElementView zoom(Zoom zoom) {
		this.zoom = zoom;
		return this;
	}
}