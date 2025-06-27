package fcva.dev.dao;

import fcva.dev.database.DatabaseConnection;
import fcva.dev.models.Cliente;
import fcva.dev.models.Entrada;
import fcva.dev.models.Evento;

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
        String sql = "SELECT * FROM entradas";

        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("cliente_nombre"),
                        rs.getString("cliente_email")
                );

                Evento evento = new Evento(
                        rs.getString("evento_nombre"), "", "", 0
                );

                Entrada entrada = new Entrada(evento, cliente);
                // Sobrescribimos el código generado con el real
                java.lang.reflect.Field codigoField = Entrada.class.getDeclaredField("codigo");
                codigoField.setAccessible(true);
                codigoField.set(entrada, rs.getString("codigo"));

                entradas.add(entrada);
            }

        } catch (Exception e) {
            System.out.println("❌ Error al obtener entradas: " + e.getMessage());
        }

        return entradas;
    }
}
