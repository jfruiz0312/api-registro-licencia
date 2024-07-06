package sv.gob.vmt.api.licencia.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import sv.gob.vmt.api.licencia.entity.LicenciaEntity;
import sv.gob.vmt.api.licencia.exceptions.ServiceException;
import sv.gob.vmt.api.licencia.repository.LicenciaRepository;

@Service
public class LicenciaServiceImpl implements LicenciaService {

	private LicenciaRepository licenciaRepository;

	public LicenciaServiceImpl(LicenciaRepository licenciaRepository) {
		this.licenciaRepository = licenciaRepository;
	}

	@Override
	public List<LicenciaEntity> fiddAll() {

		return licenciaRepository.findAll();
	}

	@Override
	public LicenciaEntity add(LicenciaEntity licencia) {

		return licenciaRepository.save(licencia);
	}

	@Override
	public LicenciaEntity update(LicenciaEntity licencia) {
		Optional<LicenciaEntity> optLicencia = licenciaRepository.findById(licencia.getId());
		if (optLicencia.isPresent()) {
			LicenciaEntity retLicencia = optLicencia.get();
			BeanUtils.copyProperties(licencia, retLicencia);
			return licenciaRepository.save(retLicencia);
		} else {
			// Handle non-existent license scenario (consider throwing a specific exception)
		}
		return licencia;
	}

	@Override
	public Optional<LicenciaEntity> findById(Long id) {
		Optional<LicenciaEntity> licenciaOptional;
		try {
			licenciaOptional = licenciaRepository.findById(id);
			if (!licenciaOptional.isPresent()) {
				throw new ServiceException("Licencia not found with ID: " + id);
			}
		} catch (ServiceException e) {
			// Log the error for debugging purposes
			System.err.println("Error finding Licencia: " + e.getMessage());
			licenciaOptional = Optional.empty(); // Return empty Optional on exception

		}
		return licenciaOptional;
	}

	@Override
	public boolean delete(Long id) {
		return getLic(id).map(lic -> {
			licenciaRepository.delete(lic);
			return true;
		}).orElse(false);
	}

	@Override
	public Optional<LicenciaEntity> getLic(Long LicenciaId) {
		return licenciaRepository.findById(LicenciaId);
	}

	@Override
	public Optional<List<LicenciaEntity>> getByDui(String dui) {

		return licenciaRepository.getByDui(dui);
	}
}
