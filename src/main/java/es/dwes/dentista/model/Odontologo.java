package es.dwes.dentista.model;

public class Odontologo {
    private final int idOdontologo;
    private final String nombre;
    private final String especialidad;

    public Odontologo(int idOdontologo, String nombre, String especialidad) {
        this.idOdontologo = idOdontologo;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    public int getIdOdontologo() { return idOdontologo; }
    public String getNombre() { return nombre; }
    public String getEspecialidad() { return especialidad; }
}