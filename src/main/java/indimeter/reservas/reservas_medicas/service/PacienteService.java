package indimeter.reservas.reservas_medicas.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import indimeter.reservas.reservas_medicas.model.Paciente;
import indimeter.reservas.reservas_medicas.repository.PacienteRepo;

@Service
@Transactional
public class PacienteService {
    
    @Autowired
    private PacienteRepo repoP;

    public boolean crearPaciente(Paciente pacienteNuevo){
        Optional<Paciente> pacienteOptional = repoP.findById(pacienteNuevo.getRut_paciente());
        if(pacienteOptional.isPresent() == false){
            repoP.saveAndFlush(pacienteNuevo);
            return true;
        }
        return false;
    }

    public List<Paciente> getAllPacientes(){
        return repoP.findAll();
    }
   
    //metodo para obtener un objeto paciente por su rut
    public Optional<Paciente> getPacienteByRut(String rut){
        return repoP.findById(rut);
    }

    /* ACTUALIZAR */
    public boolean update (Paciente paciente){
        Optional<Paciente> pacienteOptional = repoP.findById(paciente.getRut_paciente());
        if(pacienteOptional.isPresent()){
            repoP.saveAndFlush(paciente);
            return true;
        }else{
            return false;
        }
    }

    /* ELIMINAR PACIENTE.
     * EL RUT SE USARÁ COMO STRING, NO COMO ENTERO.
    */
    public boolean deletePacienteById(String rut_paciente){
        Optional<Paciente> pacienteOptional = repoP.findById(rut_paciente);
        if(pacienteOptional.isPresent()){
            repoP.deleteById(rut_paciente);
            return true;
        }else{
            return false;
        }
    }

    //metodo para obtener los pacientes que tienen cita medica con un profesional_salud por su rut
    public List<Paciente> getPacientesByProfesional(String rut){
        return repoP.queryBy(rut);
    }

    //metodo para obtener los pacientes que tienen cita medica con un profesional_salud por su rut
    public List<Paciente> getPacientesBycitaPagada(String rut){
        return repoP.queryByPagado(rut);
    }
    
   
}
