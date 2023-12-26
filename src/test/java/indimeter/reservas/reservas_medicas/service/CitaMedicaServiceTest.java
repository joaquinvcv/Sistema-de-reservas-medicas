package indimeter.reservas.reservas_medicas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import indimeter.reservas.reservas_medicas.model.CitaMedica;
import indimeter.reservas.reservas_medicas.model.Corresponde;
import indimeter.reservas.reservas_medicas.model.Especialidad;
import indimeter.reservas.reservas_medicas.model.Horario_laboral;
import indimeter.reservas.reservas_medicas.model.Paciente;
import indimeter.reservas.reservas_medicas.model.Profesional_salud;
import indimeter.reservas.reservas_medicas.repository.CitaMedicaRepo;
import indimeter.reservas.reservas_medicas.repository.CorrespondeRepo;
import indimeter.reservas.reservas_medicas.repository.Profesional_saludRepo;

@ExtendWith(MockitoExtension.class)
public class CitaMedicaServiceTest {
    @Mock
    private CitaMedicaRepo citaMedicaRepo;
    @InjectMocks
    private CitaMedicaService citaMedicaService;
    @Mock
    private Profesional_saludRepo profesionalRepo;
    @InjectMocks
    private Profesional_saludService profesionalService;
    @Mock
    private CorrespondeRepo correRepo;
    @InjectMocks
    private CorrespondeService corrService;
     
    /* TESTING HU_01 - Crear cita medica*/
    @Test
    public void siInvocoElMetodoAddCitaMedicaYLaCitaNoExisteYExisteCupoParaAgregarlaEntoncesLaCrea() throws Exception{
        //Arrange
        CitaMedica citaMedica = creaCitaMedica();
        Optional<CitaMedica> citaMedicaOptional = Optional.of(citaMedica);
        Optional<CitaMedica> citaMedicaOptionalVacio = Optional.empty();
        List<Corresponde> cor = listaDisponibles();
        when(citaMedicaRepo.findById(citaMedica.getId_cita())).thenReturn(citaMedicaOptionalVacio).thenReturn(citaMedicaOptional);
        when(correRepo.findcitasDisponibles(citaMedica.getProfesionalSalud().getRut_profesional())).thenReturn(cor);
        //Act
        boolean resultado = citaMedicaService.agendarCitaMedica(citaMedica);
        //Assert
        assertTrue(resultado);
        verify(citaMedicaRepo, times(1)).saveAndFlush(citaMedica);
    }

    @Test
    public void siInvocoElMetodoAddCitaMedicaYLaCitaNoExisteYNoExisteCupoParaAgregarlaEntoncesNoLaCrea() throws Exception{
        //Arrange
        CitaMedica citaMedica = creaCitaMedica();
        Optional<CitaMedica> citaMedicaOptional = Optional.of(citaMedica);
        Optional<CitaMedica> citaMedicaOptionalVacio = Optional.empty();
        List<Corresponde> cor = new ArrayList<>();
        when(citaMedicaRepo.findById(citaMedica.getId_cita())).thenReturn(citaMedicaOptionalVacio).thenReturn(citaMedicaOptional);
        when(correRepo.findcitasDisponibles(citaMedica.getProfesionalSalud().getRut_profesional())).thenReturn(cor);
        //Act
        boolean resultado = citaMedicaService.agendarCitaMedica(citaMedica);
        //Assert
        assertFalse(resultado);
        verify(citaMedicaRepo, times(0)).saveAndFlush(citaMedica);
    }

    @Test
    public void siInvocoElMetodoAddCitaMedicaYLaCitaSiExisteEntoncesNoLoCrea() throws Exception{
        //Arrange
        CitaMedica citaMedica = creaCitaMedica();
        Optional<CitaMedica> citaMedicaOptional = Optional.of(citaMedica);
        when(citaMedicaRepo.findById(citaMedica.getId_cita())).thenReturn(citaMedicaOptional);
        //Act
        boolean resultado = citaMedicaService.agendarCitaMedica(citaMedica);
        //Assert
        assertFalse(resultado);
        verify(citaMedicaRepo, times(0)).saveAndFlush(citaMedica);
    }
    

