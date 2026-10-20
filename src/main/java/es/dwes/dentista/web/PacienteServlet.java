package es.dwes.dentista.web;

import es.dwes.dentista.dao.PacienteDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/pacientes")
public class PacienteServlet extends HttpServlet {
    private final PacienteDAO pacienteDAO = new PacienteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("1".equals(request.getParameter("nuevo"))) {
            request.getRequestDispatcher("/WEB-INF/views/formulario-paciente.jsp").forward(request, response);
            return;
        }

        try {
            request.setAttribute("pacientes", pacienteDAO.listar());
        } catch (SQLException e) {
            request.setAttribute("error", "No se ha podido obtener el listado de pacientes.");
        }
        request.getRequestDispatcher("/WEB-INF/views/pacientes.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");
        if ("guardar".equals(accion)) {
            guardar(request, response);
        } else if ("borrar".equals(accion)) {
            borrar(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void guardar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = parametro(request, "nombre");
        String apellidos = parametro(request, "apellidos");
        String dni = parametro(request, "dni");
        String nacimiento = parametro(request, "fecha_nacimiento");

        if (nombre.isBlank() || apellidos.isBlank() || dni.isBlank()) {
            request.setAttribute("error", "Nombre, apellidos y DNI son obligatorios.");
            mostrarFormulario(request, response);
            return;
        }

        try {
            LocalDate fecha = nacimiento.isBlank() ? null : LocalDate.parse(nacimiento);
            pacienteDAO.insertar(nombre, apellidos, dni, fecha,
                    parametro(request, "telefono"), parametro(request, "email"),
                    parametro(request, "direccion"), parametro(request, "alergias"));
            response.sendRedirect(request.getContextPath() + "/pacientes?resultado=creado");
        } catch (DateTimeParseException e) {
            request.setAttribute("error", "La fecha de nacimiento no es válida.");
            mostrarFormulario(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "No se ha podido registrar el paciente. Comprueba que el DNI no esté repetido.");
            mostrarFormulario(request, response);
        }
    }

    private void borrar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int idPaciente = leerId(request.getParameter("id_paciente"));
        if (idPaciente < 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            pacienteDAO.eliminar(idPaciente);
            response.sendRedirect(request.getContextPath() + "/pacientes?resultado=borrado");
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/formulario-paciente.jsp").forward(request, response);
    }

    private String parametro(HttpServletRequest request, String nombre) {
        String valor = request.getParameter(nombre);
        return valor == null ? "" : valor.trim();
    }

    private int leerId(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException | NullPointerException e) {
            return -1;
        }
    }
}