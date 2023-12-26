package indimeter.reservas.reservas_medicas.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Table(name = "Horario_laboral")
public class Horario_laboral {
    @Id
    private int id_horario;
    private int cupos_diarios;

    @OneToOne
    @JoinColumn(name="rut_profesional")
    private Profesional_salud profesional;

    /*@ManyToMany
    @JoinTable(name = "Corresponde", joinColumns = @JoinColumn(name = "id_horario"), inverseJoinColumns = @JoinColumn(name = "id_cita"))
    private List <CitaMedica> citas ;
    */
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "horario", orphanRemoval = false)
    private List<Corresponde> horarios;

    public Horario_laboral() {
    }

    public int getId_horario() {
        return id_horario;
    }

    public void setId_horario(int id_horario) {
        this.id_horario = id_horario;
    }

    public int getCupos_diarios() {
        return cupos_diarios;
    }

    public void setCupos_diarios(int cupos_diarios) {
        this.cupos_diarios = cupos_diarios;
    }

    public Profesional_salud getProfesional() {
        return profesional;
    }

    public void setProfesional(Profesional_salud profesional) {
        this.profesional = profesional;
    }

    public List<Corresponde> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Corresponde> horarios) {
        this.horarios = horarios;
    }

}