package izv.zaidinvergeles.tienda.mysqlconnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexionDB {
    // Declaramos la conexion a MySQL
    public static Connection con;
    
    // Datos de conexión a la BD
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String user = "root";
    private static final String pass = "1234";
    private static final String url = "jdbc:mysql://localhost:3306/BD_Tienda?characterEncoding=utf8&useSSL=false&serverTimezone=UTC";
    
    // Función para conectarse a la BD
    public Connection conectar() {
        con = null;
        try {
            Class.forName(driver); // Cargar el driver
            con = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de conexión: " + e.toString());
        }
        return con;
    }
}
