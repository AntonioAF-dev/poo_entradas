package fcva.dev.models;

import java.util.UUID;

public class Entrada {
    private String codigo;
    private Evento evento;
    private Cliente cliente;

    public Entrada(Evento evento, Cliente cliente) {
        this.evento = evento;
        this.cliente = cliente;
        this.codigo = generarCodigo();
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
