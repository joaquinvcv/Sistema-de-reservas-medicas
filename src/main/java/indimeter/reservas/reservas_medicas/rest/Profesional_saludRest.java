package indimeter.reservas.reservas_medicas.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import indimeter.reservas.reservas_medicas.model.Profesional_salud;
import indimeter.reservas.reservas_medicas.service.Profesional_saludService;
import indimeter.Response.responseHandler;

@RestController
@RequestMapping(value = "/profesionales")
public class Profesional_saludRest {
    @Autowired
    private Profesional_saludService profesionalServ;
    
    // buscar
    @GetMapping(value = "/{rutId}")
    public ResponseEntity<Object> getProfesionalPorRut(@PathVariable String rutId) {
        // Recive un rut, busca entre los profesionales y lo almacena en un Optional
        Optional<Profesional_salud> profesional = profesionalServ.getProfesionalByRut(rutId);
        if (profesional.isPresent()) { // Verifica que el dato concuerde con uno existente
            return new ResponseEntity<>(responseHandler.responseProfesional_salud(profesional.get()), HttpStatus.OK); // retorna una respuesta custom con los datos del profesional
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // El profesional no existe
    }
    
    // Eliminar
    @DeleteMapping(value = "/{rut_profesional}")
    public ResponseEntity<Void> deleteProfesional(@PathVariable String rut_profesional) {
        boolean deletePro = profesionalServ.EliminarPro(rut_profesional);
        if (deletePro) {
            return new ResponseEntity<>(HttpStatus.OK);// El profesional fue eliminado con exito
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);// El profesional no existe o no se pudo eliminar
        }
    }

    // Actualizar
    @PutMapping(value = " ")
    public ResponseEntity<Object> updateProfesional(@RequestBody Profesional_salud profesional_salud) {
        boolean Actprofesional = profesionalServ.ActualizarPro(profesional_salud);
        if (Actprofesional) {
            return new ResponseEntity<>(HttpStatus.OK);// El profesional fue actualizado correctamente
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);// El profesional no existe y no puede ser actualizado
        }
    }

    // Crear
    @PostMapping("")
    public ResponseEntity<Void> addProfesional(@RequestBody Profesional_salud profesional_salud) {

        boolean crearProfe = profesionalServ.CrearPro(profesional_salud);
        if (crearProfe) {
            return new ResponseEntity<>(HttpStatus.OK);// El profesional fue creado con exito
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);// El profesional no pudo ser creado porque ya existe
                                                                // uno con el mismo rut
        }
    }
    
    @GetMapping("")
    public ResponseEntity<List<Profesional_salud>> getProfesionalesDispByEspecialidad(@RequestParam String nomEsp){
        List<Profesional_salud> listaProf = profesionalServ.getProfesionalesDispByEspecialidad(nomEsp);//obtenemos la lista de los profesionales
        if(listaProf.isEmpty()){    /*si ocurrio un error y la lista esta vacia entonces retornamos el codigo de error */
            
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(listaProf,HttpStatus.OK); //sino mostramos el contenido en json y el status sera OK
        }
    }
    
}