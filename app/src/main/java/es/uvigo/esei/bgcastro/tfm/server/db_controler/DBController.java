package es.uvigo.esei.bgcastro.tfm.server.db_controler;

import java.sql.Connection;
import java.sql.SQLException;

import es.uvigo.esei.bgcastro.tfm.server.database_connection.ConnectionConfiguration;
import es.uvigo.esei.bgcastro.tfm.server.database_connection.ConnectionUtils;

/**
 * Clase encargada de controlar la BD
 */
public class DBController {
	private final Connection connection;

    /**
     * Constructor
     *
     * @param configuration Configuracion de la BD
     * @throws SQLException
     */
    public DBController(ConnectionConfiguration configuration) throws SQLException {
        this.connection = ConnectionUtils.getConnection(configuration);
    }

    /**
     * Metodo que devuelve la conexion con la BD
     *
     * @return
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Metodo para cerrar la conexion
     */
    public void shutdown() {
        try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
