package fcva.dev.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import fcva.dev.models.Entrada;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;

public class PDFExporter {

    public static void exportar(Entrada entrada) {
        try {
            // Mostrar el diálogo para elegir la ruta
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Guardar entrada como PDF");
            chooser.setSelectedFile(new File("entrada_" + entrada.getCodigo() + ".pdf"));

            int seleccion = chooser.showSaveDialog(null);
            if (seleccion != JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(null, "⚠️ Exportación cancelada.");
                return;
            }

            File archivoDestino = chooser.getSelectedFile();

            // Crear el PDF
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(archivoDestino));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            Paragraph title = new Paragraph("🎟 Entrada Electrónica", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Código: " + entrada.getCodigo(), textFont));
            document.add(new Paragraph("Cliente: " + entrada.getCliente().getNombre(), textFont));
            document.add(new Paragraph("Email: " + entrada.getCliente().getEmail(), textFont));
            document.add(new Paragraph("Evento: " + entrada.getEvento().getNombre(), textFont));
            document.add(new Paragraph("Fecha: " + entrada.getEvento().getFecha(), textFont));
            document.add(new Paragraph("Lugar: " + entrada.getEvento().getLugar(), textFont));

            document.close();

            JOptionPane.showMessageDialog(null, "✅ PDF guardado en:\n" + archivoDestino.getAbsolutePath());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Error al exportar PDF:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
