package es.dwes.dentista.web;

import es.dwes.dentista.dao.CitaDAO;
import es.dwes.dentista.dao.PacienteDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DateTimeException;
import java.util.Collections;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/citas")
public class CitaServlet extends HttpServlet {
    private final CitaDAO citaDAO = new CitaDAO();
    private final PacienteDAO pacienteDAO = new PacienteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("1".equals(request.getParameter("nuevo"))) {
            mostrarFormulario(request, response);
            return;
        }

        boolean todas = "1".equals(request.getParameter("todas"));
        request.setAttribute("todas", todas);
        try {
            request.setAttribute("citas", citaDAO.listar(todas));
        } catch (SQLException e) {
            request.setAttribute("citas", Collections.emptyList());
            request.setAttribute("error", "No se ha podido obtener el listado de citas.");
        }
        request.getRequestDispatcher("/WEB-INF/views/citas.jsp").forward(request, response);
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
        try {
            String fechaTexto = request.getParameter("fecha");
            String horaTexto = request.getParameter("hora");
            if (fechaTexto == null || fechaTexto.isBlank() || horaTexto == null || horaTexto.isBlank()) {
                throw new DateTimeException("Fecha u hora vacía");
            }
            int idPaciente = leerId(request.getParameter("id_paciente"));
            int idOdontologo = leerId(request.getParameter("id_odontologo"));
            if (idPaciente < 1 || idOdontologo < 1) {
                throw new NumberFormatException("Selección no válida");
            }
            citaDAO.insertar(LocalDate.parse(fechaTexto), LocalTime.parse(horaTexto),
                    idPaciente, idOdontologo, request.getParameter("motivo"));
            response.sendRedirect(request.getContextPath() + "/citas?resultado=creada");
        } catch (DateTimeException | NumberFormatException e) {
            request.setAttribute("error", "Indica una fecha, hora, paciente y odontólogo válidos.");
            mostrarFormulario(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "No se ha podido registrar la cita. Comprueba las selecciones realizadas.");
            mostrarFormulario(request, response);
        }
    }

    private void borrar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int idCita = leerId(request.getParameter("id_cita"));
        if (idCita < 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            citaDAO.eliminar(idCita);
            String todas = "1".equals(request.getParameter("todas")) ? "&todas=1" : "";
            response.sendRedirect(request.getContextPath() + "/citas?resultado=anulada" + todas);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("pacientes", pacienteDAO.listarOpciones());
            request.setAttribute("odontologos", citaDAO.listarOdontologos());
        } catch (SQLException e) {
            request.setAttribute("error", "No se han podido cargar pacientes y odontólogos.");
            request.setAttribute("pacientes", Collections.emptyList());
            request.setAttribute("odontologos", Collections.emptyList());
        }
        request.getRequestDispatcher("/WEB-INF/views/formulario-cita.jsp").forward(request, response);
    }

    private int leerId(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException | NullPointerException e) {
            return -1;
        }
    }
}