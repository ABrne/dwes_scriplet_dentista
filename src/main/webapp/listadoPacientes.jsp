<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Listado de pacientes</title>
    <link rel="stylesheet" type="text/css" href="estilos.css" />
  </head>
  <body>
    <h1>Listado de pacientes</h1>
    <%
      Class.forName("com.mysql.cj.jdbc.Driver");
      String url = "jdbc:mysql://localhost:3306/dentista?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";

      String sql = "SELECT p.id_paciente, p.nombre, p.apellidos, p.dni, p.fecha_nacimiento, "
                 + "p.telefono, p.email, p.direccion, p.alergias, "
                 + "(SELECT COUNT(*) FROM cita c WHERE c.id_paciente = p.id_paciente) AS num_citas "
                 + "FROM paciente p ORDER BY p.apellidos, p.nombre";

      try (Connection conexion = DriverManager.getConnection(url, "user", "user");
           Statement s = conexion.createStatement();
           ResultSet listado = s.executeQuery(sql)) {
    %>
    <table>
      <tr>
        <th>Id</th>
        <th>Nombre</th>
        <th>Apellidos</th>
        <th>DNI</th>
        <th>Fecha nacimiento</th>
        <th>Teléfono</th>
        <th>Email</th>
        <th>Dirección</th>
        <th>Alergias</th>
        <th>Citas</th>
        <th>Acciones</th>
      </tr>
      <%
        while (listado.next()) {
          String id = listado.getString("id_paciente");
          String fechaNacimiento = listado.getString("fecha_nacimiento");
          String alergias = listado.getString("alergias");
          if (alergias == null) {
            alergias = "";
          }
      %>
      <tr>
        <td><%= id %></td>
        <td><%= listado.getString("nombre") %></td>
        <td><%= listado.getString("apellidos") %></td>
        <td><%= listado.getString("dni") %></td>
        <td><%= (fechaNacimiento == null ? "" : fechaNacimiento) %></td>
        <td><%= listado.getString("telefono") %></td>
        <td><%= listado.getString("email") %></td>
        <td><%= listado.getString("direccion") %></td>
        <td><%= alergias %></td>
        <td><%= listado.getString("num_citas") %></td>
        <td>
          <a href="formularioCita.jsp?paciente=<%= id %>">Nueva cita</a>
          <form method="post" action="borraPaciente.jsp">
            <input type="hidden" name="id_paciente" value="<%= id %>"/>
            <input type="submit" value="borrar">
          </form>
        </td>
      </tr>
      <%
        }
    %>
    </table>
    <%
    } catch (Exception e) {
    %>
    <p class="error">Error al obtener el listado de pacientes: <%= e.getMessage() %></p>
    <%
    }
    %>
    <p><a href="index.jsp">Volver al inicio</a></p>
  </body>
</html>
