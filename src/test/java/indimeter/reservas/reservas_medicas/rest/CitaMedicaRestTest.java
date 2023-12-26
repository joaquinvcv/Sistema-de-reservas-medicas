package indimeter.reservas.reservas_medicas.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.BDDMockito.given;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import indimeter.reservas.reservas_medicas.model.CitaMedica;
import indimeter.reservas.reservas_medicas.model.Corresponde;
import indimeter.reservas.reservas_medicas.model.Especialidad;
import indimeter.reservas.reservas_medicas.model.Horario_laboral;
import indimeter.reservas.reservas_medicas.model.Paciente;
import indimeter.reservas.reservas_medicas.model.Profesional_salud;
import indimeter.reservas.reservas_medicas.repository.CitaMedicaRepo;
import indimeter.reservas.reservas_medicas.repository.CorrespondeRepo;
import indimeter.reservas.reservas_medicas.service.CitaMedicaService;
import indimeter.reservas.reservas_medicas.service.CorrespondeService;
import indimeter.reservas.reservas_medicas.service.EmailSenderService;

@ExtendWith(MockitoExtension.class)
public class CitaMedicaRestTest {
    @Mock
    private CitaMedicaRepo citaMedicaRepo;
    @Mock
    private CitaMedicaService citaMedicaService;
    @Mock
    private CorrespondeRepo correRepo;
    @Mock
    private CorrespondeService correService;
    @Mock
    private EmailSenderService mailSender;
    @InjectMocks
    private CitaMedicaRest citaMedicaRest;

    private MockMvc mockMvc;
    private JacksonTester<Cita> jsonCita;
    private JacksonTester<CitaMedica> jsonCitaMedica;
    private JacksonTester<List<CitaMedica>> jsonCitasList;
    private JacksonTester<Float>jsonValorCorre;

