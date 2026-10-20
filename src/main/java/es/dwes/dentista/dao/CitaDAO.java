package es.dwes.dentista.dao;

import es.dwes.dentista.model.Cita;
import es.dwes.dentista.model.Odontologo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {
    public List<Cita> listar(boolean todas) throws SQLException {
        String sql = "SELECT c.id_cita, c.fecha, c.hora, c.motivo, p.nombre, p.apellidos, "
                + "o.nombre AS odontologo FROM cita c "
                + "JOIN paciente p ON p.id_paciente = c.id_paciente "
                + "JOIN odontologos o ON o.id_odontologo = c.id_odontologo "
                + (todas ? "" : "WHERE c.fecha >= CURDATE() ")
                + "ORDER BY c.fecha, c.hora";
        List<Cita> citas = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                String hora = resultado.getTime("hora").toString();
                citas.add(new Cita(resultado.getInt("id_cita"), resultado.getString("fecha"),
                    hora.substring(0, 5), resultado.getString("nombre"),
                        resultado.getString("apellidos"), resultado.getString("motivo"),
                        resultado.getString("odontologo")));
            }
        }
        return citas;
    }

    public List<Odontologo> listarOdontologos() throws SQLException {
        return new OdontologoDAO().listar();
    }

    public void insertar(LocalDate fecha, LocalTime hora, int idPaciente,
                         int idOdontologo, String motivo) throws SQLException {
        String sql = "INSERT INTO cita (fecha, hora, id_paciente, id_odontologo, motivo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setDate(1, java.sql.Date.valueOf(fecha));
            sentencia.setTime(2, java.sql.Time.valueOf(hora));
            sentencia.setInt(3, idPaciente);
            sentencia.setInt(4, idOdontologo);
            sentencia.setString(5, motivo == null || motivo.isBlank() ? null : motivo);
            sentencia.executeUpdate();
        }
    }

    public void eliminar(int idCita) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(
                     "DELETE FROM cita WHERE id_cita = ?")) {
            sentencia.setInt(1, idCita);
            sentencia.executeUpdate();
        }
    }
}