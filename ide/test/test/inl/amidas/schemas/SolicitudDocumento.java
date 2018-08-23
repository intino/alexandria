package inl.amidas.schemas;

public  class SolicitudDocumento implements java.io.Serializable {

	private String descripcion = "";
	private io.intino.konos.alexandria.schema.Resource fichero;

	public String descripcion() {
		return this.descripcion;
	}

	public io.intino.konos.alexandria.schema.Resource fichero() {
		return this.fichero;
	}

	public SolicitudDocumento descripcion(String descripcion) {
		this.descripcion = descripcion;
		return this;
	}

	public SolicitudDocumento fichero(io.intino.konos.alexandria.schema.Resource fichero) {
		this.fichero = fichero;
		return this;
	}

}