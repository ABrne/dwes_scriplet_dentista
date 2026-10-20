package es.dwes.dentista.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/dentista?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
    private static final String USUARIO = "user";
    private static final String CONTRASENA = "user";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private ConexionBD() {
    }

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}