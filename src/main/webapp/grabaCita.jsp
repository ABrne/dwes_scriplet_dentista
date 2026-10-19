<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Types"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Alta de cita</title>
    <link rel="stylesheet" type="text/css" href="estilos.css" />
  </head>
  <body>
    <%
      // La codificación debe fijarse ANTES de leer cualquier parámetro.
      request.setCharacterEncoding("UTF-8");

      Class.forName("com.mysql.cj.jdbc.Driver");
      String url = "jdbc:mysql://localhost:3306/dentista?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";

      String sql = "INSERT INTO cita (fecha, hora, id_paciente, id_odontologo, motivo) "
                 + "VALUES (?, ?, ?, ?, ?)";

      String fecha        = request.getParameter("fecha");
      String hora         = request.getParameter("hora");
      String idPaciente   = request.getParameter("id_paciente");
      String idOdontologo = request.getParameter("id_odontologo");
      String motivo       = request.getParameter("motivo");

      try (Connection conexion = DriverManager.getConnection(url, "user", "user");
           PreparedStatement ps = conexion.prepareStatement(sql)) {

        ps.setDate(1, java.sql.Date.valueOf(fecha));

        // El selector de hora devuelve "HH:mm"; MySQL espera "HH:mm:ss".
        if (hora != null && hora.length() == 5) {
          hora = hora + ":00";
        }
        ps.setTime(2, java.sql.Time.valueOf(hora));

        ps.setInt(3, Integer.parseInt(idPaciente));
        ps.setInt(4, Integer.parseInt(idOdontologo));
        ps.setString(5, motivo);

        ps.executeUpdate();
    %>
    <h2>Cita registrada.</h2>
    <%
    } catch (Exception e) {
    %>
    <h2 class="error">No se ha podido registrar la cita.</h2>
    <p class="detalle-error"><%= e.getMessage() %></p>
    <%
    }
    %>
    <ul>
      <li><a href="listadoCitas.jsp">Ver próximas citas</a></li>
      <li><a href="formularioCita.jsp">Registrar otra cita</a></li>
      <li><a href="index.jsp">Volver al inicio</a></li>
    </ul>
  </body>
</html>
