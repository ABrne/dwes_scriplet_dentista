<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Nueva cita</title>
    <link rel="stylesheet" type="text/css" href="estilos.css" />
  </head>
  <body>
    <h2>Introduzca los datos de la nueva cita:</h2>
    <%
      // Si se llega desde el listado de pacientes se preselecciona el paciente.
      String preseleccionado = request.getParameter("paciente");

      Class.forName("com.mysql.cj.jdbc.Driver");
      String url = "jdbc:mysql://localhost:3306/dentista?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
    %>
    <form method="post" action="grabaCita.jsp">
      Fecha <input type="date" name="fecha" required/><br/>
      Hora <input type="time" name="hora" required/><br/>
      Paciente
      <select name="id_paciente" required>
        <option value="">-- Seleccione un paciente --</option>
        <%
          try (Connection conexion = DriverManager.getConnection(url, "user", "user");
               Statement s = conexion.createStatement();
               ResultSet pacientes = s.executeQuery(
                     "SELECT id_paciente, apellidos, nombre FROM paciente ORDER BY apellidos, nombre")) {

            while (pacientes.next()) {
              String id = pacientes.getString("id_paciente");
              String texto = pacientes.getString("apellidos") + ", " + pacientes.getString("nombre");
              String selected = id.equals(preseleccionado) ? " selected" : "";
        %>
        <option value="<%= id %>"<%= selected %>><%= texto %></option>
        <%
            }
          }
        %>
      </select><br/>
      Odontólogo
      <select name="id_odontologo" required>
        <option value="">-- Seleccione un odontólogo --</option>
        <%
          try (Connection conexion = DriverManager.getConnection(url, "user", "user");
               Statement s = conexion.createStatement();
               ResultSet odontologos = s.executeQuery(
                     "SELECT id_odontologo, nombre, especialidad FROM odontologos ORDER BY nombre")) {

            while (odontologos.next()) {
        %>
        <option value="<%= odontologos.getString("id_odontologo") %>"><%= odontologos.getString("nombre") %>
          (<%= odontologos.getString("especialidad") %>)</option>
        <%
            }
          }
        %>
      </select><br/>
      Motivo <input type="text" name="motivo"/><br/>
      <input type="submit" value="Aceptar">
    </form>
    <p><a href="index.jsp">Volver al inicio</a></p>
  </body>
</html>
