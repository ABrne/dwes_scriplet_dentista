package es.dwes.dentista.dao;

import es.dwes.dentista.model.Paciente;
import es.dwes.dentista.model.PacienteOpcion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {
    public List<Paciente> listar() throws SQLException {
        String sql = "SELECT p.id_paciente, p.nombre, p.apellidos, p.dni, p.fecha_nacimiento, "
                + "p.telefono, p.email, p.direccion, p.alergias, "
                + "(SELECT COUNT(*) FROM cita c WHERE c.id_paciente = p.id_paciente) AS numero_citas "
                + "FROM paciente p ORDER BY p.apellidos, p.nombre";
        List<Paciente> pacientes = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                pacientes.add(new Paciente(
                        resultado.getInt("id_paciente"), resultado.getString("nombre"),
                        resultado.getString("apellidos"), resultado.getString("dni"),
                        resultado.getString("fecha_nacimiento"), resultado.getString("telefono"),
                        resultado.getString("email"), resultado.getString("direccion"),
                        resultado.getString("alergias"), resultado.getInt("numero_citas")));
            }
        }
        return pacientes;
    }

    public List<PacienteOpcion> listarOpciones() throws SQLException {
        String sql = "SELECT id_paciente, nombre, apellidos FROM paciente ORDER BY apellidos, nombre";
        List<PacienteOpcion> pacientes = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                pacientes.add(new PacienteOpcion(resultado.getInt("id_paciente"),
                        resultado.getString("nombre"), resultado.getString("apellidos")));
            }
        }
        return pacientes;
    }

    public void insertar(String nombre, String apellidos, String dni, LocalDate fechaNacimiento,
                         String telefono, String email, String direccion, String alergias)
            throws SQLException {
        String sql = "INSERT INTO paciente (nombre, apellidos, dni, fecha_nacimiento, telefono, email, direccion, alergias) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, nombre);
            sentencia.setString(2, apellidos);
            sentencia.setString(3, dni);
            if (fechaNacimiento == null) {
                sentencia.setNull(4, Types.DATE);
            } else {
                sentencia.setDate(4, java.sql.Date.valueOf(fechaNacimiento));
            }
            sentencia.setString(5, valorONull(telefono));
            sentencia.setString(6, valorONull(email));
            sentencia.setString(7, valorONull(direccion));
            sentencia.setString(8, valorONull(alergias));
            sentencia.executeUpdate();
        }
    }

    public void eliminar(int idPaciente) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(
                     "DELETE FROM paciente WHERE id_paciente = ?")) {
            sentencia.setInt(1, idPaciente);
            sentencia.executeUpdate();
        }
    }

    private String valorONull(String valor) {
        return valor == null || valor.isBlank() ? null : valor;
    }
}