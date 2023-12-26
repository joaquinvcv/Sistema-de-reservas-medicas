package indimeter.reservas.reservas_medicas.repository;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import indimeter.reservas.reservas_medicas.model.Horario_laboral;

@Repository
public interface HorarioLaboralRepo extends JpaRepository<Horario_laboral,Integer> {
    

    @Query(value = "SELECT c.id_cita FROM cita_medica c JOIN corresponde co ON c.id_cita=co.id_cita WHERE co.id_horario=:idHorario", nativeQuery = true)
    ArrayList<Integer> findCitasByIdHorario(@Param("idHorario") int idHorario);

    @Query(value = "Select h.* From horario_laboral as h where h.rut_profesional=:rut", nativeQuery = true)
    Optional<Horario_laboral> getbyrut(@Param("rut") String rut);
}
