package fcva.dev.models;

import java.util.UUID;

public class Entrada {
    private final String codigo;
    private final Evento evento;
    private final Cliente cliente;

    // Constructor normal (genera código aleatorio)
    public Entrada(Evento evento, Cliente cliente) {
        this.evento = evento;
        this.cliente = cliente;
        this.codigo = generarCodigo();
    }

    // Constructor adicional (usa código existente desde BD)
    public Entrada(String codigo, Evento evento, Cliente cliente) {
        this.codigo = codigo;
        this.evento = evento;
        this.cliente = cliente;
    }

    private String generarCodigo() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public String getCodigo() {
        return codigo;
    }

    public Evento getEvento() {
        return evento;
    }

    public Cliente getCliente() {
        return cliente;
    }
}
