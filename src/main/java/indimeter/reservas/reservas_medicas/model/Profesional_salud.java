package indimeter.reservas.reservas_medicas.model;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "Profesional_salud")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rut_profesional")
public class Profesional_salud{
    @Id
    private String  rut_profesional;
    private String nombre;
    private String apellido;
    private String email;
    private int  telefono;
    private String cargo;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profesionalSalud")
    private List<CitaMedica>citas;

    @ManyToOne
    @JoinColumn(name = "id_especialidad")
    private Especialidad especialidad;
 

   public String getRut_profesional() {
       return rut_profesional;
   }
   public void setRut_profesional(String rut_profesional) {
       this.rut_profesional = formatearRut(rut_profesional);
   }
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
   public String getEmail() {
       return email;
   }
   public void setEmail(String email) {
       this.email = email;
   }
   public int getTelefono() {
       return telefono;
   }
   public void setTelefono(int telefono) {
       this.telefono = telefono;
   }
   public String getCargo() {
       return cargo;
   }
   public void setCargo(String cargo) {
       this.cargo = cargo;
   }
    public Especialidad getEspecialidad() {
        return especialidad;
    }
    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }
   
   
    public static String formatearRut(String rut) {
        int cont = 0;
        String format;
        rut = rut.replace(".", "");
        rut = rut.replace("-", "");
        format = "-" + rut.substring(rut.length() - 1);
        for (int i = rut.length() - 2; i >= 0; i--) {
            format = rut.substring(i, i + 1) + format;
            cont++;
            if (cont == 3 && i != 0) {
                format = "." + format;
                cont = 0;
            }
        }
        return format;
      
    }



   
    
}