package indimeter.reservas.reservas_medicas.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import indimeter.reservas.reservas_medicas.repository.CorrespondeRepo;
import indimeter.reservas.reservas_medicas.model.CitaMedica;
import indimeter.reservas.reservas_medicas.model.Corresponde;

@Service
@Transactional
public class CorrespondeService {
    @Autowired
    private CorrespondeRepo corRepo;
    
    public List<Corresponde> citasDisponibles(String rut){
        return corRepo.findcitasDisponibles(rut);
    }

    public Corresponde actualizarHorasOcupada(CitaMedica cita){
        List<Corresponde> cor = citasDisponibles(cita.getProfesionalSalud().getRut_profesional());
        Corresponde cor2 = cor.get(0);
        cor2.setCita(cita);
        cor2.setOcupado(true);
        return corRepo.saveAndFlush(cor2);
    }

    public Corresponde getHoraActual(int id){
        return corRepo.getHorarioActual(id);
    }
}
