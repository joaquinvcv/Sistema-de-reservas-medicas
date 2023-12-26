package indimeter.reservas.reservas_medicas.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Cita_medica")
public class CitaMedica {
    @Id
    private int id_cita;
    private Date fecha_atencion;
    private String comentario;
    private String sala_atencion;
    private float costo;


    @ManyToOne
    @JoinColumn(name = "rut_paciente")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "rut_profesional")
    private Profesional_salud profesionalSalud;

    /* 
    @ManyToMany(mappedBy = "citas")
    private List<Horario_laboral> horarios;
    */
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cita", orphanRemoval = true)
    private List<Corresponde> citas;

    public CitaMedica() {
    }

    public int getId_cita() {
        return id_cita;
    }

    public void setId_cita(int id_cita) {
        this.id_cita = id_cita;
    }

    public Date getFecha_atencion() {
        return fecha_atencion;
    }

    public void setFecha_atencion(Date fecha_atencion) {
        this.fecha_atencion = fecha_atencion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getSala_atencion() {
        return sala_atencion;
    }

    public void setSala_atencion(String sala_atencion) {
        this.sala_atencion = sala_atencion;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Profesional_salud getProfesionalSalud() {
        return profesionalSalud;
    }

    public void setProfesionalSalud(Profesional_salud profesionalSalud) {
        this.profesionalSalud = profesionalSalud;
    }
     public List<Corresponde> getCitas() {
        return citas;
    }

    public void setCitas(List<Corresponde> citas) {
        this.citas = citas;
    }
    
}