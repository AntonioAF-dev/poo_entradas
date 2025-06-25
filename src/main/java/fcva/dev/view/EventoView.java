package fcva.dev.view;

import fcva.dev.models.Evento;

public class EventoView {
    public void mostrarEvento(Evento evento) {
        System.out.println("🎫 Evento: " + evento.getNombre());
        System.out.println("📅 Fecha: " + evento.getFecha());
        System.out.println("📍 Lugar: " + evento.getLugar());
        System.out.println("🎟️ Entradas disponibles: " + evento.getEntradasDisponibles());
        System.out.println("--------------------------------------------------");
    }
}
