package indimeter.reservas.reservas_medicas.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import com.fasterxml.jackson.databind.ObjectMapper;

import indimeter.reservas.reservas_medicas.model.Especialidad;
import indimeter.reservas.reservas_medicas.model.Profesional_salud;
import indimeter.reservas.reservas_medicas.service.Profesional_saludService;

@ExtendWith(MockitoExtension.class)
public class Profesional_saludRestTest {

    @Mock
    private Profesional_saludService profesional_saludService;

    @InjectMocks
    private Profesional_saludRest profesional_saludRest;
    
    private MockMvc mockMvc;
    private JacksonTester<Profesional> jsonProfesional;
    private JacksonTester<List<Profesional_salud>> jsonProfList;
    private JacksonTester<Profesional_salud> jsonProfesional_salud;

    @BeforeEach
    public void setup(){
        JacksonTester.initFields(this,new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(profesional_saludRest).build();
    }

    // Test HU 04
    @Test
    public void SiInvocoElMetodoGetProfesionalPorRutConUnRutValidoEntregaLosDatosDelProfesionalYStatusOk() throws Exception{
        //Arrange 
        String rut = "98.765.432-1";
        Profesional_salud profesional = getProfesional_salud();
        Optional<Profesional_salud> profesionalOptinal = Optional.of(profesional);
        given(profesional_saludService.getProfesionalByRut(profesional.getRut_profesional())).willReturn(profesionalOptinal);

        Profesional profe = new Profesional();
        profe.setNombre(profesional.getNombre());
        profe.setApellido(profesional.getApellido());
        profe.setRut(profesional.getRut_profesional());
        profe.setEmail(profesional.getEmail());
        profe.setTelefono(profesional.getTelefono());
        profe.setCargo(profesional.getCargo());
        profe.setId_especialidad(profesional.getEspecialidad().getId_especialidad());profe.setNombre(profesional.getNombre());
        //Act
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/profesionales/"+rut)
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonProfesional.write(profe).getJson(), response.getContentAsString());
    }
    //Testing HU 8 rest
    @Test
    void siInvocoGetProfesionalesDispByEspecialidadConEspecialidadValidaEntoncesRetornaListaYStatusOK() throws Exception{
        //Arrange
        String especialidad="Odontologia";   //usaremos una especialidad valida para el test
        List<Profesional_salud>listaProf = generarProfesionales();  //generamos nuestros profesionales
        given(profesional_saludService.getProfesionalesDispByEspecialidad(especialidad)).willReturn(listaProf); /*Creamos el dato que si funciona la funcion
        entonces debe retornar la lista de prueba */

        //Act
        MockHttpServletResponse response = mockMvc
        .perform(MockMvcRequestBuilders.get("/profesionales?nomEsp="+especialidad)
            .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        //Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus()); //Comprobamos que el status sea el esperado
        assertEquals(jsonProfList.write(listaProf).getJson(),response.getContentAsString()); //comprobamos si el json generado es el esperado
    }

    @Test
    void siInvocoGetProfesionalesDispByEspecialidadYEspecialidadNoExisteEntoncesRetornaListaVaciaYStatusNOTFOUND() throws Exception{
        //Arrange
        String especialidad="aasdkaskdak";   //usaremos una especialidad invalida para el test
        List<Profesional_salud>listaProf = List.of();  //generamos nuestros profesionales
        given(profesional_saludService.getProfesionalesDispByEspecialidad(especialidad)).willReturn(listaProf);
        //Act
        MockHttpServletResponse response = mockMvc
        .perform(MockMvcRequestBuilders.get("/profesionales?nomEsp="+especialidad)
            .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        //Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());   //corroboramos si el status es el indicado
    }


    @Test
    public void SiInvocoElMetodoGetProfesionalPorRutConUnRutNoValidoDevuelveStatusNotFound() throws Exception{
        //Arrange
        String rut = "98.765.432-1";
        Optional<Profesional_salud> optionalProfesional = Optional.empty();
        given(profesional_saludService.getProfesionalByRut(rut)).willReturn(optionalProfesional);
        //Act
        MockHttpServletResponse response = mockMvc
            .perform(MockMvcRequestBuilders.get("/profesionales/"+rut)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

 // Test 
    @Test
    public void SiInvocoElMetodoCrearProConUnProfesionalValidoDevuelveStatusOK() throws Exception{
        //Arrange
        Profesional_salud profesional = getProfesional_salud();
        given(profesional_saludService.CrearPro(any(Profesional_salud.class))).willReturn(true);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/profesionales")
                .accept(MediaType.APPLICATION_JSON).content(jsonProfesional_salud.write(profesional).getJson())
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
    @Test
    public void SiInvocoElMetodoCrearProConUnProfesionalNoValidoDevuelveStatusBadRequest() throws Exception{
        //Arrange
        Profesional_salud profesional = getProfesional_salud();
        given(profesional_saludService.CrearPro(any(Profesional_salud.class))).willReturn(false);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/profesionales")
                .accept(MediaType.APPLICATION_JSON).content(jsonProfesional_salud.write(profesional).getJson())
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
    @Test
    public void SiInvocoElMetodoActualizarProConUnProfesionalValidoDevuelveStatusOk() throws Exception{
        //Arrange
        Profesional_salud profesional = getProfesional_salud();
        profesional.setNombre("Mario");
        given(profesional_saludService.ActualizarPro(any(Profesional_salud.class))).willReturn(true);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put("/profesionales")
                .accept(MediaType.APPLICATION_JSON).content(jsonProfesional_salud.write(profesional).getJson())
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
    @Test
    public void SiInvocoElMetodoActualizarProConUnProfesionalNoValidoDevuelveStatusBadRequest() throws Exception{
        //Arrange
        Profesional_salud profesional = getProfesional_salud();
        profesional.setNombre("Mario");
        given(profesional_saludService.ActualizarPro(any(Profesional_salud.class))).willReturn(false);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put("/profesionales")
                .accept(MediaType.APPLICATION_JSON).content(jsonProfesional_salud.write(profesional).getJson())
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
    @Test
    public void SiInvocoElMetodoEliminarProConUnProfesionalValidoDevuelveStatusOk() throws Exception{
        //Arrange
        String rut = "98.765.432-1";
        given(profesional_saludService.EliminarPro(rut)).willReturn(true);
        //Act
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.delete("/profesionales/"+rut)
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
    @Test
    public void SiInvocoElMetodoEliminarProConUnProfesionalNoValidoDevuelveStatusNotFound() throws Exception{
        //Arrange
        String rut = "98.765.432-1";
        given(profesional_saludService.EliminarPro(rut)).willReturn(false);
        //Act
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.delete("/profesionales/"+rut)
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }


    
  
    /* Metodos que proveen datos */
    private List<Profesional_salud>generarProfesionales(){
        List<Profesional_salud> lista = new ArrayList<Profesional_salud>();
        Especialidad esp = new Especialidad();
        esp.setId_especialidad(1);
        esp.setNombre_especialidad("Odontologia");
        Profesional_salud p1 = new Profesional_salud();
        p1.setRut_profesional("11.912.394-1");
        p1.setNombre("Edward");
        p1.setApellido("kenway");
        p1.setEmail("pirata@ac4.com");
        p1.setCargo("Dentista");
        p1.setEspecialidad(esp);
        lista.add(p1);

        Profesional_salud p2 = new Profesional_salud();
        p2.setRut_profesional("11.912.394-1");
        p2.setNombre("Shay");
        p2.setApellido("Cormac");
        p2.setEmail("templario@acR.com");
        p2.setCargo("Cirujano");
        p2.setEspecialidad(esp);
        lista.add(p2);
        return lista;

    }

    private Profesional_salud getProfesional_salud(){
        Especialidad especialidad = new Especialidad();
        especialidad.setClave_especialidad(1);
        especialidad.setId_especialidad(12);
        especialidad.setNombre_especialidad("Odontologia");

        Profesional_salud profesional = new Profesional_salud();
        profesional.setRut_profesional("98765432-1");
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

    class Profesional{
        private String nombre;
        private String apellido;
        private String rut;
        private String email;
        private int telefono;
        private String cargo;
        private int id_especialidad;
        public Profesional(){}
        public String getNombre() {
            return nombre;
        }
        public String getApellido() {
            return apellido;
        }
        public String getRut() {
            return rut;
        }
        public String getEmail() {
            return email;
        }
        public int getTelefono() {
            return telefono;
        }
        public String getCargo() {
            return cargo;
        }
        public int getId_especialidad() {
            return id_especialidad;
        }
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
        public void setApellido(String apellido) {
            this.apellido = apellido;
        }
        public void setRut(String rut) {
            this.rut = rut;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public void setTelefono(int telefono) {
            this.telefono = telefono;
        }
        public void setCargo(String cargo) {
            this.cargo = cargo;
        }
        public void setId_especialidad(int id_especialidad) {
            this.id_especialidad = id_especialidad;
        }  
    }
}

