package fcva.dev.dao;

import fcva.dev.database.DatabaseConnection;
import fcva.dev.models.Evento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDAOImpl implements EventoDAO {

    @Override
    public List<Evento> obtenerTodos() {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT * FROM eventos";

        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Evento e = new Evento(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("fecha"),
                        rs.getString("lugar"),
                        rs.getInt("total_entradas")
                );
                eventos.add(e);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar eventos: " + e.getMessage());
        }

        return eventos;
    }

    @Override
    public Evento buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM eventos WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Evento(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("fecha"),
                        rs.getString("lugar"),
                        rs.getInt("total_entradas")
                );
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al buscar evento: " + e.getMessage());
        }

        return null;
    }

    @Override
    public void guardar(Evento evento) {
        String sql = "INSERT INTO eventos (nombre, fecha, lugar, total_entradas) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, evento.getNombre());
            stmt.setString(2, evento.getFecha());
            stmt.setString(3, evento.getLugar());
            stmt.setInt(4, evento.getEntradasDisponibles());

            stmt.executeUpdate();
            System.out.println("✅ Evento agregado correctamente.");

        } catch (SQLException e) {
            System.out.println("❌ Error al guardar evento: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(Evento evento) {
        String sql = "UPDATE eventos SET nombre = ?, fecha = ?, lugar = ?, total_entradas = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, evento.getNombre());
            stmt.setString(2, evento.getFecha());
            stmt.setString(3, evento.getLugar());
            stmt.setInt(4, evento.getEntradasDisponibles());
            stmt.setInt(5, evento.getId());

            stmt.executeUpdate();
            System.out.println("✅ Evento actualizado correctamente.");

        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar evento: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM eventos WHERE id = ?";

        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Evento eliminado correctamente.");

        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar evento: " + e.getMessage());
        }
    }
}
