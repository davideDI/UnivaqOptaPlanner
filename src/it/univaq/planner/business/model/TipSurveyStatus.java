package it.univaq.planner.business.model;

public enum TipSurveyStatus {

	Richiesta     ((short)1, "Richiesta"),
	Gestita       ((short)2, "Gestita"),
	Scartata      ((short)3, "Scartata");
	
	short id;
	String descrizione;
	
	TipSurveyStatus (short id, String descrizione) {
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
