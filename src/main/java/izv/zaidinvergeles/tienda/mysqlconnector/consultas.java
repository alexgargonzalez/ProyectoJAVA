package izv.zaidinvergeles.tienda.mysqlconnector;

import izv.zaidinvergeles.tienda.Client;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class consultas {
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
    
    public void guardarAdmin(){
        ConexionDB newConnection = new ConexionDB();
        
    }

    
}