    //Testing HU11 - deleteCitaMedicaById()
    @Test
    public void siInvocoDeleteCitaMedicaByIdYSeEliminaYStatusOk() throws Exception{
        //Act
        CitaMedica citaMedica = creaCitaMedica();
        Optional<CitaMedica> citaMedicaOptional = Optional.of(citaMedica); 
        Corresponde cor = horario();
        when(citaMedicaRepo.findById(citaMedica.getId_cita())).thenReturn(citaMedicaOptional);
        when(correRepo.getHorarioActual(citaMedica.getId_cita())).thenReturn(cor);
        //Arrange
        boolean resultado = citaMedicaService.deleteCitaMedicaById(citaMedica.getId_cita());
        //Assert
        verify(citaMedicaRepo, times(1)).deleteById(citaMedica.getId_cita());
        assertTrue(resultado);
    }

    @Test
    public void siInvocoDeleteCitaMedicaByIdYNoExisteIdYStatusNotFound() throws Exception{
       //Act
        CitaMedica citaMedica = new CitaMedica();
        //Arrange
        boolean resultado = citaMedicaService.deleteCitaMedicaById(citaMedica.getId_cita());
        //Assert
        assertFalse(resultado);

    }

    //Testing HU 12
    @Test
    public void siInvocoMisCitasMedicaYexisteProfesionalRetornarListaDeCitas(){
        //Arrange
        String rut = "20.374.974-0"; //usaremos este rut de prueba para corroborar la funcion
        //generamos nuestra lista de prueba
        List<CitaMedica> citas = obtenerCitas();
        /*creamos la situacion, cuando invoquemos la funcion entonces debe recuperar una lista 
         * con las citas
        */
        when(citaMedicaService.misCitaMedicas(rut)).thenReturn(citas);
        List<CitaMedica> resultado;

        //Act

        resultado = citaMedicaService.misCitaMedicas(rut);

        //assert
        //revisamos que la lista no este vacia
        assertFalse(resultado.isEmpty());
        //revisamos que la cantidad de datos en las listas sean iguales
        assertEquals(citas.size(), resultado.size());
        assertEquals(citas.get(0).getId_cita(), resultado.get(0).getId_cita());/*podemos revisar si que tengan las mismas citas */
        assertEquals(citas.get(0).getProfesionalSalud().getRut_profesional(), resultado.get(0).getProfesionalSalud().getRut_profesional());
        /*por ultimo podemos revisar si el rut del profesional es el esperado*/  
    }

    @Test
    void siInvocoMisCitasMedicaYNOexisteProfesionalRetornarListaVacia(){

        String rut = "20.374.974-0";    //usaremos este rut de prueba para corroborar la funcion
        List<CitaMedica> citas = List.of();  //crearemos una lista vacia
        when(citaMedicaService.misCitaMedicas(rut)).thenReturn(citas);/*creamos la situacion, cuando invoquemos la funcion entonces deberia obtener una lista 
        * vacia
        */
        //Act
        List<CitaMedica> resultado = citaMedicaService.misCitaMedicas(rut);//probamos el metodo con los antecedentes

        //Assert
        assertTrue(resultado.isEmpty()); //revisamos si obtenemos la lista esperada

    }
    //End testing hu 12

