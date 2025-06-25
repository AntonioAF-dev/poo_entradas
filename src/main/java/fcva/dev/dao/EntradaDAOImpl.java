package fcva.dev.dao;

import fcva.dev.models.Entrada;
import fcva.dev.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntradaDAOImpl implements EntradaDAO {

    @Override
    public void guardar(Entrada entrada) {
        String sql = "INSERT INTO entradas (codigo, cliente_nombre, cliente_email, evento_nombre) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, entrada.getCodigo());
            stmt.setString(2, entrada.getCliente().getNombre());
            stmt.setString(3, entrada.getCliente().getEmail());
            stmt.setString(4, entrada.getEvento().getNombre());

            stmt.executeUpdate();
            System.out.println("✅ Entrada guardada en la base de datos.");

        } catch (SQLException e) {
            System.out.println("❌ Error al guardar entrada: " + e.getMessage());
        }
    }

    @Override
    public List<Entrada> obtenerTodas() {
        List<Entrada> entradas = new ArrayList<>();
        // No se implementa aún por falta de relación directa a objetos Evento y Cliente en DB
        return entradas;
    }
}
