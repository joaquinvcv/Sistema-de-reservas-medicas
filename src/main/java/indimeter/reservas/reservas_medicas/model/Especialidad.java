package indimeter.reservas.reservas_medicas.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Especialidad")
public class Especialidad {
    @Id
    private int id_especialidad;
    private String nombre_especialidad;
    private int clave_especialidad;
    private int costo_especialidad;
    private String info_especialidad;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "especialidad")
    private List<Profesional_salud>misProfesionales;
    // Cambie esto para que el constructor de la clase pueda ser accedido
    public Especialidad(){}

    public int getId_especialidad() {
        return id_especialidad;
    }
    public void setId_especialidad(int id_especialidad) {
        this.id_especialidad = id_especialidad;
    }
    public String getNombre_especialidad() {
        return nombre_especialidad;
    }
    public void setNombre_especialidad(String nombre_especialidad) {
        this.nombre_especialidad = nombre_especialidad;
    }
    public int getClave_especialidad() {
        return clave_especialidad;
    }
    public void setClave_especialidad(int clave_especialidad) {
        this.clave_especialidad = clave_especialidad;
    }
    public int getCosto_especialidad(){
        return costo_especialidad;
    }
    public void setCosto_especialidad(int costo_especialidad){
        this.costo_especialidad = costo_especialidad;
    }
    public String getInfo_especialidad(){
        return info_especialidad;
    }
    public void setInfo_especialidad(String info_especialidad){
        this.info_especialidad = info_especialidad;
    }
    public List<Profesional_salud> getMisProfesionales() {
        return misProfesionales;
    }

    public void setMisProfesionales(ArrayList<Profesional_salud> misProfesionales) {
        this.misProfesionales = misProfesionales;
    }

    

}