package indimeter.reservas.reservas_medicas.rest;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import indimeter.reservas.reservas_medicas.model.Paciente;
import indimeter.reservas.reservas_medicas.service.PacienteService;

@RestController
@RequestMapping(value = "/pacientes" , produces = "application/json")
public class PacienteRest {
    
    @Autowired
    private PacienteService pacienteServ;

    @GetMapping(value = "/{rutId}")
    public ResponseEntity<Paciente> getPacientesPorRut(@PathVariable String rutId) {
        //devolveremos un paciente segun si existe el cliente con el rut 
        Optional<Paciente> paciente = pacienteServ.getPacienteByRut(rutId);
        if (paciente.isPresent()) {  //indicamos si el paciente esta registrado
            return new ResponseEntity<>(paciente.get(),HttpStatus.OK);  //peticion correcta, indica que el paciente existe
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); //paciente no encontrado
        }
    }
     
    @PostMapping("")
    public ResponseEntity<Void>addPaciente(@RequestBody Paciente paciente){
            boolean crearDepto = pacienteServ.crearPaciente(paciente); //crearemos un nuevo paciente y con un booleano nos dira si se creo con exito
            if(crearDepto){
                return new ResponseEntity<>(HttpStatus.CREATED); // fue creado exitosamente
            }else{
                //no es posible crear nuevo paciente
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
    }
    
    @GetMapping(value = "/misPacientes")
    public ResponseEntity<List<Paciente>> getAllPacientes() {
        List<Paciente> pacientesList = pacienteServ.getAllPacientes();
        if (!pacientesList.isEmpty()) {
            return new ResponseEntity<>(pacientesList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*
     * AGREGAR LAS FUNCIONES RESTANTES DE PACIENTE SERVICE AQUI - MIGUEL QUINTANA
     * UPDATE.
     * DELETE.
    */
    
    @PutMapping(value = "")
    public ResponseEntity<Void> updatePaciente(@RequestBody Paciente paciente){
        boolean actualizado = pacienteServ.update(paciente);
        if(actualizado){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value ="/{rut_paciente}")
    public ResponseEntity<Void>deletePacienteById(@PathVariable String rut_paciente){
        boolean eliminado = pacienteServ.deletePacienteById(rut_paciente);
        if(eliminado){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/byProfesional")
    public ResponseEntity<List<Paciente>> getPacientesPorProfesional(@RequestParam String rutId) {
        List<Paciente> paciente = pacienteServ.getPacientesByProfesional(rutId);
        if (!paciente.isEmpty()) {  //verifica que los pacientes existan
            return new ResponseEntity<>(paciente,HttpStatus.OK);  //existen pacientes
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); //no existen pacientes o error
        }
    }
    //metodo para obtener pacientes que tienen una cita medica pagada con un profesional_salud por su rut
    @GetMapping(value = "/bycitaPagada")
    public ResponseEntity<List<Paciente>> getPacientesPorCitaPagada(@RequestParam String rutPro) {
        List<Paciente> paciente = pacienteServ.getPacientesBycitaPagada(rutPro);
        if (!paciente.isEmpty()) {  
            return new ResponseEntity<>(paciente,HttpStatus.OK);  
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
    }
}
