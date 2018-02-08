package it.univaq.planner.business.model;

public enum TipUser {

	AdminAteneo       ((short)1, "admin ateneo",       "utente admin per la gestione delle risorse dell'ateneo"),
	AdminDipartimento ((short)2, "admin dipartimento", "utente admin per la gestione delle risorse del dipartimento"),
	Teacher   		  ((short)3, "teacher",            "utente che effettua la richiesta di prenotazione di una risorsa"),
	Inquirer 		  ((short)4, "inquirer",           "uscieri per verifiche"),
	Secretary  		  ((short)5, "secretary",          "segreteria dipartimento"),
	Student  	      ((short)6, "student",            "utente studente"),
	Member  		  ((short)7, "member",             "utente member che non ha ruoli specifici");
	
	short id;
	String name;
	String descrizione;
	
	TipUser (short id, String name, String descrizione) {
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
