package indimeter.reservas.reservas_medicas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import indimeter.reservas.reservas_medicas.model.CitaMedica;
import indimeter.reservas.reservas_medicas.model.Especialidad;
import indimeter.reservas.reservas_medicas.model.Horario_laboral;
import indimeter.reservas.reservas_medicas.model.Paciente;
import indimeter.reservas.reservas_medicas.model.Profesional_salud;
import indimeter.reservas.reservas_medicas.repository.CitaMedicaRepo;
import indimeter.reservas.reservas_medicas.repository.EspecialidadRepo;
import indimeter.reservas.reservas_medicas.repository.HorarioLaboralRepo;
import indimeter.reservas.reservas_medicas.repository.Profesional_saludRepo;

@ExtendWith(MockitoExtension.class)
public class EspecialidadServiceTest {
    @Mock
    private EspecialidadRepo espeRepo;
    @InjectMocks
    private EspecialidadService espeService;

    @Mock
    private Profesional_saludRepo profeRepo;
    @InjectMocks
    private Profesional_saludService profeService;

    @Mock
    private CitaMedicaRepo citaRepo;
    @InjectMocks
    private CitaMedicaService citaService;

    @Mock
    private HorarioLaboralRepo horaRepo;
    @InjectMocks
    private HorarioLaboralService horaService;

    @Test
    public void siInvocoElMetodocrearEspecialidadYLaEspecialidadNoExisteEntoncesLaCrea() throws Exception{
        //Arrange
        Especialidad espe = crearEspecialidad();
        Optional<Especialidad> especialidadOptional = Optional.of(espe);
        Optional<Especialidad> especialidadVacia = Optional.empty();
        when(espeRepo.findById(espe.getId_especialidad())).thenReturn(especialidadVacia).thenReturn(especialidadOptional);
        //Act
        boolean resultado = espeService.crearEspecialidad(espe);
        //Assert
        assertTrue(resultado);
        verify(espeRepo).saveAndFlush(espe);
    }

    @Test
    public void siInvocoElMetodocrearEspecialidadYLaEspecialidadExisteEntoncesNoLaCrea() throws Exception{
        //Arrange
        Especialidad espe = crearEspecialidad();
        Optional<Especialidad> espeOptional = Optional.of(espe);
        when(espeRepo.findById(espe.getId_especialidad())).thenReturn(espeOptional);
        //Act
        boolean resultado = espeService.crearEspecialidad(espe);
        //Assert
        assertFalse(resultado);
        verify(espeRepo, times(0)).saveAndFlush(espe);
    }

    @Test
    public void siInvocoElMetodoborrarEspecialidadYLaEspecialidadExisteYNoTieneProfesionalesAsociadosSeElimina() throws Exception{
        //Arrange
        Especialidad espe = crearEspecialidad();
        Optional<Especialidad> espeOptional = Optional.of(espe);
        List<Profesional_salud> profes = new ArrayList<>();
        when(espeRepo.findById(espe.getId_especialidad())).thenReturn(espeOptional);
        when(profeRepo.profesionalesDispByEsp(espe.getNombre_especialidad())).thenReturn(profes);
        //Act
        boolean resultado = espeService.borrarEspecialidad(espe.getId_especialidad());
        //Assert
        assertTrue(resultado);
        verify(espeRepo).deleteById(espe.getId_especialidad());
    }

    @Test
    public void siInvocoElMetodoborrarEspecialidadYLaEspecialidadNoExisteEntoncesNoLaBorra() throws Exception{
        //Arrange
        Especialidad espe = crearEspecialidad();
        Optional<Especialidad> espeOptional = Optional.empty();
        when(espeRepo.findById(espe.getId_especialidad())).thenReturn(espeOptional);
        //Act
        boolean resultado = espeService.borrarEspecialidad(espe.getId_especialidad());
        //Assert
        assertFalse(resultado);
        verify(espeRepo, times(0)).deleteById(espe.getId_especialidad());
    }

