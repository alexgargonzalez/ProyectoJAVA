package izv.zaidinvergeles.tienda.mysqlconnector;

import izv.zaidinvergeles.tienda.Carrito;
import izv.zaidinvergeles.tienda.Client;
import izv.zaidinvergeles.tienda.Login;
import izv.zaidinvergeles.tienda.Menu;
import izv.zaidinvergeles.tienda.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Clase que contiene los métodos para realizar operaciones en la base de datos
 * relacionadas con productos, clientes y carritos de compra.
 */
public class consultas {
    
    // Almacenamos el ID del cliente conectado actualmente (estático para ser compartido en toda la aplicación)
    private static int idClienteActual;

    /**
     * Constructor con parámetro para inicializar el ID del cliente
     * @param idCliente El ID del cliente a establecer
     */
    public consultas(int idCliente) {
        // Validamos que el ID no sea menor a 0
        if (idCliente < 0) {
            JOptionPane.showMessageDialog(null, "Error: Client ID cannot be negative");
            idClienteActual = 0; // Valor predeterminado o seguro
        } else {
            idClienteActual = idCliente;
        }
    }

    /**
     * Constructor vacío
     */
    public consultas() {
    }
    
    /**
     * Método para obtener el ID del cliente actual
     * @return El ID del cliente conectado actualmente
     */
    public static int getIdCliente() {
        return idClienteActual;
    }
    
    /**
     * Método para establecer el ID del cliente actual
     * @param id El nuevo ID del cliente
     */
    public static void setIdCliente(int id) {
        // Validamos que el ID no sea menor a 0
        
        idClienteActual = id;
    }
    
