package izv.zaidinvergeles.tienda; // Define el paquete donde se encuentra la clase PDFGenerator.

import com.itextpdf.text.*; // Importa las clases necesarias de iText para crear documentos PDF.
import com.itextpdf.text.pdf.*; // Importa las clases necesarias de iText para trabajar con PDF.

import javax.swing.*; // Importa las clases de Swing para mostrar diálogos.
import java.io.*; // Importa las clases de entrada/salida para manejar archivos.
import java.text.SimpleDateFormat; // Importa la clase para formatear fechas.
import java.util.ArrayList; // Importa la clase ArrayList para manejar listas de productos.
import java.util.Date; // Importa la clase Date para trabajar con fechas.

/**
 * Clase encargada de generar facturas en formato PDF a partir del carrito de compras.
 */
public class PDFGenerator {

    /**
     * Genera un archivo PDF con los datos de la compra y lo guarda en la carpeta "TiendaFacturas".
     * @param productos Lista de productos comprados.
     * @param idCliente ID del cliente que realiza la compra.
     * @param nombreCliente Nombre del cliente.
     * @return Ruta completa del archivo PDF generado.
     */
    public static String generarPDFCompra(ArrayList<Product> productos, int idCliente, String nombreCliente) {
        // Ruta de salida: carpeta en el escritorio del usuario
        String rutaDirectorio = System.getProperty("user.home") + File.separator + "TiendaFacturas"; // Define la ruta donde se guardará el PDF.
        File directorio = new File(rutaDirectorio); // Crea un objeto File que representa el directorio.
        if (!directorio.exists()) { // Verifica si el directorio no existe.
            directorio.mkdirs(); // Crea la carpeta si no existe.
        } // Crea carpeta si no existe

        // Nombre del archivo: Factura_ID_FECHA.pdf
        String fechaHora = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); // Formatea la fecha y hora actual.
        String nombreArchivo = "Bill_" + idCliente + "_" + fechaHora + ".pdf"; // Crea el nombre del archivo PDF.
        String rutaCompleta = rutaDirectorio + File.separator + nombreArchivo; // Define la ruta completa del archivo.

        Document documento = new Document(); // Crea un nuevo documento PDF.

        try {
            // Inicializamos escritura en el documento PDF
            PdfWriter.getInstance(documento, new FileOutputStream(rutaCompleta)); // Asocia el documento a un archivo de salida.
            documento.open(); // Abre el documento para escritura.

            // Secciones del PDF
            agregarCabecera(documento, nombreCliente, idCliente); // Agrega la cabecera con los datos del cliente.
            agregarTablaProductos(documento, productos); // Agrega la tabla con los productos comprados.
            agregarTotales(documento, productos); // Agrega la sección de totales.
            agregarPiePagina(documento); // Agrega el pie de página.

            documento.close(); // Cierra el documento.
            return rutaCompleta; // Devolvemos la ruta del archivo creado.

        } catch (DocumentException | IOException e) { // Captura excepciones relacionadas con el documento o la entrada/salida.
            JOptionPane.showMessageDialog(null, "Error generating PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error.
            e.printStackTrace(); // Imprime la traza del error en la consola.
            return null; // Devuelve null en caso de error.
        }
    }

    // --------------------------- SECCIONES DEL PDF ---------------------------

    /**
     * Agrega la cabecera con la fecha, número de factura y datos del cliente.
     */
    private static void agregarCabecera(Document doc, String nombre, int id) throws DocumentException {
        // Título principal
        Paragraph titulo = new Paragraph("PURCHASE INVOICE", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)); // Crea un párrafo con el título en negrita.
        titulo.setAlignment(Element.ALIGN_CENTER); // Alinea el título al centro.
        doc.add(titulo); // Agrega el título al documento.
        doc.add(new Paragraph(" ")); // Agrega un espacio en blanco.