    @Test
    public void siInvocoElMetodoborrarEspecialidadYLaEspecialidadExisteYTieneProfesionalesAsociadosYEstosProfesionalesNoTienenCitasAsociadas() throws Exception{
        //Arrange
        Especialidad espe = crearEspecialidad();
        Horario_laboral hora = crearHorario();
        Optional<Especialidad> espeOptional = Optional.of(espe);
        Optional<Horario_laboral> horarioOptional = Optional.of(hora);
        List<Profesional_salud> profes = crearProfesionales();
        List<CitaMedica> citas = new ArrayList<>();
        when(espeRepo.findById(espe.getId_especialidad())).thenReturn(espeOptional);
        when(profeRepo.profesionalesDispByEsp(espe.getNombre_especialidad())).thenReturn(profes);
        when(horaRepo.getbyrut(profes.get(0).getRut_profesional())).thenReturn(horarioOptional);
        when(citaRepo.citasByProf(profes.get(0).getRut_profesional())).thenReturn(citas);
        //Act
        boolean resultado = espeService.borrarEspecialidad(espe.getId_especialidad());
        //Assert
        assertTrue(resultado);
        verify(espeRepo, times(1)).deleteById(espe.getId_especialidad());
        verify(profeRepo, times(1)).deleteById(profes.get(0).getRut_profesional());
        verify(horaRepo, times(1)).deleteById(hora.getId_horario());
        verify(citaRepo, times(0)).deleteById(null);
    }

    @Test
    public void siInvocoElMetodoborrarEspecialidadYLaEspecialidadExisteYTienesProfesionalesAsociadosYEstosProfesionalesSiTienenCitasAsociadas() throws Exception{
        //Arrange
        Especialidad espe = crearEspecialidad();
        Horario_laboral hora = crearHorario();
        Optional<Especialidad> espeOptional = Optional.of(espe);
        Optional<Horario_laboral> horarioOptional = Optional.of(hora);
        List<Profesional_salud> profes = crearProfesionales();
        List<CitaMedica> citas = crearCitas();
        when(espeRepo.findById(espe.getId_especialidad())).thenReturn(espeOptional);
        when(profeRepo.profesionalesDispByEsp(espe.getNombre_especialidad())).thenReturn(profes);
        when(horaRepo.getbyrut(profes.get(0).getRut_profesional())).thenReturn(horarioOptional);
        when(citaRepo.citasByProf(profes.get(0).getRut_profesional())).thenReturn(citas);
        //Act
        boolean resultado = espeService.borrarEspecialidad(espe.getId_especialidad());
        //Assert
        assertTrue(resultado);
        verify(espeRepo, times(1)).deleteById(espe.getId_especialidad());
        verify(profeRepo, times(1)).deleteById(profes.get(0).getRut_profesional());
        verify(horaRepo, times(1)).deleteById(hora.getId_horario());
        verify(citaRepo, times(1)).deleteById(citas.get(0).getId_cita());
    }

    /* HU_20 TESTS */
    @Test
    public void siInvocoUpdateEspecialidadProfesionalYExisteEntoncesActualizar(){
        //ARRANGE
        Especialidad especialidad1 = crearEspecialidad();
        Optional<Especialidad> especialidadOptional = Optional.of(especialidad1);
        Especialidad especialidad2 = crearEspecialidad();
        especialidad2.setClave_especialidad(1);
        Optional<Especialidad> especialidadActualizaOptional = Optional.of(especialidad2);
        when(espeRepo.findById(especialidad1.getId_especialidad())).thenReturn(especialidadOptional).thenReturn(especialidadActualizaOptional);
        //ACT
        boolean resultado = espeService.updateEspecialidadProfesional(especialidad1);
        Optional<Especialidad> especialidadOptional2 = espeRepo.findById(especialidad1.getId_especialidad());
        //ASSERT
        assertTrue(resultado);
        assertEquals(especialidad2.getClave_especialidad(), especialidadOptional2.get().getClave_especialidad());
        verify(espeRepo, times(1)).saveAndFlush(especialidad1);
    }

