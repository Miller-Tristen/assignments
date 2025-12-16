# Task

# Challenges 

# Code 
```java
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


```
```java
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
```
```java
package autompg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ImportAutoMpg {

    // handle for tab or space 
    // handle missing HP
    private static Parsed parseLine(String line) {
        line = line.trim();
        if (line.isEmpty()) return null;

        String name = null;
int firstQuote = line.indexOf('"');
int lastQuote = line.lastIndexOf('"');
String prefix = line;

        if (firstQuote >= 0 && lastQuote > firstQuote) {
name = line.substring(firstQuote + 1, lastQuote).trim();
prefix = line.substring(0, firstQuote).trim();
} else {
// fallback: last “token” is name
String[] toks = line.split("\\s+");
if (toks.length >= 9) name = toks[toks.length - 1];
}

        String[] p = prefix.split("\\s+");
if (p.length < 8) return null;

Parsed r = new Parsed();
r.mpg = Double.parseDouble(p[0]);
r.cylinders = Integer.parseInt(p[1]);
r.displacement = Double.parseDouble(p[2]);
r.horsepower = parseNullableInt(p[3]);
r.weight = Integer.parseInt(p[4]);
r.acceleration = Double.parseDouble(p[5]);
r.modelYear = Integer.parseInt(p[6]);
r.origin = Integer.parseInt(p[7]);
r.carName = (name == null) ? "" : name;
return r;
}

    private static Integer parseNullableInt(String x) {
        if (x == null) return null;
        x = x.trim();
        if (x.equalsIgnoreCase("NA") || x.equals("?") || x.isBlank()) return null;
        return Integer.parseInt(x);
    }

    private static class Parsed {
double mpg;
int cylinders;
double displacement;
Integer horsepower; // nullable
int weight;
double acceleration;
int modelYear;
int origin;
String carName;
    }

    public static void main(String[] args) throws Exception {
        String path = (args.length > 0) ? args[0] : "data/auto-mpg.data-original";

        String sql = """
INSERT INTO auto_mpg
(mpg, cylinders, displacement, horsepower, weight, acceleration, model_year, origin, car_name)
VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        int count = 0;

try (BufferedReader br = new BufferedReader(new FileReader(path));
             Connection conn = Db.connectDb();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

String line;
            while ((line = br.readLine()) != null) {
                Parsed r = parseLine(line);
                if (r == null) continue;

ps.setDouble(1, r.mpg);
ps.setInt(2, r.cylinders);
ps.setDouble(3, r.displacement);

                if (r.horsepower == null) ps.setNull(4, java.sql.Types.INTEGER);
                else ps.setInt(4, r.horsepower);

ps.setInt(5, r.weight);
ps.setDouble(6, r.acceleration);
ps.setInt(7, r.modelYear);
ps.setInt(8, r.origin);
ps.setString(9, r.carName);

ps.addBatch();
count++;

if (count % 500 == 0) {
ps.executeBatch();
conn.commit();
                }
            }

ps.executeBatch();
conn.commit();
        }

System.out.println("Imported rows: " + count);
    }
}
```
```java
package autompg;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Task3QueryGUI extends JFrame {
    private final JTextField input = new JTextField(20);
    private final JButton run = new JButton("Run");
    private final DefaultTableModel model = new DefaultTableModel();
    private final JTable table = new JTable(model);

    public Task3QueryGUI() {
        super("Task 3 - Query Auto MPG");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 600);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
top.add(new JLabel("Enter ALL or a car name search:"));
top.add(input);
top.add(run);

        model.setColumnIdentifiers(new Object[]{
                "id","mpg","cyl","disp","hp","weight","accel","year","origin","name"
        });

add(top, BorderLayout.NORTH);
add(new JScrollPane(table), BorderLayout.CENTER);

run.addActionListener(e -> runQuery());
input.addActionListener(e -> runQuery());
    }

    private void runQuery() {
        String cmd = input.getText().trim();
if (cmd.isEmpty()) return;

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override protected Void doInBackground() throws Exception {
                model.setRowCount(0);

                String sqlAll = "SELECT * FROM auto_mpg";
                String sqlName = "SELECT * FROM auto_mpg WHERE car_name LIKE ?";

                try (Connection c = Db.connectDb()) {
if (cmd.equalsIgnoreCase("ALL")) {
try (PreparedStatement ps = c.prepareStatement(sqlAll);
ResultSet rs = ps.executeQuery()) {
fill(rs);
}
                    } else {
try (PreparedStatement ps = c.prepareStatement(sqlName)) {
ps.setString(1, "%" + cmd + "%");
try (ResultSet rs = ps.executeQuery()) {
fill(rs);
                            }
                        }
                    }
                }
                return null;
            }

            @Override protected void done() {
try { get(); }
catch (Exception ex) {
JOptionPane.showMessageDialog(Task3QueryGUI.this, ex.getMessage(),
"Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void fill(ResultSet rs) throws Exception {
        while (rs.next()) {
            model.addRow(new Object[]{
rs.getInt("id"),
rs.getDouble("mpg"),
rs.getInt("cylinders"),
rs.getDouble("displacement"),
rs.getObject("horsepower"),
rs.getInt("weight"),
rs.getDouble("acceleration"),
rs.getInt("model_year"),
rs.getInt("origin"),
rs.getString("car_name")
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Task3QueryGUI().setVisible(true));
    }
}
```
```java
package autompg;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Task4SliderGUI extends JFrame {
    private final JSlider mpgSlider = new JSlider(0, 60, 20);
    private final JSlider hpSlider = new JSlider(0, 250, 80);
    private final JLabel mpgLabel = new JLabel("MPG >= 20");
    private final JLabel hpLabel = new JLabel("HP >= 80");

    private final DefaultTableModel model = new DefaultTableModel();
    private final JTable table = new JTable(model);

    public Task4SliderGUI() {
        super("Task 4 - Filter by MPG & Horsepower");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 600);

        model.setColumnIdentifiers(new Object[]{
                "id","mpg","cyl","disp","hp","weight","accel","year","origin","name"
        });

        mpgSlider.setMajorTickSpacing(10);
        mpgSlider.setPaintTicks(true);

        hpSlider.setMajorTickSpacing(50);
        hpSlider.setPaintTicks(true);

        JPanel controls = new JPanel(new GridLayout(2, 1, 8, 8));

        JPanel row1 = new JPanel(new BorderLayout());
row1.add(mpgLabel, BorderLayout.WEST);
row1.add(mpgSlider, BorderLayout.CENTER);

        JPanel row2 = new JPanel(new BorderLayout());
row2.add(hpLabel, BorderLayout.WEST);
row2.add(hpSlider, BorderLayout.CENTER);

controls.add(row1);
controls.add(row2);

add(controls, BorderLayout.NORTH);
add(new JScrollPane(table), BorderLayout.CENTER);

mpgSlider.addChangeListener(this::onSliderChange);
hpSlider.addChangeListener(this::onSliderChange);

        runFilter(); // initial load
    }

    private void onSliderChange(ChangeEvent e) {
        mpgLabel.setText("MPG >= " + mpgSlider.getValue());
        hpLabel.setText("HP >= " + hpSlider.getValue());

        // only query when user releases the slider (prevents spam)
        if (!mpgSlider.getValueIsAdjusting() && !hpSlider.getValueIsAdjusting()) {
            runFilter();
        }
    }

    private void runFilter() {
        int minMpg = mpgSlider.getValue();
        int minHp  = hpSlider.getValue();

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override protected Void doInBackground() throws Exception {
                model.setRowCount(0);

                String sql = """
                  SELECT * FROM auto_mpg
                  WHERE mpg >= ?
                    AND horsepower IS NOT NULL
                    AND horsepower >= ?
                  ORDER BY mpg DESC
                """;

                try (Connection c = Db.connectDb();
                     PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, minMpg);
                    ps.setInt(2, minHp);

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            model.addRow(new Object[]{
rs.getInt("id"),
rs.getDouble("mpg"),
rs.getInt("cylinders"),
rs.getDouble("displacement"),
rs.getObject("horsepower"),
rs.getInt("weight"),
rs.getDouble("acceleration"),
rs.getInt("model_year"),
rs.getInt("origin"),
rs.getString("car_name")
                            });
                        }
                    }
                }
                return null;
            }

            @Override protected void done() {
                try { get(); }
                catch (Exception ex) {
JOptionPane.showMessageDialog(Task4SliderGUI.this, ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
}
};
worker.execute();
    }

    public static void main(String[] args) {
SwingUtilities.invokeLater(() -> new Task4SliderGUI().setVisible(true));
    }
}
```
# Video 
