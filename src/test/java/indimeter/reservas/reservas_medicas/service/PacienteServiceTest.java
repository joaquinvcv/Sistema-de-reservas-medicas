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
import indimeter.reservas.reservas_medicas.model.Paciente;
import indimeter.reservas.reservas_medicas.model.Profesional_salud;
import indimeter.reservas.reservas_medicas.repository.PacienteRepo;

@ExtendWith(MockitoExtension.class)
public class PacienteServiceTest {
    @Mock
    private PacienteRepo pacienteRepo;
    @Mock
    private CitaMedica citaMedica;
    @InjectMocks
    private PacienteService pacienteService;

    // Test HU 02 - crearPaciente()
    @Test void siInvocoElMetodoCrearPacienteYEsteNoExisteEntoncesLoCrea(){
        //Arrange
        Paciente paciente = crearPaciente();
        Optional<Paciente> pacienteOptional = Optional.of(paciente);
        Optional<Paciente> pacienteOptionalVacio = Optional.empty();
        when(pacienteRepo.findById(paciente.getRut_paciente())).thenReturn(pacienteOptionalVacio).thenReturn(pacienteOptional);
        //Act
        boolean resultado = pacienteService.crearPaciente(paciente);
        //Assert
        assertTrue(resultado);
        verify(pacienteRepo, times(1)).saveAndFlush(paciente);
    }

    @Test void siInvocoElMetodoCrearPacienteYEsteSiExisteEntoncesNoLoVuelveACrear(){
        //Arrange
        Paciente paciente = crearPaciente(); 
        Optional<Paciente> pacienteOptional = Optional.of(paciente);
        when(pacienteRepo.findById(paciente.getRut_paciente())).thenReturn(pacienteOptional);

        //Act
        boolean resultado = pacienteService.crearPaciente(paciente);
        //Assert
        assertFalse(resultado);
        verify(pacienteRepo, times(0)).saveAndFlush(paciente);
    }

    // Test HU 02 - update();
    @Test
    public void siInvocoElMetodoUpdateConUnPacienteExistenteEntoncesModificaSusDatosAnterriores(){
        //Arrange
        Paciente paciente = crearPaciente();
        Optional<Paciente> pacienteOptional = Optional.of(paciente);
        Paciente paciente2 = crearPaciente();
        paciente2.setNombre("Matias");
        Optional<Paciente> optionalPacienteActualiza = Optional.of(paciente2);
        when(pacienteRepo.findById(paciente.getRut_paciente())).thenReturn(pacienteOptional).thenReturn(optionalPacienteActualiza);
        //Act
        boolean resultado = pacienteService.update(paciente);
        Optional<Paciente> optional = pacienteRepo.findById(paciente.getRut_paciente());
        //Assert
        assertTrue(resultado);
        assertEquals(paciente2.getNombre(), optional.get().getNombre());
        verify(pacienteRepo, times(1)).saveAndFlush(paciente);
    }

    @Test
    public void siInvocoElMetodoUpdateConUnPacienteQueNoExisteNoActualizaNada(){
        //Arrange
        Optional<Paciente> pacienteOptional = Optional.empty();
        Paciente paciente = crearPaciente();
        Optional<Paciente> pacienteOptionalDato = Optional.of(paciente);
        when(pacienteRepo.findById(paciente.getRut_paciente())).thenReturn(pacienteOptional).thenReturn(pacienteOptionalDato);
        //Act
        boolean resultado = pacienteService.update(paciente);
        //Assert
        assertFalse(resultado);
        verify(pacienteRepo, times(0)).saveAndFlush(paciente);
    }

    // Test HU 02 - deletePacienteById();
    @Test 
    public void siInvocoElMetodoDeletePacienteByIdConUnIdDeUnPacienteExistenteEntoncesLoElimina(){
        //Arrange
        Paciente paciente = crearPaciente();
        Optional<Paciente> pacienteOptional = Optional.of(paciente);
        when(pacienteRepo.findById(paciente.getRut_paciente())).thenReturn(pacienteOptional);
        //Act
        boolean resultado = pacienteService.deletePacienteById(paciente.getRut_paciente());
        //Assert
        assertTrue(resultado);
        verify(pacienteRepo, times(1)).deleteById(paciente.getRut_paciente());
    }

    @Test
    public void siInvocoElMetodoDeletePacienteByIdConUnIdDeUnPacienteNoExistenteEntoncesNoHaceNada(){
        //Arrange 
        String rut = "20.654.253-7";
        Optional<Paciente> pacienteOptional = Optional.empty();
        when(pacienteRepo.findById(rut)).thenReturn(pacienteOptional);
        //Act
        boolean resultado = pacienteService.deletePacienteById(rut);
        //Assert
        assertFalse(resultado);
        verify(pacienteRepo, times(0)).deleteById(rut);
    }
    //End Testing HU 02
    
