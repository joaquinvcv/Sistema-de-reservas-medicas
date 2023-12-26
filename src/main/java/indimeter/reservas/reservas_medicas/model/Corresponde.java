package indimeter.reservas.reservas_medicas.model;
import java.sql.Time;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;




@Entity
@Table(name = "Corresponde")
public class Corresponde {
    @Id
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_horario")
    private Horario_laboral horario;

    @ManyToOne
    @JoinColumn(name = "id_cita")
    private CitaMedica cita;

    private Time hora_inicio;
    private Time hora_termino;
    private boolean ocupado;

    public Corresponde(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Horario_laboral getHorario() {
        return horario;
    }

    public void setHorario(Horario_laboral horario) {
        this.horario = horario;
    }

    public CitaMedica getCita() {
        return cita;
    }

    public void setCita(CitaMedica cita) {
        this.cita = cita;
    }

    public Time getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(Time hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public Time getHora_termino() {
        return hora_termino;
    }

    public void setHora_termino(Time hora_termino) {
        this.hora_termino = hora_termino;
    }
    public boolean isOcupado() {
        return ocupado;
    }
    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }
}