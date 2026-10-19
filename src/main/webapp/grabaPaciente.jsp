<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Types"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Alta de paciente</title>
    <link rel="stylesheet" type="text/css" href="estilos.css" />
  </head>
  <body>
    <%
      // La codificación debe fijarse ANTES de leer cualquier parámetro.
      request.setCharacterEncoding("UTF-8");

      Class.forName("com.mysql.cj.jdbc.Driver");
      String url = "jdbc:mysql://localhost:3306/dentista?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";

      String sql = "INSERT INTO paciente "
                 + "(nombre, apellidos, dni, fecha_nacimiento, telefono, email, direccion, alergias) "
                 + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

      String nombre     = request.getParameter("nombre");
      String apellidos  = request.getParameter("apellidos");
      String dni        = request.getParameter("dni");
      String nacimiento = request.getParameter("fecha_nacimiento");
      String telefono   = request.getParameter("telefono");
      String email      = request.getParameter("email");
      String direccion  = request.getParameter("direccion");
      String alergias   = request.getParameter("alergias");

      try (Connection conexion = DriverManager.getConnection(url, "user", "user");
           PreparedStatement ps = conexion.prepareStatement(sql)) {

        ps.setString(1, nombre);
        ps.setString(2, apellidos);
        ps.setString(3, dni);

        // La fecha puede venir vacía: en ese caso se guarda NULL.
        if (nacimiento == null || nacimiento.isBlank()) {
          ps.setNull(4, Types.DATE);
        } else {
          ps.setDate(4, java.sql.Date.valueOf(nacimiento));
        }

        ps.setString(5, telefono);
        ps.setString(6, email);
        ps.setString(7, direccion);
        ps.setString(8, alergias);

        ps.executeUpdate();
    %>
    <h2>Paciente dado de alta.</h2>
    <%
    } catch (Exception e) {
    %>
    <h2 class="error">No se ha podido registrar el paciente.</h2>
    <p class="detalle-error"><%= e.getMessage() %></p>
    <%
    }
    %>
    <ul>
      <li><a href="listadoPacientes.jsp">Ver listado de pacientes</a></li>
      <li><a href="formularioPaciente.jsp">Registrar otro paciente</a></li>
      <li><a href="index.jsp">Volver al inicio</a></li>
    </ul>
  </body>
</html>
