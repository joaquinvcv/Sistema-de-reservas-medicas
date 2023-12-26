package indimeter.reservas.reservas_medicas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import indimeter.reservas.reservas_medicas.model.Corresponde;

@Repository
public interface CorrespondeRepo extends JpaRepository<Corresponde,Integer> {
    
    @Query(value = "select c.* from corresponde as c join horario_laboral as h on c.id_horario = h.id_horario where h.rut_profesional= :rutProf AND c.ocupado=false", nativeQuery = true)
    List<Corresponde> findcitasDisponibles(@Param("rutProf") String rutProf);

    @Query(value = "select c.* from corresponde as c where c.id_cita = :idcita", nativeQuery = true)
    Corresponde getHorarioActual(@Param("idcita") int idcita);

    @Query(value = "select count(id) from corresponde",nativeQuery = true)
    int horariosTotales();
}