package indimeter.reservas.reservas_medicas.repository;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import indimeter.reservas.reservas_medicas.model.CitaMedica;

@Repository
public interface CitaMedicaRepo extends JpaRepository<CitaMedica,Integer> {
    
    @Query(value = "SELECT * from cita_medica WHERE rut_profesional = :rutProf ORDER by fecha_atencion DESC",nativeQuery = true)
    List<CitaMedica> citasByProf(@Param("rutProf") String rutProf);

    @Query(value = "select * from cita_medica where rut_profesional = :rutProf and fecha_atencion BETWEEN :fecha1 and :fecha2", nativeQuery = true)
    List<CitaMedica> citasSemana(@Param("rutProf") String rutProf, @Param("fecha1") String fecha1, @Param("fecha2") String fecha2);


}
