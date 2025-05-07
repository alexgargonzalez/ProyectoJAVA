package izv.zaidinvergeles.tienda;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

/**
 * Clase para generar facturas en formato PDF para el carrito de compras
 */
public class PDFGenerator {

    public static String generarPDFCompra(ArrayList<Product> productos, int idCliente, String nombreCliente) {
        String directorioUsuario = System.getProperty("user.home");
        String rutaDirectorio = directorioUsuario + File.separator + "TiendaFacturas";
        File directorio = new File(rutaDirectorio);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fechaHora = formatoFecha.format(new Date());
        String nombreArchivo = "Factura_" + idCliente + "_" + fechaHora + ".pdf";
        String rutaCompleta = rutaDirectorio + File.separator + nombreArchivo;

        Document documento = new Document();

        try {
            PdfWriter.getInstance(documento, new FileOutputStream(rutaCompleta));
            documento.open();

            agregarCabecera(documento, nombreCliente, idCliente);
            agregarTablaProductos(documento, productos);
            agregarTotales(documento, productos);
            agregarPiePagina(documento);

            documento.close();
            return rutaCompleta;

        } catch (DocumentException | IOException e) {
            JOptionPane.showMessageDialog(null, "Error al generar el PDF: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }

    private static void agregarCabecera(Document documento, String nombreCliente, int idCliente) throws DocumentException {
        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
        Paragraph titulo = new Paragraph("FACTURA DE COMPRA", fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);

        documento.add(new Paragraph(" "));

        Font fuenteNormal = FontFactory.getFont(FontFactory.HELVETICA, 12);
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String fechaActual = formato.format(new Date());

        Paragraph fechaParrafo = new Paragraph("Fecha: " + fechaActual, fuenteNormal);
        documento.add(fechaParrafo);

        SimpleDateFormat formatoFactura = new SimpleDateFormat("yyyyMMddHHmmss");
        String numeroFactura = formatoFactura.format(new Date());
        Paragraph facturaParrafo = new Paragraph("Nº Factura: " + numeroFactura, fuenteNormal);
        documento.add(facturaParrafo);

        documento.add(new Paragraph(" "));
        Font fuenteSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        documento.add(new Paragraph("DATOS DEL CLIENTE", fuenteSubtitulo));
        documento.add(new Paragraph("Cliente: " + nombreCliente, fuenteNormal));
        documento.add(new Paragraph("ID Cliente: " + idCliente, fuenteNormal));
        documento.add(new Paragraph(" "));
    }

    private static void agregarTablaProductos(Document documento, ArrayList<Product> productos) throws DocumentException {
        Font fuenteSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        documento.add(new Paragraph("DETALLE DE PRODUCTOS", fuenteSubtitulo));
        documento.add(new Paragraph(" "));

        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidthPercentage(100);
        float[] anchos = {1f, 4f, 1f, 1.5f};
        tabla.setWidths(anchos);

        Font fuenteEncabezado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

        String[] encabezados = {"ID", "Producto", "Precio", "Subtotal"};
        for (String encabezado : encabezados) {
            PdfPCell celda = new PdfPCell(new Phrase(encabezado, fuenteEncabezado));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setPadding(5);
            tabla.addCell(celda);
        }

        Font fuenteContenido = FontFactory.getFont(FontFactory.HELVETICA, 12);

        for (Product producto : productos) {
            tabla.addCell(crearCelda(String.valueOf(producto.getId()), fuenteContenido, Element.ALIGN_CENTER));
            tabla.addCell(crearCelda(producto.getName(), fuenteContenido, Element.ALIGN_LEFT));
            tabla.addCell(crearCelda(String.format("%.2f €", producto.getPrice()), fuenteContenido, Element.ALIGN_RIGHT));
            tabla.addCell(crearCelda(String.format("%.2f €", producto.getPrice()), fuenteContenido, Element.ALIGN_RIGHT));
        }

        documento.add(tabla);
    }

    private static void agregarTotales(Document documento, ArrayList<Product> productos) throws DocumentException {
        documento.add(new Paragraph(" "));

        double total = productos.stream().mapToDouble(Product::getPrice).sum();
        double baseImponible = total / 1.21;
        double iva = total - baseImponible;

        PdfPTable tablaTotal = new PdfPTable(2);
        tablaTotal.setWidthPercentage(40);
        tablaTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tablaTotal.setWidths(new float[]{1f, 1f});

        Font fuenteEncabezado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font fuenteContenido = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Font fuenteTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);

        tablaTotal.addCell(crearCelda("Base Imponible:", fuenteEncabezado, Element.ALIGN_RIGHT, 0));
        tablaTotal.addCell(crearCelda(String.format("%.2f €", baseImponible), fuenteContenido, Element.ALIGN_RIGHT, 0));

        tablaTotal.addCell(crearCelda("IVA (21%):", fuenteEncabezado, Element.ALIGN_RIGHT, 0));
        tablaTotal.addCell(crearCelda(String.format("%.2f €", iva), fuenteContenido, Element.ALIGN_RIGHT, 0));

        tablaTotal.addCell(crearCelda("TOTAL:", fuenteTotal, Element.ALIGN_RIGHT, 0));
        tablaTotal.addCell(crearCelda(String.format("%.2f €", total), fuenteTotal, Element.ALIGN_RIGHT, 0));

        documento.add(tablaTotal);
    }

    private static void agregarPiePagina(Document documento) throws DocumentException {
        documento.add(new Paragraph(" "));
        documento.add(new Paragraph(" "));

        Font fuentePie = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10);
        Paragraph pie = new Paragraph("Gracias por su compra. Este documento sirve como comprobante de pago.", fuentePie);
        pie.setAlignment(Element.ALIGN_CENTER);
        documento.add(pie);

        Paragraph fecha = new Paragraph("Documento generado el " +
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), fuentePie);
        fecha.setAlignment(Element.ALIGN_CENTER);
        documento.add(fecha);
    }

    private static PdfPCell crearCelda(String texto, Font fuente, int alineacion) {
        return crearCelda(texto, fuente, alineacion, 1);
    }

    private static PdfPCell crearCelda(String texto, Font fuente, int alineacion, int borde) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setHorizontalAlignment(alineacion);
        celda.setPadding(5);
        celda.setBorder(borde);
        return celda;
    }
}
