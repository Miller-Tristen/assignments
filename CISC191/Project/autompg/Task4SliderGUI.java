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
