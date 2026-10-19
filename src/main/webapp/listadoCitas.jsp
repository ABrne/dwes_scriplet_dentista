<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Listado de citas</title>
    <link rel="stylesheet" type="text/css" href="estilos.css" />
  </head>
  <body>
    <%
      // Por defecto sólo se muestran las próximas citas (fecha >= hoy).
      // Con ?todas=1 se muestra el histórico completo.
      boolean todas = "1".equals(request.getParameter("todas"));

      Class.forName("com.mysql.cj.jdbc.Driver");
      String url = "jdbc:mysql://localhost:3306/dentista?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";

      String sql = "SELECT c.id_cita, c.fecha, c.hora, c.motivo, "
                 + "p.id_paciente, p.nombre, p.apellidos, "
                 + "o.nombre AS odontologo "
                 + "FROM cita c "
                 + "JOIN paciente p ON p.id_paciente = c.id_paciente "
                 + "JOIN odontologos o ON o.id_odontologo = c.id_odontologo "
                 + (todas ? "" : "WHERE c.fecha >= CURDATE() ")
                 + "ORDER BY c.fecha, c.hora";
    %>
    <h1><%= todas ? "Todas las citas" : "Próximas citas" %></h1>
    <%
      try (Connection conexion = DriverManager.getConnection(url, "user", "user");
           Statement s = conexion.createStatement();
           ResultSet listado = s.executeQuery(sql)) {
    %>
    <table>
      <tr>
        <th>Id cita</th>
        <th>Fecha</th>
        <th>Hora</th>
        <th>Paciente</th>
        <th>Motivo</th>
        <th>Odontólogo</th>
        <th>Acciones</th>
      </tr>
      <%
        while (listado.next()) {
          String idCita = listado.getString("id_cita");
          String hora = listado.getString("hora");
          if (hora != null && hora.length() > 5) {
            hora = hora.substring(0, 5);
          }
      %>
      <tr>
        <td><%= idCita %></td>
        <td><%= listado.getString("fecha") %></td>
        <td><%= hora %></td>
        <td><%= listado.getString("apellidos") + ", " + listado.getString("nombre") %></td>
        <td><%= listado.getString("motivo") %></td>
        <td><%= listado.getString("odontologo") %></td>
        <td>
          <form method="post" action="borraCita.jsp">
            <input type="hidden" name="id_cita" value="<%= idCita %>"/>
            <input type="submit" value="anular">
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
    <p class="error">Error al obtener el listado de citas: <%= e.getMessage() %></p>
    <%
    }
    %>
    <p>
      <% if (!todas) { %>
        <a href="listadoCitas.jsp?todas=1">Ver el histórico completo</a> |
      <% } else { %>
        <a href="listadoCitas.jsp">Ver sólo las próximas citas</a> |
      <% } %>
      <a href="formularioCita.jsp">Nueva cita</a> |
      <a href="index.jsp">Volver al inicio</a>
    </p>
  </body>
</html>
