package fcva.dev.ui;

import fcva.dev.dao.VendedorDAO;
import fcva.dev.dao.VendedorDAOImpl;
import fcva.dev.models.Vendedor;

import javax.swing.*;
import java.awt.*;

public class AppLogin extends JFrame {

    private JTextField usuarioField;
    private JPasswordField contrasenaField;
    private VendedorDAO vendedorDAO = new VendedorDAOImpl();

    public AppLogin() {
        setTitle("🔐 Inicio de Sesión - Sistema de Entradas");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        construirUI();
        setVisible(true);
    }

    private void construirUI() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        usuarioField = new JTextField();
        contrasenaField = new JPasswordField();

        panel.add(new JLabel("Usuario:"));
        panel.add(usuarioField);
        panel.add(new JLabel("Contraseña:"));
        panel.add(contrasenaField);

        JButton loginBtn = new JButton("Ingresar");
        loginBtn.addActionListener(e -> iniciarSesion());

        add(panel, BorderLayout.CENTER);
        add(loginBtn, BorderLayout.SOUTH);
    }

    private void iniciarSesion() {
        String usuario = usuarioField.getText().trim();
        String contrasena = new String(contrasenaField.getPassword());

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa ambos campos.", "⚠️ Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Vendedor vendedor = vendedorDAO.autenticar(usuario, contrasena);

        if (vendedor != null) {
            JOptionPane.showMessageDialog(this, "✅ Bienvenido " + vendedor.getNombre());
            dispose(); // cerrar login
            new AppDashboard(vendedor); // pasar vendedor autenticado
        } else {
            JOptionPane.showMessageDialog(this, "❌ Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
