<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>Listado de pacientes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos.css">
  </head>
  <body>
    <h1>Listado de pacientes</h1>
    <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>
    <c:if test="${param.resultado == 'creado'}"><p class="nota">Paciente dado de alta.</p></c:if>
    <c:if test="${param.resultado == 'borrado'}"><p class="nota">Paciente eliminado. Sus citas asociadas también se han eliminado.</p></c:if>
    <table>
      <tr>
        <th>Id</th><th>Nombre</th><th>Apellidos</th><th>DNI</th>
        <th>Fecha nacimiento</th><th>Teléfono</th><th>Email</th>
        <th>Dirección</th><th>Alergias</th><th>Citas</th><th>Acciones</th>
      </tr>
      <c:forEach var="paciente" items="${pacientes}">
        <tr>
          <td><c:out value="${paciente.idPaciente}"/></td>
          <td><c:out value="${paciente.nombre}"/></td>
          <td><c:out value="${paciente.apellidos}"/></td>
          <td><c:out value="${paciente.dni}"/></td>
          <td><c:out value="${paciente.fechaNacimiento}"/></td>
          <td><c:out value="${paciente.telefono}"/></td>
          <td><c:out value="${paciente.email}"/></td>
          <td><c:out value="${paciente.direccion}"/></td>
          <td><c:out value="${paciente.alergias}"/></td>
          <td><c:out value="${paciente.numeroCitas}"/></td>
          <td>
            <a href="${pageContext.request.contextPath}/citas?nuevo=1&amp;id_paciente=${paciente.idPaciente}">Nueva cita</a>
            <form method="post" action="${pageContext.request.contextPath}/pacientes">
              <input type="hidden" name="accion" value="borrar">
              <input type="hidden" name="id_paciente" value="${paciente.idPaciente}">
              <button type="submit">Borrar</button>
            </form>
          </td>
        </tr>
      </c:forEach>
    </table>
    <p>
      <a href="${pageContext.request.contextPath}/pacientes?nuevo=1">Nuevo paciente</a> |
      <a href="${pageContext.request.contextPath}/">Volver al inicio</a>
    </p>
  </body>
</html>