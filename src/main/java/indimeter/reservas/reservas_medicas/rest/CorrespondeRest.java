package indimeter.reservas.reservas_medicas.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import indimeter.Response.responseHandler;
import indimeter.reservas.reservas_medicas.model.Corresponde;
import indimeter.reservas.reservas_medicas.service.CorrespondeService;

@RestController
@RequestMapping("/corresponde")
public class CorrespondeRest {
    @Autowired
    private CorrespondeService correspondeService;

    @GetMapping("/disponibles")
    public ResponseEntity<Object> getCitasDisponibles(@RequestParam String rut){
        List<Corresponde> correspondeList = correspondeService.citasDisponibles(rut);
        if(!correspondeList.isEmpty()){
            return new ResponseEntity<>(responseHandler.responseCorresponde(correspondeList), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}