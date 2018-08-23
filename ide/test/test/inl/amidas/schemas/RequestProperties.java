package inl.amidas.schemas;

public  class RequestProperties implements java.io.Serializable {

	private String label = "";
	private String description = "";
	private String priority = "";

	public String label() {
		return this.label;
	}

	public String description() {
		return this.description;
	}

	public String priority() {
		return this.priority;
	}

	public RequestProperties label(String label) {
		this.label = label;
		return this;
	}

	public RequestProperties description(String description) {
		this.description = description;
		return this;
	}

	public RequestProperties priority(String priority) {
		this.priority = priority;
		return this;
	}

}