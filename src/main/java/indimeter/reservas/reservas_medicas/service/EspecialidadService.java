package indimeter.reservas.reservas_medicas.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indimeter.reservas.reservas_medicas.model.CitaMedica;
import indimeter.reservas.reservas_medicas.model.Especialidad;
import indimeter.reservas.reservas_medicas.model.Horario_laboral;
import indimeter.reservas.reservas_medicas.model.Profesional_salud;
import indimeter.reservas.reservas_medicas.repository.CitaMedicaRepo;
import indimeter.reservas.reservas_medicas.repository.EspecialidadRepo;
import indimeter.reservas.reservas_medicas.repository.HorarioLaboralRepo;
import indimeter.reservas.reservas_medicas.repository.Profesional_saludRepo;

@Service
@Transactional
public class EspecialidadService {
    
    @Autowired
    private EspecialidadRepo espeRepo;
    @Autowired
    private Profesional_saludRepo profeRepo;
    @Autowired
    private CitaMedicaRepo citaRepo;
    @Autowired
    private HorarioLaboralRepo horaRepo;

    public boolean crearEspecialidad(Especialidad espe){
        Optional<Especialidad> especialidadOptional = 
            espeRepo.findById(espe.getId_especialidad());
        if(especialidadOptional.isPresent() == false){
            espeRepo.saveAndFlush(espe);
            return true;
        }else{
            return false;
        }   
    }

    public boolean borrarEspecialidad(int id){
        Optional<Especialidad> especialidadOptional = 
            espeRepo.findById(id);
        if(especialidadOptional.isPresent()){
            List<Profesional_salud> profes = profeRepo.profesionalesDispByEsp(especialidadOptional.get().getNombre_especialidad());
            if(!profes.isEmpty()){
                for (Profesional_salud pro : profes) {
                    List<CitaMedica> citas = citaRepo.citasByProf(pro.getRut_profesional());
                    Optional<Horario_laboral> hor = horaRepo.getbyrut(pro.getRut_profesional());
                    for (CitaMedica cita : citas) {
                        citaRepo.deleteById(cita.getId_cita());
                    }
                    horaRepo.deleteById(hor.get().getId_horario());
                    profeRepo.deleteById(pro.getRut_profesional());
                }
                espeRepo.deleteById(id);
                return true;
            }else{
                espeRepo.deleteById(id);
                return true;
            }
        }else{
            return false;
        }
    }

    /* HU_20 Actualizar especialidad de un profesional SPRINT 3 */
    public boolean updateEspecialidadProfesional(Especialidad espe){
        Optional<Especialidad> especialidadOptional = espeRepo.findById(espe.getId_especialidad());
        /* SI EXISTE EL ID DE ESPECIALIDAD A MODIFICAR */
        if(especialidadOptional.isPresent()){
            espeRepo.saveAndFlush(espe);
            return true;
        }else{
            return false;
        }
        
    }
    /*HU 22 obteniendo los procedimientos de una especialidad */
    public String obtenerInfoEspecialidad(String rutProf){
        //revisamos si encontramos info mediante el rut
        return espeRepo.findInfoByRutProf(rutProf);
    }
}
