package DataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
        
        public static Connection connection;
	
	public static Connection getConnection(String userName, String password, String serverName, String portNumber) throws SQLException {

	    Connection conn;
	    Properties connectionProps = new Properties();
	    connectionProps.put("user", userName);
	    connectionProps.put("password", password);
	    
	    conn = DriverManager.getConnection("jdbc:mysql://" + serverName + ":" + portNumber + "/", connectionProps);
	    
	    System.out.println("Connected to database");
	    return conn;
	}
        
        public static Connection defaultConnect()
	{   
             if(connection==null){
		try {
			 connection = ConnectionFactory.getConnection("root", "mysql", "localhost", "3306");
		} catch (SQLException e) {
			
		}
            }
		
		return connection;
	}

}
