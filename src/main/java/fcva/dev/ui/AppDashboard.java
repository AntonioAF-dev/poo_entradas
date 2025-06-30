package fcva.dev.ui;

import fcva.dev.controllers.EntradaController;
import fcva.dev.controllers.EventoController;
import fcva.dev.models.Entrada;
import fcva.dev.models.Evento;
import fcva.dev.models.Vendedor;
import fcva.dev.util.PDFExporter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class AppDashboard extends JFrame {

    private final EventoController eventoController = new EventoController();
    private final EntradaController entradaController = new EntradaController();
    private final Vendedor vendedor;

    private JTextField nombreField, fechaField, lugarField, totalEntradasField, buscarField;
    private DefaultTableModel modeloTabla, historialModelo;
    private JTable tablaEventos;
    private int eventoSeleccionadoId = -1;
    private JComboBox<String> eventoCombo;
    private JTextArea resultadoArea;
    private final Entrada[] entradaGenerada = {null};

    public AppDashboard(Vendedor vendedor) {
        this.vendedor = vendedor;
        setTitle("Sistema de Gestión de Entradas - Bienvenido " + vendedor.getNombre());
        setSize(950, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("control", new Color(245, 245, 245));
            UIManager.put("nimbusBase", new Color(50, 80, 140));
            UIManager.put("nimbusBlueGrey", new Color(190, 190, 190));
            UIManager.put("text", Color.BLACK);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Eventos", construirPanelEventos());
        tabs.addTab("Entradas", construirPanelEntradas());
        tabs.addTab("Historial", construirPanelHistorial());

        tabs.setBackground(new Color(230, 230, 250));
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 1) actualizarComboEventos();
            if (tabs.getSelectedIndex() == 2) actualizarHistorial();
        });

        add(tabs);
    }

    private JPanel construirPanelEventos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 5));
        nombreField = new JTextField();
        fechaField = new JTextField();
        lugarField = new JTextField();
        totalEntradasField = new JTextField();
        buscarField = new JTextField();

        JButton agregarBtn = new JButton("Agregar");
        JButton editarBtn = new JButton("Editar");
        JButton eliminarBtn = new JButton("Eliminar");
        JButton limpiarBtn = new JButton("Limpiar");

        agregarBtn.setBackground(new Color(46, 204, 113));
        agregarBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        agregarBtn.setForeground(Color.BLACK);
        editarBtn.setBackground(new Color(241, 196, 15));
        editarBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        editarBtn.setForeground(Color.BLACK);
        eliminarBtn.setBackground(new Color(231, 76, 60));
        eliminarBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        eliminarBtn.setForeground(Color.BLACK);
        limpiarBtn.setBackground(new Color(149, 165, 166));
        limpiarBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        limpiarBtn.setForeground(Color.BLACK);

        form.add(new JLabel("Nombre del evento:")); form.add(nombreField);
        form.add(new JLabel("Fecha (dd/mm/aaaa):")); form.add(fechaField);
        form.add(new JLabel("Lugar:")); form.add(lugarField);
        form.add(new JLabel("Total de entradas:")); form.add(totalEntradasField);
        form.add(agregarBtn); form.add(editarBtn);
        form.add(eliminarBtn); form.add(limpiarBtn);
        panel.add(form, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Fecha", "Lugar", "Entradas"}, 0);
        tablaEventos = new JTable(modeloTabla);
        tablaEventos.setFillsViewportHeight(true);
        tablaEventos.setRowHeight(25);
        tablaEventos.setGridColor(new Color(220, 220, 220));
        tablaEventos.setSelectionBackground(new Color(52, 152, 219));
        tablaEventos.setSelectionForeground(Color.WHITE);
        tablaEventos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaEventos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaEventos.getTableHeader().setBackground(new Color(41, 128, 185));
        tablaEventos.getTableHeader().setForeground(Color.black);
        panel.add(new JScrollPane(tablaEventos), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(5, 5));
        bottom.add(new JLabel("Buscar por nombre:"), BorderLayout.WEST);
        bottom.add(buscarField, BorderLayout.CENTER);

        panel.add(bottom, BorderLayout.SOUTH);

        buscarField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String filtro = buscarField.getText().trim();
                modeloTabla.setRowCount(0);
                List<Evento> eventos = filtro.isEmpty() ? eventoController.obtenerTodos() : eventoController.buscarPorNombre(filtro);
                for (Evento ev : eventos) {
                    modeloTabla.addRow(new Object[]{ev.getId(), ev.getNombre(), ev.getFecha(), ev.getLugar(), ev.getEntradasDisponibles()});
                }
            }
        });


        cargarEventos();

        agregarBtn.addActionListener(e -> {
            String nombre = nombreField.getText().trim();
            String fecha = fechaField.getText().trim();
            String lugar = lugarField.getText().trim();
            String totalStr = totalEntradasField.getText().trim();

            if (nombre.isEmpty() || fecha.isEmpty() || lugar.isEmpty() || totalStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos"); return;
            }
            if (!eventoController.validarFecha(fecha)) {
                JOptionPane.showMessageDialog(this, "Fecha inválida (dd/mm/aaaa)"); return;
            }
            try {
                int total = Integer.parseInt(totalStr);
                eventoController.guardarEvento(nombre, fecha, lugar, total);
                cargarEventos(); limpiarFormulario();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Entradas debe ser numérico");
            }
        });

        editarBtn.addActionListener(e -> {
            if (eventoSeleccionadoId == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un evento para editar"); return;
            }
            String nombre = nombreField.getText().trim();
            String fecha = fechaField.getText().trim();
            String lugar = lugarField.getText().trim();
            String totalStr = totalEntradasField.getText().trim();

            if (!eventoController.validarFecha(fecha)) {
                JOptionPane.showMessageDialog(this, "Fecha inválida"); return;
            }
            try {
                int total = Integer.parseInt(totalStr);
                eventoController.actualizarEvento(eventoSeleccionadoId, nombre, fecha, lugar, total);
                cargarEventos(); limpiarFormulario();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Entradas debe ser numérico");
            }
        });

        eliminarBtn.addActionListener(e -> {
            if (eventoSeleccionadoId == -1) return;
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este evento?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                eventoController.eliminarEvento(eventoSeleccionadoId);
                cargarEventos(); limpiarFormulario();
            }
        });

        limpiarBtn.addActionListener(e -> limpiarFormulario());

        tablaEventos.getSelectionModel().addListSelectionListener(e -> {
            int row = tablaEventos.getSelectedRow();
            if (row != -1) {
                eventoSeleccionadoId = (int) modeloTabla.getValueAt(row, 0);
                nombreField.setText(modeloTabla.getValueAt(row, 1).toString());
                fechaField.setText(modeloTabla.getValueAt(row, 2).toString());
                lugarField.setText(modeloTabla.getValueAt(row, 3).toString());
                totalEntradasField.setText(modeloTabla.getValueAt(row, 4).toString());
            }
        });

        return panel;
    }

    private void cargarEventos() {
        modeloTabla.setRowCount(0);
        for (Evento e : eventoController.obtenerTodos()) {
            modeloTabla.addRow(new Object[]{e.getId(), e.getNombre(), e.getFecha(), e.getLugar(), e.getEntradasDisponibles()});
        }
    }

    private void limpiarFormulario() {
        nombreField.setText(""); fechaField.setText(""); lugarField.setText(""); totalEntradasField.setText("");
        eventoSeleccionadoId = -1; tablaEventos.clearSelection();
    }

    private JButton crearBoton(String texto, Color fondo, Color textoColor) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setBackground(fondo);
        boton.setForeground(textoColor);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return boton;
    }

    private GridBagConstraints crearGbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private GridBagConstraints crearGbc(int x, int y, int width) {
        GridBagConstraints gbc = crearGbc(x, y);
        gbc.gridwidth = width;
        return gbc;
    }

    private JPanel construirPanelEntradas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        eventoCombo = new JComboBox<>();
        JTextField clienteNombre = new JTextField();
        JTextField clienteEmail = new JTextField();

        JButton comprarBtn = crearBoton("Comprar Entrada", new Color(46, 204, 113), Color.black);
        JButton exportarPDFBtn = crearBoton("Exportar PDF", new Color(52, 152, 219), Color.black);
        JButton listarClientesBtn = crearBoton("Listar por evento", new Color(155, 89, 182), Color.black);
        JButton exportarListaBtn = crearBoton("Exportar lista clientes", new Color(230, 126, 34), Color.black);

        resultadoArea = new JTextArea(8, 40);
        resultadoArea.setEditable(false);
        resultadoArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resultadoArea.setBackground(new Color(236, 240, 241));
        resultadoArea.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));

        int y = 0;
        form.add(new JLabel("Evento:"), crearGbc(0, y));
        form.add(eventoCombo, crearGbc(1, y++));
        form.add(new JLabel("Nombre del cliente:"), crearGbc(0, y));
        form.add(clienteNombre, crearGbc(1, y++));
        form.add(new JLabel("Email del cliente:"), crearGbc(0, y));
        form.add(clienteEmail, crearGbc(1, y++));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        botones.add(comprarBtn);
        botones.add(exportarPDFBtn);
        botones.add(listarClientesBtn);
        botones.add(exportarListaBtn);

        gbc.gridwidth = 2;
        form.add(botones, crearGbc(0, y++, 2));

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultadoArea), BorderLayout.CENTER);

        comprarBtn.addActionListener(e -> {
            String nombreEv = (String) eventoCombo.getSelectedItem();
            String nombreCl = clienteNombre.getText().trim();
            String emailCl = clienteEmail.getText().trim();

            if (nombreEv == null || nombreCl.isEmpty() || emailCl.isEmpty()) {
                resultadoArea.setText("Completa todos los campos."); return;
            }

            if (!entradaController.validarEmail(emailCl)) {
                resultadoArea.setText("Email inválido."); return;
            }

            if (entradaController.clienteYaTieneEntrada(emailCl, nombreEv)) {
                resultadoArea.setText("Este cliente ya tiene entrada para este evento."); return;
            }

            try {
                Entrada entrada = entradaController.comprarEntrada(nombreCl, emailCl, nombreEv);
                resultadoArea.setText("Entrada generada:\nCódigo: " + entrada.getCodigo() +
                        "\nCliente: " + entrada.getCliente().getNombre() +
                        "\nEmail: " + entrada.getCliente().getEmail());
                entradaGenerada[0] = entrada;
                actualizarComboEventos();
                cargarEventos();
                actualizarHistorial();
            } catch (Exception ex) {
                resultadoArea.setText("Error: " + ex.getMessage());
            }
        });

        exportarPDFBtn.addActionListener(e -> {
            if (entradaGenerada[0] != null) PDFExporter.exportar(entradaGenerada[0]);
        });

        listarClientesBtn.addActionListener(e -> {
            String eventoSel = (String) eventoCombo.getSelectedItem();
            if (eventoSel == null) return;
            StringBuilder sb = new StringBuilder("Clientes para evento: " + eventoSel + "\n\n");
            boolean encontrado = false;
            for (Entrada ent : entradaController.obtenerEntradas()) {
                if (ent.getEvento().getNombre().equals(eventoSel)) {
                    sb.append(ent.getCliente().getNombre()).append(" - ").append(ent.getCliente().getEmail()).append("\n");
                    encontrado = true;
                }
            }
            if (!encontrado) sb.append("No hay clientes para este evento.");
            resultadoArea.setText(sb.toString());
        });

        exportarListaBtn.addActionListener(e -> {
            String eventoSel = (String) eventoCombo.getSelectedItem();
            if (eventoSel == null) return;
            PDFExporter.exportarListaClientes(eventoSel, entradaController.obtenerEntradas());
        });

        return panel;
    }

    private JPanel construirPanelHistorial() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        historialModelo = new DefaultTableModel(new Object[]{"Código", "Cliente", "Email", "Evento"}, 0);
        JTable tabla = new JTable(historialModelo);
        tabla.setFillsViewportHeight(true);
        tabla.setRowHeight(25);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.setSelectionBackground(new Color(52, 73, 94));
        tabla.setSelectionForeground(Color.WHITE);

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    private void actualizarComboEventos() {
        eventoCombo.removeAllItems();
        for (Evento e : entradaController.obtenerEventosConEntradasDisponibles()) {
            eventoCombo.addItem(e.getNombre());
        }
    }

    private void actualizarHistorial() {
        historialModelo.setRowCount(0);
        for (Entrada entrada : entradaController.obtenerEntradas()) {
            historialModelo.addRow(new Object[]{
                    entrada.getCodigo(),
                    entrada.getCliente().getNombre(),
                    entrada.getCliente().getEmail(),
                    entrada.getEvento().getNombre()
            });
        }
    }
}
