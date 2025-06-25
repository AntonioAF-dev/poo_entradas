package fcva.dev.models;

public class Evento {
    private int id;
    private String nombre;
    private String fecha;
    private String lugar;
    private int totalEntradas;

    // Constructor para nuevos eventos (sin ID)
    public Evento(String nombre, String fecha, String lugar, int totalEntradas) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.lugar = lugar;
        this.totalEntradas = totalEntradas;
    }

    // Constructor para eventos con ID (editar, listar, etc.)
    public Evento(int id, String nombre, String fecha, String lugar, int totalEntradas) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.lugar = lugar;
        this.totalEntradas = totalEntradas;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public int getEntradasDisponibles() {
        return totalEntradas;
    }

    // Setters (si los necesitas)
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public void setTotalEntradas(int totalEntradas) {
        this.totalEntradas = totalEntradas;
    }
}
