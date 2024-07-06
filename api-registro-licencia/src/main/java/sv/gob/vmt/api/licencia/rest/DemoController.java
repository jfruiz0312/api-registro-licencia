package sv.gob.vmt.api.licencia.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {
	
	@GetMapping
	public String demo() {
		return "RESTful demo api-registro-licencias";
	}
	
	@GetMapping("/nombre")
	public String demo(@RequestParam(name="nombre",defaultValue="") String nombre) {
		return "RESTful demo "+ nombre;
	}

}
