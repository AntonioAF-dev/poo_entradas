package fcva.dev.controllers;

import fcva.dev.dao.EntradaDAO;
import fcva.dev.dao.EntradaDAOImpl;
import fcva.dev.dao.EventoDAO;
import fcva.dev.dao.EventoDAOImpl;
import fcva.dev.models.Cliente;
import fcva.dev.models.Entrada;
import fcva.dev.models.Evento;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;

public class EntradaController {

    private final EntradaDAO entradaDAO = new EntradaDAOImpl();
    private final EventoDAO eventoDAO = new EventoDAOImpl();

    private final Pattern emailRegex = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    public boolean validarEmail(String email) {
        return email != null && emailRegex.matcher(email).matches();
    }

    public boolean clienteYaTieneEntrada(String email, String eventoNombre) {
        return entradaDAO.obtenerTodas().stream()
                .anyMatch(e -> e.getCliente().getEmail().equalsIgnoreCase(email)
                        && e.getEvento().getNombre().equalsIgnoreCase(eventoNombre));
    }

    public Entrada comprarEntrada(String clienteNombre, String clienteEmail, String eventoNombre) {
        Evento evento = eventoDAO.buscarPorNombre(eventoNombre);
        if (evento == null || evento.getEntradasDisponibles() <= 0) {
            throw new IllegalStateException("Evento no encontrado o sin entradas disponibles.");
        }

        Cliente cliente = new Cliente(clienteNombre, clienteEmail);
        Entrada entrada = new Entrada(evento, cliente);
        entradaDAO.guardar(entrada);

        evento.setTotalEntradas(evento.getEntradasDisponibles() - 1);
        eventoDAO.actualizar(evento);

        return entrada;
    }

    public List<Entrada> obtenerEntradas() {
        return entradaDAO.obtenerTodas();
    }

    public List<Evento> obtenerEventosConEntradasDisponibles() {
        return eventoDAO.obtenerTodos().stream()
                .filter(e -> e.getEntradasDisponibles() > 0)
                .toList();
    }

    public Evento buscarEventoPorNombre(String nombre) {
        return eventoDAO.buscarPorNombre(nombre);
    }
}
