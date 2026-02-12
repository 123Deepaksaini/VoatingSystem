import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SchemaBootstrap {
    private static final Logger LOGGER = Logger.getLogger(SchemaBootstrap.class.getName());

    public static void initialize() throws SQLException {
        MyDb db = new MyDb();
        try (Connection con = db.getCon()) {
            if (con == null) {
                throw new SQLException("Database connection is null. Check MYSQL* environment variables.");
            }

            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS voter_register (" +
                    "uid BIGINT UNSIGNED NOT NULL AUTO_INCREMENT," +
                    "name VARCHAR(100) NOT NULL," +
                    "surname VARCHAR(100) NOT NULL," +
                    "voter_card_number VARCHAR(50) NOT NULL," +
                    "contact VARCHAR(20) NOT NULL," +
                    "address VARCHAR(255) NOT NULL," +
                    "dob DATE NOT NULL," +
                    "email VARCHAR(150) NOT NULL," +
                    "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "PRIMARY KEY (uid)," +
                    "UNIQUE KEY uk_voter_register_voter_card_number (voter_card_number)," +
                    "UNIQUE KEY uk_voter_register_email (email)" +
                    ") ENGINE=InnoDB"
                );

                stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS temp_voter_card_number (" +
                    "id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT," +
                    "voter_card_number VARCHAR(50) NOT NULL," +
                    "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "PRIMARY KEY (id)," +
                    "UNIQUE KEY uk_temp_voter_card_number (voter_card_number)" +
                    ") ENGINE=InnoDB"
                );

                stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS vote (" +
                    "id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT," +
                    "voter_card_number VARCHAR(50) NOT NULL," +
                    "partie VARCHAR(50) NOT NULL," +
                    "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "PRIMARY KEY (id)," +
                    "KEY idx_vote_partie (partie)," +
                    "CONSTRAINT fk_vote_voter_card " +
                    "FOREIGN KEY (voter_card_number) REFERENCES voter_register(voter_card_number) " +
                    "ON UPDATE CASCADE ON DELETE RESTRICT" +
                    ") ENGINE=InnoDB"
                );

                stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS admin_login (" +
                    "id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT," +
                    "user_name VARCHAR(100) NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "PRIMARY KEY (id)," +
                    "UNIQUE KEY uk_admin_login_user_name (user_name)" +
                    ") ENGINE=InnoDB"
                );

                stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS contact (" +
                    "id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT," +
                    "name VARCHAR(100) NOT NULL," +
                    "pnumber VARCHAR(20) NOT NULL," +
                    "email VARCHAR(150) NOT NULL," +
                    "comment TEXT NOT NULL," +
                    "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "PRIMARY KEY (id)" +
                    ") ENGINE=InnoDB"
                );

                String adminPassword = System.getenv("ADMIN_DEFAULT_PASSWORD");
                if (adminPassword == null || adminPassword.trim().isEmpty()) {
                    adminPassword = "CHANGE_ME_STRONG_PASSWORD";
                }

                stmt.executeUpdate(
                    "INSERT INTO admin_login (user_name, password) VALUES ('admin', '" +
                    adminPassword.replace("'", "''") +
                    "') ON DUPLICATE KEY UPDATE password = VALUES(password)"
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Schema bootstrap failed", e);
            throw e;
        }
    }
}
