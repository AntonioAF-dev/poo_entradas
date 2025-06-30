package fcva.dev.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import fcva.dev.models.Entrada;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class PDFExporter {

    public static void exportar(Entrada entrada) {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Guardar entrada como PDF");
            chooser.setSelectedFile(new File("entrada_" + entrada.getCodigo() + ".pdf"));

            int seleccion = chooser.showSaveDialog(null);
            if (seleccion != JFileChooser.APPROVE_OPTION) return;

            File archivoDestino = chooser.getSelectedFile();

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(archivoDestino));
            document.open();

            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font textoFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            Paragraph titulo = new Paragraph("🎟 Entrada Electrónica", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Código: " + entrada.getCodigo(), textoFont));
            document.add(new Paragraph("Cliente: " + entrada.getCliente().getNombre(), textoFont));
            document.add(new Paragraph("Email: " + entrada.getCliente().getEmail(), textoFont));
            document.add(new Paragraph("Evento: " + entrada.getEvento().getNombre(), textoFont));
            document.add(new Paragraph("Fecha: " + entrada.getEvento().getFecha(), textoFont));
            document.add(new Paragraph("Lugar: " + entrada.getEvento().getLugar(), textoFont));

            document.close();

            JOptionPane.showMessageDialog(null, "✅ PDF guardado en:\n" + archivoDestino.getAbsolutePath());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Error al exportar PDF:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // NUEVO MÉTODO
    public static void exportarListaClientes(String eventoNombre, List<Entrada> entradas) {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Guardar lista de clientes");
            chooser.setSelectedFile(new File("clientes_" + eventoNombre.replaceAll(" ", "_") + ".pdf"));

            int seleccion = chooser.showSaveDialog(null);
            if (seleccion != JFileChooser.APPROVE_OPTION) return;

            File archivo = chooser.getSelectedFile();

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(archivo));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("📋 Lista de clientes para: " + eventoNombre, titleFont));
            document.add(Chunk.NEWLINE);

            boolean encontrado = false;
            for (Entrada entrada : entradas) {
                if (entrada.getEvento().getNombre().equals(eventoNombre)) {
                    document.add(new Paragraph("👤 " + entrada.getCliente().getNombre() +
                            " - " + entrada.getCliente().getEmail(), textFont));
                    encontrado = true;
                }
            }

            if (!encontrado) {
                document.add(new Paragraph("❌ No se encontraron clientes para este evento.", textFont));
            }

            document.close();

            JOptionPane.showMessageDialog(null, "✅ Lista exportada con éxito.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Error al exportar lista: " + e.getMessage());
        }
    }
}
