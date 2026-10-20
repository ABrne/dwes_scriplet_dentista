package es.dwes.dentista.model;

public class PacienteOpcion {
    private final int idPaciente;
    private final String nombre;
    private final String apellidos;

    public PacienteOpcion(int idPaciente, String nombre, String apellidos) {
        this.idPaciente = idPaciente;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    public int getIdPaciente() { return idPaciente; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
}