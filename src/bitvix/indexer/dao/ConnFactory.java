package bitvix.indexer.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnFactory {
	   public static final int MYSQL = 0;  
	   private static final String MySQLDriver = "com.mysql.jdbc.Driver";  
	  
	   public static Connection conexao(String url, String username, String password,  
	         int db) throws ClassNotFoundException, SQLException {  
	      switch (db) {        
	      case MYSQL:           
	         Class.forName(MySQLDriver);  
	         break;  
	      }  
	      return DriverManager.getConnection(url, username, password);  
	   }  
}
