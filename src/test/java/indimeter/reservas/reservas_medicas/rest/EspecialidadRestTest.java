package indimeter.reservas.reservas_medicas.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.io.IOException;
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

import indimeter.reservas.reservas_medicas.model.Especialidad;
import indimeter.reservas.reservas_medicas.model.Profesional_salud;
import indimeter.reservas.reservas_medicas.repository.EspecialidadRepo;
import indimeter.reservas.reservas_medicas.service.EspecialidadService;

@ExtendWith(MockitoExtension.class)
public class EspecialidadRestTest {
    @Mock
    private EspecialidadRepo espeRepo;

    @Mock
    private EspecialidadService espeService;

    @InjectMocks
    private EspecialidadRest espeRest;

    private MockMvc mockMvc;
    private JacksonTester<Especialidad> jsonEspecialidad;
    private JacksonTester<String>jsonInfo;
    @BeforeEach
    public void setup(){
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(espeRest).build();
    }

    @Test
    public void siInvocoElMetodocrearEspecialidadYLaEspecialidadNoExisteEntoncesLaCreaYDevuelveStatusCREATED() throws Exception{
        //Arrange
        Especialidad espe = crearEspecialidad();
        given(espeService.crearEspecialidad(any(Especialidad.class))).willReturn(true);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/especialidad")
            .accept(MediaType.APPLICATION_JSON).content(jsonEspecialidad.write(espe).getJson())
            .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    public void siInvocoElMetodocrearEspecialidadYLaEspecialidadExisteEntoncesDevuelveStatusBAD_REQUEST() throws Exception{
        //Arrange
        Especialidad espe = crearEspecialidad();
        given(espeService.crearEspecialidad(any(Especialidad.class))).willReturn(false);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/especialidad")
            .accept(MediaType.APPLICATION_JSON).content(jsonEspecialidad.write(espe).getJson())
            .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()); 
    }

    /*HU_20 REST TEST */
    @Test
    public void siInvocoUpdateEspecialidadProfesionalYExisteEntoncesActualizaYStatusOK() throws Exception{
        //ARRANGE
        Especialidad especialidad = crearEspecialidad();
        especialidad.setClave_especialidad(3);
        given(espeService.updateEspecialidadProfesional(any(Especialidad.class))).willReturn(true);
        //ACT
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put("/especialidad/")
        .accept(org.springframework.http.MediaType.APPLICATION_JSON).content(jsonEspecialidad.write(especialidad).getJson())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //ASSERT
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void siInvocoUpdateEspecialidadProfesionalYNoExisteEntoncesNoActualizarYStatusBAD_REQUEST() throws Exception{
        //ARRANGE
        Especialidad especialidad = crearEspecialidad();
        especialidad.setClave_especialidad(3);
        given(espeService.updateEspecialidadProfesional(any(Especialidad.class))).willReturn(false);
        //ACT
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put("/especialidad/")
        .accept(org.springframework.http.MediaType.APPLICATION_JSON).content(jsonEspecialidad.write(especialidad).getJson())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //ASSERT
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
    /*HU_20 REST TEST */

    /*HU_22 TEST REST */
    @Test
    public void siInvocoObtenerInfoEspecialidadYExisteProfesionalEntoncesRetornarDatosYOK() throws IOException, Exception{
        //Arrange
        Especialidad esp = crearEspecialidad();
        Profesional_salud profe = crearProfesiona();
        profe.setEspecialidad(esp);
       
        given(espeService.obtenerInfoEspecialidad(profe.getRut_profesional())).willReturn(esp.getInfo_especialidad());

        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/especialidad/"+profe.getRut_profesional()+"/infoEsp")
        .accept(org.springframework.http.MediaType.APPLICATION_JSON).content(jsonInfo.write(esp.getInfo_especialidad()).getJson())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)).andReturn().getResponse();

        //asserts
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
    @Test
    public void siInvocoObtenerInfoEspecialidadYNoExisteProfesionalEntoncesRetornarDatosVacioNotFound() throws IOException, Exception{
        //arrange
        Especialidad esp = crearEspecialidad();
        Profesional_salud profe = crearProfesiona();
        profe.setEspecialidad(esp);
        
        given(espeService.obtenerInfoEspecialidad(profe.getRut_profesional())).willReturn(null);

         //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/especialidad/"+profe.getRut_profesional()+"/infoEsp")
        .accept(org.springframework.http.MediaType.APPLICATION_JSON).content(jsonInfo.write(esp.getInfo_especialidad()).getJson())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)).andReturn().getResponse();

        //asserts
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());

    }
    //End Testing HU REST 22
    
    private Especialidad crearEspecialidad(){
        Especialidad espe = new Especialidad();
        espe.setId_especialidad(10);
        espe.setClave_especialidad(1506);
        espe.setCosto_especialidad(12500);
        espe.setNombre_especialidad("Quiropractica");
        espe.setInfo_especialidad("Atenciones en dolor de cuello, dolor lumbar, osteoartritis y afecciones de los discos de la columna");
        return espe;
    }

    private Profesional_salud crearProfesiona(){
        Profesional_salud profe = new Profesional_salud();
        profe.setRut_profesional("20.457.697-1");
        profe.setNombre("Eduardo");
        profe.setApellido("Cifuentes");
        profe.setCargo("Quiropractico");
        return profe;
    }


}
