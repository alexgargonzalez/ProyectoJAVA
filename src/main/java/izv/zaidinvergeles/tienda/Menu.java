/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package izv.zaidinvergeles.tienda;

import izv.zaidinvergeles.tienda.mysqlconnector.consultas;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Clase Menu que representa la interfaz principal de la tienda donde
 * se muestran los productos disponibles para comprar.
 * @author alfon
 */
public class Menu extends javax.swing.JFrame {

    /**
     * Lista de productos cargados desde la base de datos
     */
    private java.util.List<Product> listaProductos;
    
    /**
     * Panel donde se mostrarán los productos
     */
    private javax.swing.JPanel panelProductos;
    
    /**
     * ID del cliente que ha iniciado sesión
     */
    private int idCliente;
    
    /**
     * Objeto para realizar consultas a la base de datos
     */
    consultas consulta = new consultas();

    /**
     * Constructor de la clase Menu.
     * Inicializa los componentes, carga el ID del cliente actual,
     * los iconos y los productos desde la base de datos.
     */
    public Menu() {
        initComponents();
        // Obtenemos el ID del cliente de la clase consultas
        this.idCliente = consulta.getIdCliente();
        System.out.println("ID del cliente en Menu: " + idCliente);
        
                // Cargar los iconos para los menús
        cargarProductosDesdeBD(); // Cargar productos desde la base de datos
        mostrarProductos(); 
        jMenu1.setText(consulta.obtenerNombreClientePorId(consulta.getIdCliente()));
    }

    /**
     * Método para cargar los iconos en los menús y elementos de la interfaz.
     * Busca los recursos de imagen y los asigna a cada componente.
     */
  

    /**
     * Método para cargar los productos desde la base de datos.
     * Utiliza la clase consultas para obtener los productos.
     */
    private void cargarProductosDesdeBD() {
        listaProductos = new ArrayList<>();
        consultas consulta = new consultas();
        listaProductos = consulta.obtenerProductos(); // Obtiene los productos desde la BD
    }

    /**
     * Método para mostrar los productos en la interfaz gráfica.
     * Crea un panel para cada producto con su nombre y botón para añadir al carrito.
     */
    private void mostrarProductos() {
        // Crear un nuevo panel con layout de cuadrícula
        panelProductos = new JPanel();
        panelProductos.setLayout(new GridLayout(0, 1, 10, 10)); // Una columna, filas infinitas, espaciado 10x10

        // Recorrer la lista de productos
        for (Product producto : listaProductos) {
            // Crear un panel para cada producto
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            // Crear etiqueta con la información del producto
            JLabel lblNombre = new JLabel(producto.toString());
            
            // Crear botón para añadir el producto al carrito
            JButton btnAgregar = new JButton("Add to cart");

            // Añadir listener al botón para manejar el evento de clic
            btnAgregar.addActionListener(e -> agregarProductoAlCarrito(producto));

            // Añadir componentes al panel del producto
            panel.add(lblNombre, BorderLayout.CENTER);
            panel.add(btnAgregar, BorderLayout.EAST);
            
            // Añadir el panel del producto al panel principal
            panelProductos.add(panel);
        }

        // Limpiar el panel principal y añadir el panel de productos con scroll
        jPanel1.removeAll();
        jPanel1.setLayout(new BorderLayout());
        jPanel1.add(new JScrollPane(panelProductos), BorderLayout.CENTER);
        
        // Actualizar la interfaz
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    /**
     * Método para agregar un producto al carrito del cliente actual.
     * @param producto El producto a añadir al carrito
     */
    private void agregarProductoAlCarrito(Product producto) {
        // Obtener la instancia de consultas y el ID del cliente actual
        consultas consulta = new consultas();
        int idCliente = consulta.getIdCliente();
        
        // Guardar el producto en el carrito de la base de datos
        boolean exito = consulta.agregarProductoAlCarrito(idCliente, producto.getId());
        
        if (exito) {
            // Mostrar mensaje de confirmación con el nombre del producto
            JOptionPane.showMessageDialog(this, 
                "YOU HAVE ADDED " + producto.getName() + " TO THE CART", 
                "Added Product", 
                JOptionPane.INFORMATION_MESSAGE);
            
            System.out.println("Product added to cart: " + producto.getName());
        } else {
            // Mostrar mensaje de error si no se pudo añadir
            JOptionPane.showMessageDialog(this, 
                "Could not add " + producto.getName() + " to the cart", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        ir_Carrito = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 102, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1200, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
        );

        jMenuBar1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenuBar1.setDoubleBuffered(true);
        jMenuBar1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuBar1.setMargin(new java.awt.Insets(0, 250, 0, 0));
        jMenuBar1.setPreferredSize(new java.awt.Dimension(300, 50));

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/usuario.png"))); // NOI18N
        jMenu1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMenu1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jMenu1.setIconTextGap(20);
        jMenu1.setPreferredSize(new java.awt.Dimension(240, 40));
        jMenu1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/img/usuario.png"))); // NOI18N
        jMenuBar1.add(jMenu1);

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/carrito-de-compras.png"))); // NOI18N
        jMenu3.setText("Cart");
        jMenu3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMenu3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jMenu3.setIconTextGap(20);
        jMenu3.setPreferredSize(new java.awt.Dimension(240, 40));
        jMenu3.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/img/usuario.png"))); // NOI18N

        ir_Carrito.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/carro-de-la-carretilla.png"))); // NOI18N
        ir_Carrito.setText("Go to cart");
        ir_Carrito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ir_CarritoActionPerformed(evt);
            }
        });
        jMenu3.add(ir_Carrito);

        jMenuBar1.add(jMenu3);

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salida.png"))); // NOI18N
        jMenu4.setText("Exit");
        jMenu4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMenu4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jMenu4.setIconTextGap(20);
        jMenu4.setPreferredSize(new java.awt.Dimension(240, 40));
        jMenu4.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/img/usuario.png"))); // NOI18N

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cerrar-sesion.png"))); // NOI18N
        jMenuItem1.setText("Sing out");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem1);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir.png"))); // NOI18N
        jMenuItem2.setText("Exit");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem2);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        // Mostrar confirmación de cierre de sesión
        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to log out?",
            "Confirm logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        // Si el usuario confirma, cerramos sesión
        if (respuesta == JOptionPane.YES_OPTION) {
            // Resetear el ID del cliente conectado
            consultas.setIdCliente(-1);
            
            // Cerrar esta ventana
            this.dispose();
            
            // Abrir ventana de login
            Login ventanaLogin = new Login();
            ventanaLogin.setVisible(true);
            ventanaLogin.setLocationRelativeTo(null);
            
            JOptionPane.showMessageDialog(
                ventanaLogin,
                "You have successfully logged out",
                "Session ended",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        // Mostrar confirmación antes de salir
        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit the app?",
            "Confirm departure",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        // Si el usuario confirma, cerramos la aplicación
        if (respuesta == JOptionPane.YES_OPTION) {
            System.exit(0); // Cierra completamente la aplicación
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void ir_CarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ir_CarritoActionPerformed
        // TODO add your handling code here:
        // Ocultar la ventana actual
        this.setVisible(false);
        
        // Crear nueva instancia de la interfaz del carrito
        Interfaz_Carrito carrito = new Interfaz_Carrito();
        
        // Mostrar la interfaz del carrito
        carrito.setVisible(true);
        carrito.setLocationRelativeTo(null);
    }//GEN-LAST:event_ir_CarritoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem ir_Carrito;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
