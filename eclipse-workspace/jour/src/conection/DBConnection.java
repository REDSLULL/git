package conection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    static Connection connect = null;

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance(); 
            connect = DriverManager.getConnection("jdbc:mysql://localhost/journal", "root", "1111");
        } catch (Exception e) {
            System.err.println(e);
        }
        return connect;
    }
}