    //Testting HU 9 -ActualizarProfesional 
    @Test
    public void siInvocoActualizarProfesionalYSiSeActualizaProfesionalEnCitaMedicaRetornarTrue() throws Exception{
        //Arrange
        Profesional_salud profesional_salud = creaProfesional();
        CitaMedica citaMedica = creaCitaMedica();
        Optional<Profesional_salud> profesional_saludOptional = Optional.of(profesional_salud);
        Optional<CitaMedica> citaMedicaOptional = Optional.of(citaMedica);
        Corresponde cor = horario();
        List<Corresponde> horarios = listaDisponibles();
        when(profesionalRepo.findById(profesional_salud.getRut_profesional())).thenReturn(profesional_saludOptional);
        when(citaMedicaRepo.findById(citaMedica.getId_cita())).thenReturn(citaMedicaOptional);
        when(correRepo.getHorarioActual(citaMedica.getId_cita())).thenReturn(cor);
        when(correRepo.findcitasDisponibles(citaMedica.getProfesionalSalud().getRut_profesional())).thenReturn(horarios);
        //Act
        boolean resultado = citaMedicaService.actualizarProfesional(profesional_salud.getRut_profesional(), citaMedica.getId_cita());
        //Assert
        assertEquals(profesional_salud.getRut_profesional(), citaMedica.getProfesionalSalud().getRut_profesional());
        assertTrue(resultado);
    }
    @Test
    public void siInvocoActualizarProfesionalYSiNoSeActualizaProfesionalEnCitaMedicaRetornarFalse() throws Exception{
        //Arrange
        Profesional_salud profesional_salud = creaProfesional();
        CitaMedica citaMedica = creaCitaMedica();
        Optional<Profesional_salud> profesional_saludOptional = Optional.empty();
        Optional<CitaMedica> citaMedicaOptional = Optional.of(citaMedica);
        when(profesionalRepo.findById(profesional_salud.getRut_profesional())).thenReturn(profesional_saludOptional);
        when(citaMedicaRepo.findById(citaMedica.getId_cita())).thenReturn(citaMedicaOptional);
        //Act
        boolean resultado = citaMedicaService.actualizarProfesional(profesional_salud.getRut_profesional(), citaMedica.getId_cita());
        //Assert
        assertFalse(resultado);
        
    }


    //testing HU 15 sprint 3
    @Test
    public void siInvocoObtenerPrecioFinalYExisteIdYNoHayCitaAFacturarEntoncesReturn1(){
        //Arrange 
        Corresponde citaTieneHorario = crearCitaVaciaParaHorario();
        Optional<Corresponde> corresponde = Optional.of(citaTieneHorario);
        float monto = -1;
        when(correRepo.findById(citaTieneHorario.getId())).thenReturn(corresponde);

        //Act 
        float montofinal = citaMedicaService.obtenerPrecioFinal(citaTieneHorario.getId());

        //Assert
        assertEquals(monto, montofinal);
    }
    @Test
    public void siInvocoObtenerPrecioFinalYExisteIdYHayCitaYPacienteTienePrevisionEntoncesReturnPrecioFinal(){
        //Arrange
        Corresponde citaTieneHorario = crearCitaParaHorario();
        Optional<Corresponde> corresponde = Optional.of(citaTieneHorario);
        when(correRepo.findById(citaTieneHorario.getId())).thenReturn(corresponde);
        
        float montoFinal = calcularPrevision(citaTieneHorario.getCita().getCosto(), citaTieneHorario.getCita().getProfesionalSalud().getEspecialidad().getCosto_especialidad());
        
        //Act
            float calculo = citaMedicaService.obtenerPrecioFinal(citaTieneHorario.getId());

        //Assert
        
        assertEquals(montoFinal, calculo);
    }
    //End testing HU 15


    //Testing HU_19 - Entregar comprobante de cita medica
    
    @Test
    public void siInvocoGetCitaComprobanteYExisteEntoncesRetornaCitaYStatusOK(){
        //Arrange
        int id = 1;
        Corresponde corresponde = creaCitaComprobante();
        
        when(correRepo.getHorarioActual(id)).thenReturn(corresponde);
        //Act
        Optional<Corresponde> resultado = Optional.of(citaMedicaService.getCitaComprobante(id));

        //Assert
        assertNotNull(resultado);
        assertEquals(resultado.get(), corresponde);
    }

    @Test
    public void siInvocoGetCitaComprobanteYNoExisteEntoncesRetornaNullYStatusNotFound(){
        //Arrange
        int id = 1;
        when(correRepo.getHorarioActual(id)).thenReturn(null);

        //Act
        Corresponde resultado = citaMedicaService.getCitaComprobante(id);

        //Assert
        assertNull(resultado);
    }
    
