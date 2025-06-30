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

            if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;
            File destino = chooser.getSelectedFile();

            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(destino));
            doc.open();

            Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font texto = FontFactory.getFont(FontFactory.HELVETICA, 12);

            Paragraph encabezado = new Paragraph("Entrada Electrónica", titulo);
            encabezado.setAlignment(Element.ALIGN_CENTER);
            doc.add(encabezado);
            doc.add(Chunk.NEWLINE);

            doc.add(new Paragraph("Código: " + entrada.getCodigo(), texto));
            doc.add(new Paragraph("Cliente: " + entrada.getCliente().getNombre(), texto));
            doc.add(new Paragraph("Email: " + entrada.getCliente().getEmail(), texto));
            doc.add(new Paragraph("Evento: " + entrada.getEvento().getNombre(), texto));
            doc.add(new Paragraph("Fecha: " + entrada.getEvento().getFecha(), texto));
            doc.add(new Paragraph("Lugar: " + entrada.getEvento().getLugar(), texto));

            doc.close();

            JOptionPane.showMessageDialog(null, "PDF guardado en:\n" + destino.getAbsolutePath());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al exportar PDF:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void exportarListaClientes(String eventoNombre, List<Entrada> entradas) {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Guardar lista de clientes");
            chooser.setSelectedFile(new File("clientes_" + eventoNombre.replaceAll(" ", "_") + ".pdf"));

            if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;
            File archivo = chooser.getSelectedFile();

            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(archivo));
            doc.open();

            Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font texto = FontFactory.getFont(FontFactory.HELVETICA, 12);

            doc.add(new Paragraph("Lista de clientes para: " + eventoNombre, titulo));
            doc.add(Chunk.NEWLINE);

            boolean hayClientes = false;
            for (Entrada entrada : entradas) {
                if (entrada.getEvento().getNombre().equals(eventoNombre)) {
                    doc.add(new Paragraph(entrada.getCliente().getNombre() + " - " + entrada.getCliente().getEmail(), texto));
                    hayClientes = true;
                }
            }

            if (!hayClientes) {
                doc.add(new Paragraph("No se encontraron clientes para este evento.", texto));
            }

            doc.close();

            JOptionPane.showMessageDialog(null, "Lista exportada con éxito");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al exportar lista: " + e.getMessage());
        }
    }
}
