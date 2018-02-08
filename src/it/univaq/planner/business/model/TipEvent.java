package it.univaq.planner.business.model;

public enum TipEvent {

	Esame     ((short)1, "Esame",     "Evento di tipo Esame"),
	Lezione   ((short)2, "Lezione",   "Evento di tipo Lezione"),
	Seminario ((short)3, "Seminario", "Evento di tipo Seminario"),
	Generico  ((short)4, "Generico",  "Evento di tipo Generico");
	
	short id;
	String name;
	String descrizione;
	
	TipEvent (short id, String name, String descrizione) {
		this.id = id;
		this.name = name;
		this.descrizione = descrizione;
	}

	public short getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public String getDescrizione() {
		return descrizione;
	}
	
}
