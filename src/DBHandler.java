import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBHandler {

	private static Connection con = null;
	private static Properties props = new Properties();

	// Write the required business logic as expected in the question description
	public static Connection establishConnection() throws ClassNotFoundException, SQLException {
		try {
			FileInputStream fis = new FileInputStream("db.properties");
			props.load(fis);
			Class.forName("db.classname");
			con = DriverManager.getConnection(props.getProperty("db.url"), props.getProperty("db.username"),
					props.getProperty("db.password"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return con;
	}
}