    /**
     * Método para obtener todos los productos de la base de datos
     * @return ArrayList con todos los productos
     */
    public ArrayList<Product> obtenerProductos() {
        ArrayList<Product> productos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Establecemos conexión a la base de datos
            conn = new ConexionDB().conectar();
            
            // Preparamos consulta SQL para obtener productos
            String sql = "SELECT id, nombre, descripcion, precio FROM product";
            ps = conn.prepareStatement(sql);
            
            // Ejecutamos la consulta
            rs = ps.executeQuery();

            // Recorremos los resultados y creamos objetos Product
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                
                // Validamos que el ID y precio no sean negativos
                if (id < 0) {
                    JOptionPane.showMessageDialog(null, "Error: A product with a negative ID was found: " + id);
                    continue; // Saltamos este producto
                }
                
                if (precio < 0) {
                    JOptionPane.showMessageDialog(null, "Error: A product with a negative price was found: " + nombre);
                    continue; // Saltamos este producto
                }
                
                // Creamos el objeto producto y lo añadimos a la lista
                Product producto = new Product(id, nombre, descripcion, precio);
                productos.add(producto);
            }
        } catch (SQLException e) {
            // Mostramos mensaje de error si hay problemas
            JOptionPane.showMessageDialog(null, "Error al obtener productos: " + e.getMessage());
        } finally {
            // Cerramos todas las conexiones para liberar recursos
            ConexionDB.cerrarConexion(conn, ps, rs);
        }
        // Retornamos la lista de productos
        return productos;
    }
    
    /**
     * Método para obtener todos los clientes de la base de datos
     * @return ArrayList con todos los clientes
     */
    public ArrayList<Client> getClientes() {
        ArrayList<Client> usuarios = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            // Creamos conexión a la base de datos
            ConexionDB db = new ConexionDB();
            conn = db.conectar();
            
            // Preparamos consulta para obtener todos los clientes
            pst = conn.prepareStatement("SELECT * FROM clients");
            rs = pst.executeQuery();
            
            // Recorremos resultados y creamos objetos Client
            while(rs.next()) {
                Client usuario = new Client();
                usuario.setId(rs.getString("id"));
                usuario.setName(rs.getString("nombre"));
                usuario.setEmail(rs.getString("email"));
                
                // Validamos que el ID no sea negativo
                try {
                    int idCliente = Integer.parseInt(usuario.getId());
                    if (idCliente < 0) {
                        JOptionPane.showMessageDialog(null, "Error: Client with negative ID found: " + idCliente);
                        continue; // Saltamos este cliente
                    }
                } catch (NumberFormatException e) {
                    // Si el ID no es un número, mostramos error
                    JOptionPane.showMessageDialog(null, "Error: Invalid client ID: " + usuario.getId());
                    continue; // Saltamos este cliente
                }
                
                usuarios.add(usuario);
            }
        } catch(Exception e) {
            // Mostramos error si hay problemas
            JOptionPane.showMessageDialog(null, "Error getting clients: " + e.getMessage());
        } finally {
            // Cerramos todas las conexiones
            ConexionDB.cerrarConexion(conn, pst, rs);
        }

        return usuarios;
    }
    
    /**
     * Método para eliminar un cliente por su nombre
     * @param nombreCliente Nombre del cliente a eliminar
     */
    public void eliminarCliente(String nombreCliente) {
        // Validamos que el nombre no esté vacío
        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: Customer name cannot be empty");
            return;
        }
        
        Connection conexion = null;
        PreparedStatement pst = null;
        
        try {
            // Establecemos conexión
            ConexionDB db = new ConexionDB();
            conexion = db.conectar();
            
            // Preparamos consulta de eliminación
            String sql = "DELETE FROM clients WHERE nombre = ?";
            pst = conexion.prepareStatement(sql);
            pst.setString(1, nombreCliente);
            
            // Ejecutamos la consulta
            int filasAfectadas = pst.executeUpdate();
            
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Successfully deleted client");
            } else {
                JOptionPane.showMessageDialog(null, "No client found to delete");
            }

        } catch (SQLException e) {
            // Mostramos error si hay problemas
            JOptionPane.showMessageDialog(null, "Error deleting client: " + e.getMessage());
        } finally {
            // Cerramos todas las conexiones
            ConexionDB.cerrarConexion(conexion, pst, null);
        }
    }
    
    /**
     * Método para guardar un nuevo cliente en la base de datos
     * @param cliente Objeto Client con los datos del cliente a guardar
     */
    public void guardarUsuario(Client cliente) {
        // Validamos que los campos no estén vacíos
        if (cliente.getName() == null || cliente.getName().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: Customer name cannot be empty");
            return;
        }
        
        if (cliente.getPassword() == null || cliente.getPassword().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: Password cannot be empty");
            return;
        }
        
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: Email cannot be empty");
            return;
        }
        
        // Validamos el formato del email (básico)
        if (!cliente.getEmail().contains("@") || !cliente.getEmail().contains(".")) {
            JOptionPane.showMessageDialog(null, "Error: The email format is invalid");
            return;
        }

        Connection conexion = null;
        PreparedStatement pst = null;
        
        try {
            // Establecemos conexión
            ConexionDB db = new ConexionDB();
            conexion = db.conectar();
            
            // Preparamos consulta de inserción
            String sql = "INSERT INTO clients(nombre, password_hash, email) VALUES (?, ?, ?)";
            pst = conexion.prepareStatement(sql);
            
            // Establecemos parámetros de la consulta
            pst.setString(1, cliente.getName());
            pst.setString(2, cliente.getPassword());
            pst.setString(3, cliente.getEmail());
            
            // Ejecutamos la consulta
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Saved successfully");

        } catch(SQLException e) {
            // Mostramos error específico para usuario duplicado
            JOptionPane.showMessageDialog(null, "Saving error: " + "the user is already created");
        } finally {
            // Cerramos todas las conexiones
            ConexionDB.cerrarConexion(conexion, pst, null);
        }
    }
    
    /**
     * Método para obtener el ID de un cliente por su nombre
     * @param nombreCliente Nombre del cliente a buscar
     * @return ID del cliente o -1 si no se encuentra
     */
    public int devolverIdCliente(String nombreCliente) {
        // Validamos que el nombre no esté vacío
        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: Customer name cannot be empty");
            return -1;
        }
        
        Connection cn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int idDelCliente = -1;
        
        try {
            // Establecemos conexión
            ConexionDB db = new ConexionDB();
            cn = db.conectar();
            
            // Preparamos consulta
            pst = cn.prepareStatement("SELECT id FROM clients WHERE nombre = ?");
            pst.setString(1, nombreCliente);
            
            // Ejecutamos consulta
            rs = pst.executeQuery();
            
            // Si hay resultados, obtenemos el ID
            if (rs.next()) {
                idDelCliente = rs.getInt("id");
                
                // Validamos que el ID no sea negativo
                
            } else {
                JOptionPane.showMessageDialog(null, "Client not found: " + nombreCliente);
            }
        } catch (Exception e) {
            // Mostramos error
            JOptionPane.showMessageDialog(null, "Error searching for client: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cerramos todas las conexiones
            ConexionDB.cerrarConexion(cn, pst, rs);
        }
        
        return idDelCliente;
    }
    
    /**
     * Método para verificar credenciales de usuario y realizar login
     * @param user Nombre de usuario
     * @param pass Contraseña del usuario
     */
   public void consultarUsuario(String user, String pass, Login loginn) {
    // Validamos que los campos no estén vacíos
    if (user == null || user.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Error: Username cannot be empty");
        return;
    }
    
    if (pass == null || pass.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Error: Password cannot be empty");
        return;
    }
       
    Connection cn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    try {
        // Establecemos conexión
        ConexionDB db = new ConexionDB();
        cn = db.conectar();
        
        // Preparamos consulta para buscar usuario
        pst = cn.prepareStatement("SELECT id, nombre, password_hash FROM clients WHERE nombre = ?");
        pst.setString(1, user);
        
        // Ejecutamos consulta
        rs = pst.executeQuery();
        // Verificamos credenciales si hay resultados
        if (rs.next()) {
            String usuarioCorrecto = rs.getString("nombre");
            String passCorrecto = rs.getString("password_hash");
            int clienteId = rs.getInt("id");
            
            // Validamos que el ID no sea negativo
            
            
            // Verificamos que usuario y contraseña coincidan
            if (user.equals(usuarioCorrecto) && pass.equals(passCorrecto)) {
                // Asignamos el ID del cliente a la variable estática
                setIdCliente(clienteId);
                
                // Mostramos mensaje de bienvenida
                JOptionPane.showMessageDialog(null, "Login successful. Welcome. " + user);
                System.out.println("ID del cliente conectado: " + idClienteActual);
                
                // Cerramos primero la ventana de login
                loginn.dispose();
                
                // Luego abrimos el menú principal (después de cerrar la ventana de login)
                Menu menu = new Menu();
                menu.setVisible(true);
                menu.setLocationRelativeTo(null);
                
                return; // Salimos del método tras login exitoso
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect password.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "User not found.");
        }
    } catch (Exception e) {
        // Mostramos error
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    } finally {
        // Cerramos todas las conexiones
        ConexionDB.cerrarConexion(cn, pst, rs);
    }   
}
    
    /**
     * Método para verificar credenciales de administrador
     * @param user Nombre de usuario administrador
     * @param pass Contraseña del administrador
     * @param passAdmin Clave especial de administrador
     * @return true si el login fue exitoso, false en caso contrario
     */
    public boolean consultarAdmin(String user, String pass, String passAdmin) {
        // Validamos que los campos no estén vacíos
        if (user == null || user.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: The admin username cannot be empty.");
            return false;
        }
        
        if (pass == null || pass.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: The admin password cannot be empty");
            return false;
        }
        
        if (passAdmin == null || passAdmin.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: The admin special key cannot be empty");
            return false;
        }
        
        Connection cn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String usuarioCorrecto = null;
        String contraseñaCorrecta = null;
        String claveCorrecta = null;
        boolean loginExitoso = false;
        
        try {
            // Establecemos conexión
            ConexionDB newConnection = new ConexionDB();
            cn = newConnection.conectar();
            
            // Preparamos consulta para administrador
            pst = cn.prepareStatement("SELECT nombre, password_hash, password_admin FROM administrator WHERE password_admin= ? ");
            pst.setString(1, passAdmin);
            
            // Ejecutamos consulta
            rs = pst.executeQuery();
            
            // Verificamos credenciales si hay resultados
            if (rs.next()) {
                usuarioCorrecto = rs.getString("nombre");
                contraseñaCorrecta = rs.getString("password_hash");
                claveCorrecta = rs.getString("password_admin");
                
                // Verificamos que todos los datos coincidan
                if (usuarioCorrecto.equals(user) && contraseñaCorrecta.equals(pass) && claveCorrecta.equals(passAdmin)) {
                    JOptionPane.showMessageDialog(null, "Login correcto. Bienvenido Sr " + user);
                    loginExitoso = true;
                } else {
                    JOptionPane.showMessageDialog(null, "The entered data set is incorrect");
                }
            } else {
                JOptionPane.showMessageDialog(null, "User not found");
            }
        } catch (Exception e) {
            // Mostramos error
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        } finally {
            // Cerramos todas las conexiones
            ConexionDB.cerrarConexion(cn, pst, rs);
        }
        
        return loginExitoso;
    }
    
    /**
     * Método para eliminar un producto del carrito de un cliente
     * @param idCliente ID del cliente propietario del carrito
     * @param idProducto ID del producto a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarProductoDelCarrito(int idCliente, int idProducto) {
        // Validamos que los IDs no sean negativos
        if (idCliente < 0) {
            JOptionPane.showMessageDialog(null, "Error: Client ID cannot be negative");
            return false;
        }
        
        if (idProducto < 0) {
            JOptionPane.showMessageDialog(null, "Error: Product ID cannot be negative");
            return false;
        }
        
        Connection conn = null;
        PreparedStatement ps = null;
        boolean resultado = false;
        
        try {
            // Establecemos conexión
            conn = new ConexionDB().conectar();
            
            // Preparamos consulta de eliminación (solo elimina una unidad)
            String sql = "DELETE FROM carrito WHERE id_cliente = ? AND id_producto = ? LIMIT 1";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idCliente);
            ps.setInt(2, idProducto);
            
            // Ejecutamos consulta y verificamos resultado
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                resultado = true;
            } else {
                JOptionPane.showMessageDialog(null, "The product was not found in the cart");
            }
        } catch (SQLException e) {
            // Mostramos error
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting product from cart: " + e.getMessage());
        } finally {
            // Cerramos todas las conexiones
            ConexionDB.cerrarConexion(conn, ps, null);
        }
        
        return resultado;
    }
    
    /**
     * Método para obtener todos los productos en el carrito de un cliente
     * @param idCliente ID del cliente propietario del carrito
     * @return ArrayList con los productos en el carrito
     */
    public ArrayList<Product> obtenerProductosDelCarrito(int idCliente) {
        // Validamos que el ID del cliente no sea negativo
        if (idCliente < 0) {
            JOptionPane.showMessageDialog(null, "Error: Client ID cannot be negative");
            return new ArrayList<>(); // Retornamos lista vacía
        }
        
        ArrayList<Product> productos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Establecemos conexión
            conn = new ConexionDB().conectar();
            
            // Preparamos consulta JOIN entre carrito y productos
            String sql = "SELECT p.id, p.nombre, p.descripcion, p.precio FROM carrito c JOIN product p ON c.id_producto = p.id WHERE c.id_cliente = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idCliente);
            
            // Ejecutamos consulta
            rs = ps.executeQuery();

            // Recorremos resultados y creamos objetos Product
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                
                // Validamos que ID y precio no sean negativos
                if (id < 0) {
                    JOptionPane.showMessageDialog(null, "Error: A product with a negative ID was found: " + id);
                    continue; // Saltamos este producto
                }
                
                if (precio < 0) {
                    JOptionPane.showMessageDialog(null, "Error: A product with a negative price was found:  " + nombre);
                    continue; // Saltamos este producto
                }
                
                // Creamos objeto producto y lo añadimos a la lista
                Product producto = new Product(id, nombre, descripcion, precio);
                productos.add(producto);
            }
        } catch (Exception e) {
            // Mostramos error
            JOptionPane.showMessageDialog(null, "Error getting products from the cart: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cerramos todas las conexiones
            ConexionDB.cerrarConexion(conn, ps, rs);
        }
        
        return productos;
    }
