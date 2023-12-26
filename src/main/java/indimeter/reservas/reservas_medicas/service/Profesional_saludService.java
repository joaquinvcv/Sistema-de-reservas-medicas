package indimeter.reservas.reservas_medicas.service;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import indimeter.reservas.reservas_medicas.model.Profesional_salud;
import indimeter.reservas.reservas_medicas.repository.Profesional_saludRepo;

@Service
@Transactional
public class Profesional_saludService {

    @Autowired
    private Profesional_saludRepo profeRepo;
    
    // Metodo de busqueda que utiliza un rut para encontrar los datos asociados
    public Optional<Profesional_salud> getProfesionalByRut(String Rut) {
        return profeRepo.findById(Rut);
    }
     
    // Metodo para actualizar los datos de un profesional
    public boolean ActualizarPro(Profesional_salud profesional_salud) {
        Optional<Profesional_salud> profesionalOptional = profeRepo.findById(profesional_salud.getRut_profesional());
        if (profesionalOptional.isPresent()) { // verifica si existe el profesional en la base de datos
            profeRepo.saveAndFlush(profesional_salud); // actualiza los datos del profesional con los nuevos
            return true;
        } else {
            return false;
        }
    }

    // Metodo para eliminar un profesional
    public boolean EliminarPro(String rut_profesional) {
        Optional<Profesional_salud> profesionalOptional = profeRepo.findById(rut_profesional);
        if (profesionalOptional.isPresent()) { // verifica si el rut existe en la base de datos
            profeRepo.deleteById(rut_profesional);// elimina el profesional de la base de datos
            return true;
        } else {
            return false;
        }
    }

    // Metodo para crear un profesional
    public boolean CrearPro(Profesional_salud profesional_saludnuevo) {
        Optional<Profesional_salud> profesionalOptional = profeRepo
                .findById(profesional_saludnuevo.getRut_profesional());
        if (profesionalOptional.isPresent() == false) { // si el rut no existe en la base de datos
            profeRepo.saveAndFlush(profesional_saludnuevo);// guarda el profesional nuevo
            return true;
        } else {
            return false;
        }
    }
    
    //Metodo para obtener profesionales con cupo disponible dado una especialidad
    public List<Profesional_salud>getProfesionalesDispByEspecialidad(String nombreEsp){
        List<Profesional_salud> listaProfesionales = profeRepo.profesionalesDispByEsp(nombreEsp); //obtenemos la lista de los profesionales
        if(listaProfesionales.isEmpty()){   //si la lista esta vacia la retornamos, sino retornamos la lista con los datos
            return listaProfesionales;
        }else{
            return listaProfesionales;
        }
    }
}