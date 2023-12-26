package indimeter.reservas.reservas_medicas.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import indimeter.reservas.reservas_medicas.model.Especialidad;
import indimeter.reservas.reservas_medicas.model.Profesional_salud;
import indimeter.reservas.reservas_medicas.repository.Profesional_saludRepo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class Profesional_saludServiceTest {
    @Mock
    private Profesional_saludRepo profesional_saludRepo;
    @InjectMocks
    private Profesional_saludService profesional_saludService;

    // Test HU 04 - getProfesionalByRyt()
    @Test
    public void siInvocoGetProfesionalByRutYExisteUnProfesionalAsociadoAEseRutEntoncesRetornaLosDatosDelProfesional(){
        //Arrange
        Profesional_salud profesional_salud = getProfesional_salud();
        Optional<Profesional_salud> optionalProfesional = Optional.of(profesional_salud);
        given(profesional_saludService.getProfesionalByRut(profesional_salud.getRut_profesional())).willReturn(optionalProfesional);
        //Act 
        Optional<Profesional_salud> resultado = profesional_saludService.getProfesionalByRut(profesional_salud.getRut_profesional());
        //Assert
        assertTrue(resultado.isPresent());
        assertEquals(profesional_salud.getRut_profesional() ,resultado.get().getRut_profesional());
    }

    @Test
    public void siInvocoGetProfesionalByRutYNoExisteUnProfesionalAsociadoAEseRutEntoncesDevuelveUnObjetoVacio(){
        //Arrange
        Optional<Profesional_salud> optionalProfesional = Optional.empty();
        given(profesional_saludService.getProfesionalByRut("123456879")).willReturn(optionalProfesional);
        //Act 
        Optional<Profesional_salud> resultado = profesional_saludService.getProfesionalByRut("123456879");
        //Assert
        assertTrue(resultado.isEmpty());
    }

    //Test HU 8 service
    @Test
    public void siInvocoGetProfesionalesDispByEspecialidadConEspecialidadValidaEntoncesRetornaLista(){
        //Arrange
        String especialidad="Odontologia";   //usaremos una especialidad valida para el test
        List<Profesional_salud>listaProf = generarProfesionales();  //generamos nuestros profesionales

        when(profesional_saludService.getProfesionalesDispByEspecialidad(especialidad)).thenReturn(listaProf); /*Cuando invoquemos la funcion
        deberia retornar la lista esperada
        */
        //Act
        List<Profesional_salud>resultado = profesional_saludService.getProfesionalesDispByEspecialidad(especialidad);

        //Assert 
        assertFalse(resultado.isEmpty());
        assertEquals(listaProf.size(), resultado.size());
        assertEquals(listaProf.get(0).getEspecialidad().getNombre_especialidad(), resultado.get(0).getEspecialidad().getNombre_especialidad());


    }
    @Test
    void siInvocoGetProfesionalesDispByEspecialidadYEspecialidadNoExisteEntoncesRetornaListaVacia(){
        //Arrange
        String especialidad = "ajsdajsdjaj"; //creamos una especialiad invalida
        List<Profesional_salud> listaProfVacia = List.of(); //creamos una lista vacia

        when(profesional_saludService.getProfesionalesDispByEspecialidad(especialidad)).thenReturn(listaProfVacia); /*Creamos el escenario que si ingresamos un valor
        no valido entonces deberia regresar una lista vacia */
        //Act
        List<Profesional_salud>resultado = profesional_saludService.getProfesionalesDispByEspecialidad(especialidad);

        //Assert
        assertTrue(resultado.isEmpty());


    }
    //End Testing HU 8
//Test HU 03 - EliminarPro()
@Test
public void siInvocoEliminarProYExisteUnProfesionalAsociadoAEseRutEntoncesRetornaUnTrue(){
  //Arrange
    Profesional_salud profesional_salud = getProfesional_salud();
    Optional<Profesional_salud> optionalProfesional = Optional.of(profesional_salud);
    given(profesional_saludService.getProfesionalByRut(profesional_salud.getRut_profesional())).willReturn(optionalProfesional);
    //Act 
    boolean resultado = profesional_saludService.EliminarPro(profesional_salud.getRut_profesional());
    //Assert
    assertEquals(true,resultado);  
}
@Test
public void siInvocoEliminarProYNoExisteUnProfesionalAsociadoAEseRutEntoncesDevuelveUnFalse(){
    //Arrange
    Profesional_salud profesional_salud = getProfesional_salud();
    Optional<Profesional_salud> optionalProfesional = Optional.empty();
    given(profesional_saludService.getProfesionalByRut(profesional_salud.getRut_profesional())).willReturn(optionalProfesional);
    //Act 
    boolean resultado = profesional_saludService.EliminarPro(profesional_salud.getRut_profesional());
    //Assert
    assertEquals(false ,resultado);
}
//Test HU 03 - ActualizarPro()
@Test
public void siInvocoActualizarProYExisteUnProfesionalAsociadoAEseRutEntoncesRetornaLosDatosDelProfesional(){
    //Arrange
    Profesional_salud profesional_salud = getProfesional_salud();
    Optional<Profesional_salud> optionalProfesional = Optional.of(profesional_salud);
    given(profesional_saludService.getProfesionalByRut(profesional_salud.getRut_profesional())).willReturn(optionalProfesional);
    //Act 
   boolean resultado = profesional_saludService.ActualizarPro(profesional_salud);
    //Assert
    assertTrue( resultado);
}
@Test
public void siInvocoActualizarProYNoExisteUnProfesionalAsociadoAEseRutEntoncesDevuelveUnObjetoVacio(){
    //Arrange
    Profesional_salud profesional_salud = getProfesional_salud();
    Optional<Profesional_salud> optionalProfesional = Optional.empty();
    given(profesional_saludService.getProfesionalByRut(profesional_salud.getRut_profesional())).willReturn(optionalProfesional);
    //Act 
    boolean resultado = profesional_saludService.ActualizarPro(profesional_salud);
    //Assert
    assertFalse(resultado);
}
 //Test HU 03 - CrearPro()
 @Test
 public void siInvocoCrearProYExisteUnProfesionalAsociadoAEseRutEntoncesRetornaFalse(){
     //Arrange
     Profesional_salud profesional_salud = getProfesional_salud();
     Optional<Profesional_salud> optionalProfesional = Optional.of(profesional_salud);
     given(profesional_saludService.getProfesionalByRut(profesional_salud.getRut_profesional())).willReturn(optionalProfesional);
     //Act 
     boolean resultado = profesional_saludService.CrearPro(profesional_salud);
     //Assert
     assertEquals(false,resultado);
 }
 @Test
 public void siInvocoCrearProYNoExisteUnProfesionalAsociadoAEseRutEntoncesDevuelveUnTrue(){
     //Arrange
     Profesional_salud profesional_salud = getProfesional_salud();
        Optional<Profesional_salud> optionalProfesional = Optional.empty();
     given(profesional_saludService.getProfesionalByRut(profesional_salud.getRut_profesional()))
     .willReturn(optionalProfesional);
     //Act 
     boolean resultado = profesional_saludService.CrearPro(profesional_salud);
     //Assert
     assertEquals(true , resultado);
 }


    /* Metodos que proveen datos */
    private Profesional_salud getProfesional_salud(){
        // Crea un profesional generico para probar datos en test
        Profesional_salud profesional = new Profesional_salud();
        profesional.setRut_profesional("98765432-1");
        return profesional;
    }

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
}
