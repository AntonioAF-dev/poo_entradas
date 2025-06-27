package fcva.dev.dao;

import fcva.dev.database.DatabaseConnection;
import fcva.dev.models.Vendedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VendedorDAOImpl implements VendedorDAO {

    @Override
    public Vendedor autenticar(String usuario, String contrasena) {
        String sql = "SELECT * FROM vendedores WHERE usuario = ? AND contrasena = ?";

        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Vendedor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("usuario"),
                        rs.getString("contrasena")
                );
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al autenticar vendedor: " + e.getMessage());
        }

        return null;
    }
}
