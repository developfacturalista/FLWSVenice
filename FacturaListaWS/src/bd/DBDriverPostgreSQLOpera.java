package bd;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class DBDriverPostgreSQLOpera{

	
	private Connection conexion = null;
	private Statement stmt = null;

	private String url;
	private String user;
	private String password;
	
	public DBDriverPostgreSQLOpera() throws IOException {
		Properties propiedades = Direccionador.getInstance().getArchivoConfiguracion();
		url = propiedades.getProperty("servidorbdopera");
		user = propiedades.getProperty("userbd");
		password = propiedades.getProperty("clavebd");
	}
	synchronized public void conectar() throws Exception {
		conexion = DriverManager.getConnection(url, user, password);
		conexion.setAutoCommit(false);
		stmt = conexion.createStatement();

	}


	synchronized public void desconectar() throws Exception {
		conexion.commit();
		stmt.close();
		conexion.close();
	}

	synchronized public void abortar() throws Exception {
		conexion.rollback();
		stmt.close();
		conexion.close();
	}

	
	public void commit() throws SQLException {
		conexion.commit();
	}

	public PreparedStatement prepareStatement(String sql) {
		try {
			return conexion.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	
}