        // Fecha y número de factura
        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()); // Formatea la fecha actual.
        doc.add(new Paragraph("Date: " + fecha)); // Agrega la fecha al documento.
        doc.add(new Paragraph("Nº Bill: " + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))); // Agrega el número de factura basado en la fecha y hora.
        doc.add(new Paragraph(" ")); // Agrega otro espacio en blanco.

        // Datos del cliente
        doc.add(new Paragraph("CUSTOMER DATA", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14))); // Agrega un subtítulo en negrita para los datos del cliente.
        doc.add(new Paragraph("Clent: " + nombre)); // Agrega el nombre del cliente.
        doc.add(new Paragraph("ID Client: " + id)); // Agrega el ID del cliente.
        doc.add(new Paragraph(" ")); // Agrega un espacio en blanco.
    }

    /**
     * Crea la tabla con la lista de productos.
     */
    private static void agregarTablaProductos(Document doc, ArrayList<Product> productos) throws DocumentException {
        doc.add(new Paragraph("PRODUCT DETAILS", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14))); // Agrega un subtítulo para la sección de productos.
        doc.add(new Paragraph(" ")); // Agrega un espacio en blanco.

        PdfPTable tabla = new PdfPTable(4); // Crea una tabla con 4 columnas: ID, Producto, Precio, Subtotal.
        tabla.setWidthPercentage(100); // La tabla ocupará el 100% del ancho del documento.
        tabla.setWidths(new float[]{1f, 4f, 1f, 1.5f}); // Define el ancho relativo de cada columna.

        // Encabezados
        String[] headers = {"ID", "Product", "Price", "Subtotal"}; // Define los títulos de las columnas.
        for (String texto : headers) // Para cada encabezado,
            tabla.addCell(crearCelda(texto, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12), Element.ALIGN_CENTER)); // agrega una celda con el texto centrado y fuente en negrita.

        // Filas con productos
        Font fuente = FontFactory.getFont(FontFactory.HELVETICA, 12); // Fuente normal para las filas de productos.
        for (Product p : productos) { // Itera sobre todos los productos.
            tabla.addCell(crearCelda(String.valueOf(p.getId()), fuente, Element.ALIGN_CENTER)); // Añade la celda con el ID centrado.
            tabla.addCell(crearCelda(p.getName(), fuente, Element.ALIGN_LEFT)); // Añade la celda con el nombre del producto alineado a la izquierda.
            tabla.addCell(crearCelda(String.format("%.2f €", p.getPrice()), fuente, Element.ALIGN_RIGHT)); // Añade la celda con el precio alineado a la derecha.
            tabla.addCell(crearCelda(String.format("%.2f €", p.getPrice()), fuente, Element.ALIGN_RIGHT)); // Añade la celda con el subtotal (igual al precio) alineado a la derecha.
        }

        doc.add(tabla); // Añade la tabla al documento.
    }

    /**
     * Agrega la sección de totales (Total).
     */
    private static void agregarTotales(Document doc, ArrayList<Product> productos) throws DocumentException {
        doc.add(new Paragraph(" ")); // Añade un espacio en blanco.

        // Cálculos
        double total = productos.stream().mapToDouble(Product::getPrice).sum(); // Calcula la suma total de los precios de todos los productos.

        PdfPTable tabla = new PdfPTable(2); // Crea una tabla con dos columnas para las etiquetas y valores.
        tabla.setWidthPercentage(40); // La tabla ocupará el 40% del ancho disponible.
        tabla.setHorizontalAlignment(Element.ALIGN_RIGHT); // La tabla se alinea a la derecha.
        tabla.setWidths(new float[]{1f, 1f}); // Columnas de igual ancho.

        // Filas con valores
        tabla.addCell(crearCelda("TOTAL:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14), Element.ALIGN_RIGHT, 0)); // Celda con la palabra "TOTAL" sin borde y alineada a la derecha.
        tabla.addCell(crearCelda(String.format("%.2f €", total), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14), Element.ALIGN_RIGHT, 0)); // Celda con el total calculado en formato de moneda, sin borde, alineada a la derecha.

        doc.add(tabla); // Añade la tabla de totales al documento.
    }

    /**
     * Añade una nota final de agradecimiento y la fecha de generación.
     */
    private static void agregarPiePagina(Document doc) throws DocumentException {
        doc.add(new Paragraph(" ")); // Añade espacio en blanco.
        doc.add(new Paragraph(" ")); // Añade espacio en blanco para separación visual.

        Font fuente = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10); // Fuente en cursiva, tamaño pequeño para las notas.
        Paragraph mensaje = new Paragraph("Thank you for your purchase. This document serves as proof of payment.", fuente); // Mensaje de agradecimiento al cliente.
        mensaje.setAlignment(Element.ALIGN_CENTER); // Alinea el mensaje centrado.
        doc.add(mensaje); // Añade el mensaje al documento.

        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()); // Fecha actual con formato día/mes/año horas:minutos:segundos.
        Paragraph generado = new Paragraph("Document generated on " + fecha, fuente); // Mensaje con fecha de generación del documento.
        generado.setAlignment(Element.ALIGN_CENTER); // Alinea en el centro.
        doc.add(generado); // Añade el mensaje al documento.
    }

    /**
     * Método de ayuda para crear celdas con alineación y estilo.
     */
    private static PdfPCell crearCelda(String texto, Font fuente, int alineacion) {
        return crearCelda(texto, fuente, alineacion, 1); // Llama a la sobrecarga que permite definir borde, por defecto con borde (1).
    }

    private static PdfPCell crearCelda(String texto, Font fuente, int alineacion, int borde) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente)); // Crea una celda nueva con el texto y la fuente especificada.
        celda.setHorizontalAlignment(alineacion); // Establece la alineación horizontal del texto en la celda.
        celda.setPadding(5); // Establece un relleno interno de 5 puntos para separar el texto del borde.
        celda.setBorder(borde); // Establece el estilo del borde (0 = sin borde, 1 = borde visible).
        return celda; // Devuelve la celda creada con sus configuraciones.
    }
}
