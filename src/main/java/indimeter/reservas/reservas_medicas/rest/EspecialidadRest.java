package indimeter.reservas.reservas_medicas.rest;

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

import indimeter.reservas.reservas_medicas.model.Especialidad;
import indimeter.reservas.reservas_medicas.service.EspecialidadService;

@RestController
@RequestMapping(value = "/especialidad")
public class EspecialidadRest {
    @Autowired
    private EspecialidadService especialidadServ;

    @PostMapping("")
    public ResponseEntity<Void> crearEspecialidad(@RequestBody Especialidad espe){
        boolean crearEspe = especialidadServ.crearEspecialidad(espe);
        if(crearEspe){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id_especialidad}")
    public ResponseEntity<Void> deleteEspecialidad(@PathVariable int id_especialidad){
        boolean borrado = especialidadServ.borrarEspecialidad(id_especialidad);
        if(borrado){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "")
    public ResponseEntity<Void> updateEspecialidad(@RequestBody Especialidad espe){
        boolean actualizado = especialidadServ.updateEspecialidadProfesional(espe);
        if(actualizado){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value = "/{rutProf}/infoEsp")
    public ResponseEntity<String> getInfoaEsp(@PathVariable String rutProf){
        String info = especialidadServ.obtenerInfoEspecialidad(rutProf);
        if(info != null){
            return new ResponseEntity<>(info,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
