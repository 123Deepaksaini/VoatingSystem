import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyDb {
    Connection con;

    public Connection getCon() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            String host = System.getenv("MYSQLHOST");
            String dbName = System.getenv("MYSQLDATABASE");
            String user = System.getenv("MYSQLUSER");
            String password = System.getenv("MYSQLPASSWORD");
            String port = System.getenv("MYSQLPORT");
            
            String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName 
                       + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            
            con = DriverManager.getConnection(url, user, password);
            
        } catch (ClassNotFoundException e) {
            Logger.getLogger(MyDb.class.getName()).log(Level.SEVERE, "MySQL Driver not found", e);
        } catch (SQLException e) {
            Logger.getLogger(MyDb.class.getName()).log(Level.SEVERE, "Database connection failed", e);
        }
        return con;
    }
}
