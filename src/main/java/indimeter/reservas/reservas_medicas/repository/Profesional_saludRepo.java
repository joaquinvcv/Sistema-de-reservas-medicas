package indimeter.reservas.reservas_medicas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import indimeter.reservas.reservas_medicas.model.Profesional_salud;

@Repository
public interface Profesional_saludRepo extends JpaRepository<Profesional_salud,String>{
    @Query(value = "SELECT profesional_salud.* FROM profesional_salud JOIN especialidad USING(id_especialidad) JOIN horario_laboral USING(rut_profesional) WHERE cupos_diarios>0 AND nombre_especialidad =:nomEsp ORDER BY nombre_especialidad", nativeQuery = true)
    List<Profesional_salud>profesionalesDispByEsp(@Param("nomEsp")String nombre_especialidad);
    @Query(value = "SELECT costo_especialidad from profesional_salud JOIN especialidad USING(id_especialidad) where rut_profesional =:rutProf",nativeQuery = true)
    float getPrecioBaseByProfesional(@Param("rutProf") String rutProf);
}