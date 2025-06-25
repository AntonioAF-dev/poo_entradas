package fcva.dev.view;

import fcva.dev.models.Entrada;

public class EntradaView {
    public void mostrarEntrada(Entrada entrada) {
        System.out.println("✅ Entrada generada con éxito:");
        System.out.println("🎟️ Código: " + entrada.getCodigo());
        System.out.println("🎫 Evento: " + entrada.getEvento().getNombre());
        System.out.println("👤 Cliente: " + entrada.getCliente().getNombre());
        System.out.println("--------------------------------------------------");
    }
}
