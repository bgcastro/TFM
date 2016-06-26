package es.uvigo.esei.bgcastro.tfm.server.database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase util para establecer la conexion a la BD
 */
public class ConnectionUtils {
    /**
     * Devuelve la conexion con la BD
     *
     * @param configuration Configuracion de la conexio
     * @return Conexion
     * @throws SQLException
     */
    public static Connection getConnection(ConnectionConfiguration configuration) throws SQLException {
		return DriverManager.getConnection(
			configuration.getConnectionString(),
			configuration.getConnectionProperties()
		);
	}
}
