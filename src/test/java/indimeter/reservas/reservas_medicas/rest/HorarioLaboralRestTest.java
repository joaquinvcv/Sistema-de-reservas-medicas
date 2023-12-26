package indimeter.reservas.reservas_medicas.rest;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import indimeter.reservas.reservas_medicas.model.Horario_laboral;
import indimeter.reservas.reservas_medicas.model.Profesional_salud;
import indimeter.reservas.reservas_medicas.service.HorarioLaboralService;

@ExtendWith(MockitoExtension.class)
public class HorarioLaboralRestTest {
    @Mock
    private HorarioLaboralService horarioLaboralService;
    @InjectMocks
    private HorarioLaboralRest horarioLaboralRest;

    private MockMvc mockMvc;
    private JacksonTester<Horario> jsonHorario; //json tester con respuesta custom (response handler)
    private JacksonTester<Horario_laboral> jsonHorario_laboral; //json tester default

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(horarioLaboralRest).build();
    }
    
    // Test HU 07
    @Test
    public void siInvocoElMetodoGetHorarioProfesionalConUnaIdValidaSeDevuelvenLosDatosAsociadosAEsaIdYUnCodigoDeRespuestaOK() throws Exception{
        //Arrange
        String rut = "98.765.432-1";
        Horario_laboral horario = getHorario();
        Optional<Horario_laboral> horarioOptional = Optional.of(horario);
        given(horarioLaboralService.getHorarioByRut(horario.getProfesional().getRut_profesional())).willReturn(horarioOptional);
        Horario hor = new Horario();
        hor.setNombre(horario.getProfesional().getNombre());
        hor.setApellido(horario.getProfesional().getApellido());
        hor.setRut(horario.getProfesional().getRut_profesional());
        hor.setCupos_diarios(horario.getCupos_diarios());
        
        //Act
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/horarios/byProfesional?rut="+rut)
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonHorario.write(hor).getJson(), response.getContentAsString());
    }

    @Test
    public void siInvocoElMetodoGetHorarioProfesionalConUnaIdNoValidaSeDevuelveCodigoDeErrorNotFound() throws Exception{
        //Arrange
        String rut = "10.203.645-9";
        Optional<Horario_laboral> horarioOptional = Optional.empty();
        given(horarioLaboralService.getHorarioByRut(rut)).willReturn(horarioOptional);
        //Act
        MockHttpServletResponse response = mockMvc
            .perform(MockMvcRequestBuilders.get("/horarios/byProfesional?rut="+rut)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }


    //Test HU 10 - updateHorarioLaboral()   
    @Test
    public void siInvocoUpdateHorarioLaboralConHorarioExistenteEntoncesActualizaYStatusOK() throws Exception{
        //Arrange
        Horario_laboral horario = creaHorario_laboral();
        horario.setCupos_diarios(3);
        given(horarioLaboralService.updateHorarioLaboral(any(Horario_laboral.class))).willReturn(true);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put("/horarios/")
                .accept(org.springframework.http.MediaType.APPLICATION_JSON).content(jsonHorario_laboral.write(horario).getJson())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void siInvocoUpdateHorarioLaboralConHorarioNoExistenteEntoncesNoActualizaYStatusBAD_REQUEST() throws Exception{
        //Arrange
        Horario_laboral horario = creaHorario_laboral();
        horario.setCupos_diarios(3);
        given(horarioLaboralService.updateHorarioLaboral(any(Horario_laboral.class))).willReturn(false);
        //Act
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put("/horarios/")
                .accept(org.springframework.http.MediaType.APPLICATION_JSON).content(jsonHorario_laboral.write(horario).getJson())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)).andReturn().getResponse();
        //Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
    //End Testing HU 10 - updateHorarioLaboral()   


    /* Metodos que proveen datos */
    public Horario_laboral getHorario(){
        Profesional_salud profesional = new Profesional_salud();
        profesional.setRut_profesional("98.765.432-1");

        Horario_laboral horario = new Horario_laboral();
        horario.setCupos_diarios(9);
        horario.setId_horario(4);
        horario.setProfesional(profesional);
        return horario;
    }

    private Horario_laboral creaHorario_laboral(){
        Horario_laboral horario = new Horario_laboral();
        Profesional_salud prof = new Profesional_salud();
        prof.setRut_profesional("16.457.892-9");
        horario.setId_horario(4);
        horario.setProfesional(prof);
        horario.setCupos_diarios(8);

        return horario;
    }



    // Ignorar esta parte que es solo para que las respuestas coincidan con las respuestas custom
    class Horario{
        private String nombre;
        private String apellido;
        private String rut;
        private int cupos_diarios;
        public String getNombre() {
            return nombre;
        }
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
        public String getApellido() {
            return apellido;
        }
        public void setApellido(String apellido) {
            this.apellido = apellido;
        }
        public String getRut() {
            return rut;
        }
        public void setRut(String rut) {
            this.rut = rut;
        }
        public int getCupos_diarios() {
            return cupos_diarios;
        }
        public void setCupos_diarios(int cupos_diarios) {
            this.cupos_diarios = cupos_diarios;
        }
    }
}