    @Test
    public void siInvocoUpdateEspecialidadProfesionalYNoExisteEntoncesNoActualizar(){
        //ARRANGE
        Optional<Especialidad> especialidadOptional = Optional.empty();
        Especialidad especialidad1 = crearEspecialidad();
        Optional<Especialidad> especialidadActualizaOptional = Optional.of(especialidad1);
        when(espeRepo.findById(especialidad1.getId_especialidad())).thenReturn(especialidadOptional).thenReturn(especialidadActualizaOptional);
        //ACT
        boolean resultado = espeService.updateEspecialidadProfesional(especialidad1);
        //ASSERT
        assertFalse(resultado);
        verify(espeRepo,times(0)).saveAndFlush(especialidad1);
        
    }
    /* END HU_20 TESTS */
    /*TESTING HU 22 */
    @Test
    public void siInvocoObtenerInfoEspecialidadYExisteProfesionalEntoncesRetornarDatos(){
        //Arrange
        Profesional_salud prof = new Profesional_salud();
        Especialidad esp = crearEspecialidad();
        prof.setRut_profesional("20.374.974-0");
        prof.setEspecialidad(esp);
        Optional<String> info = Optional.of(esp.getInfo_especialidad());
        when(espeRepo.findInfoByRutProf(prof.getRut_profesional())).thenReturn(info.get());

        //act
        String resultado = espeService.obtenerInfoEspecialidad(prof.getRut_profesional());

        //assert
        assertNotNull(resultado);
        assertEquals(esp.getInfo_especialidad(), resultado);

    }
    @Test
    public void siInvocoObtenerInfoEspecialidadYNoExisteProfesionalEntoncesRetornarDatosVacio(){
        //Arrange
        String rut = "12.869.413-4";
        when(espeRepo.findInfoByRutProf(rut)).thenReturn(null);
        //Act 
        String resultado = espeService.obtenerInfoEspecialidad(rut);

        //Assert
        assertNull(resultado);
    }




    private Especialidad crearEspecialidad(){
        Especialidad espe = new Especialidad();
        espe.setId_especialidad(10);
        espe.setClave_especialidad(1506);
        espe.setCosto_especialidad(65000);
        espe.setNombre_especialidad("Quiropractico");
        espe.setMisProfesionales(null);
        espe.setInfo_especialidad("revision de columna vertebral etc");
        return espe;
    }

    private List<Profesional_salud> crearProfesionales(){
        List<Profesional_salud> profes = new ArrayList<>();
        Profesional_salud profe = new Profesional_salud();
        profe.setRut_profesional("20.457.697-1");
        profe.setNombre("Eduardo");
        profe.setApellido("Cifuentes");
        profe.setCargo("Urologo");
        profes.add(profe);
        return profes;
    }

    private Horario_laboral crearHorario(){
        Horario_laboral hora = new Horario_laboral();
        hora.setId_horario(1);
        hora.setCupos_diarios(6);
        return hora;
    }

    private List<CitaMedica> crearCitas(){
        List<CitaMedica> citas = new ArrayList<>();
        CitaMedica cita = new CitaMedica();
        Paciente paciente = new Paciente();
        Profesional_salud profe = new Profesional_salud();
        profe.setRut_profesional("20.457.697-1");
        profe.setNombre("Eduardo");
        profe.setApellido("Cifuentes");
        profe.setCargo("Urologo");
        paciente.setRut_paciente("15.365.956-4");
        paciente.setNombre("Matias");
        paciente.setApellido("Canales");

        cita.setId_cita(12);
        cita.setPaciente(paciente);
        cita.setProfesionalSalud(profe);
        cita.setCosto(1245);
        cita.setSala_atencion("sala 12");

        citas.add(cita);
        return citas;
    }
}