    //End Testing HU_19


    //Testing HU_23 - getCitasDeSemana
    @Test
    public void siInvocoGetCitasDeSemanaYExistenEntoncesRetornaListaYStatusOK(){
        //Arrange
        String rutProf = "9.654.321-7";
        LocalDate fecha1 = LocalDate.now();
        LocalDate fecha2 = fecha1.plusWeeks(1);
        List<CitaMedica> citas = creaCitasSemana();
        when(citaMedicaRepo.citasSemana(rutProf, fecha1.toString(), fecha2.toString())).thenReturn(citas);

        //Act
        List<CitaMedica> resultado = citaMedicaService.getCitasDeSemana(rutProf);
        //Assert
        assertNotNull(resultado);
        assertEquals(resultado.size(), citas.size());
        assertEquals(resultado.get(0), citas.get(0));
    }

    @Test
    public void siInvocoGetCitasDeSemanaYNoExistenEntoncesRetornaListaVaciaYStatusNotFound(){
        //Arrange
        String rutProf = "9.654.321-7";
        LocalDate fecha1 = LocalDate.now();
        LocalDate fecha2 = fecha1.plusWeeks(1);
        List<CitaMedica> citas = null;
        when(citaMedicaRepo.citasSemana(rutProf, fecha1.toString(), fecha2.toString())).thenReturn(citas);

        //Act
        List<CitaMedica> resultado = citaMedicaService.getCitasDeSemana(rutProf);
        //Assert
        assertNull(resultado);

    }
    //End testing HU_23


    /* Metodos que proveen datos*/
    private Profesional_salud creaProfesional() {
        Especialidad especialidad = new Especialidad();
        especialidad.setId_especialidad(1);
        especialidad.setNombre_especialidad("Odontologia");
        especialidad.setCosto_especialidad(30000);
        Profesional_salud profesional_salud = new Profesional_salud();
        profesional_salud.setRut_profesional("9.654.321-7");
        profesional_salud.setNombre("Edward");
        profesional_salud.setApellido("kenway");
        profesional_salud.setEmail("pirata@ac4.com");
        profesional_salud.setCargo("Dentista");
        profesional_salud.setEspecialidad(especialidad);
        return profesional_salud;
    }
    private List<CitaMedica> obtenerCitas(){
        List<CitaMedica>misCitas = new ArrayList<CitaMedica>();
        CitaMedica citaMed = new CitaMedica();
        CitaMedica citaMed2 = new CitaMedica();
        citaMed.setId_cita(1);
        Profesional_salud profesional = new Profesional_salud();
        profesional.setRut_profesional("203749740");
        citaMed.setProfesionalSalud(profesional);
        citaMed2.setProfesionalSalud(profesional);

        Paciente paciente = new Paciente();
        paciente.setRut_paciente("172789834");
        paciente.setNombre("Ezio");
        paciente.setApellido("Auditore");
        paciente.setEdad(33);
        paciente.setN_telefono(46132578);
        citaMed.setPaciente(paciente);
        misCitas.add(citaMed);
        
        Paciente paciente2 = new Paciente();
        paciente2.setRut_paciente("164789634");
        paciente2.setNombre("Altair");
        paciente2.setApellido("Ibn-La'Ahad");
        paciente2.setEdad(20);
        paciente2.setN_telefono(98320878);
        citaMed2.setPaciente(paciente2);
        misCitas.add(citaMed2);
       
        
        return misCitas;
    }
    public CitaMedica creaCitaMedica(){
        Profesional_salud profe = new Profesional_salud();
        profe.setRut_profesional("9.654.321-7");
        CitaMedica citaMedica = new CitaMedica();
        citaMedica.setProfesionalSalud(profe);
        citaMedica.setId_cita(1);
        
        return citaMedica;
    }

    public Corresponde horario(){
        Corresponde cor = new Corresponde();
        CitaMedica cita = creaCitaMedica();
        cor.setCita(cita);
        cor.setOcupado(true);
        return cor;
    }