public String obtenerNombreClientePorId(int idCliente) {
    // Validamos que el ID no sea negativo
    if (idCliente < 0) {
        JOptionPane.showMessageDialog(null, "Error: El ID del cliente no puede ser negativo");
        return null;
    }

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        // Establecemos conexión
        conn = new ConexionDB().conectar();

        // Preparamos consulta SELECT
        String sql = "SELECT nombre FROM clients WHERE id = ?";
        ps = conn.prepareStatement(sql);
        ps.setInt(1, idCliente);

        // Ejecutamos y procesamos resultado
        rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("nombre");
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró un cliente con ID: " + idCliente);
            return null;
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error getting client name: " + e.getMessage());
        return null;
    } finally {
        ConexionDB.cerrarConexion(conn, ps, rs);
    }
}

    /**
     * Método para agregar un producto al carrito de un cliente
     * @param idCliente ID del cliente propietario del carrito
     * @param idProducto ID del producto a agregar
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean agregarProductoAlCarrito(int idCliente, int idProducto) {
        // Validamos que los IDs no sean negativos
        if (idCliente < 0) {
            JOptionPane.showMessageDialog(null, "Error: Client ID cannot be negative: ");
            return false;
        }
        
        if (idProducto < 0) {
            JOptionPane.showMessageDialog(null, "Error: Client ID cannot be negative");
            return false;
        }
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            // Establecemos conexión
            conn = new ConexionDB().conectar();
            
            // Preparamos consulta de inserción
            String sql = "INSERT INTO carrito (id_cliente, id_producto) VALUES (?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idCliente);
            ps.setInt(2, idProducto);
            
            // Ejecutamos consulta y verificamos resultado
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "The product could not be added to the cart.");
                return false;
            }
        } catch (SQLException e) {
            // Mostramos error
            JOptionPane.showMessageDialog(null, "Error al agregar producto al carrito: " + e.getMessage());
            return false;
        } finally {
            // Cerramos todas las conexiones
            ConexionDB.cerrarConexion(conn, ps, null);
        }
    }
}