package es.dwes.dentista.model;

public class Paciente {
    private final int idPaciente;
    private final String nombre;
    private final String apellidos;
    private final String dni;
    private final String fechaNacimiento;
    private final String telefono;
    private final String email;
    private final String direccion;
    private final String alergias;
    private final int numeroCitas;

    public Paciente(int idPaciente, String nombre, String apellidos, String dni,
                    String fechaNacimiento, String telefono, String email,
                    String direccion, String alergias, int numeroCitas) {
        this.idPaciente = idPaciente;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.alergias = alergias;
        this.numeroCitas = numeroCitas;
    }

    public int getIdPaciente() { return idPaciente; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getDni() { return dni; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getDireccion() { return direccion; }
    public String getAlergias() { return alergias; }
    public int getNumeroCitas() { return numeroCitas; }
}