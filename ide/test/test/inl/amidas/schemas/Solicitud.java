package inl.amidas.schemas;

public  class Solicitud implements java.io.Serializable {

	private java.util.List<SolicitudDocumento> solicitudDocumentoList = new java.util.ArrayList<>();

	public java.util.List<SolicitudDocumento> solicitudDocumentoList() {
		return this.solicitudDocumentoList;
	}

	public Solicitud solicitudDocumentoList(java.util.List<SolicitudDocumento> solicitudDocumentoList) {
		this.solicitudDocumentoList = solicitudDocumentoList;
		return this;
	}

}