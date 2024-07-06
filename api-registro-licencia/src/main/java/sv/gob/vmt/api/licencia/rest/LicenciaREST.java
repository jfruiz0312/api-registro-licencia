package sv.gob.vmt.api.licencia.rest;

import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import static sv.gob.vmt.api.licencia.commons.APIConst.API_LICENCIA;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import sv.gob.vmt.api.licencia.entity.LicenciaEntity;
import sv.gob.vmt.api.licencia.exceptions.ServiceException;
import sv.gob.vmt.api.licencia.service.LicenciaService;
import sv.gob.vmt.api.licencia.service.IUploadFileService;

@Slf4j
@RestController
@RequestMapping(API_LICENCIA)
@Validated
public class LicenciaREST {

	private LicenciaService licenciaService;
	private IUploadFileService uploadService;

	public LicenciaREST(LicenciaService licenciaService, IUploadFileService uploadService) {
		this.licenciaService = licenciaService;
		this.uploadService = uploadService;
	}

	@GetMapping(path = "/listar", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<LicenciaEntity>> findAll() {
		log.debug("Listado de licencias");
		return new ResponseEntity<>(licenciaService.fiddAll(), HttpStatus.OK);
	}

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public LicenciaEntity findById(@PathVariable("id") Long id) {
		try {
			return licenciaService.findById(id).orElse(null);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping(path = "/add", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public LicenciaEntity add(@Valid @RequestBody LicenciaEntity licencia){
		log.info("add licencias...");
		LicenciaEntity cont = new LicenciaEntity();
		try {
			cont = licenciaService.add(licencia);
		} catch (ServiceException e) {
			log.error(e.getMessage(), e);
		}
		return cont;

	}

	@PutMapping(path = "/update/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<LicenciaEntity> update(@PathVariable("id") Long id,@Valid @RequestBody LicenciaEntity licencia) {
		try {
			Optional<LicenciaEntity> existingLicense = licenciaService.findById(id);

			if (existingLicense.isPresent()) {
				// Update existing license with provided data (avoid overwriting ID)
				licencia.setId(existingLicense.get().getId()); // Set ID from existing entity
				LicenciaEntity updatedLicense = licenciaService.update(licencia);
				return ResponseEntity.ok(updatedLicense); // Return 200 OK with updated entity
			} else {
				// Handle non-existent license scenario (consider throwing a specific exception)
				return ResponseEntity.notFound().build(); // Return 500 Internal Server Error
			}
		} catch (ServiceException e) {
			// Handle service exceptions appropriately (log, return specific error response)
			log.error("Error updating license", e);
			return ResponseEntity.internalServerError().build(); // Return 500 Internal Server Error
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) throws ServiceException {
		Map<String, Object> response = new HashMap<>();

		try {
			LicenciaEntity lic = licenciaService.findById(id).get();
			String nombreFotoAnterior = lic.getFoto();

			uploadService.eliminar(nombreFotoAnterior);
			licenciaService.delete(id);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el registro de licencia de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El registro de licencia eliminado con éxito!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@PostMapping("/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id)
			throws ServiceException {
		Map<String, Object> response = new HashMap<>();

		LicenciaEntity licencia = licenciaService.findById(id).get();

		if (!archivo.isEmpty()) {

			String nombreArchivo = null;
			try {
				nombreArchivo = uploadService.copiar(archivo);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen de la licencia...");
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			String nombreFotoAnterior = licencia.getFoto();

			uploadService.eliminar(nombreFotoAnterior);

			licencia.setFoto(nombreArchivo);

			licenciaService.add(licencia);

			response.put("cliente", licencia);
			response.put("mensaje", "Has subido correctamente la imagen: " + nombreArchivo);

		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@GetMapping("/uploads/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable("nombreFoto") String nombreFoto) {

		Resource recurso = null;

		try {
			recurso = uploadService.cargar(nombreFoto);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");

		return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
	}
}
