package fcva.dev.view;

import fcva.dev.models.Cliente;

public class ClienteView {
    public void mostrarCliente(Cliente cliente) {
        System.out.println("👤 Cliente: " + cliente.getNombre() + " | 📧 Email: " + cliente.getEmail());
    }
}
