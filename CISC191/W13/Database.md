# Task
Write a code to access and manipulate databases using MySQL J connector
# Challenges
Setting up the datatbase connection correctly. Also matching the code to the tables correctly can be troublesome.
# Flow Chart 
<img width="271" height="585" alt="image" src="https://github.com/user-attachments/assets/e1fce545-f438-493e-8366-fba3ed884d8c" />

# Our Code
```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentDatabaseLab {

private static final String DB_URL = "jdbc:mysql://localhost:3306/Miramar";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "password";

public static void main(String[] args) {

try {

Class.forName("com.mysql.cj.jdbc.Driver");

} catch (ClassNotFoundException e) {

System.out.println("MySQL JDBC driver not found. Make sure the .jar is on the classpath.");
e.printStackTrace();
return;
}

try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
System.out.println("Connected to database.");

insertStudent(conn);

updateStudentZip(conn);

System.out.println("Done.");
} catch (SQLException e) {
System.out.println("Database error occurred.");
e.printStackTrace();
}
}

private static void insertStudent(Connection conn) throws SQLException {

String sql = "INSERT INTO Student " +
"(ssn, firstName, middleName, lastName, dob, street, phone, zipcode, deptID) " +
"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

try (PreparedStatement stmt = conn.prepareStatement(sql)) {

stmt.setString(1, "111222333");
stmt.setString(2, "Philip");
stmt.setString(3, "David Charles");
stmt.setString(4, "Collins");
stmt.setString(5, "1951-01-30");
stmt.setString(6, "NA");
stmt.setString(7, "NA");
stmt.setString(8, "NA");
stmt.setInt(9, 1234);

int rows = stmt.executeUpdate(); // actually runs the INSERT
System.out.println("Insert complete. Rows affected: " + rows);
}
}

private static void updateStudentZip(Connection conn) throws SQLException {

String sql = "UPDATE Student SET zipcode = ? WHERE ssn = ?";

try (PreparedStatement stmt = conn.prepareStatement(sql)) {
stmt.setString(1, "92126");      // new zipcode
stmt.setString(2, "111222333");  // same ssn we inserted

int rows = stmt.executeUpdate(); // runs the UPDATE
System.out.println("Update complete. Rows affected: " + rows);
}
}
}
```
# Video
https://www.youtube.com/watch?v=A89JzKg01Co
