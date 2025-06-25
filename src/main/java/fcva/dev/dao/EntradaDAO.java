package fcva.dev.dao;

import fcva.dev.models.Entrada;
import java.util.List;

public interface EntradaDAO {
    void guardar(Entrada entrada);
    List<Entrada> obtenerTodas();
}
