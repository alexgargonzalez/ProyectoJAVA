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

public class consultas {
    
    // Almacenamos el ID del cliente conectado actualmente (estático para ser compartido en toda la aplicación)
    private static int idClienteActual;

    public consultas(int idCliente) {
        idClienteActual = idCliente;
    }

    public consultas() {
    }
    
    public static int getIdCliente() {
        return idClienteActual;
    }
    
    public static void setIdCliente(int id) {
        idClienteActual = id;
    }
    
    public ArrayList<Product> obtenerProductos() {
        ArrayList<Product> productos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new ConexionDB().conectar(); // Usamos el método conectar()
            String sql = "SELECT id, nombre, descripcion, precio FROM product"; // Consulta SQL para obtener productos
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                Product producto = new Product(id, nombre, descripcion, precio);
                productos.add(producto); // Agregar el producto a la lista
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener productos: " + e.getMessage());
        } finally {
            ConexionDB.cerrarConexion(conn, ps, rs); // Cerramos la conexión
        }
        return productos; // Retornar la lista de productos
    }
    
    public ArrayList<Client> getClientes() {
        ArrayList<Client> usuarios = new ArrayList<>();
        
        try{
             ConexionDB db = new ConexionDB();
             Connection cn = db.conectar();
             PreparedStatement pst = cn.prepareStatement("SELECT * FROM clients");
             ResultSet rs = pst.executeQuery();
             while(rs.next()){
                 Client usuario = new Client();
                 usuario.setId(rs.getString("id"));
                 usuario.setName(rs.getString("nombre"));
                 usuario.setEmail(rs.getString("email"));
                 usuarios.add(usuario);
                 
             }
        }catch(Exception e){
            
        }

        return usuarios;
    }
    
    
    public void eliminarCliente(String nombreCliente) {
        ConexionDB db = new ConexionDB();
        String sql = "DELETE FROM clients WHERE nombre = ?";

        try (Connection conexion = db.conectar();
             PreparedStatement pst = conexion.prepareStatement(sql)) {

            pst.setString(1, nombreCliente);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cliente eliminado correctamente");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar cliente: " + e.getMessage());
        }
    }
    
    public void guardarUsuario(Client cliente){
    ConexionDB db = new ConexionDB();
    String sql = "INSERT INTO clients(nombre, password_hash, email) VALUES (?, ?, ?)";

    try (Connection conexion = db.conectar();
         PreparedStatement pst = conexion.prepareStatement(sql)) {

        pst.setString(1, cliente.getName());
        pst.setString(2, cliente.getPassword());
        pst.setString(3, cliente.getEmail());
        

        pst.executeUpdate();
        JOptionPane.showMessageDialog(null, "Guardado correctamente");

    } catch(SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al guardar: " + "el usuario ya está creado");
    }
}
    

    
    public int devolverIdCliente(String nombreCliente){
        ConexionDB db = new ConexionDB();
        int idDelCliente = -1;
        try {
            Connection cn = db.conectar();
            PreparedStatement pst = cn.prepareStatement("SELECT id FROM clients WHERE nombre = ?");
            pst.setString(1, nombreCliente);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                idDelCliente = rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idDelCliente;
    }
    
    public void consultarUsuario(String user, String pass) {
    ConexionDB db = new ConexionDB();
    String usuarioCorrecto = null;
    String passCorrecto = null;

    try {
        Connection cn = db.conectar();
        PreparedStatement pst = cn.prepareStatement("SELECT id, nombre, password_hash FROM clients WHERE nombre = ?");
        pst.setString(1, user);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            usuarioCorrecto = rs.getString("nombre");
            passCorrecto = rs.getString("password_hash");
            int clienteId = rs.getInt("id");

            if (user.equals(usuarioCorrecto) && pass.equals(passCorrecto)) {
                // Aquí asignamos el ID del cliente a la variable estática
                setIdCliente(clienteId);
                
                JOptionPane.showMessageDialog(null, "Login correcto. Bienvenido " + user);
                System.out.println("ID del cliente conectado: " + idClienteActual);
                
                Login entrar = new Login();
                Menu menu = new Menu();
                entrar.setVisible(false);
                menu.setVisible(true);
                menu.setLocationRelativeTo(null);
            } else {
                JOptionPane.showMessageDialog(null, "Contraseña incorrecta.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Usuario no encontrado.");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }   
}
    
    public void consultarAdmin(String user, String pass, String passAdmin){
        ConexionDB newConnection = new ConexionDB();
        String usuarioCorrecto = null;
        String contraseñaCorrecta = null;
        String claveCorrecta = null;
        try {
            Connection cn = newConnection.conectar();
            PreparedStatement pst = cn.prepareStatement("SELECT nombre, password_hash, password_admin FROM administrator WHERE password_admin= ? ");
            pst.setString(1, passAdmin);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                usuarioCorrecto = rs.getString("nombre");
                contraseñaCorrecta = rs.getString("password_hash");
                claveCorrecta = rs.getString("password_admin");
                if (usuarioCorrecto.equals(user) && contraseñaCorrecta.equals(pass) && claveCorrecta.equals(passAdmin)) {
                    JOptionPane.showMessageDialog(null, "Login correcto. Bienvenido Sr " + user);
                }else{
                    JOptionPane.showMessageDialog(null, "El conjunto de datos introducidos es incorrecto");
                }
            }else{
                JOptionPane.showMessageDialog(null, "Usuario no encontrado");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    public boolean eliminarProductoDelCarrito(int idCliente, int idProducto) {
    Connection conn = null;
    PreparedStatement ps = null;
    boolean resultado = false;
    
    try {
        conn = new ConexionDB().conectar();
        String sql = "DELETE FROM carrito WHERE id_cliente = ? AND id_producto = ? LIMIT 1";
        ps = conn.prepareStatement(sql);
        ps.setInt(1, idCliente);
        ps.setInt(2, idProducto);
        
        int filasAfectadas = ps.executeUpdate();
        if (filasAfectadas > 0) {
            resultado = true;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al eliminar el producto del carrito: " + e.getMessage());
    } finally {
        ConexionDB.cerrarConexion(conn, ps, null);
    }
    
    return resultado;
}
    
    public ArrayList<Product> obtenerProductosDelCarrito(int idCliente) {
    ArrayList<Product> productos = new ArrayList<>();
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        conn = new ConexionDB().conectar(); // Usamos el conectar()
        String sql = "SELECT p.id, p.nombre, p.descripcion, p.precio FROM carrito c JOIN product p ON c.id_producto = p.id WHERE c.id_cliente = ?";
        ps = conn.prepareStatement(sql);
        ps.setInt(1, idCliente);
        rs = ps.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String nombre = rs.getString("nombre");
            String descripcion = rs.getString("descripcion");
            double precio = rs.getDouble("precio");
            Product producto = new Product(id, nombre, descripcion, precio);
            productos.add(producto);
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        ConexionDB.cerrarConexion(conn, ps, rs); // Cerramos todo
    }
    return productos;
}

    // Método para agregar producto al carrito de un cliente en la base de datos
    public boolean agregarProductoAlCarrito(int idCliente, int idProducto) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean resultado = false;
        
        try {
            conn = new ConexionDB().conectar();
            String sql = "INSERT INTO carrito (id_cliente, id_producto) VALUES (?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idCliente);
            ps.setInt(2, idProducto);
            
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                resultado = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexionDB.cerrarConexion(conn, ps, null);
        }
        
        return resultado;
    }
    
}
