<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>Nueva cita</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos.css">
  </head>
  <body>
    <h1>Alta de cita</h1>
    <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>
    <form method="post" action="${pageContext.request.contextPath}/citas">
      <input type="hidden" name="accion" value="guardar">
      Fecha <input type="date" name="fecha" value="<c:out value='${param.fecha}'/>" required><br>
      Hora <input type="time" name="hora" value="<c:out value='${param.hora}'/>" required><br>
      Paciente
      <select name="id_paciente" required>
        <option value="">-- Seleccione un paciente --</option>
        <c:forEach var="paciente" items="${pacientes}">
          <option value="${paciente.idPaciente}" <c:if test="${param.id_paciente == paciente.idPaciente}">selected</c:if>>
            <c:out value="${paciente.apellidos}"/>, <c:out value="${paciente.nombre}"/>
          </option>
        </c:forEach>
      </select><br>
      Odontólogo
      <select name="id_odontologo" required>
        <option value="">-- Seleccione un odontólogo --</option>
        <c:forEach var="odontologo" items="${odontologos}">
          <option value="${odontologo.idOdontologo}" <c:if test="${param.id_odontologo == odontologo.idOdontologo}">selected</c:if>>
            <c:out value="${odontologo.nombre}"/> (<c:out value="${odontologo.especialidad}"/>)
          </option>
        </c:forEach>
      </select><br>
      Motivo <input type="text" name="motivo" value="<c:out value='${param.motivo}'/>"><br>
      <button type="submit">Aceptar</button>
    </form>
    <p><a href="${pageContext.request.contextPath}/">Volver al inicio</a></p>
  </body>
</html>