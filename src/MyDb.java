import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyDb {
    private static final Logger LOGGER = Logger.getLogger(MyDb.class.getName());

    public Connection getCon() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String host = pickEnv("MYSQLHOST");
            String port = pickEnv("MYSQLPORT");
            String dbName = pickEnv("MYSQLDATABASE");
            String user = pickEnv("MYSQLUSER");
            String password = pickEnv("MYSQLPASSWORD");
            String sslMode = pickEnv("MYSQL_SSL_MODE");

            if (port == null || port.trim().isEmpty()) {
                port = "4000";
            }
            if (sslMode == null || sslMode.trim().isEmpty()) {
                sslMode = "REQUIRED";
            }

            if (isBlank(host) || isBlank(dbName) || isBlank(user) || isBlank(password)) {
                LOGGER.severe(
                    "Missing DB env vars. Required: MYSQLHOST, MYSQLDATABASE, MYSQLUSER, MYSQLPASSWORD."
                );
                return null;
            }

            String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName 
                       + "?sslMode=" + sslMode + "&allowPublicKeyRetrieval=true&serverTimezone=UTC";

            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL Driver not found", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection failed", e);
        }
        return null;
    }

    private static String pickEnv(String... keys) {
        for (String key : keys) {
            String value = System.getenv(key);
            if (!isBlank(value)) {
                return value.trim();
            }
        }
        return null;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
