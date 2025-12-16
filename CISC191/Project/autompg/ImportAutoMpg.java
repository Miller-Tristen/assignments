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
