package fcva.dev.controllers;

import fcva.dev.dao.EventoDAO;
import fcva.dev.dao.EventoDAOImpl;
import fcva.dev.models.Evento;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EventoController {

    private final EventoDAO eventoDAO = new EventoDAOImpl();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public EventoController() {
        dateFormat.setLenient(false);
    }

    public boolean validarFecha(String fecha) {
        try {
            Date parsedDate = dateFormat.parse(fecha);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public List<Evento> obtenerTodos() {
        return eventoDAO.obtenerTodos();
    }

    public void guardarEvento(String nombre, String fecha, String lugar, int totalEntradas) {
        Evento evento = new Evento(nombre, fecha, lugar, totalEntradas);
        eventoDAO.guardar(evento);
    }

    public void actualizarEvento(int id, String nombre, String fecha, String lugar, int totalEntradas) {
        Evento evento = new Evento(id, nombre, fecha, lugar, totalEntradas);
        eventoDAO.actualizar(evento);
    }

    public void eliminarEvento(int id) {
        eventoDAO.eliminar(id);
    }

    public List<Evento> buscarPorNombre(String filtro) {
        return eventoDAO.obtenerTodos().stream()
                .filter(e -> e.getNombre().toLowerCase().contains(filtro.toLowerCase()))
                .toList();
    }
}
