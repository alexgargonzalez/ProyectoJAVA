package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class consultas {
    public void guardarUsuario(String usuario, String password){
        ConexionDB db = new ConexionDB();
        String sql = "insert into usuarios(nombre, clave) values ('" + usuario +"', '" + password +"');";
        Statement st;
        Connection conexion = db.conectar();
        try
        {
            st = conexion.createStatement();
            int rs = st.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "Guardado correctamente");
        }catch(SQLException e)
        {
            System.out.println(e);
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
        PreparedStatement pst = cn.prepareStatement("SELECT nombre, clave FROM usuarios");
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
