package fcva.dev.dao;

import fcva.dev.models.Evento;
import java.util.List;

public interface EventoDAO {
    List<Evento> obtenerTodos();
    Evento buscarPorNombre(String nombre);
    void guardar(Evento evento);
    void actualizar(Evento evento);
    void eliminar(int id);
}
