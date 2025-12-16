package autompg;

import java.sql.Connection;
import java.sql.Statement;

public class CreateSchema {
    public static void main(String[] args) throws Exception {
        try (Connection c = Db.connectServer(); Statement s = c.createStatement()) {
            s.executeUpdate("CREATE DATABASE IF NOT EXISTS " + Db.dbName());
        }

        String ddl = """
        CREATE TABLE IF NOT EXISTS auto_mpg (
            id INT AUTO_INCREMENT PRIMARY KEY,
            mpg DOUBLE,
            cylinders INT,
            displacement DOUBLE,
            horsepower INT NULL,
            weight INT,
            acceleration DOUBLE,
            model_year INT,
            origin INT,
            car_name VARCHAR(120)
        );
        """;

        try (Connection c = Db.connectDb(); Statement s = c.createStatement()) {
            s.executeUpdate(ddl);
        }

        System.out.println("Schema ready: database=" + Db.dbName() + ", table=auto_mpg");
    }
}