    //Testing HU5 - getPacienteByRut()
    @Test
    public void siInvocoGetPacienteYexistePacienteRetornaPacienteYOk(){
       //Arrange 
        /*Creamos un paciente para la prueba */
        Paciente paciente = crearPaciente();
        Optional<Paciente> optionalPaciente = Optional.of(paciente); /*creamos un optional que se instanciara con el paciente */
        /*creamos el escenario de si llamamos ala  funcion debera retornar el optional de prueba */
        when(pacienteRepo.findById(paciente.getRut_paciente())).thenReturn(optionalPaciente);

        //llamamos a la funcion que queremos testear
        Optional<Paciente> resultado = pacienteService.getPacienteByRut(paciente.getRut_paciente());

        assertNotNull(resultado); /*verificamos si no entrega un valor nulo y si el rut del optional es igual al rut del resultado */
        assertEquals(optionalPaciente.get().getRut_paciente(), resultado.get().getRut_paciente());
        
    }
    @Test
    public void siInvocoGetPacienteYnoExistePacienteRetornarNull(){
        /*Creamos un rut para la prueba */
        String rut = "20345196-0";
        Optional<Paciente> NoExistePaciente = null;/*creamos un optional nulo para la prueba*/
        /*creamos el escenario de si llamamos ala  funcion debera retornar el optional vacio osea que el rut
         * no deberia existir
         */
        when(pacienteRepo.findById(rut)).thenReturn(NoExistePaciente);

        //llamamos a la funcion que queremos testear
        Optional<Paciente> resultado = pacienteService.getPacienteByRut(rut);

        //verificamos que entrege un valor nulo que es lo que buscamos
        assertNull(resultado);
        
    }
    //End Testing HU_5 - getPacienteByRut


    //Testing HU6 - getPacientesByProfesional()
    @Test
    public void siInvocoGetPacientesByProfesionalYExistenPacientesAsociadosAlIdRetornaListadoYStatusOk(){
        //Arrange
        String rutProf = "16151417-8";
        List<Paciente> pacientes = getPacientesByProf();
        when(pacienteRepo.queryBy(rutProf)).thenReturn(pacientes);
            
        //Act
        List<Paciente> resultado = pacienteService.getPacientesByProfesional(rutProf);
        //Assert
        assertNotNull(resultado);
        assertEquals(pacientes.size(),resultado.size());
        assertEquals(pacientes.get(0),resultado.get(0));
    }

    @Test
    public void siInvocoGetPacientesByProfesionalYNoExistenPacientesAsociadosAlIdRetornaListaNull(){
        //Arrange
        String rutProf = "16151417-8";
        List<Paciente> pacientes=null;
        when(pacienteRepo.queryBy(rutProf)).thenReturn(pacientes);
        
        //Act
        List<Paciente> resultado = pacienteService.getPacientesByProfesional(rutProf);
        //Assert
        assertNull(resultado);
    }
    //End Testing HU6 - getPacientesByProfesional()
    //Testing hu24 getPacientesBycitamedicaPagado()
    @Test
    public void siInvocoGetPacientesBycitamedicaPagadoYExistenPacientesAsociadosAlIdRetornaListadoYStatusOk(){
        //Arrange
        String rutProf = "16151417-8";
        List<Paciente> pacientes = getPacientesByProf();
        when(pacienteRepo.queryByPagado(rutProf)).thenReturn(pacientes);
            
        //Act
        List<Paciente> resultado = pacienteService.getPacientesBycitaPagada(rutProf);
        //Assert
        assertNotNull(resultado);
        assertEquals(pacientes.size(),resultado.size());
        assertEquals(pacientes.get(0),resultado.get(0));
    }
    @Test
    public void siInvocoGetPacientesBycitamedicaPagadoYNoExistenPacientesAsociadosAlIdRetornaListaNull(){
        //Arrange
        String rutProf = "16151417-8";
        List<Paciente> pacientes=null;
        when(pacienteRepo.queryByPagado(rutProf)).thenReturn(pacientes);
        
        //Act
        List<Paciente> resultado = pacienteService.getPacientesBycitaPagada(rutProf);
        //Assert
        assertNull(resultado);
    
    }

    /* Metodos que proveen datos */
    private List<Paciente> getPacientesByProf(){

        Profesional_salud profSalud = new Profesional_salud();
        profSalud.setRut_profesional("16151417-8");
        
        List<Paciente> pacientes = new ArrayList<>();
        List<CitaMedica> misCitas = new ArrayList<>();

        CitaMedica citaMed = new CitaMedica();
        citaMed.setId_cita(1);
        citaMed.setProfesionalSalud(profSalud);

        Paciente paciente = new Paciente();
        paciente.setRut_paciente("16478963-4");
        paciente.setNombre("Mario");
        paciente.setApellido("Contreras");
        paciente.setEdad(15);
        paciente.setN_telefono(46132578);
        citaMed.setPaciente(paciente);
        misCitas.add(citaMed);
        pacientes.add(paciente);

        citaMed = new CitaMedica();
        citaMed.setId_cita(2);
        citaMed.setProfesionalSalud(profSalud);

        paciente = new Paciente();
        paciente.setRut_paciente("19845623-7");
        paciente.setNombre("Miki");
        paciente.setApellido("Cifuentes");
        paciente.setEdad(18);
        paciente.setN_telefono(34168594);
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
