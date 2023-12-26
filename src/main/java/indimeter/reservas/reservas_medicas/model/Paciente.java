package indimeter.reservas.reservas_medicas.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rut_paciente")
@Table(name="paciente")
public class Paciente{
    @Id
    private String rut_paciente;
    private String nombre;
    private String apellido;
    private int edad;
    private String email;
    private int n_telefono;
    private boolean tratamiento;
    private boolean prevision;
    private boolean pagocita;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paciente")
    private List<CitaMedica>misCitas;
    
    public String getRut_paciente() {
        return rut_paciente;
    }
    public void setRut_paciente(String rut_paciente) {
        this.rut_paciente = formatearRut(rut_paciente);
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
    public int getEdad() {
        return edad;
    }
    public void setEdad(int edad) {
        this.edad = edad;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getN_telefono() {
        return n_telefono;
    }
    public void setN_telefono(int n_telefono) {
        this.n_telefono = n_telefono;
    }
    public boolean isTratamiento() {
        return tratamiento;
    }
    public void setTratamiento(boolean tratamiento) {
        this.tratamiento = tratamiento;
    }
    public boolean isPrevision(){
        return prevision;
    }
    public void setPrevision(boolean prevision){
        this.prevision = prevision;
    }
    public boolean isPagoCita(){
        return pagocita;
    }
    public void setPagoCita(boolean pagocita){
        this.pagocita = pagocita;
    }
    public List<CitaMedica> getMisCitas() {
        return misCitas;
    }
    public void setMisCitas(ArrayList<CitaMedica> misCitas) {
        this.misCitas = misCitas;
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