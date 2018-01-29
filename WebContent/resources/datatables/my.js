function addparams( d ) {
	return $.extend( {}, d, {
		"sortCol": d["columns"][d["order"][0].column].data,
		"sortDir": d["order"][0].dir,
		"search": d["search"]["value"],
	} );
}