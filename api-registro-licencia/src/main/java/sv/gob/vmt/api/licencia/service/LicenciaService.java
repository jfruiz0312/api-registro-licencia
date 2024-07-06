package sv.gob.vmt.api.licencia.service;

import java.util.List;
import java.util.Optional;

import sv.gob.vmt.api.licencia.entity.LicenciaEntity;
import sv.gob.vmt.api.licencia.exceptions.ServiceException;

public interface LicenciaService {

	List<LicenciaEntity> fiddAll();

	Optional<LicenciaEntity> findById(Long id) throws ServiceException;

	Optional<List<LicenciaEntity>> getByDui(String dui);

	LicenciaEntity add(LicenciaEntity licencia) throws ServiceException;

	LicenciaEntity update(LicenciaEntity licencia);

	boolean delete(Long id) throws ServiceException;

	Optional<LicenciaEntity> getLic(Long LicenciaId);

}
