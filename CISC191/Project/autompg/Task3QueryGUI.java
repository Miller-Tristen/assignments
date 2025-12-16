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
