package indimeter.reservas.reservas_medicas.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import indimeter.Response.responseHandler;
import indimeter.reservas.reservas_medicas.model.Horario_laboral;
import indimeter.reservas.reservas_medicas.service.HorarioLaboralService;

@RestController
@RequestMapping("/horarios")
public class HorarioLaboralRest {
    @Autowired
    private HorarioLaboralService horarioService;
     
    @GetMapping(value = "/byProfesional")
    public ResponseEntity<Object> getHorarioProfesional(@RequestParam String rut){
        Optional<Horario_laboral> horario = horarioService.getHorarioByRut(rut);
        if(!horario.isEmpty()){
            return new ResponseEntity<>(responseHandler.responseHorario_laboral(horario.get()), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    } 
    
    @PutMapping(value = "")
    public ResponseEntity<Void> updateHorarioLaboral(@RequestBody Horario_laboral horarioLaboral){
        boolean actualizado = horarioService.updateHorarioLaboral(horarioLaboral);
        if(actualizado){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
}
