package autompg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Db {
    private Db() {}

    private static String env(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.isBlank()) ? def : v;
    }

    public static String host() { return env("DB_HOST", "localhost"); }
    public static String port() { return env("DB_PORT", "3306"); }
    public static String dbName() { return env("DB_NAME", "Auto"); }
    public static String user() { return env("DB_USER", "root"); }
    public static String pass() { return env("DB_PASS", ""); }

    // connect to server
    public static Connection connectServer() throws SQLException {
        String url = "jdbc:mysql://" + host() + ":" + port()
                + "/?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
        return DriverManager.getConnection(url, user(), pass());
    }

    // for auto database
    public static Connection connectDb() throws SQLException {
        String url = "jdbc:mysql://" + host() + ":" + port() + "/" + dbName()
                + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
        return DriverManager.getConnection(url, user(), pass());
    }
}
