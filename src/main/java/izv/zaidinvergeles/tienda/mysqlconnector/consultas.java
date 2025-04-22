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
    String sql = "INSERT INTO clients(nombre, password_hash, email, address) VALUES (?, ?, ?, ?)";

    try (Connection conexion = db.conectar();
         PreparedStatement pst = conexion.prepareStatement(sql)) {

        pst.setString(1, cliente.getName());
        pst.setString(2, cliente.getPassword());
        pst.setString(3, cliente.getEmail());
        pst.setString(4, cliente.getAddress());

        pst.executeUpdate();
        JOptionPane.showMessageDialog(null, "Guardado correctamente");

    } catch(SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al guardar: " + e.getMessage());
    }
}

    
    public void consultarUsuario(String user, String pass)
    {
        // TODO add your handling code here:
        ConexionDB db = new ConexionDB();
        // Se inicializa a null
        String usuarioCorrecto = null;
        String passCorrecto = null;
    try {

        Connection cn = db.conectar();
        PreparedStatement pst = cn.prepareStatement("SELECT nombre, clave FROM clients");
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            usuarioCorrecto = rs.getString(1);
            passCorrecto = rs.getString(2);
        }

        if (user.equals(usuarioCorrecto) && pass.equals(passCorrecto)) {
            JOptionPane.showMessageDialog(null, "Login correcto Bienvenido " + user);
        } else if (!user.equals(usuarioCorrecto) || pass.equals(passCorrecto)) {

            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error " + e);
    }
    }
    
}
