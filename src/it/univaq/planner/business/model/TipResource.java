package it.univaq.planner.business.model;

public enum TipResource {

	Aula        ((short)1, "Aula",        ""),
	Palestra    ((short)2, "Palestra",    ""),
	Laboratorio ((short)3, "Laboratorio", ""),
	AulaMagna   ((short)4, "Aula Magna",  "");
	
	short id;
	String name;
	String descrizione;
	
	TipResource (short id, String name, String descrizione) {
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
