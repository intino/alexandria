package inl.amidas.schemas;

public  class RequestPolicy implements java.io.Serializable {

	private java.util.List<String> workerGroups = new java.util.ArrayList<>();
	private java.util.List<String> capabilities = new java.util.ArrayList<>();
	private java.util.List<String> authorizations = new java.util.ArrayList<>();

	public java.util.List<String> workerGroups() {
		return this.workerGroups;
	}

	public java.util.List<String> capabilities() {
		return this.capabilities;
	}

	public java.util.List<String> authorizations() {
		return this.authorizations;
	}

	public RequestPolicy workerGroups(java.util.List<String> workerGroups) {
		this.workerGroups = workerGroups;
		return this;
	}

	public RequestPolicy capabilities(java.util.List<String> capabilities) {
		this.capabilities = capabilities;
		return this;
	}

	public RequestPolicy authorizations(java.util.List<String> authorizations) {
		this.authorizations = authorizations;
		return this;
	}

}