    @BeforeEach 
    public void setup() {
        JacksonTester.initFields(this,new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(citaMedicaRest).build();
    }
    
    /* TESTING HU_01 - Crear cita medica */
    @Test
    public void siInvocoElMetodoAddCitaMedicaYLaCitaNoExisteEntoncesLoCreaYDevuelveStatusCREATED()throws Exception{
        //Arrange
        CitaMedica citaMedica = creaCitaMedica();
        given(citaMedicaService.agendarCitaMedica(any(CitaMedica.class))).willReturn(true);
    
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/citas-medicas/")
            .accept(MediaType.APPLICATION_JSON).content(jsonCitaMedica.write(citaMedica).getJson())
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    public void siInvocoElMetodoAddCitaMedicaYLaCitaSiExisteEntoncesNoLoCreaYDevuelveStatusBAD_REQUEST() throws Exception{
        //Arrange
        CitaMedica citaMedica = creaCitaMedica();
        given(citaMedicaService.agendarCitaMedica(any(CitaMedica.class))).willReturn(false);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/citas-medicas/")
            .accept(MediaType.APPLICATION_JSON).content(jsonCitaMedica.write(citaMedica).getJson())
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }


    //Testing HU11 - deleteCitaMedicaById()
    @Test
    public void siInvocoDeleteCitaMedicaByIdYSeEliminaYStatusOk() throws Exception{
        //Arrange
        CitaMedica citaMedica = creaCitaMedica();
        given(citaMedicaService.deleteCitaMedicaById(citaMedica.getId_cita())).willReturn(true);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/citas-medicas/"+(citaMedica.getId_cita()))
            .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void siInvocoDeleteCitaMedicaByIdYNoExisteIdYStatusNotFound() throws Exception{
        //Arrange
        CitaMedica citaMedica = new CitaMedica();
        given(citaMedicaService.deleteCitaMedicaById(citaMedica.getId_cita())).willReturn(false);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/citas-medicas/"+(citaMedica.getId_cita()))
            .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        
    }
    //End Testing HU11 - deleteCitaMedicaById()

    //Testing rest HU 12
    @Test
    void siInvocoMisCitasMedicaYexisteProfesionalRetornarListaDeCitasYstatusOK() throws Exception{
        //Arrange
        String rut = "20.374.974-0";//usaremos este rut de prueba para corroborar la funcion
        List<CitaMedica> citas = obtenerCitas(); //creamos una lista con citas 
        /*creamos el escenario de que si invocamos el metodo con el rut, debera retornar una lista con los pacientes del profesional*/
        given(citaMedicaService.misCitaMedicas(rut)).willReturn(citas);
 
        //Act
        /*Creamos el mock response que hara prueba en la URI */
        MockHttpServletResponse response = mockMvc
            .perform(MockMvcRequestBuilders.get("/citas-medicas/"+rut)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
         
        //Asserts
        assertEquals(HttpStatus.OK.value(), response.getStatus()); //chekeamos que todo se realice correctamente
        assertEquals(jsonCitasList.write(citas).getJson(),response.getContentAsString()); //verificamos que los cuerpos json entregados por la uri sean lo mismo
    }

    @Test
    void siInvocoMisCitasMedicaYNOExisteProfesionalRetornarListaDeCitasVaciaYstatusNOTFOUND() throws Exception{
        //Arrange
        String rut = "20.374.974-0";//usaremos este rut de prueba para corroborar la funcion
        List<CitaMedica> citas = List.of(); //creamos lista vacia
        /*creamos el escenario de que si invocamos el metodo con el rut, debera retornar una lista vacia*/
        given(citaMedicaService.misCitaMedicas(rut)).willReturn(citas);
 
        //Act
        /*Creamos el mock response que hara prueba en la URI*/
        MockHttpServletResponse response = mockMvc
        .perform(MockMvcRequestBuilders.get("/citas-medicas/"+rut)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
 
        //Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());//se espera que el sistema no lo procece
        assertNotEquals(jsonCitasList.write(citas).getJson(),response.getContentAsString()); // se espera  que el cuerpo json no entrege los datos
    }
    //END Testing rest HU 12

     //Testing HU 9
    @Test
    void siInvocoActualizarProfesionalYSiSeActualizaProfesionalRetornarStatusOK() throws Exception{
        //Arrange
        CitaMedica citaMedica = creaCitaMedica();
        Profesional_salud Profe = getProfesional_salud();
        citaMedica.setProfesionalSalud(Profe);
        given(citaMedicaService.actualizarProfesional(Profe.getRut_profesional(), citaMedica.getId_cita())).willReturn(true);        
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put("/citas-medicas/actualizar/"+Profe.getRut_profesional()+"/"
            +citaMedica.getId_cita()).accept(MediaType.APPLICATION_JSON).content(jsonCitaMedica.write(citaMedica).getJson())
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void siInvocoActualizarProfesionalYSiNoSeActualizaProfesionalRetornarStatusNOTFOUND() throws Exception{
        //Arrange
        CitaMedica citaMedica = creaCitaMedica();
        Profesional_salud Profe = getProfesional_salud();
        Profe.setRut_profesional("111111111");
        citaMedica.setProfesionalSalud(Profe);
        given(citaMedicaService.actualizarProfesional(Profe.getRut_profesional(), citaMedica.getId_cita())).willReturn(false);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put("/citas-medicas/actualizar/"+Profe.getRut_profesional()+"/"
        +citaMedica.getId_cita())
                .accept(MediaType.APPLICATION_JSON).content(jsonCitaMedica.write(citaMedica).getJson())
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    //testing capa rest HU 15
    @Test
    void siInvocoObtenerPrecioFinalYExisteIdYNoHayCitaAFacturarEntoncesReturn1() throws Exception{
        //Arrange 
        Corresponde citaTieneHorario = crearCitaVaciaParaHorario();
        float vInvalido = -1;
        given(citaMedicaService.obtenerPrecioFinal(citaTieneHorario.getId())).willReturn(vInvalido);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/citas-medicas/"+citaTieneHorario.getId()+"/Pagar")
                .accept(MediaType.APPLICATION_JSON).content(jsonValorCorre.write(vInvalido).getJson())
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void siInvocoObtenerPrecioFinalYExisteIdYHayCitaYPacienteTienePrevisionEntoncesReturnPrecioFinal() throws Exception{
        //Arrange
        Corresponde citaTieneHorario = crearCitaParaHorario();
        float valor = calcularPrevision(
            citaTieneHorario.getCita().getCosto(), 
            citaTieneHorario.getCita().getProfesionalSalud().getEspecialidad().getCosto_especialidad());
        given(citaMedicaService.obtenerPrecioFinal(citaTieneHorario.getId())).willReturn(valor);

        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/citas-medicas/"+citaTieneHorario.getId()+"/Pagar")
                .accept(MediaType.APPLICATION_JSON).content(jsonValorCorre.write(valor).getJson())
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        
        //Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    //Testing HU_19 - Entregar comprobante de cita medica
    
    @Test
    public void siInvocoGetCitaComprobanteYExisteEntoncesRetornaCitaYStatusOK() throws Exception{
        //Arrange
        int id = 3;
        Corresponde corresponde = creaCitaComprobante();
        given(citaMedicaService.getCitaComprobante(id)).willReturn(corresponde);
        Cita cita = new Cita();
        CitaMedica citaMedica = corresponde.getCita();
        cita.setId(citaMedica.getId_cita());
        cita.setNombreProfesional(citaMedica.getProfesionalSalud().getNombre()+" "+citaMedica.getProfesionalSalud().getApellido());
        cita.setFecha(citaMedica.getFecha_atencion());
        cita.setHora_inicio(corresponde.getHora_inicio());
        cita.setHora_termino(corresponde.getHora_termino());
        cita.setSala(citaMedica.getSala_atencion());
        cita.setCosto(citaMedica.getCosto());
        //Act
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/citas-medicas/comprobante/"+id)
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonCita.write(cita).getJson(), response.getContentAsString());
    }

    @Test
    public void siInvocoGetCitaComprobanteYNoExisteEntoncesRetornaNullYStatusNotFound() throws Exception{
        //Arrange
        int id = 3;

        given(citaMedicaService.getCitaComprobante(id)).willReturn(null);

        //Act
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/citas-medicas/comprobante/"+id)
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }
    
    //End Testing HU_19


    //Testing HU_23 - getCitasDeSemana
    @Test
    public void siInvocoGetCitasDeSemanaYExistenEntoncesRetornaListaYStatusOK() throws Exception{
        //Arrange
        String rut = "9.453.621-5";
        List<CitaMedica> citas = creaCitasSemana();
        given(citaMedicaService.getCitasDeSemana(rut)).willReturn(citas);
        //Act
        MockHttpServletResponse response = mockMvc
        .perform(MockMvcRequestBuilders.get("/citas-medicas/semanales/"+rut)
            .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus()); 
        assertEquals(jsonCitasList.write(citas).getJson(),response.getContentAsString()); 
    }

    @Test
    public void siInvocoGetCitasDeSemanaYNoExistenEntoncesRetornaListaVaciaYStatusNotFound() throws Exception{
        //Arrange
        String rut = "9.453.621-5";
        List<CitaMedica> citas = List.of();
        given(citaMedicaService.getCitasDeSemana(rut)).willReturn(citas);
        //Act
        MockHttpServletResponse response = mockMvc
        .perform(MockMvcRequestBuilders.get("/citas-medicas/semanales/"+rut)
            .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()); 

    }
    //End testing HU_23

    @Test
    public void SePudoEnviarInformacionPorCorreo() throws Exception{
        //Arrange
        int id = 3;
        CitaMedica cita = creaCitaMedica();
        Optional<CitaMedica> citaOptional = Optional.of(cita);
        Corresponde cor = new Corresponde();
        cor.setCita(cita);
        cor.setHora_inicio(Time.valueOf(LocalTime.of(13, 30, 00)));
        String mensaje = "Mensaje enviado";
        given(citaMedicaService.getCitaById(id)).willReturn(citaOptional);
        when(correService.getHoraActual(id)).thenReturn(cor);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/citas-medicas/mandaCorreo/"+ id)
            .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(mensaje, response.getContentAsString());
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void NoSePudoEnviarInformacionPorCorreo() throws Exception{
        //Arrange
        int id = 3;
        Optional<CitaMedica> citaOptional = Optional.empty();
        String mensaje = "Mensaje no enviado";
        given(citaMedicaService.getCitaById(id)).willReturn(citaOptional);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/citas-medicas/mandaCorreo/"+ id)
            .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(mensaje, response.getContentAsString());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    /* Metodos que proveen datos */
    private Profesional_salud getProfesional_salud(){
        Especialidad especialidad = new Especialidad();
        especialidad.setClave_especialidad(1);
        especialidad.setId_especialidad(12);
        especialidad.setNombre_especialidad("Odontologia");

        Profesional_salud profesional = new Profesional_salud();
        profesional.setRut_profesional("9.453.621-5");
        profesional.setEspecialidad(especialidad);
        profesional.setNombre("Eduardo");
        profesional.setApellido("Canales");
        profesional.setEmail("Ecanales@gmail.com");
        profesional.setCargo("Dentista");
        profesional.setTelefono(987654321);
        
        List<Profesional_salud> profesionales = new ArrayList<>();
        profesionales.add(profesional);

        return profesional;
    }

    public CitaMedica creaCitaMedica(){
        CitaMedica citaMedica = new CitaMedica();
        citaMedica.setFecha_atencion(Date.valueOf(LocalDate.now()));
        citaMedica.setProfesionalSalud(getProfesional_salud());
        citaMedica.setFecha_atencion(Date.valueOf(LocalDate.now()));
        citaMedica.setId_cita(3);
        
        return citaMedica;
    }
    private List<CitaMedica> obtenerCitas(){
        List<CitaMedica>misCitas = new ArrayList<CitaMedica>();
        CitaMedica citaMed = new CitaMedica();
 
        citaMed.setId_cita(1);
        Profesional_salud profesional = new Profesional_salud();
        profesional.setRut_profesional("203749740");
        citaMed.setProfesionalSalud(profesional);
 
        Paciente paciente = new Paciente();
        paciente.setRut_paciente("172789834");
        paciente.setNombre("Ezio");
        paciente.setApellido("Auditore");
        paciente.setEdad(33);
        paciente.setN_telefono(46132578);
        citaMed.setPaciente(paciente);
 
        Paciente paciente2 = new Paciente();
        paciente2.setRut_paciente("164789634");
        paciente2.setNombre("Altair");
        paciente2.setApellido("Ibn-La'Ahad");
        paciente2.setEdad(20);
        paciente2.setN_telefono(98320878);
        citaMed.setPaciente(paciente2);
        misCitas.add(citaMed);
        
         
        return misCitas;
    }

    public Corresponde horario(){
        Corresponde cor = new Corresponde();
        cor.setId(1);
        cor.setOcupado(true);
        cor.setCita(creaCitaMedica());
        cor.setHora_inicio(Time.valueOf(LocalTime.of(13,0,0)));
        cor.setHora_termino(Time.valueOf(LocalTime.of(14, 0, 0)));
        return cor;
    }

    public Corresponde get(){
        Corresponde cor = new Corresponde();
        cor.setHora_inicio(Time.valueOf(LocalTime.of(13, 30, 0)));
        return cor;
    }

    public Corresponde crearCitaVaciaParaHorario(){
        Profesional_salud profesional = getProfesional_salud();
        profesional.getEspecialidad().setCosto_especialidad(30000);
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
    public float calcularPrevision(float costoCita,float costoBase){
        float calculoPrevision = costoCita + costoBase;
        calculoPrevision -= calculoPrevision * 0.3;
        return calculoPrevision;
    }
    public Corresponde crearCitaParaHorario(){
        Paciente paciente = new Paciente();
        paciente.setRut_paciente("12.345.678-9");
        paciente.setPrevision(true);
        Profesional_salud profesional = getProfesional_salud();
        profesional.getEspecialidad().setCosto_especialidad(30000);
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
        citaMedica.setFecha_atencion(Date.valueOf(LocalDate.of(2023, 1, 5)));
        Corresponde cor = new Corresponde();
        cor.setCita(citaMedica);
        cor.setHora_inicio(Time.valueOf(LocalTime.of(10, 30, 0)));
        cor.setHora_termino(Time.valueOf(LocalTime.of(11, 30, 0)));

        return cor;
    }

    private List<CitaMedica> creaCitasSemana(){
        Profesional_salud prof = getProfesional_salud();
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

    class Cita{
        private int id;
        private String nombreProfesional;
        private Date fecha;
        private Time hora_inicio;
        private Time hora_termino;
        private String sala;
        private float costo;
    
    
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getNombreProfesional() {
            return nombreProfesional;
        }
        public void setNombreProfesional(String nombreProfesional) {
            this.nombreProfesional = nombreProfesional;
        }
        public Date getFecha() {
            return fecha;
        }
        public void setFecha(Date fecha) {
            this.fecha = fecha;
        }
        public Time getHora_inicio() {
            return hora_inicio;
        }
        public void setHora_inicio(Time hora_inicio) {
            this.hora_inicio = hora_inicio;
        }
        public Time getHora_termino() {
            return hora_termino;
        }
        public void setHora_termino(Time hora_termino) {
            this.hora_termino = hora_termino;
        }
        public String getSala() {
            return sala;
        }
        public void setSala(String sala) {
            this.sala = sala;
        }
        public float getCosto() {
            return costo;
        }
        public void setCosto(float costo) {
            this.costo = costo;
        }
    
    }
}
