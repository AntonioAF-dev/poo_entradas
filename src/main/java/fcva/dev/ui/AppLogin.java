package fcva.dev.ui;

import fcva.dev.dao.VendedorDAO;
import fcva.dev.dao.VendedorDAOImpl;
import fcva.dev.models.Vendedor;

import javax.swing.*;
import java.awt.*;

public class AppLogin extends JFrame {

    private final JTextField usuarioField = new JTextField();
    private final JPasswordField contrasenaField = new JPasswordField();
    private final VendedorDAO vendedorDAO = new VendedorDAOImpl();

    public AppLogin() {
        setTitle("Inicio de Sesión - Sistema de Entradas");
        setSize(400, 280);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        construirUI();
        setVisible(true);
    }

    private void construirUI() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Bienvenido al Sistema de Entradas", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        panel.add(usuarioField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        panel.add(contrasenaField, gbc);

        JButton loginBtn = crearBoton("Ingresar", new Color(39, 174, 96), Color.WHITE);
        JButton salirBtn = crearBoton("Salir", new Color(231, 76, 60), Color.WHITE);
        JButton limpiarBtn = crearBoton("Limpiar", new Color(41, 128, 185), Color.WHITE);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        botones.add(loginBtn);
        botones.add(limpiarBtn);
        botones.add(salirBtn);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(botones, gbc);

        add(panel);

        loginBtn.addActionListener(e -> iniciarSesion());
        salirBtn.addActionListener(e -> System.exit(0));
        limpiarBtn.addActionListener(e -> {
            usuarioField.setText("");
            contrasenaField.setText("");
        });
    }

    private JButton crearBoton(String texto, Color fondo, Color textoColor) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setBackground(fondo);
        boton.setForeground(textoColor);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return boton;
    }

    private void iniciarSesion() {
        String usuario = usuarioField.getText().trim();
        String contrasena = new String(contrasenaField.getPassword());

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa ambos campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Vendedor vendedor = vendedorDAO.autenticar(usuario, contrasena);

        if (vendedor != null) {
            JOptionPane.showMessageDialog(this, "Bienvenido " + vendedor.getNombre());
            dispose();
            new AppDashboard(vendedor);
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