    public List<Corresponde> listaDisponibles(){
        List<Corresponde> cor = new ArrayList<>();
        Corresponde cor1 = new Corresponde();
        cor1.setId(1);
        cor1.setCita(null);
        cor1.setOcupado(false);
        cor.add(cor1);
        return cor;
    }
    
    public float calcularPrevision(float costoCita,float costoBase){
        float calculoPrevision = costoCita + costoBase;
        calculoPrevision -= calculoPrevision * 0.3;
        return calculoPrevision;
    }
    
    public Corresponde crearCitaVaciaParaHorario(){
        Profesional_salud profesional = creaProfesional();
        CitaMedica cita = new CitaMedica();
        cita.setId_cita(9);
        cita.setCosto(14000);
        cita.setProfesionalSalud(profesional);
        Horario_laboral horario = new Horario_laboral();
        horario.setId_horario(12);
        Corresponde citaTieneHorario = new Corresponde();
        citaTieneHorario.setId(10);
        citaTieneHorario.setCita(null);
        citaTieneHorario.setHorario(horario);
        return citaTieneHorario;

    }
    public Corresponde crearCitaParaHorario(){
        Paciente paciente = new Paciente();
        paciente.setRut_paciente("12.345.678-9");
        paciente.setPrevision(true);
        Profesional_salud profesional = creaProfesional();
        profesional.setRut_profesional("9.453.621-8");
        CitaMedica cita = new CitaMedica();
        cita.setId_cita(1);
        cita.setCosto(13000);
        cita.setProfesionalSalud(profesional);
        cita.setPaciente(paciente);
        Horario_laboral horario = new Horario_laboral();
        horario.setId_horario(1);
        Corresponde citaTieneHorario = new Corresponde();
        citaTieneHorario.setId(1);
        citaTieneHorario.setHorario(horario);
        citaTieneHorario.setCita(cita);
        return citaTieneHorario;

    }

    private Corresponde creaCitaComprobante(){
        Profesional_salud profe = new Profesional_salud();
        profe.setRut_profesional("9.654.321-7");
        profe.setNombre("Eduardo");
        profe.setApellido("Cifuentes");
        CitaMedica citaMedica = new CitaMedica();
        citaMedica.setProfesionalSalud(profe);
        citaMedica.setId_cita(1);
        Corresponde cor = new Corresponde();
        cor.setCita(citaMedica);
        cor.setHora_inicio(Time.valueOf(LocalTime.of(10, 30, 0)));
        cor.setHora_termino(Time.valueOf(LocalTime.of(11, 30, 0)));

        return cor;
    }

    private List<CitaMedica> creaCitasSemana(){
        Profesional_salud prof = creaProfesional();
        CitaMedica c1 = new CitaMedica();
        c1.setId_cita(1);
        c1.setFecha_atencion(Date.valueOf(LocalDate.of(2023, 01, 18)));
        c1.setSala_atencion("Sala 3");
        c1.setCosto(13789);
        c1.setProfesionalSalud(prof);
        
        CitaMedica c2 = new CitaMedica();
        c2.setId_cita(2);
        c2.setFecha_atencion(Date.valueOf(LocalDate.of(2023, 01, 22)));
        c2.setSala_atencion("Sala 4");
        c2.setCosto(25643);
        c2.setProfesionalSalud(prof);

        Corresponde cor1 = new Corresponde();
        cor1.setId(1);
        cor1.setCita(c1);
        cor1.setHora_inicio(Time.valueOf(LocalTime.of(9, 30, 0)));
        cor1.setHora_termino(Time.valueOf(LocalTime.of(10, 30, 0)));

        Corresponde cor2 = new Corresponde();
        cor2.setId(2);
        cor2.setCita(c2);
        cor2.setHora_inicio(Time.valueOf(LocalTime.of(10, 30, 0)));
        cor2.setHora_termino(Time.valueOf(LocalTime.of(11, 30, 0)));

        List<CitaMedica> citas = new ArrayList<>();
        citas.add(c1);
        citas.add(c2);

        return citas;
    }

}
