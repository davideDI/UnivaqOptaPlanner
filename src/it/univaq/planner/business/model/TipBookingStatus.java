package it.univaq.planner.business.model;

public enum TipBookingStatus {

	Richiesta     ((short)1, "Richiesta"),
	InLavorazione ((short)2, "In Lavorazione"),
	Gestita       ((short)3, "Gestita"),
	Scartata      ((short)4, "Scartata");
	
	short id;
	String descrizione;
	
	TipBookingStatus (short id, String descrizione) {
		this.id = id;
		this.descrizione = descrizione;
	}

	public short getId() {
		return id;
	}

	public String getDescrizione() {
		return descrizione;
	}
	
}
