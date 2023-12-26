package indimeter.reservas.reservas_medicas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import indimeter.reservas.reservas_medicas.model.Paciente;

@Repository
public interface PacienteRepo extends JpaRepository<Paciente,String>{
    //consulta sql para obtener los pacientes que tienen citas medicas con un profesional_salud dado su rut
    @Query(value = "SELECT p.* FROM paciente p JOIN cita_medica c ON p.rut_paciente=c.rut_paciente WHERE c.rut_profesional=:rutProf", nativeQuery = true)
    List<Paciente> queryBy(@Param("rutProf") String rutProf);
    //consulta sql para obtener los pacientes que tienen citas medicas pagadas con un profesional_salud dado su rut
    @Query(value="SELECT * FROM paciente  JOIN cita_medica WHERE rut_profesional=:rutPro AND pagocita=true ", nativeQuery = true)
    List<Paciente> queryByPagado(@Param("rutPro")String rutPro);
}
