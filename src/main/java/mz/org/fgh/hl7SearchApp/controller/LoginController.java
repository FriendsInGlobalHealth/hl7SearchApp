package mz.org.fgh.hl7SearchApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/index")
	public String login() {
		return "index";
	}
}
