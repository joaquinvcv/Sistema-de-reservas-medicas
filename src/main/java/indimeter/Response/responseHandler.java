package indimeter.Response;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


import indimeter.reservas.reservas_medicas.model.CitaMedica;
import indimeter.reservas.reservas_medicas.model.Corresponde;
import indimeter.reservas.reservas_medicas.model.Horario_laboral;
import indimeter.reservas.reservas_medicas.model.Profesional_salud;

// Clase para editar los datos que se reciben 
public class responseHandler {
    // Recive un objeto tipo profesional_salud, al cual le va a sacar datos para devolver un objeto profesional con solo los datos mas necesarios
    public static Profesional responseProfesional_salud(Profesional_salud pro){
        Profesional profe = new Profesional();
        profe.setNombre(pro.getNombre());
        profe.setApellido(pro.getApellido());
        profe.setRut(pro.getRut_profesional());
        profe.setEmail(pro.getEmail());
        profe.setTelefono(pro.getTelefono());
        profe.setCargo(pro.getCargo());
        profe.setId_especialidad(pro.getEspecialidad().getId_especialidad());
        return profe;
    }

    public static Horario responseHorario_laboral(Horario_laboral horario){
        Horario hor = new Horario();
        hor.setNombre(horario.getProfesional().getNombre());
        hor.setApellido(horario.getProfesional().getApellido());
        hor.setRut(horario.getProfesional().getRut_profesional());
        hor.setCupos_diarios(horario.getCupos_diarios());
        return hor;
    }

    public static List<horas> responseCorresponde(List<Corresponde> cor){
        List<horas> hor = new ArrayList<>();
        for (Corresponde cor2 : cor) {
           horas hori = new horas();
           hori.setId(cor2.getId());
           hori.setHora_i(cor2.getHora_inicio());
           hori.setHora_t(cor2.getHora_termino());
           hor.add(hori); 
        }

        return hor;
    }

    public static Cita responseComprobante(Corresponde cor){
        Cita comprobante = new Cita();
        CitaMedica citaMedica = cor.getCita();
        comprobante.setId(citaMedica.getId_cita());
        String nombreProf = citaMedica.getProfesionalSalud().getNombre()+" "+ citaMedica.getProfesionalSalud().getApellido();
        comprobante.setNombreProfesional(nombreProf);
        comprobante.setFecha(citaMedica.getFecha_atencion());
        comprobante.setHora_inicio(cor.getHora_inicio());
        comprobante.setHora_termino(cor.getHora_termino());
        comprobante.setSala(citaMedica.getSala_atencion());
        comprobante.setCosto(citaMedica.getCosto());

        return comprobante;
    }
}
class Cita{
    private int id;
    private String nombreProfesional;
    private Date fecha;
    private Time hora_inicio;
    private Time hora_termino;
    private String sala;
    private float costo;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombreProfesional() {
        return nombreProfesional;
    }
    public void setNombreProfesional(String nombreProfesional) {
        this.nombreProfesional = nombreProfesional;
    }
    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
    public String getSala() {
        return sala;
    }
    public void setSala(String sala) {
        this.sala = sala;
    }
    public float getCosto() {
        return costo;
    }
    public void setCosto(float costo) {
        this.costo = costo;
    }

}

class horas{
    private int id;
    private Time hora_i;
    private Time hora_t;
    public Time getHora_i() {
        return hora_i;
    }
    public void setHora_i(Time hora_i) {
        this.hora_i = hora_i;
    }
    public Time getHora_t() {
        return hora_t;
    }
    public void setHora_t(Time hora_t) {
        this.hora_t = hora_t;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    } 
}
// Manipulador y constructor para DTO profesional y Horario
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
class Profesional{
    private String nombre;
    private String apellido;
    private String rut;
    private String email;
    private int telefono;
    private String cargo;
    private int id_especialidad;
    public Profesional(){}
    public String getNombre() {
        return nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public String getRut() {
        return rut;
    }
    public String getEmail() {
        return email;
    }
    public int getTelefono() {
        return telefono;
    }
    public String getCargo() {
        return cargo;
    }
    public int getId_especialidad() {
        return id_especialidad;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public void setRut(String rut) {
        this.rut = rut;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    public void setId_especialidad(int id_especialidad) {
        this.id_especialidad = id_especialidad;
    }  
}

