package fcva.dev.ui;

import fcva.dev.models.Cliente;
import fcva.dev.models.Entrada;
import fcva.dev.models.Evento;
import fcva.dev.dao.EventoDAO;
import fcva.dev.dao.EventoDAOImpl;
import fcva.dev.dao.EntradaDAO;
import fcva.dev.dao.EntradaDAOImpl;
import fcva.dev.view.ClienteView;
import fcva.dev.view.EntradaView;
import fcva.dev.view.EventoView;

import java.util.List;
import java.util.Scanner;

public class EntradaController {

    private EventoDAO eventoDAO = new EventoDAOImpl();
    private EntradaDAO entradaDAO = new EntradaDAOImpl();

    private EventoView eventoView = new EventoView();
    private EntradaView entradaView = new EntradaView();
    private ClienteView clienteView = new ClienteView();

    public void iniciar() {
        Scanner scanner = new Scanner(System.in);

        List<Evento> eventos = eventoDAO.obtenerTodos();
        if (eventos.isEmpty()) {
            System.out.println("❌ No hay eventos disponibles en la base de datos.");
            return;
        }

        System.out.println("📋 Eventos disponibles:");
        for (Evento e : eventos) {
            System.out.println("- " + e.getNombre());
        }

        System.out.println("🔍 Ingrese el nombre del evento:");
        String nombreEvento = scanner.nextLine();
        Evento evento = eventoDAO.buscarPorNombre(nombreEvento);

        if (evento == null) {
            System.out.println("❌ Evento no encontrado.");
            return;
        }

        eventoView.mostrarEvento(evento);

        System.out.println("👤 Ingrese su nombre:");
        String nombreCliente = scanner.nextLine();

        System.out.println("📧 Ingrese su correo electrónico:");
        String emailCliente = scanner.nextLine();

        Cliente cliente = new Cliente(nombreCliente, emailCliente);
        clienteView.mostrarCliente(cliente);

        if (evento.getEntradasDisponibles() > 0) {
            Entrada entrada = new Entrada(evento, cliente);
            entradaDAO.guardar(entrada);
            entradaView.mostrarEntrada(entrada);

            // Actualizar disponibilidad local (opcional)
            evento.setTotalEntradas(evento.getEntradasDisponibles() - 1);
            eventoDAO.actualizar(evento);
        } else {
            System.out.println("❌ No hay entradas disponibles.");
        }
    }
}
