package izv.zaidinvergeles.tienda.mysqlconnector;

import izv.zaidinvergeles.tienda.Client;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class consultas {
    
    
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

    
    public void consultarUsuario(String user, String pass) {
    ConexionDB db = new ConexionDB();
    String usuarioCorrecto = null;
    String passCorrecto = null;

    try {
        Connection cn = db.conectar();
        PreparedStatement pst = cn.prepareStatement("SELECT nombre, password_hash FROM clients WHERE nombre = ?");
        pst.setString(1, user);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            usuarioCorrecto = rs.getString("nombre");
            passCorrecto = rs.getString("password_hash");

            if (user.equals(usuarioCorrecto) && pass.equals(passCorrecto)) {
                JOptionPane.showMessageDialog(null, "Login correcto. Bienvenido " + user);
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

    
}
