package indimeter.reservas.reservas_medicas.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import indimeter.reservas.reservas_medicas.model.CitaMedica;
import indimeter.reservas.reservas_medicas.model.Paciente;
import indimeter.reservas.reservas_medicas.model.Profesional_salud;
import indimeter.reservas.reservas_medicas.repository.PacienteRepo;
import indimeter.reservas.reservas_medicas.service.PacienteService;

@ExtendWith(MockitoExtension.class)
public class PacienteRestTest {

    @Mock
    private PacienteRepo pacienteRepo;

    @Mock
    private PacienteService pacienteService;

    @InjectMocks
    private PacienteRest pacienteRest;

    private MockMvc mockMvc;
    private JacksonTester<Paciente> jsonPaciente;
    private JacksonTester<List<Paciente>> jsonPacienteList;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this,new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(pacienteRest).build();
    }
    // Testing rest HU_02
    @Test   
    public void siInvocoElMetodoAddPacienteYElPacienteNoExisteEntoncesLoCreaYDevuelveStatusCREATED() throws Exception{
        //Arrange
        Paciente paciente = crearPaciente();
        given(pacienteService.crearPaciente(any(Paciente.class))).willReturn(true);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/pacientes")
        .accept(MediaType.APPLICATION_JSON).content(jsonPaciente.write(paciente).getJson())
        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test   
    public void siInvocoElMetodoAddPacienteYElPacienteExisteEntoncesLoNoCreaYDevuelveStatusBAD_REQUEST() throws Exception{
        //Arrange
        Paciente paciente = crearPaciente();
        given(pacienteService.crearPaciente(any(Paciente.class))).willReturn(false);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/pacientes")
        .accept(MediaType.APPLICATION_JSON).content(jsonPaciente.write(paciente).getJson())
        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void siInvocoElMetodoUpdatePacienteConUnPacienteExistenteEntoncesActualizaLosDatosYDevuelveStatusOK() throws Exception{
        //Arrange
        Paciente paciente = crearPaciente();
        paciente.setNombre("Matias");
        given(pacienteService.update(any(Paciente.class))).willReturn(true);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put("/pacientes")
        .accept(org.springframework.http.MediaType.APPLICATION_JSON).content(jsonPaciente.write(paciente).getJson())
        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void siInvocoElMetodoUpdatePacienteEnUnPacienteQueNoExisteEntoncesDevuelveStatusBAD_REQUEST() throws Exception{
        //Arrange
        Paciente paciente = crearPaciente();
        paciente.setNombre("Matias");
        given(pacienteService.update(any(Paciente.class))).willReturn(false);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put("/pacientes")
        .accept(org.springframework.http.MediaType.APPLICATION_JSON).content(jsonPaciente.write(paciente).getJson())
        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void siInvocoElMetodoDeletePacienteByIdConUnPacienteExistenteEntoncesLoBorraYDevuelveStatusOK() throws Exception{
        //Arrange
        Paciente paciente = crearPaciente();
        given(pacienteService.deletePacienteById(paciente.getRut_paciente())).willReturn(true);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/pacientes/"+paciente.getRut_paciente())
        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void siInvocoElMetodoDeletePacienteByIdConUnPacienteNoValidoDevuelveStatusNOT_FOUND() throws Exception{
        //Arrange
        Paciente paciente = crearPaciente();
        given(pacienteService.deletePacienteById(paciente.getRut_paciente())).willReturn(false);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/pacientes/"+paciente.getRut_paciente())
        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    //Testing rest HU_5
    @Test
    public void siInvocoGetPacienteYexistePacienteRetornaPacienteYStatusOk() throws Exception{
        //Arrange
        Paciente pacientePrueba = crearPaciente(); //creamos un paciente de prueba
        /*creamos el escenario de que si invocamos el metodo con el rut, debera retornar un optional con el paciente*/
        given(pacienteService.getPacienteByRut(pacientePrueba.getRut_paciente())).willReturn(Optional.of(pacientePrueba));

        //Act
        /*Creamos el mock response que hara prueba en la URI */
        MockHttpServletResponse response = mockMvc
        .perform(MockMvcRequestBuilders.get("/pacientes/"+pacientePrueba.getRut_paciente())
                .accept(org.springframework.http.MediaType.APPLICATION_JSON)).andReturn().getResponse();
        
        //asserts
        //verificamos si el estatus es la esperada
        assertEquals(HttpStatus.OK.value(),response.getStatus());
        /*verificamos que el json generado es la esperada */
        assertEquals(jsonPaciente.write(pacientePrueba).getJson(), response.getContentAsString());
        

    }

    @Test
    public void siInvocoGetPacienteYNoExistePacienteRetornarNullYStatusNOTFOUND() throws Exception{
        //Arrange
        Paciente pacientePrueba = crearPaciente();//creamos un paciente de prueba
        /*creamos el escenario de que si invocamos el metodo con el rut, debera retornar un optional vacio*/
        given(pacienteService.getPacienteByRut(pacientePrueba.getRut_paciente())).willReturn(Optional.empty());

        //Act
        /*Creamos el mock response que hara prueba en la URI */
        MockHttpServletResponse response = mockMvc
        .perform(MockMvcRequestBuilders.get("/pacientes/"+pacientePrueba.getRut_paciente())
                .accept(org.springframework.http.MediaType.APPLICATION_JSON)).andReturn().getResponse();
        
        //assert
        //verificamos si el estatus es la esperada
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        
    }
    //End Testing HU 5

    //Testing HU 6 
    @Test
    public void siInvocoGetPacientesByProfesionalYExistenPacientesAsociadosAlIdRetornaListadoYStatusOk() throws Exception { 
        //Act
        String rutProf = "161514178";
        List<Paciente> pacientes = getPacientesByProf();
        given(pacienteService.getPacientesByProfesional(rutProf)).willReturn(pacientes);
        
        //Arrange
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/pacientes/byProfesional?rutId="+rutProf)
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonPacienteList.write(pacientes).getJson(),response.getContentAsString());

    }
    @Test
    public void siInvocoGetPacientesByProfesionalYNoExistenPacientesAsociadosAlIdRetornaListaNullYStatusNotFound() throws Exception { 
        //Act
        String rutProf = "161514178";
        List<Paciente> pacientes = List.of();
        given(pacienteService.getPacientesByProfesional(rutProf)).willReturn(pacientes);
        
        //Arrange
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/pacientes/byProfesional?rutId="+rutProf)
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.NOT_FOUND.value(),response.getStatus());
        assertNotEquals(jsonPacienteList.write(pacientes).getJson(),response.getContentAsString());

    }
    //Testing HU24 getPacientesBycitamedicaPagado
    @Test
    public void siInvocoGetPacientesByCitaMedicaPagadoYExistenPacientesAsociadosAlIdRetornaListadoYStatusOk() throws Exception { 
        
       //Act
       String rutPro = "16.151.417-8";
       List<Paciente> pacientes = getPacientesByProf();
       given(pacienteService.getPacientesBycitaPagada(rutPro)).willReturn(pacientes);
       
       //Arrange
       MockHttpServletResponse response = mockMvc
               .perform(MockMvcRequestBuilders.get("/pacientes/bycitaPagada?rutPro="+rutPro)
                       .accept(org.springframework.http.MediaType.APPLICATION_JSON)).andReturn().getResponse();
       //Assert
       assertEquals(HttpStatus.OK.value(), response.getStatus());
       assertEquals(jsonPacienteList.write(pacientes).getJson(),response.getContentAsString());


    }
    @Test
    public void siInvocoGetPacientesByCitaMedicaPagadaYNoExistenPacientesAsociadosAlIdRetornaListaNullYStatusNotFound() throws Exception { 
        //Act
        String rutPro = "161514178";
        List<Paciente> pacientes =List.of();
        given(pacienteService.getPacientesBycitaPagada(rutPro)).willReturn(pacientes);
        
        //Arrange
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/pacientes/bycitaPagada?rutPro="+rutPro)
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.NOT_FOUND.value(),response.getStatus());
        assertNotEquals(jsonPacienteList.write(pacientes).getJson(),response.getContentAsString());
        

    }

    



    public List<Paciente> getPacientesByProf(){

        Profesional_salud profSalud = new Profesional_salud();
        profSalud.setRut_profesional("16.151.417-8");
        
        List<Paciente> pacientes = new ArrayList<>();
        List<CitaMedica> misCitas = new ArrayList<>();

        CitaMedica citaMed = new CitaMedica();
        citaMed.setId_cita(1);
        citaMed.setProfesionalSalud(profSalud);

        Paciente paciente = new Paciente();
        paciente.setRut_paciente("16.478.963-4");
        paciente.setNombre("Mario");
        paciente.setApellido("Contreras");
        paciente.setEdad(15);
        paciente.setN_telefono(46132578);
        paciente.setPagoCita(true);
        citaMed.setPaciente(paciente);
        misCitas.add(citaMed);
        pacientes.add(paciente);

        citaMed = new CitaMedica();
        citaMed.setId_cita(2);
        citaMed.setProfesionalSalud(profSalud);

        paciente = new Paciente();
        paciente.setRut_paciente("19.845.623-7");
        paciente.setNombre("Miki");
        paciente.setApellido("Cifuentes");
        paciente.setEdad(18);
        paciente.setN_telefono(34168594);
        paciente.setPagoCita(true);
        citaMed.setPaciente(paciente);
        misCitas.add(citaMed);
        pacientes.add(paciente);
        

        return pacientes;
    }


    public Paciente crearPaciente(){
        Paciente paciente = new Paciente();
        paciente.setRut_paciente("20374974-0");
        paciente.setNombre("Eduardo");
        paciente.setApellido("contreras");
        paciente.setN_telefono(991031054);
        paciente.setEdad(20);
        paciente.setTratamiento(false);
        paciente.setEmail("contreras@gmail.com");
        return paciente;

    }
    
}
