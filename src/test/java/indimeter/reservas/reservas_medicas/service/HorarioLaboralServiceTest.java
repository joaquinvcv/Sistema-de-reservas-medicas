package indimeter.reservas.reservas_medicas.service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import indimeter.reservas.reservas_medicas.model.CitaMedica;
import indimeter.reservas.reservas_medicas.model.Corresponde;
import indimeter.reservas.reservas_medicas.model.Horario_laboral;
import indimeter.reservas.reservas_medicas.model.Profesional_salud;
import indimeter.reservas.reservas_medicas.repository.CitaMedicaRepo;
import indimeter.reservas.reservas_medicas.repository.CorrespondeRepo;
import indimeter.reservas.reservas_medicas.repository.HorarioLaboralRepo;

@ExtendWith(MockitoExtension.class)
public class HorarioLaboralServiceTest {
    @Mock
    private HorarioLaboralRepo horarioLaboralRepo;
    @InjectMocks
    private HorarioLaboralService horarioLaboralService;
    @Mock
    private CitaMedicaRepo citaRepo;
    @InjectMocks
    private CitaMedicaService citaService;
    @Mock
    private CorrespondeRepo correRepo;
    @InjectMocks
    private CorrespondeService correService;
     
    // Test HU 07 - getHorarioById()
    @Test
    public void siInvocoGetHorarioByIdYExisteUnProfesionalVinculadoAEseIdConSuRutDevuelveLosDatosDeHorario(){
        //Arrange
        Horario_laboral horario = creaHorario_laboral();
        Optional<Horario_laboral> horarioOptional = Optional.of(horario);
        given(horarioLaboralService.getHorarioByRut(horario.getProfesional().getRut_profesional())).willReturn(horarioOptional);
        //Act
        Optional<Horario_laboral> resultado = horarioLaboralService.getHorarioByRut(horario.getProfesional().getRut_profesional());
        //Assert
        assertTrue(resultado.isPresent());
        assertEquals(horario.getId_horario(), resultado.get().getId_horario());
    }

    @Test
    public void siInvocoGetHorarioByIdYNoExisteUnProfesionalVinculadoAEseNoRetornaDatos(){
        //Arrange
        Optional<Horario_laboral> horarioOptional = Optional.empty();
        given(horarioLaboralService.getHorarioByRut("20.501.452-7")).willReturn(horarioOptional);
        //Act
        Optional<Horario_laboral> resultado = horarioLaboralService.getHorarioByRut("20.501.452-7");
        //Assert
        assertTrue(resultado.isEmpty());
    }

    //Test HU 10 - updateHorarioLaboral()
    @Test
    public void siInvocoUpdateHorarioLaboralYExisteYElHorarioAntiguoTieneMasCuposQueElNuevoYAlgunaCitaAsociadaEntoncesModificaSusDatosAnteriores(){
        //Arrange
        Horario_laboral hora1 = getHorario_laboral();
        Horario_laboral hora = creaHorario_laboral();
        Optional<Horario_laboral> horaOptional = Optional.of(hora);
        when(horarioLaboralRepo.findById(hora.getId_horario())).thenReturn(horaOptional);
        //Act
        boolean resultado = horarioLaboralService.updateHorarioLaboral(hora1);
        //Assert
        assertTrue(resultado);
        verify(citaRepo, times(1)).deleteById(hora.getHorarios().get(1).getCita().getId_cita());
        verify(correRepo, times(1)).deleteById(hora.getHorarios().get(1).getId());
        verify(correRepo, times(0)).deleteById(hora.getHorarios().get(0).getId());
        verify(horarioLaboralRepo, times(1)).saveAndFlush(hora1);
    }

    @Test
    public void siInvocoUpdateHorarioLaboralYExisteYElHorarioAntiguoTieneMasCuposQueElNuevoPeroNoTieneCitasAsociadasEntoncesModificaSusDatosAnteriores(){
        //Arrange
        Horario_laboral hora1 = getHorario_laboral();
        Horario_laboral hora = creaHorario_laboralVacio();
        Optional<Horario_laboral> horaOptional = Optional.of(hora);
        when(horarioLaboralRepo.findById(hora.getId_horario())).thenReturn(horaOptional);
        //Act
        boolean resultado = horarioLaboralService.updateHorarioLaboral(hora1);
        //Assert
        assertTrue(resultado);
        verify(correRepo, times(0)).deleteById(hora.getHorarios().get(0).getId());
        verify(correRepo, times(1)).deleteById(hora.getHorarios().get(1).getId());
        verify(horarioLaboralRepo, times(1)).saveAndFlush(hora1);
    }

