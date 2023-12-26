package indimeter.reservas.reservas_medicas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import indimeter.reservas.reservas_medicas.model.Especialidad;

@Repository
public interface EspecialidadRepo extends JpaRepository<Especialidad,Integer> {
    @Query(value="select info_especialidad from especialidad join profesional_salud USING(id_especialidad) where rut_profesional =:rutProf",nativeQuery = true)
    String findInfoByRutProf(@Param("rutProf")String rutProf);
}
