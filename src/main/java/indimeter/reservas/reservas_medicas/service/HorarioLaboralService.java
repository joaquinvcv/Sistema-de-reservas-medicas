package indimeter.reservas.reservas_medicas.service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;



import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indimeter.reservas.reservas_medicas.model.CitaMedica;
import indimeter.reservas.reservas_medicas.model.Corresponde;
import indimeter.reservas.reservas_medicas.model.Horario_laboral;
import indimeter.reservas.reservas_medicas.repository.CitaMedicaRepo;
import indimeter.reservas.reservas_medicas.repository.CorrespondeRepo;
import indimeter.reservas.reservas_medicas.repository.HorarioLaboralRepo;

@Service
@Transactional
public class HorarioLaboralService {
    @Autowired
    private HorarioLaboralRepo  horarioRepo;
    @Autowired
    private CitaMedicaRepo citaMedicaRepo;
    @Autowired 
    private CorrespondeRepo cRepo;
     
    //Metodo para retornar los horarios de un profesional usando su id de horario
    public Optional<Horario_laboral> getHorarioByRut(String rut){
        return horarioRepo.getbyrut(rut);
    }

    //Actualizar horario laboral - HU10
    public boolean updateHorarioLaboral(Horario_laboral horario_laboral){
        Optional<Horario_laboral> horarioOptional = horarioRepo.findById(horario_laboral.getId_horario());
        if(horarioOptional.isPresent()){ //verifica que el horario exista previamente
            if(horarioOptional.get().getCupos_diarios() > horario_laboral.getCupos_diarios()){
                //si los cupos nuevos son menores que los anteriores
                List<Corresponde> horarios = horarioOptional.get().getHorarios();
                //verifica que la cantidad de cupos sea menor que los ya usados en el horario anterior
                if(horarios.size() > horario_laboral.getCupos_diarios()){

                    int diferencia = horarios.size()-horario_laboral.getCupos_diarios();
                    Optional<CitaMedica> citaOptional;
                    for(int j = 1; j <= diferencia; j++){
                        //verifica que haya una cita asociada
                        citaOptional = Optional.ofNullable(horarios.get(horarios.size()-j).getCita());
                        //si existe cita asociada, la elimina tambien
                        if(citaOptional.isPresent()){
                            int idCita = horarios.get(horarios.size()-j).getCita().getId_cita();
                            citaMedicaRepo.deleteById(idCita);
                        }

                        cRepo.deleteById(horarios.get(horarios.size()-j).getId());
                    }
                }
            }else{
                //si los cupos nuevos son mayores
                int horasExtra = horario_laboral.getCupos_diarios()-horarioOptional.get().getCupos_diarios();
                //un cupo equivale a una hora, se calcula la diferencia
                
                if(horasExtra > 0){    
                    //si la diferencia es mayor que 0 entonces debe crear tuplas para la tabla relacion Corresponde para posible nuevas citas
                    List<Corresponde> horarios = horarioOptional.get().getHorarios();
                    LocalTime ultimaHoraInicio = (horarios.get(horarios.size()-1).getHora_inicio()).toLocalTime();
                    LocalTime ultimaHoraTermino = horarios.get(horarios.size()-1).getHora_termino().toLocalTime();
                    LocalTime horaLimite = LocalTime.of(20, 31, 00);

                    //si la ultima hora de termino mas la diferencia no se pasan de las 20:30(hora limite) entonces agrega nuevas tuplas
                    if(ultimaHoraTermino.plusHours(horasExtra).isBefore(horaLimite)){
                        int nuevoId = cRepo.horariosTotales(); // para asignar el id correspondiente a las nuevas tuplas
                        for(int j = 1; j <= horasExtra; j++){
                            Corresponde nuevoCorr = new Corresponde();
                            nuevoCorr.setId(nuevoId+j);
                            nuevoCorr.setHorario(horario_laboral);
                            nuevoCorr.setOcupado(false);
                            nuevoCorr.setHora_inicio(Time.valueOf(ultimaHoraInicio.plusHours(j)));
                            nuevoCorr.setHora_termino(Time.valueOf(ultimaHoraTermino.plusHours(j)));
                            cRepo.saveAndFlush(nuevoCorr);
                        }
                    }else{
                        return false;
                    }
                }
            }
            horarioRepo.saveAndFlush(horario_laboral);
            return true;

        }else{
            return false;
        }

    }

}