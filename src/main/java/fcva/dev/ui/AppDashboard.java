package fcva.dev.ui;

import fcva.dev.dao.EventoDAO;
import fcva.dev.dao.EventoDAOImpl;
import fcva.dev.dao.EntradaDAO;
import fcva.dev.dao.EntradaDAOImpl;
import fcva.dev.models.Cliente;
import fcva.dev.models.Entrada;
import fcva.dev.models.Evento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AppDashboard extends JFrame {

    private EventoDAO eventoDAO = new EventoDAOImpl();
    private EntradaDAO entradaDAO = new EntradaDAOImpl();

    // Componentes de Eventos
    private JTextField nombreField, fechaField, lugarField, totalEntradasField, buscarField;
    private JButton agregarBtn, editarBtn, eliminarBtn, limpiarBtn, buscarBtn;
    private JTable tablaEventos;
    private DefaultTableModel modeloTabla;
    private int eventoSeleccionadoId = -1;

    public AppDashboard() {
        setTitle("🎫 Sistema de Gestión de Entradas");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Eventos", construirPanelEventos());
        tabs.addTab("Entradas", construirPanelEntradas());
        add(tabs);
    }

    // Panel de Eventos (CRUD)
    private JPanel construirPanelEventos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 5));

        nombreField = new JTextField();
        fechaField = new JTextField();
        lugarField = new JTextField();
        totalEntradasField = new JTextField();

        formPanel.add(new JLabel("Nombre del evento:"));
        formPanel.add(nombreField);
        formPanel.add(new JLabel("Fecha (dd/mm/aaaa):"));
        formPanel.add(fechaField);
        formPanel.add(new JLabel("Lugar:"));
        formPanel.add(lugarField);
        formPanel.add(new JLabel("Total de entradas:"));
        formPanel.add(totalEntradasField);

        agregarBtn = new JButton("Agregar");
        editarBtn = new JButton("Editar");
        eliminarBtn = new JButton("Eliminar");
        limpiarBtn = new JButton("Limpiar");

        formPanel.add(agregarBtn);
        formPanel.add(editarBtn);
        formPanel.add(eliminarBtn);
        formPanel.add(limpiarBtn);

        panel.add(formPanel, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Fecha", "Lugar", "Entradas"}, 0);
        tablaEventos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaEventos);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buscarField = new JTextField(20);
        buscarBtn = new JButton("Buscar");
        bottomPanel.add(new JLabel("Buscar por nombre:"));
        bottomPanel.add(buscarField);
        bottomPanel.add(buscarBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        cargarEventos();
        conectarEventos();

        return panel;
    }

    private void cargarEventos() {
        modeloTabla.setRowCount(0);
        List<Evento> eventos = eventoDAO.obtenerTodos();
        for (Evento e : eventos) {
            modeloTabla.addRow(new Object[]{
                    e.getId(),
                    e.getNombre(),
                    e.getFecha(),
                    e.getLugar(),
                    e.getEntradasDisponibles()
            });
        }
    }

    private void conectarEventos() {
        agregarBtn.addActionListener(e -> {
            String nombre = nombreField.getText().trim();
            String fecha = fechaField.getText().trim();
            String lugar = lugarField.getText().trim();
            String totalStr = totalEntradasField.getText().trim();

            if (nombre.isEmpty() || fecha.isEmpty() || lugar.isEmpty() || totalStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos", "⚠️ Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int total;
            try {
                total = Integer.parseInt(totalStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Entradas debe ser un número", "❌ Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Evento nuevo = new Evento(nombre, fecha, lugar, total);
            eventoDAO.guardar(nuevo);
            cargarEventos();
            limpiarFormulario();
        });

        tablaEventos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaEventos.getSelectedRow() != -1) {
                int fila = tablaEventos.getSelectedRow();
                eventoSeleccionadoId = (int) modeloTabla.getValueAt(fila, 0);
                nombreField.setText(modeloTabla.getValueAt(fila, 1).toString());
                fechaField.setText(modeloTabla.getValueAt(fila, 2).toString());
                lugarField.setText(modeloTabla.getValueAt(fila, 3).toString());
                totalEntradasField.setText(modeloTabla.getValueAt(fila, 4).toString());
            }
        });

        editarBtn.addActionListener(e -> {
            if (eventoSeleccionadoId == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un evento para editar.", "⚠️ Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nombre = nombreField.getText().trim();
            String fecha = fechaField.getText().trim();
            String lugar = lugarField.getText().trim();
            String totalStr = totalEntradasField.getText().trim();

            if (nombre.isEmpty() || fecha.isEmpty() || lugar.isEmpty() || totalStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos", "⚠️ Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int total;
            try {
                total = Integer.parseInt(totalStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Entradas debe ser numérico", "❌ Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Evento editado = new Evento(eventoSeleccionadoId, nombre, fecha, lugar, total);
            eventoDAO.actualizar(editado);
            cargarEventos();
            limpiarFormulario();
        });

        eliminarBtn.addActionListener(e -> {
            if (eventoSeleccionadoId == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un evento para eliminar.", "⚠️ Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este evento?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                eventoDAO.eliminar(eventoSeleccionadoId);
                cargarEventos();
                limpiarFormulario();
            }
        });

        limpiarBtn.addActionListener(e -> limpiarFormulario());

        buscarBtn.addActionListener(e -> {
            String filtro = buscarField.getText().trim().toLowerCase();
            modeloTabla.setRowCount(0);
            List<Evento> eventos = eventoDAO.obtenerTodos();
            for (Evento ev : eventos) {
                if (ev.getNombre().toLowerCase().contains(filtro)) {
                    modeloTabla.addRow(new Object[]{
                            ev.getId(), ev.getNombre(), ev.getFecha(), ev.getLugar(), ev.getEntradasDisponibles()
                    });
                }
            }
        });
    }

    private void limpiarFormulario() {
        nombreField.setText("");
        fechaField.setText("");
        lugarField.setText("");
        totalEntradasField.setText("");
        eventoSeleccionadoId = -1;
        tablaEventos.clearSelection();
    }

    // Panel de Entradas (Venta)
    private JPanel construirPanelEntradas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<String> eventoCombo = new JComboBox<>();
        JTextField clienteNombreField = new JTextField();
        JTextField clienteEmailField = new JTextField();
        JTextArea resultadoArea = new JTextArea(6, 40);
        resultadoArea.setEditable(false);

        JButton comprarBtn = new JButton("Comprar Entrada");

        actualizarComboEventos(eventoCombo);

        formPanel.add(new JLabel("Evento:"));
        formPanel.add(eventoCombo);
        formPanel.add(new JLabel("Nombre del cliente:"));
        formPanel.add(clienteNombreField);
        formPanel.add(new JLabel("Email del cliente:"));
        formPanel.add(clienteEmailField);
        formPanel.add(new JLabel()); // espacio vacío
        formPanel.add(comprarBtn);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultadoArea), BorderLayout.CENTER);

        comprarBtn.addActionListener(e -> {
            String eventoNombre = (String) eventoCombo.getSelectedItem();
            Evento evento = eventoDAO.buscarPorNombre(eventoNombre);

            if (evento == null) {
                resultadoArea.setText("❌ Evento no encontrado.");
                return;
            }

            if (evento.getEntradasDisponibles() <= 0) {
                resultadoArea.setText("❌ No hay entradas disponibles.");
                return;
            }

            String nombre = clienteNombreField.getText().trim();
            String email = clienteEmailField.getText().trim();

            if (nombre.isEmpty() || email.isEmpty()) {
                resultadoArea.setText("⚠️ Debe completar todos los campos.");
                return;
            }

            Cliente cliente = new Cliente(nombre, email);
            Entrada entrada = new Entrada(evento, cliente);

            entradaDAO.guardar(entrada);
            evento.setTotalEntradas(evento.getEntradasDisponibles() - 1);
            eventoDAO.actualizar(evento);

            resultadoArea.setText("✅ Entrada generada:\n"
                    + "🎟️ Código: " + entrada.getCodigo()
                    + "\n👤 Cliente: " + cliente.getNombre()
                    + "\n📧 Email: " + cliente.getEmail());

            actualizarComboEventos(eventoCombo);
            cargarEventos();
        });

        return panel;
    }

    private void actualizarComboEventos(JComboBox<String> combo) {
        combo.removeAllItems();
        List<Evento> eventos = eventoDAO.obtenerTodos();
        for (Evento e : eventos) {
            if (e.getEntradasDisponibles() > 0) {
                combo.addItem(e.getNombre());
            }
        }
    }
}
