package es.dwes.dentista.dao;

import es.dwes.dentista.model.Odontologo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OdontologoDAO {
    public List<Odontologo> listar() throws SQLException {
        List<Odontologo> odontologos = new ArrayList<>();
        String sql = "SELECT id_odontologo, nombre, especialidad FROM odontologos ORDER BY nombre";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                odontologos.add(new Odontologo(resultado.getInt("id_odontologo"),
                        resultado.getString("nombre"), resultado.getString("especialidad")));
            }
        }
        return odontologos;
    }
}