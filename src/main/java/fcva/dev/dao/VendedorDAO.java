package fcva.dev.dao;

import fcva.dev.models.Vendedor;

public interface VendedorDAO {
    Vendedor autenticar(String usuario, String contrasena);
}