    @Test
    public void siInvocoUpdateHorarioLaboralYExisteYElHorarioAntiguoTieneMenosCuposQueElNuevoEntoncesModificaSusDatosAnteriores(){
        //Arrange
        Horario_laboral hora1 = getHorarioMayor();
        Horario_laboral hora = creaHorario_laboralVacio();
        Optional<Horario_laboral> horaOptional = Optional.of(hora);
        when(horarioLaboralRepo.findById(hora.getId_horario())).thenReturn(horaOptional);
        //Act
        boolean resultado = horarioLaboralService.updateHorarioLaboral(hora1);
        //Assert
        assertTrue(resultado);
        verify(horarioLaboralRepo, times(1)).saveAndFlush(hora1);
    }

    @Test
    public void siInvocoUpdateHorarioLaboralYNoExisteEntoncesNoActualiza(){
        //Arrange
        Optional<Horario_laboral> horarioOptional = Optional.empty();
        Horario_laboral horario1 = creaHorario_laboral();
        Optional<Horario_laboral> horarioActualizaOptional = Optional.of(horario1);
        when(horarioLaboralRepo.findById(horario1.getId_horario())).thenReturn(horarioOptional).thenReturn(horarioActualizaOptional);
        //Act
        boolean resultado = horarioLaboralService.updateHorarioLaboral(horario1);
        //Assert
        assertFalse(resultado);
        verify(horarioLaboralRepo,times(0)).saveAndFlush(horario1);

    }
    //End Test HU 10 - updateHorarioLaboral()

    private Horario_laboral getHorario_laboral(){
        Horario_laboral horario = new Horario_laboral();
        horario.setId_horario(4);
        horario.setCupos_diarios(1);
        Profesional_salud prof = new Profesional_salud();
        prof.setRut_profesional("16.457.892-9");
        horario.setProfesional(prof);
        return horario;
    }

    private Horario_laboral getHorarioMayor(){
        Horario_laboral horario = new Horario_laboral();
        horario.setId_horario(4);
        horario.setCupos_diarios(3);
        Profesional_salud prof = new Profesional_salud();
        prof.setRut_profesional("16.457.892-9");
        horario.setProfesional(prof);
        return horario;
    }

    private List<Corresponde> crearListVacia(){
        List<Corresponde> cor = new ArrayList<>();
        Corresponde cor1 = new Corresponde();
        cor1.setId(3);
        cor1.setCita(null);
        cor1.setHora_inicio(Time.valueOf(LocalTime.of(14, 30, 0)));
        cor1.setHora_termino(Time.valueOf(LocalTime.of(15, 30, 0)));
        cor1.setOcupado(false);

        Corresponde cor2 = new Corresponde();
        cor2.setId(4);
        cor2.setCita(null);
        cor2.setHora_inicio(Time.valueOf(LocalTime.of(15, 30, 0)));
        cor2.setHora_termino(Time.valueOf(LocalTime.of(16, 30, 0)));
        cor2.setOcupado(false);
        cor.add(cor1);
        cor.add(cor2);
        return cor;
    }
    
    private List<Corresponde> crearListCorre(){
        List<Corresponde> corre = new ArrayList<>();
        Corresponde cor1 = new Corresponde();
        cor1.setId(3);
        cor1.setCita(null);
        cor1.setHora_inicio(Time.valueOf(LocalTime.of(14, 30, 0)));
        cor1.setHora_termino(Time.valueOf(LocalTime.of(15, 30, 0)));
        cor1.setOcupado(false);
        Corresponde cor = new Corresponde();
        cor.setCita(crearCita());
        cor.setHora_inicio(Time.valueOf(LocalTime.of(13, 30, 0)));
        cor.setHora_termino(Time.valueOf(LocalTime.of(14, 30, 0)));
        cor.setId(2);
        cor.setOcupado(false);

        corre.add(cor1);
        corre.add(cor);
        return corre;
    }

    private CitaMedica crearCita(){
        CitaMedica cita = new CitaMedica();
        cita.setId_cita(1);
        Profesional_salud prof = new Profesional_salud();
        prof.setRut_profesional("16.457.892-9");
        cita.setProfesionalSalud(prof);
        return cita;
    }

    private Horario_laboral creaHorario_laboral(){
        Horario_laboral horario = new Horario_laboral();
        Profesional_salud prof = new Profesional_salud();
        prof.setRut_profesional("16.457.892-9");
        horario.setId_horario(4);
        horario.setProfesional(prof);
        horario.setCupos_diarios(2);
        horario.setHorarios(crearListCorre());
        return horario;
    }

    private Horario_laboral creaHorario_laboralVacio(){
        Horario_laboral horario = new Horario_laboral();
        Profesional_salud prof = new Profesional_salud();
        prof.setRut_profesional("16.457.892-9");
        horario.setId_horario(4);
        horario.setProfesional(prof);
        horario.setCupos_diarios(2);
        horario.setHorarios(crearListVacia());
        return horario;
    }
    
}

