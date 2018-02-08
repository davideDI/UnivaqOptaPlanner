package it.univaq.planner.business.model;

public enum TipGroup {

	Generico  ((short)1, "Generico",  "Palazzina Generica");
	
	short id;
	String name;
	String descrizione;
	
	TipGroup (short id, String name, String descrizione) {
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
