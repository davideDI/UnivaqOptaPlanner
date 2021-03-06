package it.univaq.planner.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HttpErrorsController extends ABaseController {

	@RequestMapping(value="404")
	public String notFound() {
		
		logger.error("HttpErrorsController - notFound()");
		return "notFound";
		
	}
	
	@RequestMapping(value="4xx")
	public String clientError() {
		
		logger.error("HttpErrorsController - clientError() - 4XX");
		return "clientError";
		
	}
	
	@RequestMapping(value="5xx")
	public String serverError() {
		
		logger.error("HttpErrorsController - serverError() - 5XX");
		return "serverError";
		
	}
	
}
