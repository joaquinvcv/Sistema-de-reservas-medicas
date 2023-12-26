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
import org.springframework.web.bind.annotation.RestController;

import indimeter.Response.responseHandler;
import indimeter.reservas.reservas_medicas.model.CitaMedica;
import indimeter.reservas.reservas_medicas.model.Corresponde;
import indimeter.reservas.reservas_medicas.service.CitaMedicaService;
import indimeter.reservas.reservas_medicas.service.CorrespondeService;
import indimeter.reservas.reservas_medicas.service.EmailSenderService;

@RestController
@RequestMapping("/citas-medicas")
public class CitaMedicaRest {
    
    @Autowired
    private CitaMedicaService cMedicaService;
    @Autowired
    private CorrespondeService correService;
    @Autowired
    private EmailSenderService senderService;

    /* AGREGAR UNA CITA MEDICA - HU 01 */
    @PostMapping("")
    public ResponseEntity<Void>addCitaMedica(@RequestBody CitaMedica citaMedica){
        boolean crearCita = cMedicaService.agendarCitaMedica(citaMedica);
        if(crearCita){
            correService.actualizarHorasOcupada(citaMedica);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    
    @GetMapping(value = "/{rutId}")
    public ResponseEntity<List<CitaMedica>> getCitasOrdenadas(@PathVariable String rutId) {
        //primero obtenemos nuestas citas en orden descendente por fecha mediante el rut del profesional
        List<CitaMedica> citas = cMedicaService.misCitaMedicas(rutId);
        if (!citas.isEmpty()) {  //verifica que hay citas proximas 
            return new ResponseEntity<>(citas,HttpStatus.OK);  //existen citas
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); //no existen citas proximas
        }
    }

    //Eliminar cita medica - HU 11
    @DeleteMapping(value ="/{idCita}")
    public ResponseEntity<Void>deleteCitaMedicaById(@PathVariable int idCita){
        boolean eliminado = cMedicaService.deleteCitaMedicaById(idCita);
        if(eliminado){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    //Actualizar profesional en cita medica - HU 9 sprint 2
    @PutMapping(value = "/actualizar/{rutprofesional}"+"/{id}")
    public ResponseEntity<String>ActualizarProfesionalenCita(@PathVariable String rutprofesional, 
    @PathVariable int id){
        boolean actualizado = cMedicaService.actualizarProfesional(rutprofesional, id);
        if(actualizado){   
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //obtener precio final de una cita medica HU 15 sprint 3
    @GetMapping(value = "/{idCita}/Pagar")
    public ResponseEntity<Float>getPrecioFinalCita(@PathVariable int idCita){
        float cita = cMedicaService.obtenerPrecioFinal(idCita);
        if(cita >= 0){
            return new ResponseEntity<>(cita,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Obtener las citas de la semana - HU 23 sprint 3
    @GetMapping(value = "/semanales/{rutProfesional}")
    public ResponseEntity<List<CitaMedica>>getCitasDeSemana(@PathVariable String rutProfesional){
        List<CitaMedica> citas = cMedicaService.getCitasDeSemana(rutProfesional);
        if(!citas.isEmpty()){
            return new ResponseEntity<>(citas,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Entregar comprobante de la cita - HU_19 sprint 3
    @GetMapping(value = "/comprobante/{id}")
    public ResponseEntity<Object> getCitaComprobante(@PathVariable int id){
        Optional<Corresponde> cor = Optional.ofNullable(cMedicaService.getCitaComprobante(id));
        if(!cor.isEmpty()){
            return new ResponseEntity<>(responseHandler.responseComprobante(cor.get()),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping(value = "/mandaCorreo/{id}")
    public ResponseEntity<Object> mandaCorreo(@PathVariable int id){
        Optional<CitaMedica> cita = cMedicaService.getCitaById(id);
        String a;
        if(cita.isPresent()){
            sendMailHoraActualizada(id);
            a = "Mensaje enviado";
            return new ResponseEntity<>(a, HttpStatus.OK);
        }else{
            a = "Mensaje no enviado";
            return new ResponseEntity<>(a, HttpStatus.BAD_REQUEST);
        }
    }

    public void sendMailHoraActualizada(int id){
        String body;
        Optional<CitaMedica> cita1 = cMedicaService.getCitaById(id);
        CitaMedica cita = cita1.get();
        Corresponde cor = correService.getHoraActual(id);
        body = "Se le informa que la cita del dia " + cita.getFecha_atencion() + " se cambió de horario a las " + cor.getHora_inicio() 
            + " con especialista " + cita.getProfesionalSalud().getNombre() + " " + cita.getProfesionalSalud().getApellido() +".";
        senderService.sendEmail(
            "mariosamc8@gmail.com", "Modificaciones en el horario de su cita", body);  
    } 
}

