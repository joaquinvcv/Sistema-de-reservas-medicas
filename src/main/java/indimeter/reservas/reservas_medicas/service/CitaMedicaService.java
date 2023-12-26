package indimeter.reservas.reservas_medicas.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indimeter.reservas.reservas_medicas.model.CitaMedica;
import indimeter.reservas.reservas_medicas.model.Corresponde;
import indimeter.reservas.reservas_medicas.model.Profesional_salud;
import indimeter.reservas.reservas_medicas.repository.CitaMedicaRepo;
import indimeter.reservas.reservas_medicas.repository.CorrespondeRepo;
import indimeter.reservas.reservas_medicas.repository.Profesional_saludRepo;

@Service
@Transactional
public class CitaMedicaService {
    @Autowired
    private CitaMedicaRepo cMedicaRepo;
    @Autowired
    private Profesional_saludRepo profeRepo;
    @Autowired
    private CorrespondeRepo corRepo;

    //metodo para obtener la lista de citas ordenadas por fecha dado un rut de profesional;
    public List<CitaMedica>misCitaMedicas(String rut){
        return cMedicaRepo.citasByProf(rut);
    }

    //Agendar una cita medica - HU 01.
    public boolean agendarCitaMedica(CitaMedica citaNueva){
        Optional<CitaMedica> citaOptional = cMedicaRepo.findById(citaNueva.getId_cita());
        if(!citaOptional.isPresent()){
            String rut = citaNueva.getProfesionalSalud().getRut_profesional();
            List<Corresponde> cor = corRepo.findcitasDisponibles(rut);
            if(!cor.isEmpty()){
                cMedicaRepo.saveAndFlush(citaNueva);
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    
    //Eliminar cita medica - HU 11
    public boolean deleteCitaMedicaById(int id_cita){
        Optional<CitaMedica> citaOptional = cMedicaRepo.findById(id_cita);
        Corresponde cor = corRepo.getHorarioActual(id_cita);
        if(citaOptional.isPresent()){
            cor.setCita(null);
            cor.setOcupado(false);
            corRepo.saveAndFlush(cor);
            cMedicaRepo.deleteById(id_cita);
            return true;
        }else{
            return false;
        }
    }

    //Actualizar profesional de una cita medica - HU 09 sprint 2
    public boolean actualizarProfesional(String rut, int id){
        Optional<Profesional_salud> profesionalOptional = profeRepo.findById(rut);
        Optional<CitaMedica> citaOptional = cMedicaRepo.findById(id);
        if(profesionalOptional.isPresent() && citaOptional.isPresent()){
            Corresponde cor2 = corRepo.getHorarioActual(id);
            List<Corresponde> cor = corRepo.findcitasDisponibles(rut);
            Corresponde cor3 = cor.get(0);
            if(!cor.isEmpty()){
                cor2.setCita(null);
                cor2.setOcupado(false);
                corRepo.saveAndFlush(cor2);
                cor3.setCita(citaOptional.get());
                cor3.setOcupado(true);
                corRepo.saveAndFlush(cor3);
                citaOptional.get().setProfesionalSalud(profesionalOptional.get());
                cMedicaRepo.saveAndFlush(citaOptional.get());
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    //obtener el precio total de una cita medica - HU 15 sprint 3
    public float obtenerPrecioFinal(int idCita){
        //verificamos que el ide exista
        Optional<Corresponde> cOptional = corRepo.findById(idCita);
        float monto = -1;   //definimos nuestros monto invalido
        if(cOptional.isPresent()){
            //verificamos si hay una cita asociada al la hora
            if(cOptional.get().getCita() != null){
                /*si es asi obtenemos el coste de la especialidad del profesional 
                y lo sumamos con el coste de la cita*/
                float montoBase = cOptional.get().getCita().getProfesionalSalud().getEspecialidad().getCosto_especialidad();
                monto = montoBase + cOptional.get().getCita().getCosto();
                if(cOptional.get().getCita().getPaciente().isPrevision()){
                    //si el paciente tiene prevision se le descuenta el 30% al total
                    
                    monto -= monto*0.30;
                   
                }
                return monto;
            }
        }
        return monto;
    }
    //Obtener las citas de la semana - HU 23 sprint 3
    public List<CitaMedica> getCitasDeSemana(String rutProfesional){
        LocalDate fecha1 = LocalDate.now();
        LocalDate fecha2 = fecha1.plusWeeks(1);

        List<CitaMedica>citasSemana = cMedicaRepo.citasSemana(rutProfesional,fecha1.toString(),fecha2.toString());
        return citasSemana;
    }

    //Entregar comprobante de la cita - HU_19 sprint 3
    public Corresponde getCitaComprobante(int id){
        
        return corRepo.getHorarioActual(id);
    }
    
    public Optional<CitaMedica> getCitaById(int id){
        Optional<CitaMedica> cita = cMedicaRepo.findById(id);
        return cita;
    }
}