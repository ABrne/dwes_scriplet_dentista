package es.dwes.dentista.model;

public class Cita {
    private final int idCita;
    private final String fecha;
    private final String hora;
    private final String nombrePaciente;
    private final String apellidosPaciente;
    private final String motivo;
    private final String nombreOdontologo;

    public Cita(int idCita, String fecha, String hora, String nombrePaciente,
                String apellidosPaciente, String motivo, String nombreOdontologo) {
        this.idCita = idCita;
        this.fecha = fecha;
        this.hora = hora;
        this.nombrePaciente = nombrePaciente;
        this.apellidosPaciente = apellidosPaciente;
        this.motivo = motivo;
        this.nombreOdontologo = nombreOdontologo;
    }

    public int getIdCita() { return idCita; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getNombrePaciente() { return nombrePaciente; }
    public String getApellidosPaciente() { return apellidosPaciente; }
    public String getMotivo() { return motivo; }
    public String getNombreOdontologo() { return nombreOdontologo; }
}