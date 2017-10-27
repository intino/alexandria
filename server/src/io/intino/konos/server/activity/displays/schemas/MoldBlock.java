package io.intino.konos.server.activity.displays.schemas;

public class MoldBlock implements java.io.Serializable {

	private String name = "";
	private Boolean expanded = true;
	private String layout = "";
	private String style = "";
	private Integer width = 0;
	private Integer height = 0;
	private Boolean hidden = true;
	private Boolean hiddenIfMobile = true;
	private java.util.List<Stamp> stampList = new java.util.ArrayList<>();
	private java.util.List<MoldBlock> moldBlockList = new java.util.ArrayList<>();

	public String name() {
		return this.name;
	}

	public Boolean expanded() {
		return this.expanded;
	}

	public String layout() {
		return this.layout;
	}

	public String style() {
		return this.style;
	}

	public Integer width() {
		return this.width;
	}

	public Integer height() {
		return this.height;
	}

	public Boolean hidden() {
		return this.hidden;
	}

	public Boolean hiddenIfMobile() {
		return this.hiddenIfMobile;
	}

	public java.util.List<Stamp> stampList() {
		return this.stampList;
	}

	public java.util.List<MoldBlock> moldBlockList() {
		return this.moldBlockList;
	}

	public MoldBlock name(String name) {
		this.name = name;
		return this;
	}

	public MoldBlock expanded(Boolean expanded) {
		this.expanded = expanded;
		return this;
	}

	public MoldBlock layout(String layout) {
		this.layout = layout;
		return this;
	}

	public MoldBlock style(String style) {
		this.style = style;
		return this;
	}

	public MoldBlock width(Integer width) {
		this.width = width;
		return this;
	}

	public MoldBlock height(Integer height) {
		this.height = height;
		return this;
	}

	public MoldBlock hidden(Boolean hidden) {
		this.hidden = hidden;
		return this;
	}

	public MoldBlock hiddenIfMobile(Boolean hiddenIfMobile) {
		this.hiddenIfMobile = hiddenIfMobile;
		return this;
	}

	public MoldBlock stampList(java.util.List<Stamp> stampList) {
		this.stampList = stampList;
		return this;
	}

	public MoldBlock moldBlockList(java.util.List<MoldBlock> moldBlockList) {
		this.moldBlockList = moldBlockList;
		return this;
	}
}