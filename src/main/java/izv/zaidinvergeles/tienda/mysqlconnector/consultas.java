package izv.zaidinvergeles.tienda.mysqlconnector;

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

    
    public boolean consultarUsuario(String user, String pass) {
    ConexionDB db = new ConexionDB();
    String usuarioCorrecto = null;
    String passCorrecto = null;
    boolean encontrado = false;

    try {
        Connection cn = db.conectar();
        PreparedStatement pst = cn.prepareStatement("SELECT nombre, password_hash FROM clients WHERE nombre = ?");
        pst.setString(1, user);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            
            usuarioCorrecto = rs.getString("nombre");
            passCorrecto = rs.getString("password_hash");

            if (user.equals(usuarioCorrecto) && pass.equals(passCorrecto)) {
                encontrado = true;

               
                
            } 
        } 

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }   
    return encontrado;
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


    

    
}
