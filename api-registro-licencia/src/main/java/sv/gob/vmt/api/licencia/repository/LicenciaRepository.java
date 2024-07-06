package sv.gob.vmt.api.licencia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sv.gob.vmt.api.licencia.entity.LicenciaEntity;

@Repository
public interface LicenciaRepository extends JpaRepository<LicenciaEntity, Long> {

	//Optional<LicenciaEntity> getLic(Long LicenciaId);

	Optional<List<LicenciaEntity>> getByDui(String dui);

}
